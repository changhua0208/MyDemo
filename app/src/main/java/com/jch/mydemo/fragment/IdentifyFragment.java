package com.jch.mydemo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jch.mydemo.NewIdentifyActivity;
import com.jch.mydemo.R;
import com.jch.mydemo.mode.DaoSession;
import com.jch.mydemo.mode.Identity;
import com.jch.mydemo.mode.IdentityDao;
import com.jch.mydemo.utils.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/9/10 下午4:52
 */

public class IdentifyFragment extends Fragment {
    public static final int CODE_NEW_IDENTITY = 100;

    @BindView(R.id.rv_identity)
    RecyclerView mRecyclerView;

    MyRecyclerAdapter mAdapter;

//    String titleSex;

    @BindString(R.string.address) String titleAddr;
    @BindString(R.string.item) String titleItems;
    @BindString(R.string.name) String titleName;
    @BindString(R.string.number) String titleNo;
    @BindString(R.string.comparison) String titleComparison;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_identity,null);
        ButterKnife.bind(this,view);

        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        initData();
        return view;
    }

    private void initData(){
        DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
        List<Identity> list = daoSession.getIdentityDao().loadAll();
        mAdapter.addAll(list);

    }

    @OnClick(R.id.btn_new)
    public void onNew(){
        Intent intent = new Intent(getContext(), NewIdentifyActivity.class);
        startActivityForResult(intent,CODE_NEW_IDENTITY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_NEW_IDENTITY && resultCode == Activity.RESULT_OK){
            DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
            String idNo = data.getStringExtra("identityNo");
            List<Identity> list = daoSession.getIdentityDao().queryBuilder()
                    .where(IdentityDao.Properties.IdentityNo.eq(idNo))
                    .list();
            if(list.size() > 0){
                mAdapter.addAll(list);
            }
        }
    }

    class IdentityViewHolder extends  RecyclerView.ViewHolder{
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_addr)
        TextView tvAddr;
        @BindView(R.id.tv_comparison)
        TextView tvComparison;
        @BindView(R.id.tv_items)
        TextView tvItems;

        public IdentityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<IdentityViewHolder>{

        List<Identity> list;

        public MyRecyclerAdapter(){
            list = new ArrayList<>();
        }

        @Override
        public IdentityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.row_identity,null);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(p);
            return new IdentityViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(IdentityViewHolder holder, int position) {
            if(position == 0){
                holder.tvName.setText(titleName);
                holder.tvAddr.setText(titleAddr);
                holder.tvNo.setText(titleNo);
                holder.tvComparison.setText(titleComparison);
                holder.tvItems.setText(titleItems);
            }
            else {
                Identity i = list.get(position -1);
                holder.tvName.setText(i.getName());
                holder.tvAddr.setText(i.getAddress());
                holder.tvNo.setText("" + i.getId());
                holder.tvComparison.setText(i.getComparison());
                holder.tvItems.setText(i.getItems());
            }
        }

        @Override
        public int getItemCount() {
            return list == null? 1 : list.size() + 1;
        }

        public void addAll(List<Identity>  list){
            if(list != null && list.size() > 0){
                int position = this.list.size() + 1;
                this.list.addAll(list);
                notifyItemRangeInserted(position,list.size());
            }
        }

        public void addIdentity(Identity identity){
            int position = this.list.size() + 1;
            this.list.add(identity);
            notifyItemInserted(position);

        }
    }
}
