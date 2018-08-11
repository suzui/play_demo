package models.access;

import models.organize.Organize;
import org.apache.commons.lang.StringUtils;
import vos.CrowdVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Crowd extends BaseCrowd {
    
    public static Crowd add(CrowdVO crowdVO) {
        Crowd crowd = new Crowd();
        crowd.organize = crowdVO.organizeId != null ? Organize.findByID(crowdVO.organizeId) : null;
        crowd.edit(crowdVO);
        return crowd;
    }
    
    public void edit(CrowdVO crowdVO) {
        this.name = crowdVO.name != null ? crowdVO.name : name;
        this.organizeIds = crowdVO.organizeIds != null ? StringUtils.join(crowdVO.organizeIds, ",") : organizeIds;
        this.save();
    }
    
    public void del() {
        super.del();
    }
    
    public static List<Crowd> fetchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Crowd.find(defaultSql("id in (:ids)")).bind("ids", ids.toArray()).fetch();
    }
    
    public static List<Crowd> fetchByOrganize(Organize organize) {
        return Crowd.find(defaultSql("organize = ?"), organize).fetch();
    }
    
    public static List<Crowd> fetchAll() {
        return Crowd.find(defaultSql()).fetch();
    }
    
    public static List<Crowd> fetch(CrowdVO crowdVO) {
        Object[] data = data(crowdVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Crowd.find(defaultSql(StringUtils.join(hqls, " and ")) + crowdVO.condition, params.toArray())
                .fetch(crowdVO.page, crowdVO.size);
    }
    
    public static int count(CrowdVO crowdVO) {
        Object[] data = data(crowdVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Crowd.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(CrowdVO crowdVO) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(crowdVO.search)) {
            hqls.add("concat_ws(',',name) like ?");
            params.add("%" + crowdVO.search + "%");
        }
        if (crowdVO.organizeId != null) {
            hqls.add("organize.id=?");
            params.add(crowdVO.organizeId);
        }
        return new Object[]{hqls, params};
    }
}
