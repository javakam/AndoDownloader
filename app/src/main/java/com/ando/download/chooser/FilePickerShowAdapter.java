package com.ando.download.chooser;

import com.ando.download.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import androidx.annotation.NonNull;

/**
 * Title:FilePickerShowAdapter
 * <p>
 * Description: 选择附件后显示
 * <p>
 * 注意，请不要在convert方法里注册控件id
 * </p>
 *
 * @author Changbao
 * @date 2020/1/21 14:35
 */
public class FilePickerShowAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {

    public FilePickerShowAdapter() {
        super(R.layout.item_file_picker_show);

        // 先注册需要点击的子控件id（注意，请不要写在convert方法里）
        addChildClickViewIds(R.id.xmlRootLayout, R.id.btn_file_upload);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, FileBean fileBean) {
        holder.getView(R.id.iv_type);

        holder.setText(R.id.tv_name, fileBean.getName());
        holder.setText(R.id.tv_detail, "类型:" + fileBean.getMimeType() + "  路径:" + fileBean.getPath());


    }

}