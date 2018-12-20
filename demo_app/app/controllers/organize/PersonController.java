package controllers.organize;

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
import vos.PersonVO;
import vos.Result;
import vos.Result.StatusCode;

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
        Person person = Person.findByPhone(phone, PersonType.ORGANIZE);
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
    
    @ActionMethod(name = "登陆", clazz = PersonVO.class)
    public static void login(@ParamField(name = "用户名") String username,
                             @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = Person.findByUsername(username, PersonType.ORGANIZE);
        if (person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (!person.isPasswordRight(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_ERROR));
        }
        if (!person.isOrganize() || person.roots().isEmpty()) {
            renderJSON(Result.failed(StatusCode.SYSTEM_ACCESS_FOBIDDEN));
        }
        setHeader("root", person.roots().get(0).id + "");
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
        Person person = Person.findByUsername(username, PersonType.ORGANIZE);
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
        if (!Person.isPhoneAvailable(phone, PersonType.ORGANIZE)) {
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
        if (!Person.isEmailAvailable(email, PersonType.ORGANIZE)) {
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
    
    @ActionMethod(name = "详情", clazz = PersonVO.class)
    public static void info(PersonVO vo) {
        renderJSON(Result.succeed(new PersonVO(getAccessTokenByToken())));
    }
    
    @ActionMethod(name = "编辑", param = "-name,-avatar,-sex,-remark")
    public static void edit(PersonVO vo) {
        Person person = getPersonByToken();
        person.edit(vo);
        renderJSON(Result.succeed(new PersonVO(person)));
    }
    
}
