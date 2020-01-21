package com.ando.download.queue;

import com.ando.download.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liulishuo.okdownload.DownloadTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Title: QueueTaskAdapter
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  16:47
 */
public class QueueTaskAdapter extends BaseQuickAdapter<DownloadTask, BaseViewHolder> {

    private QueueController controller;

    public QueueTaskAdapter(QueueController controller) {
        super(R.layout.item_queue);
        this.controller = controller;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DownloadTask bean) {
        if (bean!=null) {
            controller.bind(holder, bean);
        }

//        Button itemAction = holder.getView(R.id.bt_down_action);
//        TextView nameTv = holder.getView(R.id.tv_down_name);
//        TextView statusTv = holder.getView(R.id.tv_down_status);
//        ProgressBar progressBar = holder.getView(R.id.progressbar_down);

    }

    @Override
    public int getItemCount() {
        return controller.size();
    }

}