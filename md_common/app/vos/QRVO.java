package vos;

import annotations.DataField;
import enums.QRType;

public class QRVO extends OneData {
    
    @DataField(name = "二维码类型")
    public Integer type;
    @DataField(name = "包含数据")
    public String data;
    @DataField(name = "附加数据")
    public String addition;
    
    public QRVO() {

    }

    public QRVO(QRType type, String data, String addition) {
        this.type = type.code();
        this.data = data;
        this.addition = addition;
    }

    public QRVO(QRType type, String data) {
        this(type, data, null);
    }
    
    
}