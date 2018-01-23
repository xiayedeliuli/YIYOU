package cn.bmob.imdemo.YiYou.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/7/23.
 */

public class LastInfo extends BmobObject implements Serializable {
    private String LastItem;
    private String LastSpace;
    private Long LastTime;
    private Integer Int;
    private Boolean Aarrive;
    private Boolean Acancel;
    private String Asex;
    private String Aname;
    private String AID;
    private String Aavater;
    private Boolean Barrive;
    private Boolean Bcancel;
    private String Bsex;
    private String Bname;
    private String BID;
    private String Bavater;
    private Boolean isDELETE;
    private Boolean isJXpipei;


    public Boolean getJXpipei() {
        return isJXpipei;
    }

    public void setJXpipei(Boolean JXpipei) {
        isJXpipei = JXpipei;
    }

    public Boolean getDELETE() {
        return isDELETE;
    }

    public void setDELETE(Boolean DELETE) {
        isDELETE = DELETE;
    }

    public Boolean getAarrive() {
        return Aarrive;
    }

    public void setAarrive(Boolean aarrive) {
        Aarrive = aarrive;
    }

    public Boolean getAcancel() {
        return Acancel;
    }

    public void setAcancel(Boolean acancel) {
        Acancel = acancel;
    }

    public String getAsex() {
        return Asex;
    }

    public void setAsex(String asex) {
        Asex = asex;
    }

    public String getAname() {
        return Aname;
    }

    public void setAname(String aname) {
        Aname = aname;
    }

    public Boolean getBcancel() {
        return Bcancel;
    }

    public void setBcancel(Boolean bcancel) {
        Bcancel = bcancel;
    }

    public String getBsex() {
        return Bsex;
    }

    public void setBsex(String bsex) {
        Bsex = bsex;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getBavater() {
        return Bavater;
    }

    public void setBavater(String bavater) {
        Bavater = bavater;
    }

    public String getBname() {
        return Bname;
    }

    public void setBname(String bname) {
        Bname = bname;
    }

    public Boolean getBarrive() {
        return Barrive;
    }

    public void setBarrive(Boolean barrive) {
        Barrive = barrive;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getAavater() {
        return Aavater;
    }

    public void setAavater(String aavater) {
        Aavater = aavater;
    }

    public String getLastItem() {
        return LastItem;
    }

    public void setLastItem(String lastItem) {
        LastItem = lastItem;
    }

    public String getLastSpace() {
        return LastSpace;
    }

    public void setLastSpace(String lastSpace) {
        LastSpace = lastSpace;
    }

    public Long getLastTime() {
        return LastTime;
    }

    public void setLastTime(Long lastTime) {
        LastTime = lastTime;
    }


    public Integer getInt() {
        return Int;
    }

    public void setInt(Integer anInt) {
        Int = anInt;
    }
}
