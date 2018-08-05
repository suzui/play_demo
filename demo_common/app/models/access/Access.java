package models.access;

import org.apache.commons.lang.StringUtils;
import play.Play;

import javax.persistence.Entity;
import java.util.Collections;
import java.util.List;

@Entity
public class Access extends BaseAccess {
    
    public static Access add(String code, String name, String intro) {
        Access access = new Access();
        access.code = code;
        access.name = name;
        access.intro = intro;
        return access.save();
    }
    
    public static Access add(String code, String name) {
        return add(code, name, null);
    }
    
    public static void init() {
        for (int i = 1; i < 100; i++) {
            String access = Play.configuration.getProperty("access." + i);
            if (StringUtils.isBlank(access)) {
                break;
            }
            String[] array = StringUtils.split(access, ",");
            if (findByCode(array[0]) != null) {
                continue;
            }
            if (array.length == 2) {
                add(array[0], array[1]);
            } else if (array.length == 3) {
                add(array[0], array[1], array[2]);
            }
        }
    }
    
    public static List<Access> fetchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Access.find(defaultSql("id in (:ids)")).bind("ids", ids.toArray()).fetch();
    }
    
    public static List<Access> fetchAll() {
        return Access.find(defaultSql()).fetch();
    }
}
