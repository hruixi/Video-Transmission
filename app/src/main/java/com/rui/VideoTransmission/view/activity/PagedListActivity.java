package com.rui.VideoTransmission.view.activity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;

import com.rui.VideoTransmission.R;
import com.rui.VideoTransmission.base.BaseActivity;
import com.rui.VideoTransmission.databinding.ActivityPagedListBinding;
import com.rui.VideoTransmission.model.net.NetworkStatus;
import com.rui.VideoTransmission.view.adapter.RedditPagedListAdapter;
import com.rui.VideoTransmission.viewmodel.Common_vm;
import com.rui.VideoTransmission.viewmodel.viewmodel_factory.MainVmFactory;

public class PagedListActivity extends BaseActivity {
    private static final String TAG = "PagedListActivity";

    private ActivityPagedListBinding mBinding;
    private Common_vm mViewModel;
    private RedditPagedListAdapter mAdapter;
//    private LiveData<PagedList<>>

    public static void start(Context from) {
        Intent intent = new Intent(from, PagedListActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void initViewBinding() {
        mBinding = ActivityPagedListBinding.inflate(LayoutInflater.from(this), mHostLayout, true);
    }

    @Override
    public void initData() {
        mViewModel = new ViewModelProvider(this, new MainVmFactory()).get(Common_vm.class);
        initAdapter();

        // TODO
        /** 目前没有查明为什么getDataSource会在DataSource.postValue之前执行
         * （即initAdapter()还没执行完mViewModel.getReddits().observe就开始执行initSwipeToRefresh()）；
         *  所以需要让initSwipeToRefresh()滞后执行 **/
        new Handler().postDelayed(this::initSwipeToRefresh, 500);

    }

    @Override
    public String setToolbarTitle() {
        return "【PagedList Activity】";
    }
    @Override
    protected boolean isNeedToolbar() {
        return true;
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mAdapter = new RedditPagedListAdapter(this);
        mBinding.pagedListRv.setLayoutManager(linearLayoutManager);
        mBinding.pagedListRv.setAdapter(mAdapter);

        /** 当订阅时才会发出网络请求 **/
//        mViewModel.getReddits().observe(this, mAdapter::submitList);
        mViewModel.getReddits().observe(this, childrenDataBeans ->
            mAdapter.submitList(childrenDataBeans, () -> {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.pagedListRv.getLayoutManager();
                if (layoutManager == null)
                    return;
                int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mBinding.pagedListRv.scrollToPosition(position);
                }
            }));
    }

    private void initSwipeToRefresh() {
        mBinding.pagedListSrl.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        setSwipeRefreshing();

        mBinding.pagedListSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 下拉刷新
                mViewModel.refresh();
                setSwipeRefreshing();
            }
        });
    }

    private void setSwipeRefreshing() {
        // 根据加载情况是否显示loading...
        if (mViewModel.getRefreshStatus() != null) {
            mViewModel.getRefreshStatus().observe(PagedListActivity.this, networkStatus -> {
                mBinding.pagedListSrl.setRefreshing(networkStatus == NetworkStatus.LOADING);
            });
        }
    }
}
