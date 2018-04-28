package vos;

import annotations.DataField;
import models.BaseModel;
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
    @DataField(name = "上级组织id")
    public Long parentId;
    @DataField(name = "组织类型")
    public Integer type;
    
    @DataField(name = "上级组织", enable = false)
    public OrganizeVO parent;
    @DataField(name = "下级组织", enable = false)
    public List<OrganizeVO> children;
    @DataField(name = "排序", enable = false)
    public Double rank;
    
    public OrganizeVO() {
        this.condition = " order by rank ";
    }
    
    public OrganizeVO(Organize organize) {
        super(organize);
        this.organizeId = organize.id;
        this.name = organize.name;
        this.rank = organize.rank;
        this.type = organize.type.code();
        if (organize.isRoot()) {
            this.logo = organize.logo;
        } else {
            this.parent = new OrganizeVO(organize.parent());
            this.parentId = organize.parent.id;
        }
    }
    
    public void children(List<Organize> children) {
        this.children = children.stream().map(o -> new OrganizeVO(o)).collect(Collectors.toList());
    }
    
    public static OrganizeVO tree() {
        Map<Long, OrganizeVO> map = new LinkedHashMap<>();
        for (Organize organize : Organize.fetchBySource()) {
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
        return map.get(BaseModel.getSource());
    }
    
    
}
