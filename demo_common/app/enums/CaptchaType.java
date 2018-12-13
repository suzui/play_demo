package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;
import org.apache.commons.lang.StringUtils;
import utils.CacheUtils;

@EnumClass(name = "验证码类型")
public enum CaptchaType implements BaseEnum {
    REGIST(100, "注册", "40001"), LOGIN(101, "登陆", ""), PASSWORD(102, "忘记密码", ""), PHONE(103, "手机绑定", ""), EMAIL(104, "邮箱绑定", "");
    private int code;
    private String intro;
    private String sms;
    
    private CaptchaType(int code, String intro, String sms) {
        this.code = code;
        this.intro = intro;
        this.sms = sms;
    }
    
    public static CaptchaType convert(int code) {
        for (CaptchaType type : CaptchaType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
    
    public int code() {
        return this.code;
    }
    
    public String intro() {
        return this.intro;
    }
    
    public String sms() {
        return this.sms;
    }
    
    public String key(String key) {
        return this.code + "_" + key;
    }
    
    public void cache(String key, String captcha) {
        String _key = key(key);
        CacheUtils.set(_key, captcha, "10mn");
    }
    
    public boolean validate(String key, String captcha) {
        String _key = key(key);
        String cachecaptcha = (String) CacheUtils.get(_key);
        if (!StringUtils.equals(cachecaptcha, captcha)) {
            return false;
        }
        CacheUtils.delete(_key);
        return true;
    }
    
    public boolean validateOnly(String key, String captcha) {
        String _key = key(key);
        String cachecaptcha = (String) CacheUtils.get(_key);
        if (!StringUtils.equals(cachecaptcha, captcha)) {
            return false;
        }
        return true;
    }
    
}
