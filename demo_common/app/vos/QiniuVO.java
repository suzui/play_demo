package vos;

import annotations.DataField;
import utils.QiniuUtils;

public class QiniuVO extends OneData {
    
    @DataField(name = "七牛凭证")
    public String uptoken;
    
    public QiniuVO() {
        this.uptoken = QiniuUtils.upToken();
    }
    
}
