package controllers.admin;

import annotations.ActionMethod;
import annotations.ParamField;
import binders.PasswordBinder;
import enums.CaptchaType;
import enums.PersonType;
import models.person.Person;
import models.token.AccessToken;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.binding.As;
import utils.CacheUtils;
import utils.SMSUtils;
import vos.PageData;
import vos.PersonVO;
import vos.Result;
import vos.Result.StatusCode;

import java.util.List;
import java.util.stream.Collectors;

public class PersonController extends ApiController {
    
    @ActionMethod(name = "验证码获取")
    public static void captcha(@ParamField(name = "验证码类型") Integer type,
                               @ParamField(name = "手机号") String phone) {
        CaptchaType captchaType = CaptchaType.convert(type);
        if (captchaType == null) {
            renderJSON(Result.failed());
        }
        String captcha = RandomStringUtils.randomNumeric(4);
        if (!Person.isPhoneLegal(phone)) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_UNVALID));
        }
        Person person = Person.findByPhone(phone, PersonType.ADMIN);
        if (captchaType == CaptchaType.REGIST && person != null) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        if (captchaType == CaptchaType.LOGIN && person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (captchaType == CaptchaType.PASSWORD && person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (captchaType == CaptchaType.PHONE && person != null) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        Logger.info("[captcha] %s,%s,%s", type, phone, captcha);
        SMSUtils.send(captchaType, captcha, phone);
        captchaType.cache(phone, captcha);
        renderJSON(Result.succeed(captcha));
    }
    
    @ActionMethod(name = "验证码校验")
    public static void validate(@ParamField(name = "验证码类型") Integer type,
                                @ParamField(name = "手机号") String phone, @ParamField(name = "验证码") String captcha) {
        CaptchaType captchaType = CaptchaType.convert(type);
        if (captchaType == null) {
            renderJSON(Result.failed());
        }
        if (!captchaType.validateOnly(phone, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "登录", clazz = PersonVO.class)
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
    
    @ActionMethod(name = "登出")
    public static void logout() {
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "忘记密码", clazz = PersonVO.class)
    public static void forgetPassword(@ParamField(name = "用户名") String username,
                                      @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password,
                                      @ParamField(name = "验证码") String captcha) {
        if (!CaptchaType.PASSWORD.validate(username, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        Person person = Person.findByUsername(username, PersonType.ADMIN);
        if (person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (StringUtils.isNotBlank(password)) {
            if (!Person.isPasswordLegal(password)) {
                renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_UNVALID));
            }
            person.editPassword(password);
        }
        AccessToken accessToken = AccessToken.add(person);
        renderJSON(Result.succeed(new PersonVO(accessToken)));
    }
    
    @ActionMethod(name = "绑定手机")
    public static void bindPhone(@ParamField(name = "手机号") String phone, @ParamField(name = "验证码") String captcha) {
        if (!CaptchaType.PHONE.validate(phone, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        if (!Person.isPhoneAvailable(phone, PersonType.ADMIN)) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        Person person = getPersonByToken();
        person.editPhone(phone);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "绑定邮箱")
    public static void bindEmail(@ParamField(name = "邮箱") String email, @ParamField(name = "验证码") String captcha) {
        if (!CaptchaType.EMAIL.validate(email, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        if (!Person.isEmailAvailable(email, PersonType.ADMIN)) {
            renderJSON(Result.failed(StatusCode.PERSON_EMAIL_EXIST));
        }
        Person person = getPersonByToken();
        person.editEmail(email);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "验证密码")
    public static void validatePassword(@ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = getPersonByToken();
        if (!person.isPasswordRight(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_ERROR));
        }
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "重置密码")
    public static void resetPassword(@ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = getPersonByToken();
        if (!Person.isPasswordLegal(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_UNVALID));
        }
        person.editPassword(password);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "人员列表", param = "page,size,-name,-phone", clazz = {PageData.class, PersonVO.class})
    public static void list(PersonVO vo) {
        vo.type = PersonType.ADMIN.code();
        int total = Person.count(vo);
        List<Person> persons = Person.fetch(vo);
        List<PersonVO> personVOs = persons.stream().map(p -> new PersonVO(p).roles(p.roles())).collect(Collectors.toList());
        renderJSON(Result.succeed(new PageData(vo.page, vo.size, total, personVOs)));
    }
    
    @ActionMethod(name = "人员详情", param = "-personId", clazz = PersonVO.class)
    public static void info(PersonVO vo) {
        if (vo.personId == null) {
            renderJSON(Result.succeed(new PersonVO(getAccessTokenByToken())));
        }
        Person person = Person.findByID(vo.personId);
        PersonVO personVO = new PersonVO(person);
        renderJSON(Result.succeed(personVO.roles(person.roles())));
    }
    
    @ActionMethod(name = "人员新增", param = "name,phone,sex,-remark,roleIds", clazz = PersonVO.class)
    public static void add(PersonVO vo) {
        vo.type = PersonType.ADMIN.code();
        Person person = Person.add(vo);
        renderJSON(Result.succeed(new PersonVO(person)));
    }
    
    @ActionMethod(name = "人员编辑", param = "-personId,-name,-phone,-sex,-remark,-roleIds")
    public static void edit(PersonVO vo) {
        Person person = vo.personId == null ? getPersonByToken() : Person.findByID(vo.personId);
        person.edit(vo);
        renderJSON(Result.succeed(new PersonVO(person)));
    }
    
    @ActionMethod(name = "人员删除", param = "personId")
    public static void delete(PersonVO vo) {
        Person person = Person.findByID(vo.personId);
        person.del();
        renderJSON(Result.succeed());
    }
    
}
