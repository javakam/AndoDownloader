package com.ando.download.many

import android.util.Log
import android.util.SparseArray
import android.widget.ProgressBar
import android.widget.TextView
import com.ando.download.R
import com.ando.download.config.ProgressUtil
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener1
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist

 class QueueListener : DownloadListener1() {

    private val holderMap = SparseArray<BaseViewHolder>()

    fun bind(task: DownloadTask, holder: BaseViewHolder) {
        Log.i(TAG, "bind " + task.id + " with " + holder)
        // replace.
        val size = holderMap.size()
        for (i in 0 until size) {
            if (holderMap.valueAt(i) === holder) {
                holderMap.removeAt(i)
                //holderMap.put(i, null)
                break
            }
        }
        holderMap.put(task.id, holder)
    }

    fun resetInfo(task: DownloadTask, holder: BaseViewHolder) {
        //Holder View
        val tvStatus: TextView = holder.getView(R.id.tv_down_status)
        val progressBar: ProgressBar = holder.getView(R.id.progressbar_down)

        // task name
        val taskName = TagUtil.getTaskName(task)
        holder.setText(R.id.tv_down_name, taskName)

        // process references
        val status = TagUtil.getStatus(task)
        if (status != null) {
            //  started
            tvStatus.text = status
            if (status == EndCause.COMPLETED.name) {
                progressBar.progress = progressBar.max
            } else {
                val total = TagUtil.getTotal(task)
                if (total == 0L) {
                    progressBar.progress = 0
                } else {
                    //向 ProgressBar 设置进度
                    ProgressUtil.calcProgressToViewAndMark(progressBar,
                            TagUtil.getOffset(task), total, false)
                }
            }
        } else {
            // non-started
            val statusOnStore = StatusUtil.getStatus(task)
            TagUtil.saveStatus(task, statusOnStore.toString())
            if (statusOnStore == StatusUtil.Status.COMPLETED) {
                tvStatus.text = EndCause.COMPLETED.name
                progressBar.progress = progressBar.max
            } else {
                when (statusOnStore) {
                    StatusUtil.Status.IDLE    -> tvStatus.setText(R.string.state_idle)
                    StatusUtil.Status.PENDING -> tvStatus.setText(R.string.state_pending)
                    StatusUtil.Status.RUNNING -> tvStatus.setText(R.string.state_running)
                    else                      -> tvStatus.setText(R.string.state_unknown)
                }

                if (statusOnStore == StatusUtil.Status.UNKNOWN) {
                    progressBar.progress = 0
                } else {

                    //断点信息
                    val info = StatusUtil.getCurrentInfo(task)
                    if (info != null) {
                        TagUtil.saveTotal(task, info.totalLength)
                        TagUtil.saveOffset(task, info.totalOffset)
                        //向 ProgressBar 设置进度
                        ProgressUtil.calcProgressToViewAndMark(progressBar,
                                info.totalOffset, info.totalLength, false)
                    } else {
                        progressBar.progress = 0
                    }
                }
            }
        }
    }

    fun clearBoundHolder() = holderMap.clear()

    override fun taskStart(
            task: DownloadTask,
            model: Listener1Assist.Listener1Model
    ) {
        val status = "taskStart"
        TagUtil.saveStatus(task, status)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)
    }

    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {
        val status = "retry"
        TagUtil.saveStatus(task, status)

        val holder = holderMap.get(task.id) ?: return
        holder.setText(R.id.tv_down_status, status)
    }

    override fun connected(
            task: DownloadTask,
            blockCount: Int,
            currentOffset: Long,
            totalLength: Long
    ) {
        val status = "connected"
        TagUtil.saveStatus(task, status)
        TagUtil.saveOffset(task, currentOffset)
        TagUtil.saveTotal(task, totalLength)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)

        ProgressUtil.calcProgressToViewAndMark(
                holder.getView(R.id.progressbar_down),
                currentOffset,
                totalLength,
                false
        )
    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        val status = "progress"
        TagUtil.saveStatus(task, status)
        TagUtil.saveOffset(task, currentOffset)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)

        Log.i(TAG, "progress " + task.id + " with " + holder)
        ProgressUtil.updateProgressToViewWithMark(holder.getView(R.id.progressbar_down), currentOffset, false)
    }

    override fun taskEnd(
            task: DownloadTask,
            cause: EndCause,
            realCause: Exception?,
            model: Listener1Assist.Listener1Model
    ) {
        val status = cause.toString()
        TagUtil.saveStatus(task, status)

        Log.w(TAG, "${task.url} end with: $cause")
        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)

        if (cause == EndCause.COMPLETED) {
            var progressBar: ProgressBar = holder.getView(R.id.progressbar_down)
            progressBar.progress = progressBar.max
        }
    }

    companion object {
        private const val TAG = "123"
    }
}