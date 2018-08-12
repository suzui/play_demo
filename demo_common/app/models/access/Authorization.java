package models.access;

import models.token.BasePerson;

import javax.persistence.Entity;

@Entity
public class Authorization extends BaseAuthorization {
    
    public static Authorization add(BasePerson person, BaseRole role) {
        Authorization authorization = findByPersonAndRole(person, role);
        if (authorization != null) {
            return authorization;
        }
        authorization = new Authorization();
        authorization.person = person;
        authorization.role = role;
        authorization.organize = role.organize;
        return authorization.save();
    }
    
    public static Authorization add(BasePerson person, BaseRole role, BaseCrowd crowd) {
        Authorization authorization = findByPersonAndRoleAndCrowd(person, role, crowd);
        if (authorization != null) {
            return authorization;
        }
        authorization = new Authorization();
        authorization.person = person;
        authorization.role = role;
        authorization.crowd = crowd;
        authorization.organize = role.organize;
        return authorization.save();
    }
    
    
}
