package controllers.admin;

import annotations.ActionMethod;
import annotations.ParamField;
import binders.PasswordBinder;
import models.person.Person;
import models.token.AccessToken;
import play.data.binding.As;
import vos.PersonVO;
import vos.Result;
import vos.Result.StatusCode;

public class AdminController extends ApiController {
    
    @ActionMethod(name = "管理员登录", clazz = PersonVO.class)
    public static void login(@ParamField(name = "用户名") String username,
                             @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = Person.findByUsername(username);
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
    
    @ActionMethod(name = "管理员详情", clazz = PersonVO.class)
    public static void info() {
        renderJSON(Result.succeed(new PersonVO(getAccessTokenByToken())));
    }
    
}