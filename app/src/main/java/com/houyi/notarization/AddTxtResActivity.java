package com.houyi.notarization;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.utils.CurrentNotaUtils;
import com.houyi.notarization.utils.FileUtils;
import com.houyi.notarization.utils.NotaFileHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author changhua.jiang
 * @since 2018/11/6 11:53 AM
 */

public class AddTxtResActivity extends SimpleNotaActivity {
    //public static final int CODE_ADD_RESOURCE = 101;
    private static final int CODE_OPEN_FILE_BROWSER = 102;

    @BindView(R.id.gv_res_list)
    GridView mGird;

    private GridAdapter mGridAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivy_add_textres);
        mGridAdapter = new GridAdapter();
        Notarization nota = CurrentNotaUtils.currentNota();
        File[] files = NotaFileHelper.getInstance().listTxtResource(nota);
        mGridAdapter.addListNoRefresh(files);
        mGird.setAdapter(mGridAdapter);
    }

//    @OnClick(R.id.btn_add_txt_res)
//    public void onAddTxtResource(){
//        Intent intent = new Intent(this,AddTxtResourceDlg.class);
//        startActivityForResult(intent,CODE_ADD_RESOURCE);
//    }

    @Override
    public void onAddResource(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, CODE_OPEN_FILE_BROWSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_OPEN_FILE_BROWSER && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String path = FileUtils.getRealFilePath(this,uri);
            Notarization nota = CurrentNotaUtils.currentNota();
            try {
                NotaFileHelper.getInstance().saveTextResource(nota,path);
                mGridAdapter.add(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class GridAdapter extends BaseAdapter{
        private List<File> list = new ArrayList<>();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AddTxtResActivity.this).inflate(R.layout.grid_item_blue,null);
            TextView name = view.findViewById(R.id.tv_file_name);
            name.setText(list.get(position).getName());
            if((position + 1) % 2 == 0){
                name.setBackgroundResource(R.drawable.bg_red_large_btn);
            }
            return view;
        }

        public void addList(List<File> list){
            if(list != null)
                this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void addListNoRefresh(File[] files) {
            if (files != null) {
                for (File f : files)
                    this.list.add(f);
            }
        }

        public void add(File file){
            this.list.add(file);
            notifyDataSetChanged();
        }
    }
}
