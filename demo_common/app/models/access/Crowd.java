package models.access;

import models.organize.Organize;
import org.apache.commons.lang.StringUtils;
import vos.CrowdVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Crowd extends BaseCrowd {
    
    public static Crowd add(CrowdVO vo) {
        Crowd crowd = new Crowd();
        crowd.organize = vo.organizeId != null ? Organize.findByID(vo.organizeId) : null;
        crowd.edit(vo);
        return crowd;
    }
    
    public void edit(CrowdVO vo) {
        this.name = vo.name != null ? vo.name : name;
        this.organizeIds = vo.organizeIds != null ? StringUtils.join(vo.organizeIds, ",") : organizeIds;
        this.save();
    }
    
    public void del() {
        super.del();
    }
    
    public static List<Crowd> fetch(CrowdVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Crowd.find(defaultSql(StringUtils.join(hqls, " and ")) + vo.condition, params.toArray())
                .fetch(vo.page, vo.size);
    }
    
    public static int count(CrowdVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Crowd.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(CrowdVO vo) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.search)) {
            hqls.add("concat_ws(',',name) like ?");
            params.add("%" + vo.search + "%");
        }
        if (StringUtils.isNotBlank(vo.name)) {
            hqls.add("name like ?");
            params.add("%" + vo.name + "%");
        }
        if (vo.organizeId != null) {
            hqls.add("organize.id=?");
            params.add(vo.organizeId);
        }
        return new Object[]{hqls, params};
    }
}
