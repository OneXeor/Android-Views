package io.singulart.particle_progress_bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class ParticleProgressBar extends android.support.v7.widget.AppCompatImageView {

    private float BALL_SIZE = 10;
    private int BALLS_COUNT;

    private List<Ball> balls;

    private int startScale;
    private int color;

    private boolean enabled;

    long startTime;

    public ParticleProgressBar(Context context) {
        super(context);
        createBalls();
    }

    public ParticleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        createBalls();
    }

    private void init(Context context, AttributeSet attributeSet) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        if (attributeSet != null) {
            final TypedArray a = context.obtainStyledAttributes(attributeSet,
                    R.styleable.ParticleProgressBar, 0, 0);

            enabled = a.getBoolean(R.styleable.ParticleProgressBar_ppb_enabled, true);
            BALLS_COUNT = a.getInteger(R.styleable.ParticleProgressBar_ppb_count_balls, 8);
            color = a.getColor(R.styleable.ParticleProgressBar_ppb_color, Color.WHITE);

            startScale = BALLS_COUNT;
            a.recycle();
        }
        startTime = System.currentTimeMillis();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!enabled)
            return;
        for (Ball ball : balls) {
            ball.render(canvas);
        }

        instantiateBalls();
    }

    private void instantiateBalls() {
        if (startScale == balls.size())
            startScale = 0;

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            if (startScale == i) {
                ball.setSize(BALL_SIZE);
            } else if (ball.getSize() > 0.5F) {
                ball.setSize(ball.getSize() * 0.89F);
            }
        }

        startScale++;

        postInvalidateDelayed((long) ((900 / (BALLS_COUNT * 2))));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initBallPositionChanged();
    }

    private void initBallPositionChanged() {
        if (balls != null) {
            for (int i = 0; i < balls.size(); i++) {

                double centerViewW = getWidth() / 2F;
                double centerViewH = getHeight() / 2F;
                double radius = centerViewW * 0.5; //(getWidth() / 2);

                Ball ball = balls.get(i);
                BALL_SIZE = (float) ((2F * Math.PI * radius) / BALLS_COUNT / 3F);//(getWidth() / BALLS_COUNT) / 2;
                ball.setSize((float) (BALL_SIZE * 0.8));

                double x = (radius * Math.cos(Math.toRadians(ball.getAngle()))) + centerViewW;
                double y = (radius * Math.sin(Math.toRadians(ball.getAngle()))) + centerViewH;
                ball.setPosition(x, y);
            }
        }
    }

    private void createBalls() {
        balls = new ArrayList<>();
        for (int i = 0; i < BALLS_COUNT; i++) {
            balls.add(new Ball(BALL_SIZE, color, (360F / BALLS_COUNT) * i));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

