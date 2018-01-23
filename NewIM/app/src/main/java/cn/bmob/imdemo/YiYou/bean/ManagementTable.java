package cn.bmob.imdemo.YiYou.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/4/24.
 */

 public class ManagementTable extends BmobObject implements Serializable {
    private Integer jishu;
    private Boolean beoff;
    private Boolean dongjie;
    private Boolean studentcomfirm;
    private Boolean isfenghao;

    public Boolean getIsfenghao() {
        return isfenghao;
    }

    public void setIsfenghao(Boolean isfenghao) {
        this.isfenghao = isfenghao;
    }

    public Boolean getBeoff() {
        return beoff;
    }

    public void setBeoff(Boolean beoff) {
        this.beoff = beoff;
    }

    public Boolean getDongjie() {
        return dongjie;
    }

    public void setDongjie(Boolean dongjie) {
        this.dongjie = dongjie;
    }

    public Boolean getStudentcomfirm() {
        return studentcomfirm;
    }

    public void setStudentcomfirm(Boolean studentcomfirm) {
        this.studentcomfirm = studentcomfirm;
    }

    public Integer getJishu() {
        return jishu;
    }

    public void setJishu(Integer jishu) {
        this.jishu = jishu;
    }
}