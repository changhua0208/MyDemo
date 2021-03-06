package com.houyi.notarization.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.houyi.notarization.ComparisonActivity;
import com.houyi.notarization.DetailActivity;
import com.houyi.notarization.NewNotaActivity;
import com.houyi.notarization.NotarizationActivity;
import com.houyi.notarization.R;
import com.houyi.notarization.mode.DaoSession;
import com.houyi.notarization.mode.FaceVerifyResult;
import com.houyi.notarization.mode.FaceVerifyResultDao;
import com.houyi.notarization.mode.FpVerifyResult;
import com.houyi.notarization.mode.FpVerifyResultDao;
import com.houyi.notarization.mode.Notarization;
import com.houyi.notarization.mode.Person;
import com.houyi.notarization.utils.ApplicationUtils;
import com.houyi.notarization.utils.CurrentIdentityUtils;
import com.houyi.notarization.utils.CurrentNotaUtils;
import com.houyi.notarization.utils.IdentityHelper;
import com.houyi.notarization.utils.NotaDaoHelper;
import com.houyi.notarization.utils.ScreenUtils;

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

public class HomeFragment extends Fragment{
    public static final int CODE_NEW_IDENTITY = 100;

    @BindView(R.id.rv_identity)
    RecyclerView mRecyclerView;

    MyRecyclerAdapter mAdapter;

    @BindString(R.string.address) String titleAddr;
    @BindString(R.string.item) String titleItems;
    @BindString(R.string.applicant) String titleName;
    @BindString(R.string.number) String titleNo;
    @BindString(R.string.notary) String titleComparison;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_identity,null);
        ButterKnife.bind(this,view);

        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        initData();

//        RxBus.getInstance().toObserverable(VerifyEvent.class).subscribe(new Observer<VerifyEvent>(){
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(VerifyEvent verifyEvent) {
//                Person identity = CurrentIdentityUtils.currentIdentity();
//                DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
//                daoSession.getPersonDao().update(identity);
//                mAdapter.notifyDataSetChanged();
//            }
//
//        });

        return view;
    }

    private void initData(){
        List<Notarization> list = NotaDaoHelper.selectNota(0);
        mAdapter.addAll(list);

    }

    @OnClick(R.id.btn_new)
    public void onNew(){
        Intent intent = new Intent(getContext(), NewNotaActivity.class);
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

    class MyRecyclerAdapter extends RecyclerView.Adapter<IdentityViewHolder>{

        List<Notarization> list;

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
                final Notarization nota = list.get(position -1);
                holder.tvName.setText(nota.getPerson().getName());
                holder.tvAddr.setText(nota.getPerson().getAddress());
                holder.tvNo.setText("" + nota.getNid());
                holder.tvComparison.setText(nota.getNotary());
                holder.tvItems.setText(nota.getNotarization());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CurrentNotaUtils.save(nota);
                        Intent intent = new Intent(HomeFragment.this.getActivity(),NotarizationActivity.class);
                        HomeFragment.this.startActivity(intent);
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

    class PopMenu {
        private PopupWindow mWindow;
        private int w;
        private int h;
        private Person identity;

        public PopMenu(Person identity){
            this.identity = identity;
            View contentView=LayoutInflater.from(getContext()).inflate(R.layout.win_select_identity_menu, null, false);
            w = ScreenUtils.dip2px(getContext(),300);
            h = ScreenUtils.dip2px(getContext(),300);
            mWindow = new PopupWindow(contentView,w,h,false);
            mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mWindow.setOutsideTouchable(true);
            mWindow.setTouchable(true);
            ButterKnife.bind(this,contentView);

            IdentityHelper.getInstance().loadIdentityResource(identity);
            CurrentIdentityUtils.save(this.identity);
        }

        public void show(View anchor){
            mWindow.showAsDropDown(anchor,anchor.getWidth() / 2 - w / 2,10);
        }

        @OnClick(R.id.btn_comparison)
        public void onComparision(){

            Intent intent = new Intent(HomeFragment.this.getActivity(), ComparisonActivity.class);
            HomeFragment.this.getActivity().startActivity(intent);
            dismiss();
        }

        @OnClick(R.id.btn_cancel)
        public void onCancel(){
            dismiss();
        }

        @OnClick(R.id.btn_more_details)
        public void onMoreDetails(){
            //TODO
            Intent intent = new Intent(HomeFragment.this.getContext(), DetailActivity.class);
            HomeFragment.this.startActivity(intent);
            dismiss();
        }

        @OnClick(R.id.btn_delete)
        public void onDelete(){
            dismiss();
            new AlertDialog.Builder(HomeFragment.this.getContext())
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DaoSession daoSession = ApplicationUtils.getApplication().getDaoSession();
                            daoSession.getPersonDao().delete(identity);
                            FaceVerifyResultDao faceVerifyResultDao = daoSession.getFaceVerifyResultDao();
                            List<FaceVerifyResult> list1 = faceVerifyResultDao.queryBuilder().where(FaceVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo()))
                                    .list();
                            if(list1.size() > 0){
                                faceVerifyResultDao.delete(list1.get(0));
                            }

                            FpVerifyResultDao fpVerifyResultDao = daoSession.getFpVerifyResultDao();
                            List<FpVerifyResult> list2 = fpVerifyResultDao.queryBuilder().where(FpVerifyResultDao.Properties.IdentityNo.eq(identity.getIdentityNo())).list();
                            if(list2.size() > 0){
                                fpVerifyResultDao.delete(list2.get(0));
                            }

                            IdentityHelper.getInstance().deleteIdentityFiles(identity);
                            //mAdapter.remove(identity);

                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .setMessage(R.string.msg_delete)
                    .show();

        }

        private void dismiss(){
            if(mWindow.isShowing())
                mWindow.dismiss();
        }
    }
}
