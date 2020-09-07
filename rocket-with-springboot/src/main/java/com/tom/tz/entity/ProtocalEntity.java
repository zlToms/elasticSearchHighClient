package com.tom.tz.entity;

public class ProtocalEntity {
    private static final long serialVersionUID = 4830732405409013387L;
    private ESEntity esEntity;
    private Object object;
    private String clazz;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public ESEntity getEsEntity() {
        return esEntity;
    }

    public void setEsEntity(ESEntity esEntity) {
        this.esEntity = esEntity;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
