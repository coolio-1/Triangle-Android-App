package com.coolio1.android.triangleandroidapp;

/**
 * Created by coolio1 on 30/3/18.
 */
import android.content.Context;
import android.graphics.*;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private Path mTrianglePath;
    private Circle mCircle1;
    private Circle mCircle2;
    private Circle mCircle3;
    private Circle mCircles[];
    private Paint mPaint;
    private Paint mCirclePaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int mDrawColor;
    private int mBackgroundColor;

    public class Circle
    {
        float centreX;
        float centreY;
        int radius;
        Circle(float x, float y, int r)
        {
            centreX = x;
            centreY = y;
            radius = r;
        }
    }

    DrawingView(Context context)
    {
        this(context, null);

    }

    public DrawingView (Context context, AttributeSet attributeSet)
    {
        super(context);
        mBackgroundColor = ResourcesCompat.getColor(getResources(),
                R.color.white, null);
        mDrawColor = ResourcesCompat.getColor(getResources(),
                R.color.opaque_yellow, null);

        mCircles = new Circle[3];

        mTrianglePath = new Path();

        mPaint = new Paint();
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mDrawColor);
        mPaint.setColor(mDrawColor);
        mPaint.setAntiAlias(true);
        // Dithering affects how colors with higher-precision device
        // than the are down-sampled.
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE); // default: FILL
        mPaint.setStrokeJoin(Paint.Join.ROUND); // default: MITER
        mPaint.setStrokeCap(Paint.Cap.ROUND); // default: BUTT
        mPaint.setStrokeWidth(12);


    }

    @Override
    protected void onSizeChanged(int width, int height,
                                 int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
// Create bitmap, create canvas with bitmap, fill canvas with color.
        mBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
// Fill the Bitmap with the background color.
        mCanvas.drawColor(mBackgroundColor);
        mCircles[0] = new Circle(width / 2 - 125, height / 2 - 100, 75);
        mCircles[1] = new Circle(width / 2 + 125, height / 2 - 100, 75);
        mCircles[2] = new Circle(width / 2, height / 2 + 100 , 75);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draws a white background
        canvas.drawBitmap(mBitmap, 0, 0, null);
        //Initiating the triangular path
        mTrianglePath = new Path();
        mTrianglePath.moveTo(mCircles[0].centreX, mCircles[0].centreY);
        mTrianglePath.lineTo(mCircles[1].centreX, mCircles[1].centreY);
        mTrianglePath.lineTo(mCircles[2].centreX, mCircles[2].centreY);
        mTrianglePath.lineTo(mCircles[0].centreX, mCircles[0].centreY);
       //Drawing the triangular path
        canvas.drawPath(mTrianglePath, mPaint);
        //Drawing the circles or edges
        for(Circle circle: mCircles)
        canvas.drawCircle(circle.centreX, circle.centreY, circle.radius, mCirclePaint);



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int circleNumber;
        // Invalidate() is inside the case statements because there are many
        // other types of motion events passed into this listener,
        // and we don't want to invalidate the view for those.
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:


                // No need to invalidate
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("ActionMove", "YES");
                circleNumber = isCircleTouched(x, y);

                if(circleNumber != -1) {
                    touchMove(x, y, circleNumber);

                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("ActionUp", "YES");
                invalidate();

                break;
            default:
                // Do nothing.
        }
        return true;
    }

    //Called when any circle is dragged
    public void touchMove(float x, float y, int circleNumber)
    {
        mCircles[circleNumber].centreX = x;
        mCircles[circleNumber].centreY = y;

    }


    public int isCircleTouched(float x, float y)
    {
        for(int i = 0;i < 3;i++)
        {
            float dx = x - mCircles[i].centreX;
            float dy = y - mCircles[i].centreY;
            if((dx*dx + dy*dy) <= mCircles[i].radius*mCircles[i].radius)
            {
                return  i;
            }
        }
        return -1;
    }



}
