package com.yl.mvvmdemo.ui;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yl.mvvmdemo.R;
import com.yl.mvvmdemo.databinding.ActivityMainBinding;
import com.yl.mvvmdemo.viewmodel.ExpressViewModel;

/**
 * 主页
 * Created by yangle on 2017/7/26.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */

public class MainActivity extends BaseActivity {

    private ProgressDialog progressDialog;
    private ExpressViewModel expressViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        expressViewModel = new ExpressViewModel(this, this, binding);
        binding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expressViewModel.getExpressInfo("yuantong", "11111111111");
            }
        });

        // 显示Loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取快递信息...");
        expressViewModel.isShowLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (expressViewModel.isShowLoading.get()) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });

        // 显示错误信息
        expressViewModel.errorMessage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Toast.makeText(MainActivity.this, expressViewModel.errorMessage.get(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
