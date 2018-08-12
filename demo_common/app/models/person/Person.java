package models.person;

import enums.PersonType;
import enums.Sex;
import models.token.BasePerson;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import utils.BaseUtils;
import utils.CodeUtils;
import vos.PersonVO;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person extends BasePerson {
    
    public static Person add(PersonVO personVO) {
        Person person = new Person();
        person.username = personVO.username;
        person.email = personVO.email;
        person.phone = personVO.phone;
        person.password = personVO.password;
        person.type = PersonType.NORMAL;
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
    
    public void del() {
        super.del();
    }
    
    public static List<Person> fetch(PersonVO personVO) {
        Object[] data = data(personVO);
        List<String> hqls = (List<String>) data[0];
        List<Object> params = (List<Object>) data[1];
        return Person.find(defaultSql(StringUtils.join(hqls, " and ")) + personVO.condition, params.toArray())
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