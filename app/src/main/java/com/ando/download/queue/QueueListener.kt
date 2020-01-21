package com.ando.download.queue

import android.util.Log
import android.util.SparseArray
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.ando.download.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.core.Util
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
        val tvAction: Button = holder.getView(R.id.bt_down_action)
        val tvStatus: TextView = holder.getView(R.id.tv_down_status)
        val progressBar: ProgressBar = holder.getView(R.id.progressbar_down)

        // task name
        val taskName = QueueTagUtils.getTaskName(task)
        holder.setText(R.id.tv_down_name, taskName)

        // process references
        val status = QueueTagUtils.getStatus(task)

        Log.i(TAG, "setProgress $status")

        if (status != null) {
            //  started
            tvStatus.text = status
            if (status == EndCause.COMPLETED.name) {
                progressBar.progress = progressBar.max
                //  tvAction.setText(R.string.delete)
            } else {
                // tvAction.setText(R.string.cancel)
                val total = QueueTagUtils.getTotal(task)
                if (total == 0L) {
                    progressBar.progress = 0
                } else {
                    //向 ProgressBar 设置进度
                    ProgressUtils.calcProgressToViewAndMark(progressBar,
                            QueueTagUtils.getOffset(task), total, false)
                }
            }
        } else {
            // non-started
            val statusOnStore = StatusUtil.getStatus(task)
            QueueTagUtils.saveStatus(task, statusOnStore.toString())
            if (statusOnStore == StatusUtil.Status.COMPLETED) {
                tvStatus.text = EndCause.COMPLETED.name
                progressBar.progress = progressBar.max
                //  tvAction.setText(R.string.delete)
            } else {
                when (statusOnStore) {
                    StatusUtil.Status.IDLE    -> {
                        tvStatus.setText(R.string.state_idle);// tvAction.setText(R.string.start)
                    }
                    StatusUtil.Status.PENDING -> {
                        tvStatus.setText(R.string.state_pending); //tvAction.setText(R.string.start)
                    }
                    StatusUtil.Status.RUNNING -> {
                        tvStatus.setText(R.string.state_running); //tvAction.setText(R.string.cancel)
                    }
                    else                      -> {
                        tvStatus.setText(R.string.state_unknown); //tvAction.setText(R.string.start)
                    }
                }

                if (statusOnStore == StatusUtil.Status.UNKNOWN) {
                    progressBar.progress = 0
                    // tvAction.setText(R.string.start)
                } else {
                    //  tvAction.setText(R.string.start)

                    //断点信息
                    val info = StatusUtil.getCurrentInfo(task)
                    if (info != null) {
                        QueueTagUtils.saveTotal(task, info.totalLength)
                        QueueTagUtils.saveOffset(task, info.totalOffset)
                        //向 ProgressBar 设置进度
                        ProgressUtils.calcProgressToViewAndMark(progressBar,
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
        val status = DownloadListener1Status.TASKSTART
        QueueTagUtils.saveStatus(task, status)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)
        holder.setText(R.id.bt_down_action, R.string.cancel)

    }

    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {
        val status = DownloadListener1Status.RETRY
        QueueTagUtils.saveStatus(task, status)

        val holder = holderMap.get(task.id) ?: return
        holder.setText(R.id.tv_down_status, status)
        holder.setText(R.id.bt_down_action, R.string.retry)
    }

    override fun connected(
            task: DownloadTask,
            blockCount: Int,
            currentOffset: Long,
            totalLength: Long
    ) {
        val status = DownloadListener1Status.CONNECTED
        QueueTagUtils.saveStatus(task, status)
        QueueTagUtils.saveOffset(task, currentOffset)
        QueueTagUtils.saveTotal(task, totalLength)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)
        holder.setText(R.id.bt_down_action, R.string.cancel)

        ProgressUtils.calcProgressToViewAndMark(
                holder.getView(R.id.progressbar_down),
                currentOffset,
                totalLength,
                false
        )

    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        val status = DownloadListener1Status.PROGRESS

        QueueTagUtils.saveStatus(task, status)
        QueueTagUtils.saveOffset(task, currentOffset)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)
        holder.setText(R.id.bt_down_action, R.string.cancel)


        //todo 2020年1月17日 15:30:52 暂不支持查看下载速度  DownloadListener4WithSpeed
        // val speed: String = taskSpeed.speed()


        //eg: 48.7 MB/48.9 MB
        val readableTotalLength = Util.humanReadableBytes(totalLength, true)
        val readableOffset = Util.humanReadableBytes(currentOffset, true)
        val progressStatus = "$readableOffset/$readableTotalLength"

        //eg: 进度：100.0%
        val percent = currentOffset.toFloat() / totalLength * 100

        //eg: 【6、progress】48738304[48.7 MB/48.9 MB]，速度：2.4 MB/s，进度：99.60372%
        // Log.i("123", "【6、progress】$currentOffset[$progressStatus]，速度：$speed，进度：$percent%")

        Log.i("123", "【progress】$currentOffset[$progressStatus]，进度：$percent%")

        ProgressUtils.updateProgressToViewWithMark(holder.getView(R.id.progressbar_down), currentOffset, false)

    }

    override fun taskEnd(
            task: DownloadTask,
            cause: EndCause,
            realCause: Exception?,
            model: Listener1Assist.Listener1Model
    ) {
        val status = cause.toString()
        QueueTagUtils.saveStatus(task, status)

        Log.w(TAG, "${task.file?.absolutePath} end with: $cause ]---===---[  Exception : ${realCause?.message}")
        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)
        //DownloadTask.cancel 已完成->delete else 未完成->继续 / 开始
        val taskStatus = StatusUtil.getStatus(task)
        when {
            taskStatus == StatusUtil.Status.COMPLETED -> {
                holder.setText(R.id.bt_down_action, R.string.delete)
            }
            task.info?.totalOffset ?: -1 > 0L         -> {
                holder.setText(R.id.bt_down_action, R.string.goon)
            }
            else                                      -> {
                holder.setText(R.id.bt_down_action, R.string.start)
            }
        }

        if (cause == EndCause.COMPLETED) {
            val progressBar: ProgressBar = holder.getView(R.id.progressbar_down)
            progressBar.progress = progressBar.max
        }
    }

    companion object {
        private const val TAG = "123"
    }
}