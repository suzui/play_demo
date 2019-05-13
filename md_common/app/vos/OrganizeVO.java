package vos;

import annotations.DataField;
import com.fasterxml.jackson.annotation.JsonInclude;
import enums.OrganizeStatus;
import enums.OrganizeType;
import models.organize.Organize;
import utils.DateUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrganizeVO extends OneData {
    
    @DataField(name = "组织id")
    public Long organizeId;
    @DataField(name = "code")
    public String code;
    @DataField(name = "名称")
    public String name;
    @DataField(name = "logo")
    public String logo;
    @DataField(name = "图片")
    public String image;
    @DataField(name = "编号")
    public String number;
    @DataField(name = "行业'")
    public String trade;
    @DataField(name = "规模")
    public String employee;
    @DataField(name = "单位")
    public String unit;
    @DataField(name = "地址")
    public String address;
    @DataField(name = "介绍")
    public String intro;
    @DataField(name = "备注")
    public String remark;
    @DataField(name = "开始时间")
    public Long startTime;
    @DataField(name = "结束时间")
    public Long endTime;
    @DataField(name = "有效期")
    public Integer effectDay;
    @DataField(name = "上级组织id")
    public Long parentId;
    @DataField(name = "排序")
    public Double rank;
    @DataField(name = "组织状态", enums = OrganizeStatus.class)
    public Integer status;
    @DataField(name = "组织类型", enums = OrganizeType.class)
    public Integer type;
    
    @DataField(name = "负责人")
    public PersonVO person;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "负责人名称")
    public String personName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "负责人手机号")
    public String personPhone;
    
    @DataField(name = "上级组织")
    public OrganizeVO parent;
    @DataField(name = "下级组织")
    public List<OrganizeVO> children;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DataField(name = "根机构id")
    public Long rootId;
    
    public OrganizeVO() {
        this.condition = " order by rank ";
    }
    
    public OrganizeVO(Organize organize) {
        super(organize.id);
        this.organizeId = organize.id;
        this.code = organize.code;
        this.name = organize.name;
        this.logo = organize.logo;
        this.image = organize.image;
        this.number = organize.number;
        this.trade = organize.trade;
        this.employee = organize.employee;
        this.unit = organize.unit;
        this.address = organize.address;
        this.intro = organize.intro;
        this.remark = organize.remark;
        this.startTime = organize.startTime;
        this.endTime = organize.endTime;
        if (organize.isRoot()) {
            this.effectDay = DateUtils.dayBetween(organize.startTime, organize.endTime);
        }
        this.rank = organize.rank;
        this.status = organize.status.code();
        this.type = organize.type.code();
        if (organize.parent != null) {
            this.parent = new OrganizeVO(organize.parent());
            this.parentId = organize.parent.id;
        }
        if (organize.person != null) {
            this.person = new PersonVO(organize.person());
        }
    }
    
    public void children(List<Organize> children) {
        this.children = children.stream().map(o -> new OrganizeVO(o)).collect(Collectors.toList());
    }
    
    public static OrganizeVO tree(Organize root) {
        Map<Long, OrganizeVO> map = new LinkedHashMap<>();
        List<Organize> organizes = Organize.fetchByRoot(root);
        for (Organize organize : organizes) {
            map.put(organize.id, new OrganizeVO(organize));
        }
        for (OrganizeVO organizeVO : map.values()) {
            if (organizeVO.parent == null) {
                continue;
            }
            if (map.get(organizeVO.parentId).children == null) {
                List<OrganizeVO> children = new ArrayList<>();
                map.get(organizeVO.parentId).children = children;
            }
            map.get(organizeVO.parentId).children.add(organizeVO);
        }
        return map.get(root.id);
    }
    
    
}
