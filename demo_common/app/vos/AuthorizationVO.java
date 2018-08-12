package vos;

import annotations.DataField;
import models.access.Authorization;

public class AuthorizationVO extends OneData {
    
    @DataField(name = "授权id")
    public Long authorizationId;
    @DataField(name = "权限组id")
    public Long permissionId;
    @DataField(name = "权限组名称")
    public String permissionName;
    @DataField(name = "范围组id")
    public Long crowdId;
    @DataField(name = "范围组名称")
    public String crowdName;
    
    public AuthorizationVO() {
    
    }
    
    public AuthorizationVO(Authorization authorization) {
        super(authorization.id);
        this.authorizationId = authorization.id;
        if (authorization.permission != null) {
            this.permissionId = authorization.permission.id;
            this.permissionName = authorization.permission.name;
        }
        if (authorization.crowd != null) {
            this.crowdId = authorization.crowd.id;
            this.crowdName = authorization.crowd.name;
        }
    }
    
}
