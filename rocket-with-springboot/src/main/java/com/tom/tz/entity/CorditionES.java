package com.tom.tz.entity;

public class CorditionES {
    private int start ; //默认从零开始
    private int size ; //默认10个
    private String orderPropery;
    private int order ; //默认正序 前提是orderProperty有值

    public int getStart() {
        return start;
    }

    public int getSize() {
        return size;
    }

    public String getOrderPropery() {
        return orderPropery;
    }

    public int getOrder() {
        return order;
    }

    private CorditionES (Builder builder){
        this.start =builder.start;
        this.size = builder.size;
        this.order =builder.order;
        this.orderPropery = builder.orderPropery;
    }

public static class Builder{
    private int start = 0; //默认从零开始
    private int size = 10; //默认10个
    private String orderPropery;
    private int order = 1 ; //默认正序 前提是orderProperty有值

    public Builder start(int start){
       this.start = start;
       return this;
    }
    public Builder size(int size){
        this.size = size;
        return this;
    }

    public Builder orderProperty(String orderProperty){
        this.orderPropery = orderProperty;
        return this;
    }

    public Builder orderType(int orderType){
        if(orderType == 1){
            this.order =orderType;
        }else if(orderType == 2){
            this.order =orderType;
        }else{
            this.order =1;
        }
        return this;
    }

    public CorditionES build(){
        return new CorditionES(this);
    }
}

}
