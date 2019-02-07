package io.singulart.bottomnavigationbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;

import static io.singulart.bottomnavigationbar.Constants.USER_CLICK_OFFSET;

public class BottomNavigationBar extends View {

    private int barColor;

    private int itemSelectedColor;

    private float topLeftRadius;
    private float topRightRadius;

    private float shadowRadius;

    private float cutoutDeep;
    private float cutoutButtomOffset;
    private float bnbCutoutLeftTop_radius;
    private float bnbCutoutRightTop_radius;
    private float bnbCutoutBottomLeft_radius;
    private float bnbCutoutBottomRight_radius;

    private float textItemSize;
    private int textItemColor;

    private boolean isBtnInCenter;

    private int lastSelectedItem;

    @Nullable
    private CenterNavigationButton centerNavigationButton;

    @Nullable
    private Menu menu;

    @NonNull
    private Path backPath = new Path();
    @NonNull
    private RectF radiusRect = new RectF();
    @NonNull
    private Paint backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    @NonNull
    private Paint centerBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BottomNavigationBar(Context context) {
        super(context);
        setUp();
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setCenterNavigationButton(@Nullable CenterNavigationButton centerNavigationButton) {
        this.centerNavigationButton = centerNavigationButton;
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BottomNavigationBar, 0, 0);

            barColor = typedArray.getColor(R.styleable.BottomNavigationBar_bnb_background_color, Color.WHITE);
            itemSelectedColor = typedArray.getColor(R.styleable.BottomNavigationBar_bnb_item_selected_color, Color.BLUE);

            topLeftRadius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_top_left_radius, 0);
            topRightRadius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_top_right_radius, 0);

            shadowRadius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_shadow_radius, 12);

            isBtnInCenter = typedArray.getBoolean(R.styleable.BottomNavigationBar_bnb_center_btn, false);

            cutoutDeep = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_deep, 0);
            cutoutButtomOffset = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_bottom_offset, 0);
            bnbCutoutLeftTop_radius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_leftTop_radius, 0);
            bnbCutoutRightTop_radius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_rightTop_radius, 0);
            bnbCutoutBottomLeft_radius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_bottomLeft_radius, 0);
            bnbCutoutBottomRight_radius = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_cutout_bottomRight_radius, 0);

            int menuRes = typedArray.getResourceId(R.styleable.BottomNavigationBar_bnb_items, -1);
            textItemSize = typedArray.getDimension(R.styleable.BottomNavigationBar_bnb_text_item_size, 10);
            textItemColor = typedArray.getColor(R.styleable.BottomNavigationBar_bnb_text_item_color, Color.WHITE);

            if (menuRes != -1) {
                menu = new PopupMenu(context, null).getMenu();
                new MenuInflater(context).inflate(menuRes, menu);
                if (menu == null || menu.size() < 2) {
                    throw new RuntimeException("Menu items count cannot be less then 2");
                }
            }
            typedArray.recycle();
            setUp();
            invalidate();
        }
    }

    @NonNull
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    @NonNull
    private Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @NonNull
    private Rect textRect = new Rect();

    private void setUp() {
        backPaint.setColor(barColor);
        backPaint.setStyle(Paint.Style.FILL);

        centerBtnPaint.setColor(Color.WHITE);
        centerBtnPaint.setStyle(Paint.Style.FILL);

        textPaint.setTextSize(textItemSize);
        textPaint.setColor(textItemColor);

        iconPaint.setColor(textItemColor);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                Rect view = new Rect();
                getDrawingRect(view);
                if (view.intersect(new Rect((int) event.getX() - USER_CLICK_OFFSET, (int) event.getY() - USER_CLICK_OFFSET, (int) event.getX() + USER_CLICK_OFFSET, (int) event.getY() + USER_CLICK_OFFSET))) {
                    lastSelectedItem = onItemClicked(new RectF(event.getX() - USER_CLICK_OFFSET, event.getY() - USER_CLICK_OFFSET, event.getX() + USER_CLICK_OFFSET, event.getY() + USER_CLICK_OFFSET));
                    setLastSelectedItem(lastSelectedItem);
                    if (centerNavigationButton != null && isBtnInCenter)
                        if (lastSelectedItem == 2) {
                            centerNavigationButton.setButtonState(ButtonState.PRESSED);
                        } else
                            centerNavigationButton.setButtonState(ButtonState.NORMAL);
                }
                return true;
        }
        return super.dispatchTouchEvent(event);
    }

    public void setLastSelectedItem(int lastSelectedItem) {
        this.lastSelectedItem = lastSelectedItem;
        invalidate();
    }

    private int onItemClicked(RectF rectF) {
        if (menu != null) {
            int cellW = (getWidth() / menu.size());

            for (int i = 0; i < menu.size(); i++) {
                int startCell = i != 0 ? (i * cellW) : 0;
                int endCell = startCell + cellW;

                RectF cellRect = new RectF(startCell, 0, endCell, getHeight());
                if (rectF.intersect(cellRect)) {
                    return i;
                }
            }
        }
        return 0;
    }


    private void drawMenu(Canvas canvas) {
        if (menu != null)
            for (int i = 0; i < menu.size(); i++) {

                ColorFilter filter = new PorterDuffColorFilter(i != lastSelectedItem ? textItemColor : itemSelectedColor, PorterDuff.Mode.SRC_IN);

                textPaint.setColor(i != lastSelectedItem ? textItemColor : itemSelectedColor);
                iconPaint.setColor(i != lastSelectedItem ? textItemColor : itemSelectedColor);
                iconPaint.setColorFilter(filter);

                MenuItem item = menu.getItem(i);
                textPaint.getTextBounds(item.getTitle().toString(), 0, item.getTitle().length(), textRect);
                int cellW = (getWidth() / menu.size());
                int startText = i != 0 ? (i * cellW) + cellW / 2 - textRect.width() / 2 : cellW / 2 - textRect.width() / 2;

                @Nullable Bitmap bitmap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (item.getIcon() instanceof VectorDrawable) {
                        bitmap = getBitmapFromVector(item.getIcon());
                    } else
                        bitmap = ((BitmapDrawable) item.getIcon()).getBitmap();
                } else {
                    bitmap = ((BitmapDrawable) item.getIcon()).getBitmap();
                }
                if (i != 2 && bitmap != null || !isBtnInCenter && bitmap != null) {
                    int startIcon = i != 0 ? (i * cellW) + cellW / 2 - bitmap.getWidth() / 2 : cellW / 2 - bitmap.getWidth() / 2;
                    canvas.drawBitmap(bitmap, startIcon, getHeight() / 1.7F - bitmap.getHeight(), iconPaint);
//                    Matrix matrix;
//                    matrix
                }
                canvas.drawText(item.getTitle().toString(), startText, getHeight() - (textRect.height() / 1.5F), textPaint);
            }
    }

    private Bitmap getBitmapFromVector(Drawable drawable) {
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backPath.rewind();
        radiusRect.set(0, shadowRadius, topLeftRadius * 2, topLeftRadius * 1.5F);
        backPath.moveTo(0, shadowRadius);
        backPath.arcTo(radiusRect, 180, 90, false);

        if (isBtnInCenter) {
            backPath.lineTo(getCenter() - cutoutDeep - (bnbCutoutLeftTop_radius * 2), shadowRadius);
            // Center cutout radius top left
            radiusRect.set((getCenter() - cutoutDeep - (bnbCutoutLeftTop_radius * 2)),
                    shadowRadius,
                    getCenter() - cutoutDeep,
                    bnbCutoutLeftTop_radius * 2);
            backPath.arcTo(radiusRect, 270, 90, false);

            // Center cutout left bottom
            radiusRect.set((getCenter() - cutoutDeep),
                    -(shadowRadius + bnbCutoutBottomLeft_radius / 1.6F),
                    getCenter() + bnbCutoutBottomLeft_radius,
                    cutoutButtomOffset);
            backPath.arcTo(radiusRect, 180, -90, false);

            // Center cutout right bottom
            radiusRect.set((getCenter() - bnbCutoutBottomRight_radius),
                    -(shadowRadius + bnbCutoutBottomRight_radius / 1.6F),
                    getCenter() + cutoutDeep, cutoutButtomOffset);
            backPath.arcTo(radiusRect, 90, -90, false);

            // Center cutout radius top right
            radiusRect.set((getCenter() + cutoutDeep),
                    shadowRadius,
                    getCenter() + cutoutDeep + (bnbCutoutRightTop_radius * 2),
                    bnbCutoutRightTop_radius * 2);
            backPath.arcTo(radiusRect, 180, 90, false);

//            backPath.close();
        }

        radiusRect.set(getWidth() - ((topRightRadius * 2) + shadowRadius), shadowRadius, getWidth(), (topRightRadius * 1.5F) + shadowRadius);
        backPath.lineTo(getWidth() - shadowRadius, shadowRadius);
        backPath.arcTo(radiusRect, 270, 90, false);

        backPath.lineTo(getWidth(), getHeight());
        backPath.lineTo(0, getHeight());
        backPath.lineTo(0, shadowRadius + topLeftRadius);
        backPath.close();


        if (shadowRadius > 0)
            backPaint.setShadowLayer(shadowRadius, 0, -2, Color.WHITE);

        canvas.drawPath(backPath, backPaint);
        drawMenu(canvas);
//        invalidate();
    }

    private float getCenter() {
        return getWidth() / 2F;
    }
}
