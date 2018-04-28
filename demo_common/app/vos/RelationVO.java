package vos;

import annotations.DataField;
import models.organize.Relation;

public class RelationVO extends OneData {
    
    @DataField(name = "成员关系id")
    public Long relationId;
    @DataField(name = "用户id")
    public Long personId;
    @DataField(name = "组织id")
    public Long organizeId;
    @DataField(name = "排序")
    public Double rank;
    
    public RelationVO() {
    }
    
    public RelationVO(Relation relation) {
        super(relation);
        this.relationId = relation.id;
        this.personId = relation.person.id;
        this.organizeId = relation.organize.id;
        this.rank = relation.rank;
    }
    
}
