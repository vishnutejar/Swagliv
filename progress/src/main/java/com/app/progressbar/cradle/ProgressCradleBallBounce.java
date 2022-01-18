package com.app.progressbar.cradle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.app.progressbar.R;

public class ProgressCradleBallBounce extends View {

    private int width;
    private int height;

    private Paint paint;

    private int loadingColor = Color.RED;
    private Context context;

    public ProgressCradleBallBounce(Context context) {
        super(context);
        this.context=context;
        initView(null);
    }

    public ProgressCradleBallBounce(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView(attrs);
    }

    public ProgressCradleBallBounce(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CradleBallBounce);
            loadingColor = typedArray.getColor(R.styleable.CradleBallBounce_cradle_ball_color, ContextCompat.getColor(context,R.color.colorPrimary));
            typedArray.recycle();
        }
        paint = new Paint();
        paint.setColor(loadingColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
    }

    public void setLoadingColor(int color) {
        loadingColor = color;
        paint.setColor(color);
        postInvalidate();
    }

    public int getLoadingColor() {
        return loadingColor;
    }
}