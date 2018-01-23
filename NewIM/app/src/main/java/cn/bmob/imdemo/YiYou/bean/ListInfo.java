package cn.bmob.imdemo.YiYou.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/4/24.
 */

public class ListInfo extends BmobObject implements Serializable {
    private String belong;
    private String name;
    private String index;
    private String info;
    private String number;
    private Integer zan;
    private Integer za;
    private String firsturl;
    private Boolean isBlack;
    private String  userimage;
    private String infosex;
    private Integer ping;
    private String[] arr;
    private String userid;
    private String useridmanage;

    //
    private String system;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUseridmanage() {
        return useridmanage;
    }

    public void setUseridmanage(String useridmanage) {
        this.useridmanage = useridmanage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }

    public Integer getPing() {
        return ping;
    }

    public void setPing(Integer ping) {
        this.ping = ping;
    }

    public String getInfosex() {
        return infosex;
    }

    public void setInfosex(String infosex) {
        this.infosex = infosex;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getFirsturl() {
        return firsturl;
    }

    public void setFirsturl(String firsturl) {
        this.firsturl = firsturl;
    }

    public Integer getZan() {
        return zan;
    }

    public void setZan(Integer zan) {
        this.zan = zan;
    }

    public Integer getZa() {
        return za;
    }

    public void setZa(Integer za) {
        this.za = za;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
