package cn.bmob.imdemo.bean;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

import cn.bmob.imdemo.db.NewFriend;
import cn.bmob.v3.BmobUser;

/**
 * @author :smile
 * @project:User
 * @date :2016-01-22-18:11
 */
public class User extends BmobUser {
    private String Studentkey;
    private String studentid;
    //这个保存校区地理位置的状态，这个状态为false不能发动态，匹配
    private Boolean confirmState;
    //这个保存学号和真人验证状态，这个状态验证完了应该相应的加积分
    private String avatar;
    private String qianming;
    private String realavatar;
    private Integer mywallet;
    private String[] interest;
    private String manageid;
    private List<String> follow;
    private List<String> pictures;
    private String index;
    private String MyReferences;
    private Integer aweek;
    private String sex;




    public Boolean getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(Boolean confirmState) {
        this.confirmState = confirmState;
    }

    public Integer getAweek() {
        return aweek;
    }

    public void setAweek(Integer aweek) {
        this.aweek = aweek;
    }

    public String getMyReferences() {
        return MyReferences;
    }

    public void setMyReferences(String myReferences) {
        MyReferences = myReferences;
    }

    public Integer getMywallet() {
        return mywallet;
    }

    public void setMywallet(Integer mywallet) {
        this.mywallet = mywallet;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<String> getFollow() {
        return follow;
    }

    public void setFollow(List<String> follow) {
        this.follow = follow;
    }

    public String getManageid() {
        return manageid;
    }

    public void setManageid(String manageid) {
        this.manageid = manageid;
    }

    public String[] getInterest() {
        return interest;
    }

    public void setInterest(String[] interest) {
        this.interest = interest;
    }


    public String getRealavatar() {
        return realavatar;
    }

    public void setRealavatar(String realavatar) {
        this.realavatar = realavatar;
    }

    public String getQianming() {
        return qianming;
    }

    public void setQianming(String qianming) {
        this.qianming = qianming;
    }

    public String getStudentkey() {
        return Studentkey;
    }

    public void setStudentkey(String studentkey) {
        Studentkey = studentkey;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public boolean isConfirmState() {
        return confirmState;
    }

    public void setConfirmState(boolean confirmState) {
        this.confirmState = confirmState;
    }

    public User(){}

    public User(NewFriend friend){
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
