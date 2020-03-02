package com.ando.download.queue.speed;

import com.ando.download.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.liulishuo.okdownload.DownloadTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Title: QueueTaskAdapter4WithSpeed
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/15  16:47
 */
public class QueueTaskAdapter4WithSpeed extends BaseQuickAdapter<DownloadTask, BaseViewHolder> {

    private QueueController4WithSpeed controller;

    public QueueTaskAdapter4WithSpeed(QueueController4WithSpeed controller) {
        super(R.layout.item_queue_speed);
        this.controller = controller;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable DownloadTask bean) {
        if (bean != null) {
            controller.bind(this, holder, bean);
        }
    }

    @Override
    public int getItemCount() {
        return controller.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}