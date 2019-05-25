package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;

@EnumClass(name = "二维码场景")
public enum QRType implements BaseEnum {
    
    PERSON(100, "个人二维码");
    
    private int code;
    private String intro;
    
    private QRType(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static QRType convert(int code) {
        for (QRType type : QRType.values()) {
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
}
