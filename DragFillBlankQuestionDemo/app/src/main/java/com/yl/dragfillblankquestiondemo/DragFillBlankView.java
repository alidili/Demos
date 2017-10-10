package com.yl.dragfillblankquestiondemo;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 拖拽填空题
 * Created by yangle on 2017/10/9.
 */

public class DragFillBlankView extends RelativeLayout implements View.OnDragListener,
        View.OnLongClickListener {

    private TextView tvContent;
    private LinearLayout llOption;
    // 初始数据
    private String originContent;
    // 初始答案范围集合
    private List<AnswerRange> originAnswerRangeList;
    // 填空题内容
    private SpannableStringBuilder content;
    // 选项列表
    private List<String> optionList;
    // 答案范围集合
    private List<AnswerRange> answerRangeList;
    // 答案集合
    private List<String> answerList;
    // 选项位置
    private int optionPosition;
    // 一次拖拽填空是否完成
    private boolean isFillBlank;

    public DragFillBlankView(Context context) {
        this(context, null);
    }

    public DragFillBlankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFillBlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_drag_fill_blank, this);

        tvContent = (TextView) findViewById(R.id.tv_content);
        llOption = (LinearLayout) findViewById(R.id.ll_option);
    }

    /**
     * 设置数据
     *
     * @param originContent   源数据
     * @param optionList      选项列表
     * @param answerRangeList 答案范围集合
     */
    public void setData(String originContent, List<String> optionList, List<AnswerRange> answerRangeList) {
        if (TextUtils.isEmpty(originContent) || optionList == null || optionList.isEmpty()
                || answerRangeList == null || answerRangeList.isEmpty()) {
            return;
        }

        // 初始数据
        this.originContent = originContent;
        // 初始答案范围集合
        this.originAnswerRangeList = new ArrayList<>();
        this.originAnswerRangeList.addAll(answerRangeList);
        // 获取课文内容
        this.content = new SpannableStringBuilder(originContent);
        // 选项列表
        this.optionList = optionList;
        // 答案范围集合
        this.answerRangeList = answerRangeList;

        // 避免重复创建拖拽选项
        if (llOption.getChildCount() < 1) {
            // 拖拽选项列表
            List<Button> itemList = new ArrayList<>();
            for (String option : optionList) {
                Button btnAnswer = new Button(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, dp2px(10), 0);
                btnAnswer.setLayoutParams(params);
                btnAnswer.setBackgroundColor(Color.parseColor("#4DB6AC"));
                btnAnswer.setTextColor(Color.WHITE);
                btnAnswer.setText(option);
                btnAnswer.setOnLongClickListener(this);
                itemList.add(btnAnswer);
            }

            // 显示拖拽选项
            for (int i = 0; i < itemList.size(); i++) {
                llOption.addView(itemList.get(i));
            }
        } else {
            // 不显示已经填空的选项
            for (int i = 0; i < llOption.getChildCount(); i++) {
                Button button = (Button) llOption.getChildAt(i);
                String option = button.getText().toString();
                if (!answerList.isEmpty() && answerList.contains(option)) {
                    button.setVisibility(INVISIBLE);
                } else {
                    button.setVisibility(VISIBLE);
                }
            }
        }

        // 设置下划线颜色
        for (AnswerRange range : this.answerRangeList) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4DB6AC"));
            content.setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 答案集合
        answerList = new ArrayList<>();
        for (int i = 0; i < answerRangeList.size(); i++) {
            answerList.add("");
        }

        // 设置填空处点击事件
        for (int i = 0; i < this.answerRangeList.size(); i++) {
            AnswerRange range = this.answerRangeList.get(i);
            BlankClickableSpan blankClickableSpan = new BlankClickableSpan(i);
            content.setSpan(blankClickableSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 填空处设置触摸事件
        tvContent.setMovementMethod(new TouchLinkMovementMethod());
        tvContent.setText(content);
        tvContent.setOnDragListener(this);
    }

    /**
     * 更新答案
     *
     * @param answerList 答案列表
     */
    public void updateAnswer(List<String> answerList) {
        // 重新初始化数据
        setData(originContent, optionList, originAnswerRangeList);

        // 重新填写已经存在的答案
        if (answerList != null && !answerList.isEmpty()) {
            for (int i = 0; i < answerList.size(); i++) {
                String answer = answerList.get(i);
                if (!TextUtils.isEmpty(answer)) {
                    fillAnswer(answer, i);
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        startDrag(v);
        return true;
    }


    /**
     * 开始拖拽
     *
     * @param v 当前对象
     */
    private void startDrag(View v) {
        // 选项内容
        String optionContent = ((Button) v).getText().toString();
        // 记录当前答案选项的位置
        optionPosition = getOptionPosition(optionContent);
        // 开始拖拽后在列表中隐藏答案选项
        v.setVisibility(INVISIBLE);

        ClipData.Item item = new ClipData.Item(optionContent);
        ClipData data = new ClipData(null, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
        v.startDrag(data, new DragShadowBuilder(v), null, 0);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED: // 拖拽开始
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            case DragEvent.ACTION_DRAG_ENTERED: // 被拖拽View进入目标区域
                return true;

            case DragEvent.ACTION_DRAG_LOCATION: // 被拖拽View在目标区域移动
                return true;

            case DragEvent.ACTION_DRAG_EXITED: // 被拖拽View离开目标区域
                return true;

            case DragEvent.ACTION_DROP: // 放开被拖拽View
                int position = 0;

                // 获取TextView的Layout对象
                Layout layout = tvContent.getLayout();

                // 当前x、y坐标
                float currentX = event.getX();
                float currentY = event.getY();

                // 如果拖拽答案没有进行填空则return
                boolean isContinue = false;

                for (int i = 0; i < answerRangeList.size(); i++) {
                    AnswerRange range = answerRangeList.get(i);

                    // 获取TextView中字符坐标
                    Rect bound = new Rect();
                    int line = layout.getLineForOffset(range.start);
                    layout.getLineBounds(line, bound);

                    // 字符顶部y坐标
                    int yAxisTop = bound.top - dp2px(10);
                    // 字符底部y坐标
                    int yAxisBottom = bound.bottom + dp2px(5);
                    // 字符左边x坐标
                    float xAxisLeft = layout.getPrimaryHorizontal(range.start) - dp2px(10);
                    // 字符右边x坐标
                    float xAxisRight = layout.getSecondaryHorizontal(range.end) + dp2px(10);

                    if (xAxisRight > xAxisLeft) { // 填空在一行
                        if (currentX > xAxisLeft && currentX < xAxisRight &&
                                currentY < yAxisBottom && currentY > yAxisTop) {
                            position = i;
                            isContinue = true;
                            break;
                        }
                    } else { // 跨行填空
                        if ((currentX > xAxisLeft || currentX < xAxisRight) &&
                                currentY < yAxisBottom && currentY > yAxisTop) {
                            position = i;
                            isContinue = true;
                            break;
                        }
                    }
                }

                if (!isContinue) {
                    return true;
                }

                // 释放拖放阴影，并获取移动数据
                ClipData.Item item = event.getClipData().getItemAt(0);
                String answer = item.getText().toString();

                // 重复拖拽，在答案列表中显示原答案
                String oldAnswer = answerList.get(position);
                if (!TextUtils.isEmpty(oldAnswer)) {
                    llOption.getChildAt(getOptionPosition(oldAnswer)).setVisibility(VISIBLE);
                }

                // 填写答案
                fillAnswer(answer, position);
                isFillBlank = true;
                return true;

            case DragEvent.ACTION_DRAG_ENDED: // 拖拽完成
                if (!isFillBlank) {
                    llOption.getChildAt(optionPosition).setVisibility(VISIBLE);
                } else {
                    isFillBlank = false;
                }
                return true;

            default:
                break;
        }

        return false;
    }

    /**
     * 填写答案
     *
     * @param answer   当前填空处答案
     * @param position 填空位置
     */
    private void fillAnswer(String answer, int position) {
        answer = " " + answer + " ";

        // 替换答案
        AnswerRange range = answerRangeList.get(position);
        content.replace(range.start, range.end, answer);

        // 更新当前的答案范围
        AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
        answerRangeList.set(position, currentRange);

        // 答案设置下划线
        content.setSpan(new UnderlineSpan(),
                currentRange.start, currentRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将答案添加到集合中
        answerList.set(position, answer.replace(" ", ""));

        // 更新内容
        tvContent.setText(content);

        for (int i = 0; i < answerRangeList.size(); i++) {
            if (i > position) {
                // 获取下一个答案原来的范围
                AnswerRange oldNextRange = answerRangeList.get(i);
                int oldNextAmount = oldNextRange.end - oldNextRange.start;
                // 计算新旧答案字数的差值
                int difference = currentRange.end - range.end;

                // 更新下一个答案的范围
                AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                        oldNextRange.start + difference + oldNextAmount);
                answerRangeList.set(i, nextRange);
            }
        }
    }

    /**
     * 触摸事件
     */
    class BlankClickableSpan extends ClickableSpan {

        private int position;

        public BlankClickableSpan(int position) {
            this.position = position;
        }

        @Override
        public void onClick(final View widget) {
            // 显示原有答案
            String oldAnswer = answerList.get(position);
            if (!TextUtils.isEmpty(oldAnswer)) {
                answerList.set(position, "");
                updateAnswer(answerList);
                startDrag(llOption.getChildAt(getOptionPosition(oldAnswer)));
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // 不显示下划线
            ds.setUnderlineText(false);
        }
    }

    /**
     * 获取选项位置
     *
     * @param option 选项内容
     * @return 选项位置
     */
    private int getOptionPosition(String option) {
        for (int i = 0; i < llOption.getChildCount(); i++) {
            Button btnOption = (Button) llOption.getChildAt(i);
            if (btnOption.getText().toString().equals(option)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 获取答案列表
     *
     * @return 答案列表
     */
    public List<String> getAnswerList() {
        return answerList;
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
