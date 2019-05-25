package enums;

import annotations.EnumClass;
import interfaces.BaseEnum;
import org.apache.commons.lang.StringUtils;

@EnumClass(name = "机构状态")
public enum OrganizeStatus implements BaseEnum {
    WAIT(100, "未生效"), EFFECTIVE(101, "生效中"), EXPIRE(102, "已失效");
    private int code;
    private String intro;
    
    private OrganizeStatus(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }
    
    public static OrganizeStatus convert(int code) {
        for (OrganizeStatus status : OrganizeStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
    
    public static OrganizeStatus convert(String code) {
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
