package io.singulart.bottomnavigationbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CenterNavigationButton extends View {


    private int backgroundColor;

    private int foregroundColor;

    @NonNull
    private Path btnPath = new Path();
    @NonNull
    private Path btnForegroundPath = new Path();
    @NonNull
    private Paint btnPaint = new Paint();
    @NonNull
    private Paint btnForegroundPaint = new Paint();
    @NonNull
    private Paint btnForegroundArcPaint = new Paint();
    @NonNull
    RectF centerArcRect = new RectF();

    private float tX = -1;
    private float tY = -1;

    public CenterNavigationButton(Context context) {
        super(context);
        setUp();
    }

    public CenterNavigationButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet set) {
        if (set != null) {
            TypedArray a = context.obtainStyledAttributes(set, R.styleable.CenterNavigationButton, 0, 0);

            backgroundColor = a.getColor(R.styleable.CenterNavigationButton_cnb_background_color, Color.WHITE);

            foregroundColor = a.getColor(R.styleable.CenterNavigationButton_cnb_foreground_color, Color.WHITE);

            a.recycle();
            setUp();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                tX = event.getX();
                tY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                tX = -1;
                tY = -1;
                invalidate();
                break;

        }
        return true;
    }

    private void setUp() {
        btnPaint.setStyle(Paint.Style.FILL);
        btnPaint.setAntiAlias(true);


        btnForegroundPaint.setStyle(Paint.Style.FILL);
        btnForegroundPaint.setAntiAlias(true);
        btnForegroundPaint.setColor(foregroundColor);
        btnForegroundPaint.setShadowLayer(18, 0, 0, Color.argb(201, 33, 217, 204));

        btnForegroundArcPaint.set(btnForegroundPaint);
        btnForegroundArcPaint.setStyle(Paint.Style.STROKE);
        btnForegroundArcPaint.setStrokeWidth(9);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        btnPaint.setColor(tX != -1 ? Color.WHITE : backgroundColor);

        float cX = getCenterWidth();
        float cY = getCenterHeight();
        float cbSize = (cX < cY ? cX : cY) - 10;

        btnPath.rewind();
        btnPath.moveTo(cX, cY);
        btnPath.addCircle(cX, cY, cbSize, Path.Direction.CW);
        btnPath.close();

        btnForegroundPath.rewind();
        btnForegroundPath.moveTo(cX, cY);
        btnForegroundPath.addCircle(cX, cY, cbSize * 0.2F, Path.Direction.CW);
        btnForegroundPath.close();

        canvas.drawPath(btnPath, btnPaint);
        canvas.drawPath(btnForegroundPath, btnForegroundPaint);

        float offSet = 0.25F;

        centerArcRect.set(cX - ((cbSize * offSet) * 2), cY - ((cbSize * offSet) * 2), cX + ((cbSize * offSet) * 2), cY + ((cbSize * offSet) * 2));

        double cXOnCircle = cX + centerArcRect.width() / 2 * Math.cos(315D * Math.PI / 180);
        double cYOnCircle = cY + centerArcRect.width() / 2 * Math.sin(315D * Math.PI / 180);

        canvas.drawCircle((float) cXOnCircle, (float) cYOnCircle, btnForegroundArcPaint.getStrokeWidth(), btnForegroundPaint);

        canvas.drawCircle(cX, cY - ((cbSize * offSet) * 2), btnForegroundArcPaint.getStrokeWidth() / 2, btnForegroundPaint);
        canvas.drawCircle(cX + ((cbSize * offSet) * 2), cY, btnForegroundArcPaint.getStrokeWidth() / 2, btnForegroundPaint);

        canvas.drawArc(centerArcRect, 270, -270, false, btnForegroundArcPaint);

        centerArcRect.set(10, 10, getWidth() - 10, getHeight() - 10);
        // Calculating on circle position
        double cXOnArc = cX + centerArcRect.width() / 2 * Math.cos(45D * Math.PI / 180);
        double cYOnArc = cY + centerArcRect.width() / 2 * Math.sin(45D * Math.PI / 180);
        double cXOnArc1 = cX + centerArcRect.width() / 2 * Math.cos(134D * Math.PI / 180);
        double cYOnArc1 = cY + centerArcRect.width() / 2 * Math.sin(134D * Math.PI / 180);

        canvas.drawCircle((float) cXOnArc, (float) cYOnArc, btnForegroundArcPaint.getStrokeWidth() / 2, btnForegroundPaint);
        canvas.drawCircle((float) cXOnArc1, (float) cYOnArc1, btnForegroundArcPaint.getStrokeWidth() / 2, btnForegroundPaint);

        canvas.drawArc(centerArcRect, 45, 90, false, btnForegroundArcPaint);
    }

    private float getCenterWidth() {
        return getWidth() / 2F;
    }

    private float getCenterHeight() {
        return getHeight() / 2f;
    }
}
