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
    @DataField(name = "机构id")
    public Long organizeId;
    @DataField(name = "机构ids", demo = "[1,2,3]")
    public List<Long> organizeIds;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "机构列表")
    public List<OrganizeVO> organizes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "成员列表")
    public List<PersonVO> persons;
    
    
    public CrowdVO() {
        this.condition = " order by id";
    }
    
    public CrowdVO(Crowd crowd) {
        this.crowdId = crowd.id;
        this.name = crowd.name;
        if (crowd.organize != null) {
            this.organizeId = crowd.organize.id;
        }
        this.organizeIds = BaseUtils.idsToList(crowd.organizeIds);
    }
    
    public CrowdVO organizes(List<Organize> organizes) {
        this.organizes = organizes.stream().map(o -> new OrganizeVO(o)).collect(Collectors.toList());
        return this;
    }
    
    public CrowdVO persons(List<Authorization> authorizations) {
        this.persons = authorizations.stream().map(a -> new PersonVO(a.person())).collect(Collectors.toList());
        return this;
    }
    
    
}
