package com.pcp.myapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.pcp.myapp.R;
import com.pcp.myapp.base.activity.BaseActivity;
import com.pcp.myapp.net.DataManager;
import com.pcp.myapp.net.MainPresenter;
import com.pcp.myapp.net.NetCallBack;
import com.pcp.myapp.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {

    @BindView(R.id.tvTestTitle)
    AppCompatTextView tvTestTitle;
    @BindView(R.id.tvTestContent)
    AppCompatTextView tvTestContent;
    @BindView(R.id.tvTestAnswer)
    AppCompatTextView tvTestAnswer;
    @BindView(R.id.etTestAnswer)
    EditText etTestAnswer;
    @BindView(R.id.tvTestCommit)
    AppCompatTextView tvTestCommit;

    private MainPresenter newPresenter;
    private final static String TEST_ID = "test_id";
    private final static String TEST_TITLE = "test_title";
    private final static String TEST_ANSWER = "test_answer";

    public static void launchActivity(Activity activity, String id, String title, String answer) {
        Intent intent = new Intent(activity, TestActivity.class);
        intent.putExtra(TEST_ID, id);
        intent.putExtra(TEST_TITLE, title);
        intent.putExtra(TEST_ANSWER, answer);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        AppCompatTextView tvTitle = findViewById(R.id.tvCommonTitle);
        tvTitle.setText("问答详情");
        AppCompatImageView ivBack = findViewById(R.id.ivCommonBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent() != null) {
            String id = getIntent().getStringExtra(TEST_ID);
            String title = getIntent().getStringExtra(TEST_TITLE);
            String answer = getIntent().getStringExtra(TEST_ANSWER);
            if (!TextUtils.isEmpty(title)) {
                tvTestTitle.setText("问题：" + title);
            }
            if (!TextUtils.isEmpty(answer)) {
                tvTestContent.setText(answer);
            }
            etTestAnswer.setVisibility(View.VISIBLE);
            loadAnswer(id);
        }
    }

    @Override
    protected void initPresenter() {
        newPresenter = new MainPresenter(new DataManager());
    }

    private void loadAnswer(String id) {
        newPresenter.loadAnswer(id, new NetCallBack<String>() {
            @Override
            public void onLoadSuccess(String resultList) {
                LogUtils.d("loadCategoryList_ok==" + resultList);
            }

            @Override
            public void onLoadFailed(String errMsg) {
                LogUtils.e("loadCategoryList_err==" + errMsg);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
