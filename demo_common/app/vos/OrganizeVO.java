package vos;

import annotations.DataField;
import models.organize.Organize;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrganizeVO extends OneData {
    
    @DataField(name = "组织id")
    public Long organizeId;
    @DataField(name = "组织名称")
    public String name;
    @DataField(name = "logo")
    public String logo;
    @DataField(name = "行业'")
    public String industry;
    @DataField(name = "员工规模")
    public String employee;
    @DataField(name = "介绍")
    public String intro;
    @DataField(name = "开始时间")
    public Long startTime;
    @DataField(name = "结束时间")
    public Long endTime;
    @DataField(name = "上级组织id")
    public Long parentId;
    @DataField(name = "根组织id")
    public Long rootId;
    @DataField(name = "排序")
    public Double rank;
    @DataField(name = "组织类型")
    public Integer type;
    
    @DataField(name = "负责人")
    public PersonVO person;
    
    @DataField(name = "上级组织")
    public OrganizeVO parent;
    @DataField(name = "下级组织")
    public List<OrganizeVO> children;
    
    public OrganizeVO() {
        this.condition = " order by rank ";
    }
    
    public OrganizeVO(Organize organize) {
        super(organize);
        this.organizeId = organize.id;
        this.name = organize.name;
        this.logo = organize.logo;
        this.industry = organize.industry;
        this.employee = organize.employee;
        this.startTime = organize.startTime;
        this.endTime = organize.endTime;
        this.rank = organize.rank;
        this.rootId = organize.root.id;
        this.type = organize.type.code();
        if (organize.parent != null) {
            this.parent = new OrganizeVO(organize.parent());
            this.parentId = organize.parent.id;
        }
        if (organize.person != null) {
            this.person = new PersonVO(organize.person());
        }
    }
    
    public void children(List<Organize> children) {
        this.children = children.stream().map(o -> new OrganizeVO(o)).collect(Collectors.toList());
    }
    
    public static OrganizeVO tree(Organize root) {
        Map<Long, OrganizeVO> map = new LinkedHashMap<>();
        List<Organize> organizes = Organize.fetchByRoot(root);
        for (Organize organize : organizes) {
            map.put(organize.id, new OrganizeVO(organize));
        }
        for (OrganizeVO organizeVO : map.values()) {
            if (organizeVO.parent == null) {
                continue;
            }
            if (map.get(organizeVO.parent.organizeId).children == null) {
                List<OrganizeVO> children = new ArrayList<>();
                map.get(organizeVO.parent.organizeId).children = children;
            }
            map.get(organizeVO.parent.organizeId).children.add(organizeVO);
        }
        return map.get(root.id);
    }
    
    
}
