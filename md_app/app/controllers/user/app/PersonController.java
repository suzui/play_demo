package controllers.user.app;

import annotations.ActionMethod;
import annotations.ParamField;
import binders.PasswordBinder;
import enums.CaptchaType;
import enums.PersonType;
import models.person.Person;
import models.token.AccessToken;
import notifiers.Mails;
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
                               @ParamField(name = "手机号", required = false) String phone,
                               @ParamField(name = "邮箱", required = false) String email) {
        CaptchaType captchaType = CaptchaType.convert(type);
        if (captchaType == null) {
            renderJSON(Result.failed());
        }
        Boolean flag = null;
        if (StringUtils.isNotBlank(phone)) {
            flag = true;
        } else if (StringUtils.isNotBlank(email)) {
            flag = false;
        }
        if (flag == null) {
            renderJSON(Result.failed());
        }
        String captcha = RandomStringUtils.randomNumeric(4);
        if (flag) {
            if (!Person.isPhoneLegal(phone)) {
                renderJSON(Result.failed(StatusCode.PERSON_PHONE_UNVALID));
            }
            if (captchaType == CaptchaType.EMAIL) {
                renderJSON(Result.failed());
            }
            Person person = Person.findByPhone(phone, PersonType.USER);
            
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
        } else {
            if (!Person.isEmailLegal(email)) {
                renderJSON(Result.failed(StatusCode.PERSON_EMAIL_UNVALID));
            }
            if (captchaType == CaptchaType.PHONE) {
                renderJSON(Result.failed());
            }
            Person person = Person.findByEmail(email, PersonType.USER);
            if (captchaType == CaptchaType.REGIST && person != null) {
                renderJSON(Result.failed(StatusCode.PERSON_EMAIL_EXIST));
            }
            if (captchaType == CaptchaType.LOGIN && person == null) {
                renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
            }
            if (captchaType == CaptchaType.PASSWORD && person == null) {
                renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
            }
            if (captchaType == CaptchaType.EMAIL && person != null) {
                renderJSON(Result.failed(StatusCode.PERSON_EMAIL_EXIST));
            }
            Mails.captcha(email, captcha);
            Logger.info("[captcha] %s,%s,%s", type, email, captcha);
            captchaType.cache(email, captcha);
        }
        renderJSON(Result.succeed(captcha));
    }
    
    @ActionMethod(name = "用户注册", clazz = PersonVO.class)
    public static void regist(@ParamField(name = "手机号", required = false) String phone,
                              @ParamField(name = "邮箱", required = false) String email,
                              @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password,
                              @ParamField(name = "验证码") String captcha) {
        Boolean flag = null;
        if (StringUtils.isNotBlank(phone)) {
            flag = true;
        } else if (StringUtils.isNotBlank(email)) {
            flag = false;
        }
        if (flag == null) {
            renderJSON(Result.failed());
        }
        Person person = null;
        if (flag) {
            if (!CaptchaType.REGIST.validate(phone, captcha)) {
                renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
            }
            person = Person.findByPhone(phone, PersonType.USER);
            if (person != null) {
                renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
            }
        } else {
            if (!CaptchaType.REGIST.validate(email, captcha)) {
                renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
            }
            person = Person.findByEmail(email, PersonType.USER);
            if (person != null) {
                renderJSON(Result.failed(StatusCode.PERSON_EMAIL_EXIST));
            }
        }
        if (!Person.isPasswordLegal(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_UNVALID));
        }
        person = Person.regist(phone, email, password);
        AccessToken accessToken = AccessToken.add(person);
        renderJSON(Result.succeed(new PersonVO(accessToken)));
    }
    
    @ActionMethod(name = "用户登陆", clazz = PersonVO.class)
    public static void login(@ParamField(name = "用户名") String username,
                             @ParamField(name = "密码", required = false) @As(binder = PasswordBinder.class) String password,
                             @ParamField(name = "验证码", required = false) String captcha) {
        Person person = Person.findByUsername(username, PersonType.USER);
        if (StringUtils.isNotBlank(captcha)) {
            if (!CaptchaType.LOGIN.validate(username, captcha)) {
                renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
            }
            if (person == null) {
                person = Person.regist(username);
            }
        } else {
            if (person == null) {
                renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
            }
            if (!person.isPasswordRight(password)) {
                renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_ERROR));
            }
        }
        AccessToken accessToken = AccessToken.add(person);
        renderJSON(Result.succeed(new PersonVO(accessToken)));
    }
    
    @ActionMethod(name = "忘记密码", clazz = PersonVO.class)
    public static void forgetPassword(@ParamField(name = "用户名") String username,
                                      @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password,
                                      @ParamField(name = "验证码") String captcha) {
        if (!CaptchaType.PASSWORD.validate(username, captcha)) {
            renderJSON(Result.failed(StatusCode.PERSON_CAPTCHA_ERROR));
        }
        Person person = Person.findByUsername(username, PersonType.USER);
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
        if (!Person.isPhoneAvailable(phone, PersonType.USER)) {
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
        if (!Person.isEmailAvailable(email, PersonType.USER)) {
            renderJSON(Result.failed(StatusCode.PERSON_PHONE_EXIST));
        }
        Person person = getPersonByToken();
        person.editEmail(email);
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "用户详情", clazz = PersonVO.class)
    public static void info() {
        renderJSON(Result.succeed(new PersonVO(getAccessTokenByToken())));
    }
    
    @ActionMethod(name = "信息编辑", param = "-name,-avatar,-sex")
    public static void edit(PersonVO vo) {
        Person person = getPersonByToken();
        person.edit(vo);
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
    
    @ActionMethod(name = "用户登出")
    public static void logout() {
        renderJSON(Result.succeed());
    }
}