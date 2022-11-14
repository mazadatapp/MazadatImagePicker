package com.mazadatimagepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class RectangleHole extends ViewGroup {

    float dp;
    int aspect_ratio_x,aspect_ratio_y;
    public RectangleHole(Context context) {
        super(context);
        init(context);
    }

    public RectangleHole(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public RectangleHole(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        dp=context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void setAspectRatio(int x,int y) {
        aspect_ratio_x=x;
        aspect_ratio_y=y;
        invalidate();
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint black=new Paint();
        black.setColor(Color.parseColor("#cc000000"));
        canvas.drawRect(0,0,getWidth(),getHeight(),black);

        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float width= getWidth()*2.6f/3;
        float height=width*aspect_ratio_y/aspect_ratio_x;
        canvas.drawRect(getWidth()/2 - width/2,getHeight()/2 -height/2, getWidth()/2 + width/2,getHeight()/2 + height/2, eraser);
    }
}
