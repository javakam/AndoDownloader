package com.ando.download.queue.speed

import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.ando.download.R
import com.ando.download.queue.DownloadListener1Status
import com.ando.download.queue.ProgressUtils
import com.ando.download.queue.QueueTagUtils
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend.Listener4SpeedModel

/**
 * Title: QueueDownloadListener4WithSpeed
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/3/2  14:38
 */
class QueueDownloadListener4WithSpeed : DownloadListener4WithSpeed() {
    companion object {
        private const val TAG = "123"
    }

    private var totalLength: Long = 0
    private var readableTotalLength: String? = null

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
        val ivDelete: ImageView = holder.getView(R.id.iv_down_delete)
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
                //tvAction.setText(R.string.delete)
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
                ivDelete.visibility = View.VISIBLE
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

    override fun taskStart(task: DownloadTask) {
        Log.i("123", "【1、taskStart】")

        val status = DownloadListener1Status.TASKSTART
        QueueTagUtils.saveStatus(task, status)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)
        holder.setText(R.id.bt_down_action, R.string.cancel)
    }

    override fun infoReady(task: DownloadTask, info: BreakpointInfo, fromBreakpoint: Boolean, model: Listener4SpeedModel) {
        totalLength = info.totalLength
        readableTotalLength = Util.humanReadableBytes(totalLength, true)
        Log.i("123", "【2、infoReady】当前进度" + info.totalOffset.toFloat() / totalLength * 100 + "%" + "，" + info.toString())
    }

    override fun connectStart(task: DownloadTask, blockIndex: Int, requestHeaders: Map<String?, List<String?>?>) {
        Log.i("123", "【3、connectStart】$blockIndex")
    }

    override fun connectEnd(task: DownloadTask, blockIndex: Int, responseCode: Int, responseHeaders: Map<String?, List<String?>?>) {
        Log.i("123", "【4、connectEnd】$blockIndex，$responseCode")

        val status = DownloadListener1Status.CONNECTED
        QueueTagUtils.saveStatus(task, status)
        QueueTagUtils.saveTotal(task, totalLength)
    }

    override fun progressBlock(task: DownloadTask, blockIndex: Int, currentBlockOffset: Long, blockSpeed: SpeedCalculator) { //Log.i("123", "【5、progressBlock】" + blockIndex + "，" + currentBlockOffset);
        Log.w("123", "【5、progressBlock】 currentOffset=[$currentBlockOffset]，totalLength=$totalLength , speed=${blockSpeed.speed()}")

        val status = DownloadListener1Status.PROGRESS
        QueueTagUtils.saveStatus(task, status)

        val holder = holderMap.get(task.id) ?: return
        ProgressUtils.calcProgressToViewAndMark(
                holder.getView(R.id.progressbar_down),
                currentBlockOffset,
                totalLength,
                false
        )

    }

    override fun progress(task: DownloadTask, currentOffset: Long, taskSpeed: SpeedCalculator) {
        val readableOffset = Util.humanReadableBytes(currentOffset, true)
        val progressStatus = "$readableOffset/$readableTotalLength"
        val speed = taskSpeed.speed()
        val percent = currentOffset.toFloat() / totalLength * 100

        //eg: 【6、progress】13195049[13.2 MB/43.4 MB]，速度：667.4 kB/s，进度：30.385971%
        // Log.i("123", "【6、progress】$currentOffset[$progressStatus]，速度：$speed，进度：$percent%")
        Log.i("123", "【6、progress】$currentOffset[$progressStatus]，速度：$speed，进度：$percent%")


        val status = DownloadListener1Status.PROGRESS
        QueueTagUtils.saveStatus(task, status)
        QueueTagUtils.saveOffset(task, currentOffset)

        val holder = holderMap.get(task.id) ?: return

        holder.setText(R.id.tv_down_status, status)
        holder.setText(R.id.bt_down_action, R.string.cancel)
        holder.setText(R.id.tv_down_written_total, progressStatus)
        holder.setText(R.id.tv_down_speed, speed)
        holder.setText(R.id.tv_down_percent, "$percent%")


        ProgressUtils.updateProgressToViewWithMark(holder.getView(R.id.progressbar_down), currentOffset, false)

    }

    override fun blockEnd(task: DownloadTask, blockIndex: Int, info: BlockInfo?, blockSpeed: SpeedCalculator) {
        Log.i("123", "【7、blockEnd】$blockIndex")
    }

    override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?, taskSpeed: SpeedCalculator) {
        Log.i("123", "【8、taskEnd】" + cause.name + "：" + if (realCause != null) realCause.message else "无异常")


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
                val ivDelete: ImageView = holder.getView(R.id.iv_down_delete)
                ivDelete.visibility = View.VISIBLE
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
            val ivDelete: ImageView = holder.getView(R.id.iv_down_delete)
            ivDelete.visibility = View.VISIBLE
        }
    }
}