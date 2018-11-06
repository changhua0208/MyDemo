package com.houyi.notarization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/11/6 11:53 AM
 */

public class AddTxtResActivity extends BaseNotaActivity {
    public static final int CODE_ADD_RESOURCE = 101;

    @BindView(R.id.gv_res_list)
    GridView mGird;

    private GridAdapter mGridAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitivy_add_textres);
        mGridAdapter = new GridAdapter();
        List<String> mock = new ArrayList<String>();
        mock.add("xxxxxxx1.dox");
        mock.add("xxxxxxx2.dox");
        mock.add("xxxxxxxxx3.dox");
        mock.add("xxxxxxxxxx4.dox");
        mock.add("xxxxxxx1.dox");
        mock.add("xxxxxxx2.dox");
        mock.add("xxxxxxxxx3.dox");
        mock.add("xxxxxxxxxx4.dox");
        mock.add("xxxxxxx1.dox");
        mock.add("xxxxxxx2.dox");
        mock.add("xxxxxxxxx3.dox");
        mock.add("xxxxxxxxxx4.dox");
        mock.add("xxxxxxx1.dox");
        mock.add("xxxxxxx2.dox");
        mock.add("xxxxxxxxx3.dox");
        mock.add("xxxxxxxxxx4.dox");
        mock.add("xxxxxxx1.dox");
        mock.add("xxxxxxx2.dox");
        mock.add("xxxxxxxxx3.dox");
        mock.add("xxxxxxxxxx4.dox");
        mGridAdapter.addList(mock);
        mGird.setAdapter(mGridAdapter);
    }

    @OnClick(R.id.btn_add_txt_res)
    public void onAddTxtResource(){
        Intent intent = new Intent(this,AddTxtResourceDlg.class);
        startActivityForResult(intent,CODE_ADD_RESOURCE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_ADD_RESOURCE && resultCode == RESULT_OK){

        }
    }

    class GridAdapter extends BaseAdapter{
        private List<String> list = new ArrayList<>();

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
            name.setText(list.get(position));
            if((position + 1) % 2 == 0){
                name.setBackgroundResource(R.drawable.bg_red_large_btn);
            }
            return view;
        }

        public void addList(List<String> list){
            if(list != null)
                this.list.addAll(list);
            notifyDataSetChanged();
        }
    }
}
