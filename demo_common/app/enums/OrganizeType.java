package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;

@EnumClass(name = "组织类型")
public enum OrganizeType implements BaseEnum {
    NORMAL(100, "普通组织"), ORGANIZE(101, "机构组织");
    private int code;
    private String intro;
    
    private OrganizeType(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static OrganizeType convert(int code) {
        for (OrganizeType captchaType : OrganizeType.values()) {
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
