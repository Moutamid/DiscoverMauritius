package com.moutamid.sqlapp.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class ColoredUnderlineTextView extends AppCompatTextView {
    private float underlineSpacing = 30f; // Adjust this value for the desired spacing
    private Paint underlinePaint;

    public ColoredUnderlineTextView(Context context) {
        super(context);
        init();
    }

    public ColoredUnderlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColoredUnderlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        underlinePaint = new Paint();
        underlinePaint.setColor(getCurrentTextColor());
        underlinePaint.setStyle(Paint.Style.STROKE);
        underlinePaint.setStrokeWidth(5f); // Adjust the thickness of the underline
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

