package com.houyi.notarization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.utils.CurrentNotaUtils;
import com.houyi.notarization.utils.NotaDaoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author changhua.jiang
 * @since 2018/11/5 9:23 PM
 */

public class PartiesActivity extends BaseActivity {
    public static final int CODE_NEW_IDENTITY = 100;

    @BindView(R.id.rv_identity)
    RecyclerView mRecyclerView;

    MyRecyclerAdapter mAdapter;

    @BindString(R.string.number) String titleNo;
    @BindString(R.string.applicant) String titleName;
    @BindString(R.string.address) String titleAddr;
    @BindString(R.string.birthday) String titleItems;
    @BindString(R.string.comparison) String titleComparison;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        ButterKnife.bind(this);
        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData(){
        Notarization nota = CurrentNotaUtils.currentNota();
        List<Notarization> list = NotaDaoHelper.selectNota(nota.getNid());
        mAdapter.addAll(list);

    }

    @OnClick(R.id.btn_new)
    public void onNew(){
        Intent intent = new Intent(this, NewNotaActivity.class);
        intent.putExtra("role",1);
        startActivityForResult(intent,CODE_NEW_IDENTITY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_NEW_IDENTITY && resultCode == Activity.RESULT_OK){
            String pid = data.getStringExtra("identityNo");
            long nid = data.getLongExtra("nid",-1);
            List<Notarization> list = NotaDaoHelper.selectNota(pid,nid);
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
            //itemView.setOnClickListener();
        }
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<PartiesActivity.IdentityViewHolder>{

        List<Notarization> list;

        public MyRecyclerAdapter(){
            list = new ArrayList<>();
        }

        @Override
        public PartiesActivity.IdentityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout itemView = (LinearLayout) LayoutInflater.from(PartiesActivity.this).inflate(R.layout.row_identity,null);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(p);

            return new PartiesActivity.IdentityViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PartiesActivity.IdentityViewHolder holder, int position) {
            if(position == 0){
                holder.tvName.setText(titleName);
                holder.tvAddr.setText(titleAddr);
                holder.tvNo.setText(titleNo);
                holder.tvComparison.setText(titleComparison);
                holder.tvItems.setText(titleItems);
            }
            else {
                final Notarization nota = list.get(position -1);
                holder.tvName.setText(nota.getPerson().getName());
                holder.tvAddr.setText(nota.getPerson().getAddress());
                holder.tvNo.setText("" + position);
                holder.tvComparison.setText(nota.hasCompared() ? "是":"否");
                holder.tvItems.setText(nota.getPerson().getBirthDay());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CurrentNotaUtils.save(nota);
//                        Intent intent = new Intent(PartiesActivity.this,NotarizationActivity.class);
//                        PartiesActivity.this.startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list == null? 1 : list.size() + 1;
        }

        public void addAll(List<Notarization>  list){
            if(list != null && list.size() > 0){
                int position = this.list.size() + 1;
                this.list.addAll(list);
                notifyItemRangeInserted(position,list.size());
            }
        }

        public void addIdentity(Notarization nota){
            int position = this.list.size() + 1;
            this.list.add(nota);
            notifyItemInserted(position);
        }

        public void remove(Notarization nota){
            if(list.contains(nota)){
                list.remove(nota);
                notifyDataSetChanged();
            }
        }
    }

//    class PopMenu {
//        private PopupWindow mWindow;
//        private int w;
//        private int h;
//        private Person identity;
//
//        public PopMenu(Person identity){
//            this.identity = identity;
//            View contentView=LayoutInflater.from(getContext()).inflate(R.layout.win_select_identity_menu, null, false);
//            w = ScreenUtils.dip2px(getContext(),300);
//            h = ScreenUtils.dip2px(getContext(),300);
//            mWindow = new PopupWindow(contentView,w,h,false);
//            mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            mWindow.setOutsideTouchable(true);
//            mWindow.setTouchable(true);
//            ButterKnife.bind(this,contentView);
//
//            IdentityHelper.getInstance().loadIdentityResource(identity);
//            CurrentIdentityUtils.save(this.identity);
//        }
//
//        public void show(View anchor){
//            mWindow.showAsDropDown(anchor,anchor.getWidth() / 2 - w / 2,10);
//        }
//
//        @OnClick(R.id.btn_comparison)
//        public void onComparision(){
//
//            Intent intent = new Intent(PartiesActivity.this, ComparisonActivity.class);
//            PartiesActivity.this.startActivity(intent);
//            dismiss();
//        }
//
//        @OnClick(R.id.btn_cancel)
//        public void onCancel(){
//            dismiss();
//        }
//
//        @OnClick(R.id.btn_more_details)
//        public void onMoreDetails(){
//            //TODO
//            Intent intent = new Intent(PartiesActivity.this, DetailActivity.class);
//            PartiesActivity.this.startActivity(intent);
//            dismiss();
//        }
//
//        @OnClick(R.id.btn_delete)
//        public void onDelete(){
//            dismiss();
//            new AlertDialog.Builder(PartiesActivity.this)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
//                            daoSession.getPersonDao().delete(identity);
//                            FaceVerifyResultDao faceVerifyResultDao = daoSession.getFaceVerifyResultDao();
//                            List<FaceVerifyResult> list1 = faceVerifyResultDao.queryBuilder().where(FaceVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo()))
//                                    .list();
//                            if(list1.size() > 0){
//                                faceVerifyResultDao.delete(list1.get(0));
//                            }
//
//                            FpVerifyResultDao fpVerifyResultDao = daoSession.getFpVerifyResultDao();
//                            List<FpVerifyResult> list2 = fpVerifyResultDao.queryBuilder().where(FpVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo())).list();
//                            if(list2.size() > 0){
//                                fpVerifyResultDao.delete(list2.get(0));
//                            }
//
//                            IdentityHelper.getInstance().deleteIdentityFiles(identity);
//                            //mAdapter.remove(identity);
//
//                        }
//                    })
//                    .setNegativeButton(R.string.cancel, null)
//                    .setMessage(R.string.msg_delete)
//                    .show();
//
//        }
//
//        private void dismiss(){
//            if(mWindow.isShowing())
//                mWindow.dismiss();
//        }
//    }


}
