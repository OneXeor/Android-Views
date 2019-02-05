package io.singulart.pulse_button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class PulseButton extends View {

    private Paint circlePaint;
    private Paint circleStrokePaint;
    private Paint circleStrokeOuterPaint;

    private int pbColorCenterCircle;
    private int pbColorCenterCircleStroke;
    private int[] pbColorWaveCircleStrokeArr;

    private float pbWavePadding;
    private float pbWavePaddingInterAct;

    private float pbInnerCircleStrokePadding;

    private float pbInnerCircleStrokeWidth;
    private float pbOuterCircleStrokeWidth;

    @Nullable
    private Bitmap bmIcon;

    private Rect srcRect;

    private static int DEFAULT_ALPHA = 255;

    public PulseButton(Context context) {
        super(context);
        initDef();
    }

    public PulseButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (context != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.PulseButton, 0, 0);

            pbColorCenterCircle = a.getColor(R.styleable.PulseButton_pb_color_center_circle, Color.WHITE);
            pbColorCenterCircleStroke = a.getColor(R.styleable.PulseButton_pb_color_center_circle_stroke, Color.WHITE);

            pbWavePadding = a.getDimension(R.styleable.PulseButton_pb_wave_padding, 10);
            pbWavePaddingInterAct = pbWavePadding;
            pbInnerCircleStrokePadding = a.getDimension(R.styleable.PulseButton_pb_inner_circle_stroke_padding, 20);
            int pbColorWaveCircleStroke = a.getColor(R.styleable.PulseButton_pb_color_wave_circle_stroke, Color.WHITE);
            pbColorWaveCircleStrokeArr = getARGB(pbColorWaveCircleStroke);

            pbInnerCircleStrokePadding = a.getDimension(R.styleable.PulseButton_pb_stroke_width_of_center_circle, 2);
            pbOuterCircleStrokeWidth = a.getDimension(R.styleable.PulseButton_pb_stroke_width_of_outer_circle, 2);
            int srcId = a.getResourceId(R.styleable.PulseButton_pb_src, -1);

            if (srcId != -1) {
                Drawable drawable = ContextCompat.getDrawable(context, srcId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable instanceof VectorDrawable) {
                    bmIcon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bmIcon);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                } else
                    bmIcon = BitmapFactory.decodeResource(getResources(), srcId);
            }

            initDef();
            a.recycle();
        }
    }

    private void initDef() {
        circlePaint = new Paint();
        circlePaint.setColor(pbColorCenterCircle);

        circleStrokePaint = new Paint();
        circleStrokePaint.setColor(pbColorCenterCircleStroke);
        circleStrokePaint.setStyle(Paint.Style.STROKE);
        circleStrokePaint.setStrokeWidth(pbInnerCircleStrokeWidth);

        srcRect = new Rect();
        if (bmIcon != null)
            srcRect.set((int) (getCenterWidth() - (bmIcon.getWidth() / 2F)) + 4, (int) (getCenterHeight() - (bmIcon.getHeight() / 2F)),
                    (int) (getCenterWidth() + (bmIcon.getWidth() / 2F)) + 4, (int) getCenterHeight() + (bmIcon.getHeight() / 2));

        circleStrokeOuterPaint = new Paint();
        circleStrokeOuterPaint.setARGB(pbColorWaveCircleStrokeArr[0], pbColorWaveCircleStrokeArr[1],
                pbColorWaveCircleStrokeArr[2], pbColorWaveCircleStrokeArr[3]);
        circleStrokeOuterPaint.setStyle(Paint.Style.STROKE);
        circleStrokeOuterPaint.setStrokeWidth(pbOuterCircleStrokeWidth);

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDef();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getCenterWidth(), getCenterHeight(), getRadius(), circlePaint);
        canvas.drawCircle(getCenterWidth(), getCenterHeight(), getRadius() - pbInnerCircleStrokePadding, circleStrokePaint);

        int alpha = (int) ((pbWavePaddingInterAct / pbWavePadding) * DEFAULT_ALPHA);

        if (alpha > 0) {
            circleStrokeOuterPaint.setAlpha(alpha);
            canvas.drawCircle(getCenterWidth(), getCenterHeight(), getWaveRadius(), circleStrokeOuterPaint);
        }

        if (pbWavePaddingInterAct <= 0)
            pbWavePaddingInterAct = pbWavePadding;
        else
            pbWavePaddingInterAct -= pbWavePadding / 90;


        if (bmIcon != null)
            canvas.drawBitmap(bmIcon, null, srcRect, null);
        invalidate();
    }

    private float getCenterWidth() {
        return getWidth() / 2F;
    }

    private float getCenterHeight() {
        return getHeight() / 2F;
    }

    private float getWaveRadius() {
        return (getWidth() > getHeight() ? getHeight() / 2F : getWidth() / 2F) - pbWavePaddingInterAct;
    }

    private float getRadius() {
        return (getWidth() > getHeight() ? getHeight() / 2F : getWidth() / 2F) - pbWavePadding;
    }

    private int[] getARGB(int color) {
        int[] lineColor = new int[4];
        lineColor[0] = Color.alpha(color);
        lineColor[1] = Color.red(color);
        lineColor[2] = Color.green(color);
        lineColor[3] = Color.blue(color);
        return lineColor;
    }
}
