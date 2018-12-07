package controllers.admin;

import annotations.ActionMethod;
import annotations.ParamField;
import binders.PasswordBinder;
import enums.PersonType;
import models.person.Person;
import models.token.AccessToken;
import play.data.binding.As;
import vos.PageData;
import vos.PersonVO;
import vos.Result;
import vos.Result.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

public class PersonController extends ApiController {
    
    @ActionMethod(name = "管理员登录", clazz = PersonVO.class)
    public static void login(@ParamField(name = "用户名") String username,
                             @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = Person.findByUsername(username, PersonType.ADMIN);
        if (person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (!person.isPasswordRight(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_ERROR));
        }
        if (!person.isAdmin()) {
            renderJSON(Result.failed(StatusCode.SYSTEM_ACCESS_FOBIDDEN));
        }
        AccessToken accessToken = AccessToken.add(person);
        renderJSON(Result.succeed(new PersonVO(accessToken)));
    }
    
    @ActionMethod(name = "管理员登出")
    public static void logout() {
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "管理员列表", param = "page,size,-name,-phone", clazz = {PageData.class, PersonVO.class})
    public static void list(PersonVO vo) {
        int total = Person.count(vo);
        List<Person> persons = Person.fetch(vo);
        List<PersonVO> personVOs = persons.stream().map(p -> new PersonVO(p)).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, personVOs)));
    }
    
    @ActionMethod(name = "管理员详情", param = "-personId", clazz = PersonVO.class)
    public static void info(PersonVO vo) {
        if (vo.personId == null) {
            renderJSON(Result.succeed(new PersonVO(getAccessTokenByToken())));
        }
        Person person = Person.findByID(vo.personId);
        PersonVO personVO = new PersonVO(person);
        renderJSON(Result.succeed(personVO));
    }
    
    @ActionMethod(name = "管理员新增", param = "name,phone,roleIds", clazz = PersonVO.class)
    public static void add(PersonVO vo) {
        Person person = Person.add(vo);
        renderJSON(Result.succeed(new PersonVO(person)));
    }
    
    @ActionMethod(name = "管理员编辑", param = "personId,-name,-roleIds")
    public static void edit(PersonVO vo) {
        Person person = Person.findByID(vo.personId);
        person.edit(vo);
        renderJSON(Result.succeed(new PersonVO(person)));
    }
    
    @ActionMethod(name = "管理员删除", param = "personId")
    public static void delete(PersonVO vo) {
        Person person = Person.findByID(vo.personId);
        person.del();
        renderJSON(Result.succeed());
    }
    
}
