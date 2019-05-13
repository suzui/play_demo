package vos;

import annotations.DataField;
import enums.PersonType;
import enums.Sex;
import models.person.Person;

public class SimplePersonVO extends OneData {
    
    @DataField(name = "用户id")
    public Long personId;
    @DataField(name = "用户名")
    public String username;
    @DataField(name = "手机号")
    public String phone;
    @DataField(name = "邮箱")
    public String email;
    @DataField(name = "编号")
    public String number;
    @DataField(name = "姓名")
    public String name;
    @DataField(name = "头像")
    public String avatar;
    @DataField(name = "简介")
    public String intro;
    @DataField(name = "备注")
    public String remark;
    @DataField(name = "性别", enums = Sex.class)
    public Integer sex;
    @DataField(name = "类别", enums = PersonType.class)
    public Integer type;
    
    public SimplePersonVO() {
    }
    
    public SimplePersonVO(Person person) {
        super(person.id);
        this.personId = person.id;
        this.username = person.username;
        this.phone = person.phone;
        this.email = person.email;
        this.number = person.number;
        this.name = person.name;
        this.avatar = person.avatar;
        this.intro = person.intro;
        this.remark = person.remark;
        this.sex = person.sex.code();
        this.type = person.type.code();
    }
    
}
