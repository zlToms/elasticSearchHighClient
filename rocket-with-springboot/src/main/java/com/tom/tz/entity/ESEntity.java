package com.tom.tz.entity;

import com.tom.tz.enums.ESOperationType;

public class ESEntity {
    
    private  static final long serialVersionUID = -7320328933341461905L;
    private String index;
    private String type;
    private int id;
    private ESOperationType esType;

    public ESOperationType getEsType() {
        return esType;
    }

    public void setEsType(ESOperationType esType) {
        this.esType = esType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
