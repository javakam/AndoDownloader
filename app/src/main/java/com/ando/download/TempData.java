package com.ando.download;

import com.ando.download.config.TaskBean;
import com.ando.download.demo.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: TempData
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/16  16:15
 */
public class TempData {

    public static List<TaskBean> getTaskBeans(String parentPath) {

        List<TaskBean> list = new ArrayList<>();
        TaskBean taskBean1 = new TaskBean("1. WeChat",
                "http://dldir1.qq.com/weixin/android/weixin6516android1120.apk"
                , parentPath);

        TaskBean taskBean2 = new TaskBean("2. LiuLiShuo",
                "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk"
                , parentPath);

        TaskBean taskBean3 = new TaskBean("3. Alipay",
                "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk"
                , parentPath);

        TaskBean taskBean4 = new TaskBean("4. QQ for Mac",
                "https://dldir1.qq.com/qqfile/QQforMac/QQ_V6.2.0.dmg"
                , parentPath);

        TaskBean taskBean5 = new TaskBean("5. ZhiHu",
                "https://zhstatic.zhihu.com/pkg/store/zhihu/futureve-mobile-zhihu-release-5.8.2(596).apk"
                , parentPath);

        TaskBean taskBean6 = new TaskBean("6. NetEaseMusic",
                "http://d1.music.126.net/dmusic/CloudMusic_official_4.3.2.468990.apk"
                , parentPath);

//
//        url = "http://d1.music.126.net/dmusic/NeteaseMusic_1.5.9_622_officialsite.dmg"
//        boundTask = builder.bind(url)
//        TagUtil.saveTaskName(boundTask, "7. NetEaseMusic for Mac")
//
//        url = "http://dldir1.qq.com/weixin/Windows/WeChatSetup.exe"
//        boundTask = builder.bind(url)
//        TagUtil.saveTaskName(boundTask, "8. WeChat for Windows")
//
//        url = "https://dldir1.qq.com/foxmail/work_weixin/wxwork_android_2.4.5.5571_100001.apk"
//        boundTask = builder.bind(url)
//        TagUtil.saveTaskName(boundTask, "9. WeChat Work")
//
//        url = "https://dldir1.qq.com/foxmail/work_weixin/WXWork_2.4.5.213.dmg"
//        boundTask = builder.bind(url)
//        TagUtil.saveTaskName(boundTask, "10. WeChat Work for Mac")

        list.add(taskBean1);
        list.add(taskBean2);
        list.add(taskBean3);
        list.add(taskBean4);
        list.add(taskBean5);
        list.add(taskBean6);
        return list;
    }


}