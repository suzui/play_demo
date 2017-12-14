package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;
import org.apache.commons.lang.StringUtils;
import play.cache.Cache;

@EnumClass(name = "用户类型")
public enum PersonType implements BaseEnum {
    ADMIN(100, "管理员"), NORMAL(101, "普通用户");
    private int code;
    private String intro;
    
    private PersonType(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static PersonType convert(int code) {
        for (PersonType captchaType : PersonType.values()) {
            if (captchaType.code == code) {
                return captchaType;
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
    
}
