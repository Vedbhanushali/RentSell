package com.example.rentsell;

public class C11F1_C11F2_M {

    String title;
    String price;
    String location;
    String type;
    String property_for;
    String tid;
    String pid;
    String timeStamp;
    String imgUrl;
    Boolean feature;

    public C11F1_C11F2_M(String title, String price, String location, String type, String property_for) {
        this.title = title;
        this.price = price;
        this.location = location;
        this.type = type;
        this.property_for = property_for;
    }

    public C11F1_C11F2_M(String title, String price, String location, String type, String property_for, String tid, String pid, String timeStamp, String imgUrl) {
        this.title = title;
        this.price = price;
        this.location = location;
        this.property_for = property_for;
        this.type = type;
        this.tid = tid;
        this.pid = pid;
        this.timeStamp = timeStamp;
        this.imgUrl = imgUrl;
    }

    public C11F1_C11F2_M(String title, String price, String location, String type, String property_for, String tid, String pid, String timeStamp, String imgUrl, Boolean feature) {
        this.title = title;
        this.price = price;
        this.location = location;
        this.property_for = property_for;
        this.type = type;
        this.tid = tid;
        this.pid = pid;
        this.timeStamp = timeStamp;
        this.imgUrl = imgUrl;
        this.feature = feature;
    }

    public Boolean getFeature() {
        return feature;
    }

    public void setFeature(Boolean feature) {
        this.feature = feature;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty_for() {
        return property_for;
    }

    public void setProperty_for(String property_for) {
        this.property_for = property_for;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
