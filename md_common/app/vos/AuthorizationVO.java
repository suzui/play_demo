package vos;

import annotations.DataField;
import models.access.Authorization;

public class AuthorizationVO extends OneData {
    
    @DataField(name = "授权id")
    public Long authorizationId;
    @DataField(name = "角色id")
    public Long roleId;
    @DataField(name = "角色名称")
    public String roleName;
    @DataField(name = "范围id")
    public Long crowdId;
    @DataField(name = "范围名称")
    public String crowdName;
    
    public AuthorizationVO() {
    
    }
    
    public AuthorizationVO(Authorization authorization) {
        super(authorization.id);
        this.authorizationId = authorization.id;
        if (authorization.role != null) {
            this.roleId = authorization.role.id;
            this.roleName = authorization.role.name;
        }
        if (authorization.crowd != null) {
            this.crowdId = authorization.crowd.id;
            this.crowdName = authorization.crowd.name;
        }
    }
    
}
