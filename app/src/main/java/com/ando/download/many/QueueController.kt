package com.ando.download.many

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.ando.download.DemoUtil
import com.ando.download.R
import com.ando.download.config.TaskBean
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.liulishuo.okdownload.DownloadContext
import com.liulishuo.okdownload.DownloadContextListener
import com.liulishuo.okdownload.DownloadTask
import java.io.File

class QueueController {
    private val taskList = arrayListOf<DownloadTask>()
    private var context: DownloadContext? = null
    private val listener = QueueListener()
    private var queueDir: File? = null

    private var urlList: List<TaskBean>? = null

    fun initTasks(urlList: List<TaskBean>, context: Context, listener: DownloadContextListener) {

        val set = DownloadContext.QueueSet()
        val parentFile = File(DemoUtil.getParentFile(context), "queue")
        this.queueDir = parentFile
        this.urlList = urlList

        set.setParentPathFile(parentFile)
        set.minIntervalMillisCallbackProcess = 200

        val builder = set.commit()

        for (taskBean in urlList) {
            var boundTask = builder.bind(taskBean.url)
            TagUtil.saveTaskName(boundTask, taskBean.tname)
        }

        builder.setListener(listener)

        //1 builder.build() 生成 DownloadContext 对象
        //2 把 DownloadContext.tasks 放 taskList 中一份
        this.context = builder.build().also { this.taskList.addAll(it.tasks) }

    }

    fun deleteFiles() {
        if (queueDir != null) {
            val children = queueDir!!.list()
            if (children != null) {
                for (child in children) {
                    if (!File(queueDir, child).delete()) {
                        Log.w("123", "delete $child failed!")
                    }
                }
            }

            if (!queueDir!!.delete()) {
                Log.w("123", "delete $queueDir failed!")
            }
        }

        for (task in taskList) {
            TagUtil.clearProceedTask(task)
        }
    }

    fun setPriority(task: DownloadTask, priority: Int) {
        val newTask = task.toBuilder().setPriority(priority).build()
        this.context = context?.toBuilder()
                ?.bindSetTask(newTask)
                ?.build()
                ?.also {
                    //修改优先级之后,重新设置 taskList 数据
                    taskList.clear()
                    taskList.addAll(it.tasks)
                }
        newTask.setTags(task)
        TagUtil.savePriority(newTask, priority)
    }

    fun start(isSerial: Boolean) {
        this.context?.start(listener, isSerial)
    }

    fun stop() {
        if (this.context?.isStarted == true) {
            this.context?.stop()
        }
    }

    fun start(holder: BaseViewHolder, position: Int) {

        val task = taskList[position]
        Log.d(TAG, "bind " + position + " for " + task.url)

        //DownloadTask.Id 对应 ViewHolder
        listener.bind(task, holder)
        //ViewHolder 设置数据
        listener.resetInfo(task, holder)

        task.enqueue(CustomDownloadListener4WithSpeed())

//        // priority
//        val priority = TagUtil.getPriority(task)
//        holder.priorityTv.text = holder.priorityTv.context.getString(R.string.priority, priority)
//        holder.prioritySb.progress = priority
//
//        if (this.context?.isStarted == true) {
//            holder.prioritySb.isEnabled = false
//        } else {
//            holder.prioritySb.isEnabled = true
//            println(2)
//
//        }

    }

    internal fun bind(holder: BaseViewHolder, position: Int) {
        val task = taskList[position]
        Log.d(TAG, "bind " + position + " for " + task.url)

        //DownloadTask.Id 对应 ViewHolder
        listener.bind(task, holder)
        //ViewHolder 设置数据
        listener.resetInfo(task, holder)

        //Holder View
        val itemAction: Button = holder.getView(R.id.bt_down_action)
        val tvStatus: TextView = holder.getView(R.id.tv_down_status)
        val progressBar: ProgressBar = holder.getView(R.id.progressbar_down)

        // 开始
        itemAction.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                start(holder, position)
            }
        })

        /*// priority
        val priority = TagUtil.getPriority(task)
        holder.priorityTv.text = holder.priorityTv.context.getString(R.string.priority, priority)
        holder.prioritySb.progress = priority
        if (this.context?.isStarted == true) {
            holder.prioritySb.isEnabled = false
        } else {

            holder.prioritySb.isEnabled = true

            //非下载中状态下,可以调整优先级
            holder.prioritySb.setOnSeekBarChangeListener(
                    object : SeekBar.OnSeekBarChangeListener {
                        var isFromUser: Boolean = false

                        override fun onProgressChanged(
                                seekBar: SeekBar,
                                progress: Int,
                                fromUser: Boolean
                        ) {
                            isFromUser = fromUser
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {}

                        override fun onStopTrackingTouch(seekBar: SeekBar) {
                            if (isFromUser) {
                                val taskPriority = seekBar.progress
                                setPriority(task, taskPriority)
                                holder.priorityTv.text =
                                    seekBar.context.getString(R.string.priority, taskPriority)
                            }
                        }
                    })
        }*/

    }

    fun size(): Int = taskList.size

    companion object {
        private const val TAG = "123"
    }

}