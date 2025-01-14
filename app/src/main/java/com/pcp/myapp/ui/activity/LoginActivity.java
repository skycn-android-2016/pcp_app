package com.pcp.myapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.ToastUtils;
import com.pcp.myapp.R;
import com.pcp.myapp.base.activity.BaseActivity;
import com.pcp.myapp.base.utils.Constant;
import com.pcp.myapp.base.utils.Utils;
import com.pcp.myapp.bean.LoginBo;
import com.pcp.myapp.custom.CustomEditText;
import com.pcp.myapp.custom.loading.LoadingView;
import com.pcp.myapp.net.DataManager;
import com.pcp.myapp.net.NetCallBack;
import com.pcp.myapp.net.MainPresenter;
import com.pcp.myapp.utils.MmkvUtil;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.username)
    EditText mUsername;

    @BindView(R.id.password)
    CustomEditText mPassword;

    @BindView(R.id.login)
    Button mLoginButton;

    @BindView(R.id.go_register)
    Button mRegister;

    @BindView(R.id.loading_view)
    LoadingView mLoading;

    @BindView(R.id.login_toolbar)
    Toolbar mToolbar;

    private MainPresenter loginPresenter;
    private long mExitTime = 0;

    public static void launchActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initToolbar();
        mLoginButton.getBackground().setColorFilter(
                Utils.getColor(activity), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected void initPresenter() {
        loginPresenter = new MainPresenter(new DataManager());
        //loginPresenter.attachView(this);
    }

    private void initToolbar() {
        getWindow().setStatusBarColor(Utils.getColor(activity));
        mToolbar.setBackgroundColor(Utils.getColor(activity));
        mToolbar.setTitle("欢迎登录");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.login)
    public void login() {
        String userNameText = mUsername.getText().toString();
        String passWordText = mPassword.getText().toString();
        if (TextUtils.isEmpty(userNameText) || TextUtils.isEmpty(passWordText)) {
            ToastUtils.showShort(getString(R.string.complete_info));
            return;
        }
        startAnim();
        loginPresenter.login(userNameText, passWordText, new NetCallBack<LoginBo>() {
            @Override
            public void onLoadSuccess(LoginBo data) {
                stopAnim();
                MmkvUtil.saveRole(data.role);
                MmkvUtil.saveUserName(data.username);
                MainActivity.launchActivity(activity);
            }

            @Override
            public void onLoadFailed(String errMsg) {
                stopAnim();
                ToastUtils.showShort(errMsg);
            }
        });
    }

    @OnClick(R.id.go_register)
    public void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - mExitTime > Constant.EXIT_TIME) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = curTime;
        } else {
            super.onBackPressed();
            //System.exit(0);
        }
    }


    private void startAnim() {
        mLoading.setVisibility(View.VISIBLE);
        mLoading.startTranglesAnimation();
    }

    private void stopAnim() {
        mLoading.setVisibility(View.GONE);
    }
}
