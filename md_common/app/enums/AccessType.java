package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;
import org.apache.commons.lang.StringUtils;

@EnumClass(name = "权限类型")
public enum AccessType implements BaseEnum {
    BOS(100, "超级后台"), ORGANIZE(101, "机构后台"), PERSON(102, "用户端");
    private int code;
    private String intro;
    
    private AccessType(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static AccessType convert(int code) {
        for (AccessType type : AccessType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
    
    public static AccessType convert(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return convert(Integer.parseInt(code));
    }
    
    public int code() {
        return this.code;
    }
    
    public String intro() {
        return this.intro;
    }
}
