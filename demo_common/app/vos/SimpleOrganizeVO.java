package vos;

import annotations.DataField;
import enums.OrganizeStatus;
import enums.OrganizeType;
import models.organize.Organize;

public class SimpleOrganizeVO extends OneData {
    
    @DataField(name = "组织id")
    public Long organizeId;
    @DataField(name = "组织名称")
    public String name;
    @DataField(name = "logo")
    public String logo;
    @DataField(name = "介绍")
    public String intro;
    @DataField(name = "上级组织id")
    public Long parentId;
    @DataField(name = "排序")
    public Double rank;
    @DataField(name = "组织状态", enums = OrganizeStatus.class)
    public Integer status;
    @DataField(name = "组织类型", enums = OrganizeType.class)
    public Integer type;
    
    public SimpleOrganizeVO() {
    }
    
    public SimpleOrganizeVO(Organize organize) {
        super(organize.id);
        this.organizeId = organize.id;
        this.name = organize.name;
        this.logo = organize.logo;
        this.intro = organize.intro;
        this.status = organize.status.code();
        this.type = organize.type.code();
        this.rank = organize.rank;
        if (organize.parent != null) {
            this.parentId = organize.parent.id;
        }
    }
}
