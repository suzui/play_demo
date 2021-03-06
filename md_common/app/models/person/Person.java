package models.person;

import enums.PersonType;
import enums.Sex;
import exceptions.ResultException;
import models.access.Authorization;
import models.access.Role;
import models.area.Area;
import models.organize.Organize;
import models.organize.Relation;
import models.token.BasePerson;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import utils.BaseUtils;
import utils.CodeUtils;
import vos.PersonVO;
import vos.StatusCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person extends BasePerson {
    
    @ManyToOne
    public Area area;
    
    public static Person add(PersonVO vo) {
        if (!Person.isPhoneAvailable(vo.phone, vo.type)) {
            throw new ResultException(StatusCode.PERSON_PHONE_EXIST);
        }
        Person person = new Person();
        person.username = vo.username;
        person.email = vo.email;
        person.phone = vo.phone;
        person.password = BaseUtils.initPassword();
        person.type = PersonType.convert(vo.type);
        person.edit(vo);
        return person;
    }
    
    public static Person regist(String username) {
        Person person = new Person();
        person.username = username;
        person.phone = isPhoneLegal(username) ? username : null;
        person.email = isEmailLegal(username) ? username : null;
        person.password = CodeUtils.md5(RandomStringUtils.random(6));
        return person.save();
    }
    
    public static Person regist(String phone, String email, String password) {
        Person person = new Person();
        person.username = StringUtils.isNotBlank(phone) ? phone : email;
        person.phone = phone;
        person.email = email;
        person.password = StringUtils.isBlank(password) ? CodeUtils.md5(RandomStringUtils.random(6)) : password;
        return person.save();
    }
    
    public static void initAdmin() {
        if (fetchAll().isEmpty()) {
            Person person = new Person();
            person.name = "管理员";
            person.username = "admin";
            person.password = BaseUtils.initPassword();
            person.type = PersonType.ADMIN;
            person.origin = true;
            person.save();
        }
    }
    
    public void edit(PersonVO vo) {
        if (!StringUtils.equals(this.phone, vo.phone)) {
            if (!isPhoneAvailable(vo.phone, this.type)) {
                throw new ResultException(StatusCode.PERSON_PHONE_EXIST);
            }
        }
        this.name = vo.name != null ? vo.name : name;
        this.avatar = vo.avatar != null ? vo.avatar : avatar;
        this.phone = vo.phone != null ? vo.phone : phone;
        this.idcard = vo.idcard != null ? vo.idcard : idcard;
        this.number = vo.number != null ? vo.number : number;
        this.avatar = vo.avatar != null ? vo.avatar : avatar;
        this.intro = vo.intro != null ? vo.intro : intro;
        this.remark = vo.remark != null ? vo.remark : remark;
        this.sex = vo.sex != null ? Sex.convert(vo.sex) : sex;
        this.save();
        if (vo.roleIds != null) {
            vo.roleIds.forEach(roleId -> Authorization.add(this, Role.findByID(roleId)));
            (this.isAdmin() ? this.authorizations() : this.authorizations(Organize.findByID(vo.rootId))).stream().filter(authorization -> !vo.roleIds.contains(authorization.role.id)).forEach(authorization -> authorization.del());
        }
        if (vo.organizeIds != null) {
            vo.organizeIds.forEach(organizeId -> Relation.add(Organize.findByID(organizeId), this));
            this.relations(Organize.findByID(vo.rootId)).stream().filter(relation -> !vo.organizeIds.contains(relation.organize.id)).forEach(relation -> relation.del());
        }
    }
    
    public void editPassword(String password) {
        this.password = password;
        this.save();
    }
    
    public void editPhone(String phone) {
        this.phone = phone;
        this.save();
    }
    
    public void editEmail(String email) {
        this.email = email;
        this.save();
    }
    
    public void editAccount(String phone, String email, String password) {
        if (phone != null) {
            this.phone = phone;
        }
        if (email != null) {
            this.email = email;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password.length() < 32 ? CodeUtils.md5(password) : password;
        }
        this.save();
    }
    
    public boolean isAdmin() {
        return PersonType.ADMIN == this.type;
    }
    
    public boolean isOrganize() {
        return PersonType.ORGANIZE == this.type;
    }
    
    public void del() {
        super.del();
    }
    
    public static List<Person> fetch(PersonVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Person.find(defaultSql(StringUtils.join(hqls, " and ")) + vo.condition, params.toArray())
                .fetch(vo.page, vo.size);
    }
    
    public static int count(PersonVO vo) {
        Object[] data = data(vo);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Person.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(PersonVO vo) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(vo.search)) {
            hqls.add("concat_ws(',',name,phone,email) like ?");
            params.add("%" + vo.search + "%");
        }
        if (StringUtils.isNotBlank(vo.name)) {
            hqls.add("name like ?");
            params.add("%" + vo.name + "%");
        }
        if (StringUtils.isNotBlank(vo.phone)) {
            hqls.add("phone like ?");
            params.add("%" + vo.phone + "%");
        }
        if (vo.type != null) {
            hqls.add("type = ?");
            params.add(PersonType.convert(vo.type));
        }
        hqls.add("origin = false");
        return new Object[]{hqls, params};
    }
}