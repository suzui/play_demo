package models.access;

import models.token.BasePerson;

import javax.persistence.Entity;

@Entity
public class Authorization extends BaseAuthorization {
    
    public static Authorization add(BasePerson person, BasePermission permission) {
        Authorization authorization = findByPersonAndPermission(person, permission);
        if (authorization != null) {
            return authorization;
        }
        authorization = new Authorization();
        authorization.person = person;
        authorization.permission = permission;
        authorization.organize = permission.organize;
        return authorization.save();
    }
    
    public static Authorization add(BasePerson person, BasePermission permission, BaseCrowd crowd) {
        Authorization authorization = findByPersonAndPermissionAndCrowd(person, permission, crowd);
        if (authorization != null) {
            return authorization;
        }
        authorization = new Authorization();
        authorization.person = person;
        authorization.permission = permission;
        authorization.crowd = crowd;
        authorization.organize = permission.organize;
        return authorization.save();
    }
    
    
}
