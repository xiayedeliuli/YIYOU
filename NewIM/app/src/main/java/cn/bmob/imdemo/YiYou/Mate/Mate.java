package cn.bmob.imdemo.YiYou.Mate;

import cn.bmob.v3.BmobObject;

/**匹配表
 * @author smile
 * @project Friend
 * @date 2016-04-26
 */
public class Mate extends BmobObject{
    /*初始化属性*/
    //确定的匹配对象属性，用于让对方跳转到自己的匹配用户界面
    private String[] CertainMateUser;
    private String BelongToUerID;
    private String sex;

    //是否匹配状态
    private Boolean WhetherMatching;
    //用于控制表内其他数据改变时不被监听
    private Boolean MateUserFlag;
    //匹配属性：性别，项目，时间
    private String MateItem;
    private String MateSex;
    private Long MateTIme;
    //同步锁ID
    private String SynchronizedID;
    //监听这个数组的改变，一改变就跳转到指定的activity对话
    private String[] MateUserActivity;




    public Long getMateTIme() {
        return MateTIme;
    }

    public void setMateTIme(Long mateTIme) {
        MateTIme = mateTIme;
    }

    public String[] getMateUserActivity() {
        return MateUserActivity;
    }

    public void setMateUserActivity(String[] mateUserActivity) {
        MateUserActivity = mateUserActivity;
    }

    public Boolean getMateUserFlag() {
        return MateUserFlag;
    }

    public void setMateUserFlag(Boolean mateUserFlag) {
        MateUserFlag = mateUserFlag;
    }

    public String getSynchronizedID() {
        return SynchronizedID;
    }

    public void setSynchronizedID(String synchronizedID) {
        SynchronizedID = synchronizedID;
    }

    public String[] getCertainMateUser() {
        return CertainMateUser;
    }

    public void setCertainMateUser(String[] certainMateUser) {
        CertainMateUser = certainMateUser;
    }

    public Boolean getWhetherMatching() {
        return WhetherMatching;
    }

    public void setWhetherMatching(Boolean whetherMatching) {
        WhetherMatching = whetherMatching;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMateItem() {
        return MateItem;
    }

    public void setMateItem(String mateItem) {
        MateItem = mateItem;
    }

    public String getMateSex() {
        return MateSex;
    }

    public void setMateSex(String mateSex) {
        MateSex = mateSex;
    }

    public String getBelongToUerID() {
        return BelongToUerID;
    }

    public void setBelongToUerID(String belongToUerID) {
        BelongToUerID = belongToUerID;
    }
}
