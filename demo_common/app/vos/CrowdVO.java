package vos;

import annotations.DataField;
import com.fasterxml.jackson.annotation.JsonInclude;
import models.access.Authorization;
import models.access.Crowd;
import models.organize.Organize;
import utils.BaseUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CrowdVO extends OneData {
    
    @DataField(name = "范围id")
    public Long crowdId;
    @DataField(name = "范围名称")
    public String name;
    @DataField(name = "组织ids", demo = "[1,2,3]")
    public List<Long> organizeIds;
    @DataField(name = "组织列表")
    public List<SimpleOrganizeVO> organizes;
    @DataField(name = "人员列表")
    public List<SimplePersonVO> persons;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "根机构id")
    public Long rootId;
    
    public CrowdVO() {
        this.condition = " order by id";
    }
    
    public CrowdVO(Crowd crowd) {
        super(crowd.id);
        this.crowdId = crowd.id;
        this.name = crowd.name;
        if (crowd.root != null) {
            this.rootId = crowd.root.id;
        }
        this.organizeIds = BaseUtils.idsToList(crowd.organizeIds);
    }
    
    public CrowdVO organizes(List<Organize> organizes) {
        this.organizes = organizes.stream().map(o -> new SimpleOrganizeVO(o)).collect(Collectors.toList());
        return this;
    }
    
    public CrowdVO persons(List<Authorization> authorizations) {
        this.persons = authorizations.stream().map(a -> new SimplePersonVO(a.person())).collect(Collectors.toList());
        return this;
    }
    
    
}
