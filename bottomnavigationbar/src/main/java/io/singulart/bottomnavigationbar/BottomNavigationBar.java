package io.singulart.bottomnavigationbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

public class BottomNavigationBar extends View {

    private int barColor;

    private float topLeftRadius;
    private float topRightRadius;

    private float shadowRadius;

    private int startAngle;
    private int sweepAngle;

    private float rectLeft;
    private float rectRight;
    private float rectTop;
    private float rectBottom;

    private float cutoutDeep;
    private float cutoutOffset;
    private float bnbCutoutLeftTop_radius;
    private float bnbCutoutRightTop_radius;

    private boolean centerBtn;

    @Nullable
    private Menu menu;

    @NonNull
    private Path backPath = new Path();
    @NonNull
    private RectF radiusRect = new RectF();
    @NonNull
    private Paint backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BottomNavigationBar(Context context) {
        super(context);
        setUp();
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BottomNavigationBar, 0, 0);

            barColor = typedArray.getColor(R.styleable.BottomNavigationBar_bnb_background_color, Color.WHITE);

            topLeftRadius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_top_left_radius, 0);
            topRightRadius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_top_right_radius, 0);

            shadowRadius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_shadow_radius, 12);

            centerBtn = typedArray.getBoolean(R.styleable.BottomNavigationBar_bnb_center_btn, false);

            startAngle = typedArray.getInt(R.styleable.BottomNavigationBar_bnb_start_angle, 0);
            sweepAngle = typedArray.getInt(R.styleable.BottomNavigationBar_bnb_sweep_angle, 0);

            rectTop = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_rect_top, 0);
            rectLeft = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_rect_left, 0);
            rectRight = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_rect_right, 0);
            rectBottom = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_rect_bottom, 0);

            cutoutDeep = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_deep, 0);
            cutoutOffset = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_offset, 0);
            bnbCutoutLeftTop_radius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_leftTop_radius, 0);
            bnbCutoutRightTop_radius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_rightTop_radius, 0);

            int menuRes = typedArray.getResourceId(R.styleable.BottomNavigationBar_bnb_items, -1);

            if (menuRes != -1) {
                menu = new PopupMenu(context, null).getMenu();
                new MenuInflater(context).inflate(menuRes, menu);
            }
            typedArray.recycle();
            setUp();
            invalidate();
        }
    }

    private void setUp() {
        backPaint.setColor(barColor);
        backPaint.setStyle(Paint.Style.FILL);
    }

    private Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backPath.rewind();
        radiusRect.set(0, shadowRadius, topLeftRadius * 2, topLeftRadius * 2);
        backPath.moveTo(0, shadowRadius);
        backPath.arcTo(radiusRect, 180, 90, false);


        radiusRect.set(getWidth() - ((topRightRadius * 2) + shadowRadius), shadowRadius, getWidth(), (topRightRadius * 2) + shadowRadius);
        backPath.lineTo(getWidth() - shadowRadius, shadowRadius);
        backPath.arcTo(radiusRect, 270, 90, false);

        backPath.lineTo(getWidth(), getHeight());
        backPath.lineTo(0, getHeight());
        backPath.lineTo(0, shadowRadius + topLeftRadius);

        backPath.close();


        if (shadowRadius > 0)
            backPaint.setShadowLayer(shadowRadius, 0, -2, Color.WHITE);

        if (centerBtn) {
            backPath.lineTo(getCenter() - cutoutDeep, shadowRadius);

            radiusRect.set(getCenter() - cutoutDeep - (bnbCutoutLeftTop_radius * 2), shadowRadius, getCenter() - cutoutDeep, bnbCutoutLeftTop_radius * 2);
            backPath.arcTo(radiusRect, 270, 90);

            path.rewind();
            path.addCircle(getCenter(), -cutoutOffset, cutoutDeep, Path.Direction.CW);

            radiusRect.set((getCenter() + cutoutDeep), shadowRadius, getCenter() + cutoutDeep + (bnbCutoutRightTop_radius * 2), bnbCutoutRightTop_radius * 2);
            backPath.arcTo(radiusRect, 180, 90, false);

            canvas.clipPath(path, Region.Op.DIFFERENCE);
        }
        canvas.drawPath(backPath, backPaint);


        invalidate();
    }

    private float getCenter() {
        return getWidth() / 2F;
    }
}
