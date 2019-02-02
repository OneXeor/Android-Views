package io.singulart.particle_progress_bar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Ball {

    private Paint paint;
    private float size;

    private PointF position;
    private int color;

    private double angle;

    Ball(float size, int color, double angle) {
        this.size = size;
        this.angle = angle;
        this.color = color;
        init();
    }

    private void init() {
        initPaint();
        initPoint();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    private void initPoint() {
        if (position == null)
            position = new PointF();
    }

    public double getAngle() {
        return angle;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(double x, double y) {
        this.position.set((float) x, (float) y);
    }

    void render(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, size, paint);
    }
}