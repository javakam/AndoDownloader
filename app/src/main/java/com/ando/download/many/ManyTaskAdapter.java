package com.ando.download.many;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.ando.download.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liulishuo.okdownload.DownloadTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Title: ManyTaskAdapter
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  16:47
 */
public class ManyTaskAdapter extends BaseQuickAdapter<DownloadTask, BaseViewHolder> {

    public ManyTaskAdapter() {
        super(R.layout.item_many_task);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DownloadTask bean) {
        TextView tvInfo = holder.getView(R.id.tv_task_info);
        TextView tvStart = holder.getView(R.id.tv_task_start);
        ProgressBar progressBar = holder.getView(R.id.progress_bar_task);


    }

//    private void download(int position) {
//        DownloadTask task = getData().get(position);
//        // 0：没有下载  1：下载中  2：暂停  3：完成
//        if (itemInfo.status == 0) {
//            if (task == null) {
//                task = createDownloadTask(itemInfo);
//                map.put(itemInfo.pkgName, task);
//            }
//            task.enqueue(createDownloadListener(position));
//            itemInfo.status = 1; //更改状态
//            Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
//        } else if (itemInfo.status == 1) {//下载中
//            if (task != null) {
//                task.cancel();
//            }
//            itemInfo.status = 2;
//            Toast.makeText(this, "暂停下载", Toast.LENGTH_SHORT).show();
//        } else if (itemInfo.status == 2) {
//            if (task != null) {
//                task.enqueue(createDownloadListener(position));
//            }
//            itemInfo.status = 1;
//            Toast.makeText(this, "继续下载", Toast.LENGTH_SHORT).show();
//        } else if (itemInfo.status == 3) {//下载完成的，直接跳转安装APP
//            Utils.launchOrInstallApp(this, itemInfo.pkgName);
//            Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
//        }
//    }

}