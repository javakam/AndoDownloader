package com.ando.download.many;

import android.widget.Button;
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

    private QueueController controller;
    public ManyTaskAdapter( QueueController controller) {
        super(R.layout.item_queue);
        this.controller=controller;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DownloadTask bean) {
        Button itemAction = holder.getView(R.id.bt_down_action);
        TextView nameTv = holder.getView(R.id.tv_down_name);
        TextView statusTv = holder.getView(R.id.tv_down_status);
        ProgressBar progressBar = holder.getView(R.id.progressbar_down);

    }

    @Override
    public int getItemCount() {
        return controller.size();
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