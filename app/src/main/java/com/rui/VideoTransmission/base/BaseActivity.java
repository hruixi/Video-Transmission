package com.rui.VideoTransmission.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rui.VideoTransmission.R;
import org.greenrobot.eventbus.EventBus;
import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity
        implements IProgressBar, IToolbar, IViewActivity{
//    private ProgressBarDialog mProgressBarDialog;
    // mHostLayout = mToolbar + mRootLayout
    private Toolbar mToolbar;         //标题栏
    private TextView mToolbarTitleTv; //标题文本框
    protected LinearLayout mHostLayout; //BaseActivity的布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHostLayout();
        // 先initViewBinding()，再setContentView(mHostLayout)
        initViewBinding();
        setContentView(mHostLayout);
        registerEventBus();
        addToolbar();

        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }



    /**
     * 在baseActivity里统一管理toolbar，需要初始化BaseActivity的布局文件activity_base
     */
    private void initHostLayout() {
        //通过findViewById(android.R.id.content)拿到window的ViewGroup然后将声明的mHostLayout添加到这个ViewGroup中
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        mHostLayout = new LinearLayout(this);
        mHostLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(mHostLayout);
        getLayoutInflater().inflate(R.layout.activity_base_host, mHostLayout, true);

        mToolbar = mHostLayout.findViewById(R.id.base_toolbar);
        mToolbarTitleTv = mHostLayout.findViewById(R.id.toolbar_titleTv);
    }

    private void registerEventBus() {
        if (isNeedEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }
    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 是否需要eventbus
     */
    protected  boolean isNeedEventBus() {
        return false;
    }

    private void addToolbar() {
        if (isNeedToolbar()) {
            setSupportActionBar(mToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setNavigationIcon(setToolbarIcon(R.drawable.ic_back));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            //TextView动态赋值后，xml文件里面固定的跑马灯属性已经不可用，需要用代码添加跑马灯属性
            mToolbarTitleTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mToolbarTitleTv.setMarqueeRepeatLimit(-1);
            mToolbarTitleTv.setSingleLine(true);
            mToolbarTitleTv.setSelected(true);
            mToolbarTitleTv.setFocusable(true);
            mToolbarTitleTv.setFocusableInTouchMode(true);
            mToolbarTitleTv.setText(setToolbarTitle());
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }
    /**
     * 是否展示ToolBar
     * @return
     */
    protected boolean isNeedToolbar() {
        return false;
    }

    //---------------------------- IProgressbar -------------------------//
    @Override
    public void showProgressBar() {
//        mProgressBarDialog.show();
    }
    @Override
    public void hideProgressBar() {
//        mProgressBarDialog.dismiss();
    }

    //---------------------------- IToolbar -------------------------//
    @Override
    public Drawable setToolbarIcon(@DrawableRes int iconId) {
        return getResources().getDrawable(iconId);
    }

    @Override
    public String setToolbarTitle() {
        return "title";
    }

    @Override
    public boolean isShowToolbarIcon() {
        return true;
    }
}
