package com.yl.fillblankquestiondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 填空题
 * Created by yangle on 2017/9/2.
 * <p>
 * Website：http://www.yangle.tech
 * <p>
 * GitHub：https://github.com/alidili
 * <p>
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * <p>
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fbv_content)
    FillBlankView fbvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        String content = "纷纷扬扬的________下了半尺多厚。天地间________的一片。我顺着________工地走了四十多公里，" +
                "只听见各种机器的吼声，可是看不见人影，也看不见工点。一进灵官峡，我就心里发慌。";

        // 答案范围集合
        List<AnswerRange> rangeList = new ArrayList<>();
        rangeList.add(new AnswerRange(5, 13));
        rangeList.add(new AnswerRange(23, 31));
        rangeList.add(new AnswerRange(38, 46));

        fbvContent.setData(content, rangeList);
    }
}
