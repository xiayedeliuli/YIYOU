package cn.bmob.imdemo.YiYou.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author :smile
 * @project:User
 * @date :2016-01-22-18:11
 */
public class MyUser extends BmobObject {
    //自我属性
    private String username;
    private String userid;
    private String useravatar;
    private String usersex;
    private String userRealAvater;
    //匹配属性
    private String WasMated;
    private Boolean onback;
    private Boolean ensure;
    private String item;
    private Long date;
    private String choicesex;
    private Integer CountMatePeriod;
    private String SecondDemond;
    //通知对方的属性
    private String OtherObjectId;

    private  String JXpipei;
    private  String JXpipei2;
    private String JXpipei2name;
    private String JXpipei2avatr;
    private String space;
    private Long millis;
    private Boolean areJianting;


    public Boolean getAreJianting() {
        return areJianting;
    }

    public void setAreJianting(Boolean areJianting) {
        this.areJianting = areJianting;
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public Boolean getOnback() {
        return onback;
    }

    public void setOnback(Boolean onback) {
        this.onback = onback;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }




    public String getJXpipei2avatr() {
        return JXpipei2avatr;
    }

    public void setJXpipei2avatr(String JXpipei2avatr) {
        this.JXpipei2avatr = JXpipei2avatr;
    }

    public String getJXpipei2name() {
        return JXpipei2name;
    }

    public void setJXpipei2name(String JXpipei2name) {
        this.JXpipei2name = JXpipei2name;
    }

    public String getJXpipei2() {
        return JXpipei2;
    }

    public void setJXpipei2(String JXpipei2) {
        this.JXpipei2 = JXpipei2;
    }

    public String getJXpipei() {
        return JXpipei;
    }

    public void setJXpipei(String JXpipei) {
        this.JXpipei = JXpipei;
    }

    public String getOtherObjectId() {
        return OtherObjectId;
    }

    public void setOtherObjectId(String otherObjectId) {
        OtherObjectId = otherObjectId;
    }

    public String getSecondDemond() {
        return SecondDemond;
    }

    public void setSecondDemond(String secondDemond) {
        SecondDemond = secondDemond;
    }



    public String getChoicesex() {
        return choicesex;
    }

    public void setChoicesex(String choicesex) {
        this.choicesex = choicesex;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Boolean getEnsure() {
        return ensure;
    }

    public void setEnsure(Boolean ensure) {
        this.ensure = ensure;
    }


    public String getUserRealAvater() {
        return userRealAvater;
    }

    public void setUserRealAvater(String userRealAvater) {
        this.userRealAvater = userRealAvater;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsersex() {
        return usersex;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }


    public String getWasMated() {
        return WasMated;
    }

    public void setWasMated(String wasMated) {
        WasMated = wasMated;
    }


    public Integer getCountMatePeriod() {
        return CountMatePeriod;
    }

    public void setCountMatePeriod(Integer countMatePeriod) {
        CountMatePeriod = countMatePeriod;
    }
}


