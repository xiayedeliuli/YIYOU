package cn.bmob.imdemo.YiYou.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.imdemo.R;

/**
 * Created by DELL on 2017/12/23.
 */

public class DrawerLayoutList {
    //侧滑菜单的数据源
    public static List<Map<String, Object>> getlist() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("logo", R.drawable.eye);
        map.put("title", "非法名单");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("logo", R.drawable.cehua_comfirm);
        map.put("title", "校区认证");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("logo", R.drawable.cehua_jiangpin);
        map.put("title", "兑换奖品");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("logo", R.drawable.cehua_kefu);
        map.put("title", "人工客服");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("logo", R.drawable.cehua_pipei);
        map.put("title", "我的匹配");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("logo", R.drawable.cehua_share);
        map.put("title", "分享好友");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("logo", R.drawable.cehua_set2);
        map.put("title", "系统设置");
        list.add(map);

        return list;
    }
}
