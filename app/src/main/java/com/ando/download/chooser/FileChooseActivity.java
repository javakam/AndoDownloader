package com.ando.download.chooser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ando.download.R;
import com.ando.download.base.BaseSwipeItemActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ando.download.config.FileConstant.REQUEST_CHOOSEFILE;

/**
 * Title:
 * <p>
 * Description: 选择文件;上传文件
 * <p>
 * Changed From : https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
 * </p>
 *
 * @author Changbao
 * @date 2020/1/21  11:01
 */
public class FileChooseActivity extends BaseSwipeItemActivity {

    private TextView tv_file_path;
    private Button bt_file_path;
    private FilePickerShowAdapter mAdapter;
    private List<FileBean> mFiles;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mFiles = new ArrayList<>();

        tv_file_path = findViewById(R.id.tv_file_path);
        bt_file_path = findViewById(R.id.bt_file_path);

        //选择文件
        bt_file_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        //
        mAdapter = new FilePickerShowAdapter();
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.w("123", "mFiles : " + mFiles.size() + "position : " + position);

                if (view.getId() == R.id.xmlRootLayout) {
                    Intent intent = Intent.createChooser(OpenFileUtils.openFile(getApplicationContext(),
                            mFiles.get(position).getPath()),
                            "选择程序");
                    startActivity(intent);

                } else if (view.getId() == R.id.btn_file_upload) {
                    //上传文件
                    //todo 2020年1月22日 16:51:49  文件上传
                    //https://blog.csdn.net/k_bb_666/article/details/79612555  代码:  https://github.com/kb18519142009/UploadService
                    //Okhttp Official Demo https://github.com/square/okhttp/tree/master/samples


                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDeleteClick(SwipeMenuBridge menuBridge, int position, int menuPosition) {
        mFiles.remove(position);
        mAdapter.replaceData(mFiles);
    }

    private void chooseFile() {
        //选择文件【调用系统的文件管理】
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        //intent.setType("file/*");//比*/*少了一些侧边栏选项
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CHOOSEFILE);
    }


    private String chooseFilePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//选择文件返回
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOOSEFILE:
                    Uri uri = data.getData();
                    chooseFilePath = FileChooser.getChooseFileResultPath(this, uri);
                    Log.d("123", "选择文件返回：" + chooseFilePath);

                    final FileBean fileBean = new FileBean();
                    fileBean.setId(UUID.randomUUID().toString().replace("-", ""));
                    fileBean.setPath(chooseFilePath);
                    fileBean.setMimeType(data.getType());

                    fileBean.setName(FileChooser.getFileName(chooseFilePath));

                    //刚刚选择的文件
                    tv_file_path.setText(chooseFilePath);

                    mFiles.add(fileBean);
                    mAdapter.replaceData(mFiles);


                    break;
                default:
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_choose;
    }


}