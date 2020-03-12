package com.ando.download.queue.speed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ando.download.R;
import com.ando.download.TempData;
import com.ando.download.config.TaskBean;
import com.ando.download.demo.Utils;
import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadContextListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;

import java.util.List;

/**
 * <pre>
 *     Activity 中 :
 *
 *    @Override
 *     public void onDestroy() {
 *         super.onDestroy();
 *         OkDownload.with().downloadDispatcher().cancelAll();
 *     }
 * </pre>
 */
public class DownloadView extends FrameLayout {

    private QueueController4WithSpeed controller;
    private RecyclerView mRvTasks;
    private QueueTaskAdapter4WithSpeed mAdapter;

    private List<TaskBean> taskBeans;

    public DownloadView(@NonNull Context context) {
        this(context, null, 0, 0);
    }

    public DownloadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public DownloadView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DownloadView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        controller = new QueueController4WithSpeed();

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_download_view, this, false);

        mRvTasks = view.findViewById(R.id.rv_tasks);
        mRvTasks.setItemAnimator(null);
        mRvTasks.setHasFixedSize(true);
        mAdapter = new QueueTaskAdapter4WithSpeed(controller);
        mRvTasks.setAdapter(mAdapter);

        addView(view);

    }

    public QueueController4WithSpeed getController() {
        return controller;
    }
    public void notifyDataSetChanged(){
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setData(List<TaskBean> taskBeans, DownloadContextListener listener) {
        this.taskBeans = taskBeans;

        if (listener == null) {
            listener = new DownloadContextListener() {
                @Override
                public void taskEnd(@NonNull DownloadContext context, @NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, int remainCount) {
                }

                @Override
                public void queueEnd(@NonNull DownloadContext context) {
                    // to cancel
                    controller.stop();
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            };
        }
        //快速初始化 use -> createDownloadContextListener
        controller.initTasks(this.taskBeans, getContext(), listener);

        if (mAdapter != null) {
            mAdapter.replaceData(controller.getTaskList());
        }


    }
}