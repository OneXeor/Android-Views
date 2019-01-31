package io.singulart.particlesviewsample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParticlesView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;

    private int[] lineColor;

    public ParticlesView(Context context) {
        super(context);
        this.getHolder().addCallback(this);
        lineColor = new int[]{255, 255, 255, 255};
    }

    public ParticlesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getHolder().addCallback(this);
        lineColor = new int[]{255, 255, 255, 255};
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(holder, getResources());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }


    class DrawThread extends Thread {

        private AtomicBoolean isRunning = new AtomicBoolean();

        private final SurfaceHolder holder;

        private Resources resources;

        private List<DotItem> items;

        private Paint linePaint;

        private Path path;

        private final int NODE_DISTANCE = 200;

        DrawThread(SurfaceHolder holder, Resources resources) {
            this.holder = holder;
            this.resources = resources;
            this.linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setAntiAlias(true);
            linePaint.setDither(true);
            linePaint.setStrokeWidth(2);
            linePaint.setStyle(Paint.Style.STROKE);
            path = new Path();
            createDots();
        }

        @Override
        public void run() {
            Canvas canvas;
            super.run();
            while (isRunning.get()) {
                canvas = null;
                try {
                    // получаем объект Canvas и выполняем отрисовку
                    canvas = holder.lockCanvas(null);
                    synchronized (holder) {
                        linePaint.setColor(Color.WHITE);
                        canvas.drawColor(Color.rgb(244, 67, 54));
                        for (int i = 0; i < items.size(); i++) {
                            DotItem itemI = items.get(i);
                            itemI.render(canvas);
                            linkParticles(i, itemI, canvas);
                            moveParticles(itemI);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        // отрисовка выполнена. выводим результат на экран
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private void moveParticles(DotItem itemI) {
            // move particle
            PointF pointF = itemI.getPosition();
            float ms = itemI.getSpeed() / 2;


            if (itemI.getPosition().x + 20 >= getWidth() - 20)
                itemI.setVx(-itemI.getVx());
            else if (itemI.getPosition().x - 20 <= 20) itemI.setVx(-itemI.getVx());

            if (itemI.getPosition().y + 20 >= getHeight() - 20)
                itemI.setVy(-itemI.getVy());
            else if (itemI.getPosition().y - 20 <= 20) itemI.setVy(-itemI.getVy());


            pointF.set(pointF.x + (itemI.getVx() * ms), pointF.y + (itemI.getVy() * ms));
        }

        private void linkParticles(int i, DotItem itemI, Canvas canvas) {
            for (int j = i + 1; j < items.size(); j++) {
                DotItem itemj = items.get(j);

                float dx = itemI.getPosition().x - itemj.getPosition().x;
                float dy = itemI.getPosition().y - itemj.getPosition().y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist <= NODE_DISTANCE) {
                    path = new Path();
                    path.moveTo(itemI.getPosition().x, itemI.getPosition().y);
                    path.lineTo(itemj.getPosition().x, itemj.getPosition().y);
                    path.close();
                    int opacity = Math.abs((int) (NODE_DISTANCE - dist));

                    if (opacity > 0) {
                        int[] colorLine = lineColor;
                        linePaint.setARGB(opacity, colorLine[2], colorLine[1], colorLine[0]);
                        canvas.drawPath(path, linePaint);
                    }
                }
            }
        }


        void setRunning(boolean running) {
            this.isRunning.set(running);
        }

        private void createDots() {
            items = new ArrayList<>();
            float maxX = getWidth() - 20;
            float minX = 20;
            float maxY = getHeight() - 20;
            float minY = 20;
            float minSize = 5;
            float maxSize = 7;

            Random rand = new Random();
            while (items.size() < 30) {
                float finalX = rand.nextFloat() * (maxX - minX) + minX;
                float finalY = rand.nextFloat() * (maxY - minY) + minY;
                float size = rand.nextFloat() * (maxSize - minSize) + minSize;
                float finalSpeed = rand.nextFloat() * (3.2F - 1.8F) + 1.8F;
                items.add(new DotItem(size, new PointF(finalX, finalY), Color.argb(lineColor[3], lineColor[2], lineColor[1], lineColor[0]),
                        finalSpeed));
            }
            Log.d(this.getClass().getSimpleName(), "size: " + items.size());
        }

    }

    public static void main(String[] args) {
        double d1 = Math.signum(700);
        int d2 = (int) Math.signum(400);
        System.out.println("Di " + (d1));
        System.out.println("DI " + (d2));
    }
}
