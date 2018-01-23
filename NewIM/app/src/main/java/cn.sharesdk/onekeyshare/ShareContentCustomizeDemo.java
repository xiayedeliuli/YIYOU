package cn.sharesdk.onekeyshare;

import cn.bmob.imdemo.bean.User;
import cn.bmob.v3.BmobUser;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by DELL on 2017/9/17.
 */

public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {
    @Override
    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
        if(SinaWeibo.NAME.equals(platform.getName())){
            User user = BmobUser.getCurrentUser(User.class);
            String text="我的邀请码"+user.getObjectId()+"定个时间，定个地点，就能约出男神女神！不信你来试";
            paramsToShare.setText(text);
        }
    }
}
