package com.onexeor.particlesview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticlesView extends View {

    private int[] lineColor;

    private int linkingNodeDistance;

    private int linkingLineColor;

    private float linkingLineWidth;

    private int nodesCount;

    private float nodesSize;

    private float nodesSpeedMax;

    private float nodesSpeedMin;

    private int nodesColor;

    private int backgroundColor;


    private List<DotItem> items;

    private Paint linePaint;

    private Path path;


    public ParticlesView(Context context) {
        super(context);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStrokeWidth(linkingLineWidth);
        linePaint.setStyle(Paint.Style.STROKE);
        path = new Path();
        linkingLineColor = Color.WHITE;
        initDef();
    }

    public ParticlesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStrokeWidth(linkingLineWidth);
        linePaint.setStyle(Paint.Style.STROKE);
        path = new Path();
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        if (attributeSet != null) {
            final TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.ParticlesView, 0, 0);

            linkingNodeDistance = a.getInt(R.styleable.ParticlesView_pv_linking_nodes_distance, 200);
            linkingLineColor = a.getColor(R.styleable.ParticlesView_pv_linking_line_color, Color.WHITE);
            linkingLineWidth = a.getDimension(R.styleable.ParticlesView_pv_linking_line_width, 2);

            nodesColor = a.getColor(R.styleable.ParticlesView_pv_nodes_color, Color.WHITE);
            nodesCount = a.getInt(R.styleable.ParticlesView_pv_nodes_count, 30);
            nodesSize = a.getDimension(R.styleable.ParticlesView_pv_nodes_size, 5);
            nodesSpeedMax = a.getFloat(R.styleable.ParticlesView_pv_nodes_speed_max, 2F);
            nodesSpeedMin = a.getFloat(R.styleable.ParticlesView_pv_nodes_speed_max, 1F);

            backgroundColor = a.getColor(R.styleable.ParticlesView_pv_background_color, Color.BLACK);

            initDef();
            a.recycle();
        }
    }

    private void initDef() {
        lineColor = getARGB(linkingLineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(items == null)
            createDots();
        // Fill background
        canvas.drawColor(backgroundColor);
        for (int i = 0; i < items.size(); i++) {
            DotItem itemI = items.get(i);
            // Render particles
            itemI.render(canvas);
            // Linking particles
            linkParticles(i, itemI, canvas);
            // Moving particles
            moveParticles(itemI);
        }
    }

    private void moveParticles(DotItem item) {
        // move particle
        PointF pointF = item.getPosition();
        float ms = item.getSpeed() / 2;


        if (item.getPosition().x + item.getSize() >= getWidth() - 5)
            item.setVx(-item.getVx());
        else if (item.getPosition().x - item.getSize() <= 5) item.setVx(-item.getVx());

        if (item.getPosition().y + item.getSize() >= getHeight() - 5)
            item.setVy(-item.getVy());
        else if (item.getPosition().y - item.getSize() <= 5) item.setVy(-item.getVy());

        pointF.set(pointF.x + (item.getVx() * ms), pointF.y + (item.getVy() * ms));
        postInvalidate();
    }

    private void linkParticles(int i, DotItem itemI, Canvas canvas) {
        for (int j = i + 1; j < items.size(); j++) {
            DotItem itemj = items.get(j);
            float dx = itemI.getPosition().x - itemj.getPosition().x;
            float dy = itemI.getPosition().y - itemj.getPosition().y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist <= linkingNodeDistance) {
                path.reset();
                path.moveTo(itemI.getPosition().x, itemI.getPosition().y);
                path.lineTo(itemj.getPosition().x, itemj.getPosition().y);
                path.close();
                int opacity = (int) ((255 / linkingNodeDistance) * (linkingNodeDistance - dist));

                if (opacity > 0) {
                    linePaint.setARGB(opacity, lineColor[1], lineColor[2], lineColor[3]);
                    canvas.drawPath(path, linePaint);
                }
            }
            invalidate();
        }
    }


    private void createDots() {
        items = new ArrayList<>();

        Random rand = new Random();
        while (items.size() < nodesCount) {
            float finalX = (float) (Math.random() * getWidth());
            float finalY = (float) (Math.random() * getHeight());
            float size = (float) (Math.random() * nodesSize);
            float finalSpeed = rand.nextFloat() * (nodesSpeedMax - nodesSpeedMin) + nodesSpeedMin;

            DotItem dotItem = new DotItem();
            dotItem.setSize(size == 0F ? (0.3F * nodesSize) : size);
            dotItem.setPosition(new PointF(finalX, finalY));
            dotItem.setColor(nodesColor);
            dotItem.setSpeed(finalSpeed);
            dotItem.setVx((float) Math.random() - 0.5F);
            dotItem.setVy((float) Math.random() - 0.5F);
            items.add(dotItem);
        }
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
