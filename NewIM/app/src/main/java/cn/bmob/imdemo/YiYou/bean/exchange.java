package cn.bmob.imdemo.YiYou.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/7/23.
 */

public class exchange extends BmobObject {
    private String name;
    private Integer leftnumber;
    private Integer integral;
    private String url;
    private String beizhu;
    private  Integer paixu;

    public Integer getPaixu() {
        return paixu;
    }

    public void setPaixu(Integer paixu) {
        this.paixu = paixu;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getLeftnumber() {
        return leftnumber;
    }

    public void setLeftnumber(Integer leftnumber) {
        this.leftnumber = leftnumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
