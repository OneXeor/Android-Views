package io.singulart.particle_progress_bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ParticleProgressBar extends android.support.v7.widget.AppCompatImageView {

    private int framesPerSecond = 60;
    private int animationDuration = 1000;

    private int BALL_SIZE = 10;
    private int BALLS_COUNT;

    private List<Ball> balls;

    private int startScale;
    private int color;

    private Timer timer;

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
        long elapsedTime = System.currentTimeMillis() - startTime;
        for (Ball ball : balls) {
            ball.render(canvas);
        }
        indeterminateBalls(elapsedTime);
    }

    private void indeterminateBalls(long elapsedTime) {
        if (startScale == balls.size())
            startScale = 0;

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            if (startScale == i) {
                ball.setSize(BALL_SIZE + ((BALLS_COUNT) * 0.6F));
            } else {
                ball.setSize(ball.getSize() * 0.6F);
            }
        }
        startScale++;

            postInvalidateDelayed(2500 / framesPerSecond);
//        postInvalidate();
    }

//    int framesPerSecond = 60;
//    long animationDuration = 10000; // 10 seconds
//
//    Matrix matrix = new Matrix(); // transformation matrix
//
//    Path path = new Path();       // your path
//    Paint paint = new Paint();    // your paint
//
//    long startTime;
//
//    public MyView(Context context) {
//        super(context);
//
//        // start the animation:
//        this.startTime = System.currentTimeMillis();
//        this.postInvalidate();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        long elapsedTime = System.currentTimeMillis() - startTime;
//
//        matrix.postRotate(30 * elapsedTime/1000);        // rotate 30° every second
//        matrix.postTranslate(100 * elapsedTime/1000, 0); // move 100 pixels to the right
//        // other transformations...
//
//        canvas.concat(matrix);        // call this before drawing on the canvas!!
//
//        canvas.drawPath(path, paint); // draw on canvas
//
//        if(elapsedTime < animationDuration)
//            this.postInvalidateDelayed( 1000 / framesPerSecond);
//    }

    //

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initBallPositionChanged();
    }

    private void initBallPositionChanged() {
        if (balls != null) {
            for (int i = 0; i < balls.size(); i++) {
                Ball ball = balls.get(i);
                BALL_SIZE = (getWidth() / BALLS_COUNT) / 2;
                ball.setSize((float) (BALL_SIZE * 0.6));

                //may be ooopse)
                ball.setConstSize(ball.getSize());

                double centerViewW = getWidth() / 2;
                double centerViewH = getHeight() / 2;
                double radius = (getWidth() / 2 - BALL_SIZE * 2) * 0.5;
                double x = (radius * Math.cos(Math.toRadians(ball.getAngle()))) + centerViewW;
                double y = (radius * Math.sin(Math.toRadians(ball.getAngle()))) + centerViewH;
                ball.setPosition(x, y);
            }
        }
    }

    private void createBalls() {
        balls = new ArrayList<>();
        for (int i = 0; i < BALLS_COUNT; i++) {
            balls.add(new Ball(BALL_SIZE, color, (360 / BALLS_COUNT) * i));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        super.onDetachedFromWindow();
    }
}

