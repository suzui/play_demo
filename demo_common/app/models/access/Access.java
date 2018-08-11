package models.access;

import enums.AccessType;
import org.apache.commons.lang.StringUtils;
import play.Play;

import javax.persistence.Entity;

@Entity
public class Access extends BaseAccess {
    
    public static Access add(String code, String name, String intro, AccessType type) {
        Access access = findByCodeAndType(code, type);
        if (access != null) {
            return access;
        }
        access = new Access();
        access.code = code;
        access.name = name;
        access.intro = intro;
        access.type = type;
        return access.save();
    }
    
    public static void init() {
        for (int i = 1; i < 100; i++) {
            String access = Play.configuration.getProperty("access." + i);
            if (StringUtils.isBlank(access)) {
                break;
            }
            String[] array = StringUtils.split(access, ",");
            if (array.length == 2) {
                add(array[0], array[1], null, AccessType.ADMIN);
            } else if (array.length == 3) {
                add(array[0], array[1], array[2], AccessType.ADMIN);
            }
        }
    }
}
