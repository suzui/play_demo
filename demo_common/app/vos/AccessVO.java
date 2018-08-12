package vos;

import annotations.DataField;
import enums.AccessType;
import models.access.Access;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccessVO extends OneData {
    
    @DataField(name = "权限id")
    public Long accessId;
    @DataField(name = "权限code")
    public String code;
    @DataField(name = "权限名称")
    public String name;
    @DataField(name = "权限说明")
    public String intro;
    
    @DataField(name = "父权限code")
    public String parentCode;
    @DataField(name = "子权限列表")
    public List<AccessVO> children;
    
    public AccessVO() {
    
    }
    
    public AccessVO(Access access) {
        super(access.id);
        this.accessId = access.id;
        this.code = access.code;
        this.name = access.name;
        this.intro = access.intro;
        this.parentCode = access.parentCode();
    }
    
    public void children(List<Access> accesss) {
        this.children = accesss.stream().map(a -> new AccessVO(a)).collect(Collectors.toList());
    }
    
    public static List<AccessVO> list(AccessType type) {
        Map<String, AccessVO> map = new LinkedHashMap<>();
        List<Access> access = Access.fetchByType(type);
        access.forEach(a -> map.put(a.code, new AccessVO(a)));
        map.values().forEach(accessVO -> {
            if (accessVO.parentCode != null) {
                if (map.get(accessVO.parentCode).children == null) {
                    List<AccessVO> children = new ArrayList<>();
                    map.get(accessVO.parentCode).children = children;
                }
                map.get(accessVO.parentCode).children.add(accessVO);
            }
        });
        List<AccessVO> accessVOs = new ArrayList<>();
        map.values().forEach(accessVO -> {
            if (accessVO.parentCode == null) {
                accessVOs.add(accessVO);
            }
        });
        return accessVOs;
    }
}
