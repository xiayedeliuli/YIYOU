package cn.bmob.imdemo.YiYou.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/7/23.
 */

public class complain extends BmobObject {
    //通用属性
   private String USERID;
    private String USERimage;
    private String USERname;
    //区分类型，分为三种投诉，兑换，验证
    private String type;
    //是否完成
    private Boolean IsCompleted;


    //后面这三个是为了积分兑换那一页刷新方便
    private String ExchangeUrl;
    private Integer integral;
    private String beizhu_dh;
    private String phonenumber;

    //后面这两个是用于投诉
    private String wascomplainedUSERID;
    private String compliainTEXT;
    private String wascomplainedAvater;

    //后面三个是用于学号验证
    private String studentid;
    private String studentkey;
    private String realavater;
    private String References;
    private String manageid;





    public String getWascomplainedAvater() {
        return wascomplainedAvater;
    }

    public void setWascomplainedAvater(String wascomplainedAvater) {
        this.wascomplainedAvater = wascomplainedAvater;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getManageid() {
        return manageid;
    }

    public void setManageid(String manageid) {
        this.manageid = manageid;
    }

    public String getUSERimage() {
        return USERimage;
    }

    public void setUSERimage(String USERimage) {
        this.USERimage = USERimage;
    }

    public String getUSERname() {
        return USERname;
    }

    public void setUSERname(String USERname) {
        this.USERname = USERname;
    }

    public String getReferences() {
        return References;
    }

    public void setReferences(String references) {
        References = references;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentkey() {
        return studentkey;
    }

    public void setStudentkey(String studentkey) {
        this.studentkey = studentkey;
    }

    public String getRealavater() {
        return realavater;
    }

    public void setRealavater(String realavater) {
        this.realavater = realavater;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeizhu_dh() {
        return beizhu_dh;
    }

    public void setBeizhu_dh(String beizhu_dh) {
        this.beizhu_dh = beizhu_dh;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public String getExchangeUrl() {
        return ExchangeUrl;
    }

    public void setExchangeUrl(String exchangeUrl) {
        ExchangeUrl = exchangeUrl;
    }

    public Boolean getCompleted() {
        return IsCompleted;
    }

    public void setCompleted(Boolean completed) {
        IsCompleted = completed;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getWascomplainedUSERID() {
        return wascomplainedUSERID;
    }

    public void setWascomplainedUSERID(String wascomplainedUSERID) {
        this.wascomplainedUSERID = wascomplainedUSERID;
    }

    public String getCompliainTEXT() {
        return compliainTEXT;
    }

    public void setCompliainTEXT(String compliainTEXT) {
        this.compliainTEXT = compliainTEXT;
    }
}
