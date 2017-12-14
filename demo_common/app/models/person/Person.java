package models.person;

import enums.PersonType;
import enums.Sex;
import models.token.BasePerson;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import utils.CodeUtils;
import vos.PersonVO;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Person extends BasePerson {
    
    @Enumerated(EnumType.STRING)
    public Sex sex = Sex.NOPOINT;
    @Enumerated(EnumType.STRING)
    public PersonType type = PersonType.NORMAL;
    
    public static Person add(PersonVO personVO) {
        Person person = new Person();
        person.username = personVO.username;
        person.email = personVO.email;
        person.phone = personVO.phone;
        person.password = personVO.password;
        person.edit(personVO);
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
        person.password = password;
        return person.save();
    }
    
    public static void initAdmin() {
        if (!fetchAll().isEmpty()) {
            Person person = new Person();
            person.name = "管理员";
            person.username = "admin";
            person.password = CodeUtils.md5("123456");
            person.type = PersonType.ADMIN;
            person.save();
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
    
    public void editInfo(String name, String avatar, String intro, Sex sex, List<String> colunms) {
        if (colunms.contains("name")) {
            this.name = name;
        }
        if (colunms.contains("avatar")) {
            this.avatar = avatar;
        }
        if (colunms.contains("intro")) {
            this.intro = intro;
        }
        if (colunms.contains("sex")) {
            this.sex = sex;
        }
        this.save();
    }
    
    public void edit(PersonVO personVO) {
        this.name = personVO.name != null ? personVO.name : name;
        this.avatar = personVO.avatar != null ? personVO.avatar : avatar;
        this.intro = personVO.intro != null ? personVO.intro : intro;
        this.sex = personVO.sex != null ? Sex.convert(personVO.sex) : sex;
        this.save();
    }
    
    public static boolean isPhoneAvailable(String phone) {
        return Person.findByPhone(phone) == null;
    }
    
    public static boolean isEmailAvailable(String email) {
        return Person.findByEmail(email) == null;
    }
    
    public boolean isAdmin() {
        return this.type != null && this.type == PersonType.NORMAL;
    }
    
    public void del() {
        this.logicDelete();
    }
    
    public static Person findByID(Long id) {
        return Person.find(defaultSql("id=?"), id).first();
    }
    
    public static Person findByUsername(String username) {
        return Person.find(defaultSql("username=?"), username).first();
    }
    
    public static Person findByPhone(String phone) {
        return Person.find(defaultSql("phone=?"), phone).first();
    }
    
    public static Person findByEmail(String email) {
        return Person.find(defaultSql("email=?"), email).first();
    }
    
    public static List<Person> fetchByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return Person.find(defaultSql("id in (:ids)")).bind("ids", ids.toArray()).fetch();
    }
    
    public static List<Person> fetchAll() {
        return Person.find(defaultSql()).fetch();
    }
    
    public static List<Person> fetch(PersonVO personVO) {
        Object[] data = data(personVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Person.find(defaultSql(StringUtils.join(hqls, " and ")) + personVO.condition(), params.toArray())
                .fetch(personVO.page, personVO.size);
    }
    
    public static int count(PersonVO personVO) {
        Object[] data = data(personVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return (int) Person.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
    }
    
    private static Object[] data(PersonVO personVO) {
        List<String> hqls = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(personVO.search)) {
            hqls.add("concat_ws(',',name,phone,email) like ?");
            params.add("%" + personVO.search + "%");
        }
        return new Object[]{hqls, params};
    }
}