package com.yl.fillblankquestiondemo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 填空题
 * Created by yangle on 2017/8/30.
 */

public class FillBlankView extends RelativeLayout {

    private TextView tvContent;
    private Context context;
    // 答案集合
    private List<String> answerList;
    // 答案范围集合
    private List<AnswerRange> rangeList;
    // 答案填空处ClickableSpan对象集合
    private List<BlankClickableSpan> blankClickableSpanList;
    // 填空题内容
    private SpannableStringBuilder content;

    public FillBlankView(Context context) {
        this(context, null);
    }

    public FillBlankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FillBlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_fill_blank, this);

        tvContent = (TextView) findViewById(R.id.tv_content);
    }

    /**
     * 设置数据
     *
     * @param originContent   源数据
     * @param answerRangeList 答案范围集合
     */
    public void setData(String originContent, List<AnswerRange> answerRangeList) {
        if (TextUtils.isEmpty(originContent) || answerRangeList == null
                || answerRangeList.isEmpty()) {
            return;
        }

        // 获取课文内容
        content = new SpannableStringBuilder(originContent);

        // 答案范围集合
        rangeList = answerRangeList;

        // 设置下划线颜色
        for (AnswerRange range : rangeList) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#83AFF3"));
            content.setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 答案集合
        answerList = new ArrayList<>();
        for (int i = 0; i < rangeList.size(); i++) {
            answerList.add("");
        }

        // 设置填空处点击事件
        blankClickableSpanList = new ArrayList<>();
        for (int i = 0; i < rangeList.size(); i++) {
            AnswerRange range = rangeList.get(i);

            BlankClickableSpan blankClickableSpan = new BlankClickableSpan(i);
            content.setSpan(blankClickableSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            blankClickableSpanList.add(blankClickableSpan);
        }

        // 设置此方法后，点击事件才能生效
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(content);
    }

    /**
     * 点击事件
     */
    class BlankClickableSpan extends ClickableSpan {

        private int position;

        public BlankClickableSpan(int position) {
            this.position = position;
        }

        @Override
        public void onClick(final View widget) {
            final View view = LayoutInflater.from(context).inflate(R.layout.layout_input, null);
            final EditText etInput = (EditText) view.findViewById(R.id.et_answer);

            // 显示原有答案
            String oldAnswer = answerList.get(position);
            if (!TextUtils.isEmpty(oldAnswer)) {
                etInput.setText(oldAnswer);
                etInput.setSelection(oldAnswer.length());
            }

            new AlertDialog.Builder(context, R.style.DialogStyle)
                    .setTitle("请输入答案")
                    .setView(view)
                    .setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String answer = etInput.getText().toString();
                            if (TextUtils.isEmpty(answer)) {
                                Toast.makeText(context, "答案不能为空", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            fillAnswer(answer, position);
                        }
                    })
                    .create()
                    .show();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // 不显示下划线
            ds.setUnderlineText(false);
        }
    }

    /**
     * 填写答案
     *
     * @param answer   当前填空处答案
     * @param position 填空位置
     */
    private void fillAnswer(String answer, int position) {
        answer = " " + answer + " ";

        // 移除原来的点击事件
        content.removeSpan(blankClickableSpanList.get(position));

        // 替换答案
        AnswerRange range = rangeList.get(position);
        content.replace(range.start, range.end, answer);

        // 更新当前的答案范围
        AnswerRange currentRange = new AnswerRange(range.start,
                range.start + answer.length());
        rangeList.set(position, currentRange);

        // 重新设置点击事件
        content.setSpan(new BlankClickableSpan(position), currentRange.start, currentRange.end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 答案设置下划线
        content.setSpan(new UnderlineSpan(),
                currentRange.start, currentRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.setText(content);

        // 将答案添加到集合中
        answerList.set(position, answer.replace(" ", ""));

        for (int i = 0; i < rangeList.size(); i++) {
            if (i > position) {
                // 获取下一个答案原来的范围
                AnswerRange oldNextRange = rangeList.get(i);
                int oldNextAmount = oldNextRange.end - oldNextRange.start;
                // 计算新旧答案字数的差值
                int difference = currentRange.end - range.end;

                // 移除原来的点击事件
                content.removeSpan(blankClickableSpanList.get(i));

                // 更新下一个答案的范围
                AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                        oldNextRange.start + difference + oldNextAmount);
                rangeList.set(i, nextRange);

                // 重新设置点击事件
                content.setSpan(new BlankClickableSpan(i), nextRange.start, nextRange.end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (!TextUtils.isEmpty(answerList.get(i))) {
                    // 答案设置下划线
                    content.setSpan(new UnderlineSpan(),
                            nextRange.start, nextRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                // 更新下一个答案的点击范围
                tvContent.setText(content);
            }
        }
    }

    /**
     * 获取答案列表
     *
     * @return 答案列表
     */
    public List<String> getAnswerList() {
        return answerList;
    }
}
