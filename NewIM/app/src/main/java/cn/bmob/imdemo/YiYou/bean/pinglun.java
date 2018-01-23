package cn.bmob.imdemo.YiYou.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/4/24.
 */

 public class pinglun extends BmobObject implements Serializable {
    private String ketitext;
    private String zhuti;
    private String text;
    private String keti;
    private Boolean  panduan;
    private String userimage;
    private String sex;
    private String belongobjectid;
    private Integer NoteType;
    private Boolean IsNoticeClick;
    private String userid;
    private String touserid;
    private String username;
    private String t0manageid;
    private String manageid;
    private String tomyuserid;
    private Boolean isjX;

    //
    private String system;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Boolean getIsjX() {
        return isjX;
    }

    public void setIsjX(Boolean isjX) {
        this.isjX = isjX;
    }

    public String getTomyuserid() {
        return tomyuserid;
    }

    public void setTomyuserid(String tomyuserid) {
        this.tomyuserid = tomyuserid;
    }

    public String getManageid() {
        return manageid;
    }

    public void setManageid(String manageid) {
        this.manageid = manageid;
    }

    public String getT0manageid() {
        return t0manageid;
    }

    public void setT0manageid(String t0manageid) {
        this.t0manageid = t0manageid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getPanduan() {
        return panduan;
    }

    public void setPanduan(Boolean panduan) {
        this.panduan = panduan;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTouserid() {
        return touserid;
    }

    public void setTouserid(String touserid) {
        this.touserid = touserid;
    }

    public Boolean getNoticeClick() {
        return IsNoticeClick;
    }

    public void setNoticeClick(Boolean noticeClick) {
        IsNoticeClick = noticeClick;
    }

    public Integer getNoteType() {
        return NoteType;
    }

    public void setNoteType(Integer noteType) {
        NoteType = noteType;
    }

    public String getBelongobjectid() {
        return belongobjectid;
    }

    public void setBelongobjectid(String belongobjectid) {
        this.belongobjectid = belongobjectid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getKetitext() {
        return ketitext;
    }

    public void setKetitext(String ketitext) {
        this.ketitext = ketitext;
    }

    public String getZhuti() {
        return zhuti;
    }

    public void setZhuti(String zhuti) {
        this.zhuti = zhuti;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public String getKeti() {
        return keti;
    }

    public void setKeti(String keti) {
        this.keti = keti;
    }


}