package fodel.com.fodelscanner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import fodel.com.fodelscanner.R;

/**
 * Created by fula on 2018/7/19.
 */

public class CircleTextView extends android.support.v7.widget.AppCompatTextView {

    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 画笔颜色 默认灰色
     */
    private int paintNormalColor = 0xFFDCDCDC;

    public CircleTextView(Context context) {
        super(context);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context,attrs);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint(context,attrs);
    }

    /**
     * 初始化画笔和自定义属性
     * @param context
     * @param attrs
     */
    private void initPaint(Context context,AttributeSet attrs){
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);
        paintNormalColor = typeArray.getColor(R.styleable.CircleTextView_paintNormalColor,paintNormalColor);
        mPaint = new Paint();
    }

    /**
     * 调用onDraw绘制边框
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(paintNormalColor);
        mPaint.setStyle(Paint.Style.FILL);
        //创建一个区域,限制圆弧范围
        RectF rectF = new RectF();
        //设置半径,比较长宽,取最大值
        int radius = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();
        //设置Padding 不一致,绘制出的是椭圆;一致的是圆形
        rectF.set(getPaddingLeft(),getPaddingTop(),radius-getPaddingRight(),radius-getPaddingBottom());
        //绘制圆弧
        canvas.drawArc(rectF,0,360,false,mPaint);
        //最后调用super方法,解决文本被所绘制的圆圈背景锁覆盖的问题
        super.onDraw(canvas);
    }
}
