package cn.bmob.imdemo.YiYou.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by DELL on 2017/7/23.
 */

public class MessageKefu extends BmobObject {
   private String head;
    private String text;
    private String userimage;

    private String kefuid;
    private String url;
    private Boolean iskefu;
    private String kefuname;
    private String kefuavater;

    private Integer nownumber;

    public Integer getNownumber() {
        return nownumber;
    }

    public void setNownumber(Integer nownumber) {
        this.nownumber = nownumber;
    }

    public String getKefuname() {
        return kefuname;
    }

    public void setKefuname(String kefuname) {
        this.kefuname = kefuname;
    }

    public String getKefuavater() {
        return kefuavater;
    }

    public void setKefuavater(String kefuavater) {
        this.kefuavater = kefuavater;
    }

    public String getKefuid() {
        return kefuid;
    }

    public void setKefuid(String kefuid) {
        this.kefuid = kefuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIskefu() {
        return iskefu;
    }

    public void setIskefu(Boolean iskefu) {
        this.iskefu = iskefu;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }


}
