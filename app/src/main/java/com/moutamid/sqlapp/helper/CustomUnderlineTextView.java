package com.moutamid.sqlapp.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomUnderlineTextView extends AppCompatTextView {
    private float underlineSpacing = 30f;
    private Paint underlinePaint;
    public CustomUnderlineTextView(Context context) {
        super(context);
        init();
    }
    public CustomUnderlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CustomUnderlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        underlinePaint = new Paint();
        underlinePaint.setColor(Color.WHITE);
        underlinePaint.setStyle(Paint.Style.STROKE);
        underlinePaint.setStrokeWidth(2f); // Adjust the thickness of the underline
        underlinePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();
        TextPaint textPaint = getPaint();
        textPaint.getTextBounds(getText().toString(), 0, getText().length(), rect);

        float startX = getPaddingLeft() + rect.left;
        float stopX = startX + rect.width();
        float baseline = getBaseline();
        float underlineY = baseline + underlineSpacing; // Adjust this value for the distance between text and underline

        canvas.drawLine(startX, underlineY, stopX, underlineY, underlinePaint);
    }

    public void setUnderlineSpacing(float spacing) {
        underlineSpacing = spacing;
        invalidate();
    }
}

