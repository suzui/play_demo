package models.organize;

import enums.OrganizeStatus;
import enums.OrganizeType;
import models.token.BaseOrganize;
import org.apache.commons.lang.StringUtils;
import utils.DateUtils;
import vos.OrganizeVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Organize extends BaseOrganize {
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = STRING + "'机构状态'")
    public OrganizeStatus status = OrganizeStatus.WAIT;
    
    public static Organize init(OrganizeVO vo) {
        Organize organize = new Organize();
        organize.type = OrganizeType.ORGANIZE;
        organize.rank = 0d;
        organize.edit(vo);
        organize.root = organize;
        return organize.save();
    }
    
    public static Organize add(OrganizeVO vo) {
        Organize organize = new Organize();
        organize.type = OrganizeType.convert(vo.type);
        organize.parent = Organize.findByID(vo.parentId);
        organize.rank = organize.parent().initRank();
        organize.root = Organize.findByID(vo.rootId);
        organize.edit(vo);
        return organize;
    }
    
    public void edit(OrganizeVO vo) {
        this.code = vo.code != null ? vo.code : code;
        this.name = vo.name != null ? vo.name : name;
        this.logo = vo.logo != null ? vo.logo : logo;
        this.image = vo.image != null ? vo.image : image;
        this.number = vo.number != null ? vo.number : number;
        this.trade = vo.trade != null ? vo.trade : trade;
        this.employee = vo.employee != null ? vo.employee : employee;
        this.unit = vo.unit != null ? vo.unit : unit;
        this.address = vo.address != null ? vo.address : address;
        this.intro = vo.intro != null ? vo.intro : intro;
        this.remark = vo.remark != null ? vo.remark : remark;
        this.startTime = vo.startTime != null ? vo.startTime : startTime;
        this.endTime = vo.endTime != null ? vo.endTime : endTime;
        this.save();
        this.freshStatus();
    }
    
    public boolean available() {
        return this.status == OrganizeStatus.EFFECTIVE;
    }
    
    public void freshStatus() {
        if (!this.isRoot()) {
            return;
        }
        if (this.startTime <= DateUtils.truncate(new Date()).getTime()) {
            this.status = OrganizeStatus.EFFECTIVE;
        }
        if (this.endTime <= DateUtils.ceiling(new Date()).getTime()) {
            this.status = OrganizeStatus.EXPIRE;
        }
        this.save();
    }
    
    public void del() {
        Relation.fetchByOrganize(this).forEach(r -> r.del());
        this.children().forEach(o -> o.del());
        this.logicDelete();
    }
    
    public static List<Organize> fetch(OrganizeVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Organize.find(defaultSql(StringUtils.join(hqls, " and ")) + vo.condition, params.toArray())
                .fetch(vo.page, vo.size);
    }
    
    public static int count(OrganizeVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Organize.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(OrganizeVO vo) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.name)) {
            hqls.add("name like ?");
            params.add("%" + vo.name + "%");
        }
        if (StringUtils.isNotBlank(vo.personName)) {
            hqls.add("person.name like ?");
            params.add("%" + vo.personName + "%");
        }
        if (StringUtils.isNotBlank(vo.personPhone)) {
            hqls.add("person.phone like ?");
            params.add("%" + vo.personPhone + "%");
        }
        if (vo.type != null) {
            hqls.add("type=?");
            params.add(OrganizeType.convert(vo.type));
        }
        if (vo.parentId != null) {
            hqls.add("parent.id=?");
            params.add(vo.parentId);
        }
        if (vo.rootId != null) {
            hqls.add("root.id=?");
            params.add(vo.rootId);
        }
        return new Object[]{hqls, params};
    }
}