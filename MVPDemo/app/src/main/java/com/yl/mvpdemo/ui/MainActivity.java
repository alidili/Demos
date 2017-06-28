package com.yl.mvpdemo.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.mvpdemo.R;
import com.yl.mvpdemo.bean.ExpressInfo;
import com.yl.mvpdemo.presenter.ExpressPresenter;
import com.yl.mvpdemo.view.ExpressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页
 * Created by yangle on 2017/6/26.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */

public class MainActivity extends BaseActivity implements ExpressView {

    @BindView(R.id.tv_post_info)
    TextView tvPostInfo;

    private ProgressDialog progressDialog;
    private ExpressPresenter expressPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        expressPresenter = new ExpressPresenter(this, this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取快递信息...");
    }

    @OnClick(R.id.btn_get_post_info)
    public void onViewClicked() {
        expressPresenter.getExpressInfo("yuantong", "11111111111");
    }

    @Override
    public void updateView(ExpressInfo expressInfo) {
        tvPostInfo.setText(expressInfo.toString());
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
