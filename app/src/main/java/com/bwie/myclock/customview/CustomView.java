package com.bwie.myclock.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class CustomView extends View {
    private int x, y;//外圆圆心
    private Paint mPaint;
    private int r;//外圆半径
    private Paint paintNum;
    private Paint paintSecond;
    private Paint paintMinute;
    private Paint paintHour;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        //外圆画笔
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        paintNum = new Paint();
        paintNum.setColor(Color.BLACK);
        paintNum.setAntiAlias(true);
        paintNum.setTextSize(35);
        paintNum.setStyle(Paint.Style.STROKE);
        paintNum.setTextAlign(Paint.Align.CENTER);

        paintSecond = new Paint();
        paintSecond.setColor(Color.RED);
        paintSecond.setAntiAlias(true);
        paintSecond.setStrokeWidth(5);
        paintSecond.setStyle(Paint.Style.FILL);

        paintMinute = new Paint();
        paintMinute.setColor(Color.BLACK);
        paintMinute.setAntiAlias(true);
        paintMinute.setStrokeWidth(8);
        paintMinute.setStyle(Paint.Style.FILL);

        paintHour = new Paint();
        paintHour.setColor(Color.BLACK);
        paintHour.setAntiAlias(true);
        paintHour.setStrokeWidth(13);
        paintHour.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //必须获取当前屏幕的宽高,不然会绘制失败
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        x = width / 2;
        y = height / 2;
        r = x - 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制外圆
        canvas.drawCircle(x, y, r, mPaint);
        //绘制圆心
        canvas.drawCircle(x, y, 15, paintMinute);
        //绘制刻度
        drawLines(canvas);
        //绘制数字
        drawText(canvas);
        postInvalidateDelayed(1000);
    }

    //绘制分钟刻度和时刻度
    private void drawLines(Canvas canvas) {
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                //绘制时刻度
                mPaint.setStrokeWidth(8);
                canvas.drawLine(x, y - r, x, y - r + 40, mPaint);
            } else {
                //绘制分钟刻度
                mPaint.setStrokeWidth(3);
                canvas.drawLine(x, y - r, x, y - r + 30, mPaint);
            }
            //绕着(x,y)旋转6
            canvas.rotate(6, x, y);
        }
    }

    private void drawText(Canvas canvas) {
        // 获取文字高度用于设置文本垂直居中
        float textSize = (paintNum.getFontMetrics().bottom - paintNum.getFontMetrics().top);
        // 数字离圆心的距离,40为刻度的长度,20文字大小
        int distance = r - 40 - 20;
        // 数字的坐标(a,b)
        float a, b;
        // 每30°写一个数字
        for (int i = 0; i < 12; i++) {
            double sin = Math.sin(i * 30 * Math.PI / 180);
            Log.i("TAG", "我是sin算出来的数" + sin);
            a = (float) (distance * Math.sin(i * 30 * Math.PI / 180) + x);
            double cos = Math.cos(i * 30 * Math.PI / 180);
            Log.i("TAG", "我是cos算出来的数" + cos);
            b = (float) (y - distance * Math.cos(i * 30 * Math.PI / 180));
            if (i == 0) {
                canvas.drawText("12", a, b + textSize / 3, paintNum);
            } else {
                canvas.drawText(String.valueOf(i), a, b + textSize / 3, paintNum);
            }
        }
    }
}
