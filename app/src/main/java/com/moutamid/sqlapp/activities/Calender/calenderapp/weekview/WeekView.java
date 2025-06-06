package com.moutamid.sqlapp.activities.Calender.calenderapp.weekview;


import static com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.WeekViewUtil.isSameDay;
import static com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.WeekViewUtil.today;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.calenderapp.MainActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.Event;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.EventDbHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeekView extends View {
    public static float rectLeft, rectTop, rectRight, rectBottom;

    @Deprecated
    public static final int LENGTH_SHORT = 1;
    @Deprecated
    public static final int LENGTH_LONG = 2;
    public static Context mContext;
    int forward = 1080;
    public static Paint shadowPaint;
    public static boolean stop;
    public static Paint mTimeTextPaint;
    public static float mTimeTextWidth;
    public static float mTimeTextHeight;
    public static Paint mHeaderTextPaint;
    public static float mHeaderTextHeight;
    public static View shadow;
    public static Paint jHeaderTextPaint;
    public static Paint jtodayHeaderTextPaint;
    public static float jHeaderTextHeight;
    public static float mHeaderHeight;
    public static  GestureDetectorCompat mGestureDetector;
    public static OverScroller mScroller;
    public static PointF mCurrentOrigin = new PointF(0f, 0f);
    public static Direction mCurrentScrollDirection = Direction.NONE;
    public static Paint mHeaderBackgroundPaint;
    public float mWidthPerDay;
    public static Paint mDayBackgroundPaint;
    public static Paint mHourSeparatorPaint;
    public static float mHeaderMarginBottom;
    public static Paint jheaderEventTextpaint;
    public static int jheaderEventheight;
    public static Paint mFutureBackgroundPaint;
    public static Paint mPastBackgroundPaint;
    public static Paint mFutureWeekendBackgroundPaint;
    public static Paint mPastWeekendBackgroundPaint;
    public static Paint mNowLinePaint;
    public static Paint jNowbackPaint;
    //    public static Paint mTodayHeaderTextPaint;
    public static Paint mEventBackgroundPaint;
    public static float mHeaderColumnWidth;
    public static List<EventRect> mEventRects;
    public static List<? extends WeekViewEvent> mPreviousPeriodEvents;
    public static List<? extends WeekViewEvent> mCurrentPeriodEvents;
    public static List<? extends WeekViewEvent> mNextPeriodEvents;
    public static TextPaint mEventTextPaint;
    public static Paint mHeaderColumnBackgroundPaint;
    public static int mFetchedPeriod = -1; // the middle period the calendar has fetched.
    public static boolean mRefreshEvents = false;
    public static Direction mCurrentFlingDirection = Direction.NONE;
    public static ScaleGestureDetector mScaleDetector;
    public static boolean mIsZooming;
    public static Calendar mFirstVisibleDay;
    public static Calendar mLastVisibleDay;
    public static boolean mShowFirstDayOfWeekFirst = false;
    public static int mDefaultEventColor;
    public static int mMinimumFlingVelocity = 0;
    public static int mScaledTouchSlop = 0;
    // Attributes and their default values.
    public static int mHourHeight = 50;
    public static int mNewHourHeight = -1;
    public static int mMinHourHeight = 0; //no minimum specified (will be dynamic, based on screen)
    public static int mEffectiveMinHourHeight = mMinHourHeight; //compensates for the fact that you can't keep zooming out.
    public static int mMaxHourHeight = 250;
    public static int mColumnGap = 10;
    public static int mFirstDayOfWeek = Calendar.MONDAY;
    public static int mTextSize = 12;
    public static int mHeaderColumnPadding = 10;
    public static int mHeaderColumnTextColor = Color.BLUE;
    public static int mNumberOfVisibleDays = 1;
    public static int mHeaderRowPadding = 10;
    public static int mHeaderRowBackgroundColor = Color.BLUE;
    public static int mDayBackgroundColor = Color.rgb(245, 245, 245);
    public static int mPastBackgroundColor = Color.rgb(227, 227, 227);
    public static int mFutureBackgroundColor = Color.rgb(245, 245, 245);
    public static int mPastWeekendBackgroundColor = 0;
    public static int mFutureWeekendBackgroundColor = 0;
    public static int mNowLineColor = Color.rgb(102, 102, 102);
    public static int mNowLineThickness = 2;
    public static int mHourSeparatorColor = Color.rgb(230, 230, 230);
    //    public static int mTodayBackgroundColor = Color.rgb(19, 65, 105);
    public static int mHourSeparatorHeight = 2;

    public static int mEventTextSize = 12;
    public static int mEventTextColor = Color.BLACK;
    public static int mEventPadding = 8;
    public static int mHeaderColumnBackgroundColor = Color.WHITE;
    public static boolean mIsFirstDraw = true;
    public static boolean mAreDimensionsInvalid = true;
    @Deprecated
//    public static int mDayNameLength = LENGTH_LONG;
    public static int mOverlappingEventGap = 0;
    public static int mEventMarginVertical = 0;
    public static float mXScrollingSpeed = 1f;
    public static Calendar mScrollToDay = null;
    public static double mScrollToHour = -1;
    public static int mEventCornerRadius = 0;
    public static boolean mShowDistinctWeekendColor = false;
    public static boolean mShowNowLine = false;
    public static boolean mShowDistinctPastFutureColor = false;
    public static boolean mHorizontalFlingEnabled = true;
    public static boolean mVerticalFlingEnabled = true;
    public static int mAllDayEventHeight = 100;
    public static int mScrollDuration = 150;
    public static float weekx;
    // Listeners.
    public static EventClickListener mEventClickListener;
    public static EventLongPressListener mEventLongPressListener;
    public static WeekViewLoader mWeekViewLoader;
    public static EmptyViewClickListener mEmptyViewClickListener;
    public static EmptyViewLongPressListener mEmptyViewLongPressListener;
    public static List<Event> events= new ArrayList<>();
    public  Canvas canvas_var ;
    public GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            weekx = mCurrentOrigin.x;
            goToNearestOrigin();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
             Log.d("state", e1+"  -  "+ e2+"  -  "+ mCurrentScrollDirection+"  -  "+ distanceX);
            // Check if view is zoomed.
            if (mIsZooming)
                return true;
            switch (mCurrentScrollDirection) {
                case NONE: {

                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
//                        if (distanceX > 0) {
//                            Log.d("state", "1");
//                            mCurrentScrollDirection = Direction.LEFT;
//                        } else {
//                            Log.d("state", "2");
//                            mCurrentScrollDirection = Direction.RIGHT;
//                        }
                    } else {
                        mCurrentScrollDirection = Direction.VERTICAL;
                    }
                    break;
                }
                case LEFT: {
                    Log.d("state", "3");
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX < -mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.RIGHT;
                    }
                    break;
                }
                case RIGHT: {
                    Log.d("state", "4");
                    // Change direction if there was enough change.
                    if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX > mScaledTouchSlop)) {
                        mCurrentScrollDirection = Direction.LEFT;
                    }
                    break;
                }
            }

            // Calculate the new origin after scroll.
            switch (mCurrentScrollDirection) {
//                case LEFT:
//                case RIGHT:
//                    mCurrentOrigin.x -= distanceX * mXScrollingSpeed;
//                    ViewCompat.postInvalidateOnAnimation(WeekView.this);
//                    break;
                case VERTICAL:

                    mCurrentOrigin.y -= distanceY;
                    ViewCompat.postInvalidateOnAnimation(WeekView.this);
                    break;
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (mIsZooming)
                return true;

            mScroller.forceFinished(true);

            mCurrentFlingDirection = mCurrentScrollDirection;


            float target = 0;
            switch (mCurrentFlingDirection) {
                case LEFT:
                    target = weekx - (mWidthPerDay * getNumberOfVisibleDays());
                    Log.d("move", "left" + weekx+"   "+ mWidthPerDay+ "   "+ getNumberOfVisibleDays()+ "  "+ target);

                    ValueAnimator va = ValueAnimator.ofFloat(mCurrentOrigin.x, target);
                    va.setDuration(10);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mCurrentOrigin.x = (float) animation.getAnimatedValue();
                            invalidate();
                        }

                    });
                    va.start();

                    break;
                case RIGHT:
                    target = weekx + (mWidthPerDay * getNumberOfVisibleDays());
                    Log.d("move", "right" + weekx+"   "+ mWidthPerDay+ "   "+ getNumberOfVisibleDays()+ "  "+ target);
                    ValueAnimator va1 = ValueAnimator.ofFloat(mCurrentOrigin.x, target);
                    va1.setDuration(10);
                    va1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mCurrentOrigin.x = (float) animation.getAnimatedValue();
                            invalidate();
                        }

                    });

                    va1.start();
                    //  forward= (int) (forward+mWidthPerDay);
                    break;
//
                case VERTICAL:
                    mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, 0, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(mHourHeight * 24 + mHeaderHeight + mHeaderRowPadding * 3 + mHeaderMarginBottom + mTimeTextHeight / 2 - getHeight()), 0);
                    ViewCompat.postInvalidateOnAnimation(WeekView.this);
                    break;
            }
//
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 100);


            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

           for (Event event : events) {
                if (e.getX() > event.left && e.getX() < event.width && e.getY() > event.top && e.getY() < event.bottom) {
                    Log.d("Parameters", "ID: " + event.id);
                    Log.d("Parameters", "Title: " + event.title);
                    Log.d("Parameters", "Time: " + event.time);
                    Log.d("Parameters", "Description: " + event.description);
                    Log.d("Parameters", "Checked: " + event.checked);
                    Log.d("Parameters", "Date: " + event.date);
                    ViewEventDailogue adEventDailogue = new ViewEventDailogue(mContext, event.id, event.title, event.time, event.description, event.checked, event.date, event.exact_time);
                    adEventDailogue.show();
                    return super.onSingleTapConfirmed(e);
                }
            }
//            if (mEventRects != null && mEventClickListener != null) {
//                List<EventRect> reversedEventRects = mEventRects;
//                // Collections.reverse(reversedEventRects);
//                for (EventRect event : reversedEventRects) {
//                    if (event.rectF != null && e.getX() > event.rectF.left && e.getX() < event.rectF.right && e.getY() > event.rectF.top && e.getY() < event.rectF.bottom) {
//                        mEventClickListener.onEventClick(event.event, event.rectF);
//                        playSoundEffect(SoundEffectConstants.CLICK);
//                        Toast.makeText(mContext, "event", Toast.LENGTH_SHORT).show();
//                        return super.onSingleTapConfirmed(e);
//                    }
//                }
//            }

            // If the tap was on in an empty space, then trigger the callback.
            if (mEmptyViewClickListener != null && e.getX() > mHeaderColumnWidth && e.getY() > (mHeaderHeight + mHeaderRowPadding * 3 + mHeaderMarginBottom)) {
                Calendar selectedTime = getTimeFromPoint(e.getX(), e.getY());
                if (selectedTime != null) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mEmptyViewClickListener.onEmptyViewClicked(selectedTime);
                }
            }

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);

            if (mEventLongPressListener != null && mEventRects != null) {
                List<EventRect> reversedEventRects = mEventRects;
                //   Collections.reverse(reversedEventRects);
                for (EventRect event : reversedEventRects) {
                    if (event.rectF != null && e.getX() > event.rectF.left && e.getX() < event.rectF.right && e.getY() > event.rectF.top && e.getY() < event.rectF.bottom) {
                        mEventLongPressListener.onEventLongPress(event.originalEvent, event.rectF);
                        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                        return;
                    }
                }
            }

            // If the tap was on in an empty space, then trigger the callback.
            if (mEmptyViewLongPressListener != null && e.getX() > mHeaderColumnWidth && e.getY() > (mHeaderHeight + mHeaderRowPadding * 3 + mHeaderMarginBottom)) {
                Calendar selectedTime = getTimeFromPoint(e.getX(), e.getY());
                if (selectedTime != null) {
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    mEmptyViewLongPressListener.onEmptyViewLongPress(selectedTime);
                }
            }
        }
    };
    public static DateTimeInterpreter mDateTimeInterpreter;
    public static ScrollListener mScrollListener;

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Hold references.
        mContext = context;

        // Get the attribute values (if any).
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeekView, 0, 0);
        try {
            mFirstDayOfWeek = a.getInteger(R.styleable.WeekView_firstDayOfWeek, mFirstDayOfWeek);
            mHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_hourHeight, mHourHeight);
            mMinHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_minHourHeight, mMinHourHeight);
            mEffectiveMinHourHeight = mMinHourHeight;
            mMaxHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_maxHourHeight, mMaxHourHeight);
            mTextSize = a.getDimensionPixelSize(R.styleable.WeekView_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, context.getResources().getDisplayMetrics()));
            mHeaderColumnPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerColumnPadding, mHeaderColumnPadding);
            mColumnGap = a.getDimensionPixelSize(R.styleable.WeekView_columnGap, mColumnGap);
            mColumnGap = a.getDimensionPixelSize(R.styleable.WeekView_columnGap, mColumnGap);
            mHeaderColumnTextColor = a.getColor(R.styleable.WeekView_headerColumnTextColor, mHeaderColumnTextColor);
            mNumberOfVisibleDays = a.getInteger(R.styleable.WeekView_noOfVisibleDays, mNumberOfVisibleDays);
            mShowFirstDayOfWeekFirst = a.getBoolean(R.styleable.WeekView_showFirstDayOfWeekFirst, mShowFirstDayOfWeekFirst);
            mHeaderRowPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerRowPadding, mHeaderRowPadding);
            mHeaderRowBackgroundColor = a.getColor(R.styleable.WeekView_headerRowBackgroundColor, mHeaderRowBackgroundColor);
            mDayBackgroundColor = a.getColor(R.styleable.WeekView_dayBackgroundColor, mDayBackgroundColor);
            mFutureBackgroundColor = a.getColor(R.styleable.WeekView_futureBackgroundColor, mFutureBackgroundColor);
            mPastBackgroundColor = a.getColor(R.styleable.WeekView_pastBackgroundColor, mPastBackgroundColor);
            mFutureWeekendBackgroundColor = a.getColor(R.styleable.WeekView_futureWeekendBackgroundColor, mFutureBackgroundColor); // If not set, use the same color as in the week
            mPastWeekendBackgroundColor = a.getColor(R.styleable.WeekView_pastWeekendBackgroundColor, mPastBackgroundColor);
            mNowLineColor = a.getColor(R.styleable.WeekView_nowLineColor, mNowLineColor);
            mNowLineThickness = a.getDimensionPixelSize(R.styleable.WeekView_nowLineThickness, mNowLineThickness);
            mHourSeparatorColor = a.getColor(R.styleable.WeekView_hourSeparatorColor, mHourSeparatorColor);
//            mTodayBackgroundColor = a.getColor(R.styleable.WeekView_todayBackgroundColor, mTodayBackgroundColor);
            mHourSeparatorHeight = a.getDimensionPixelSize(R.styleable.WeekView_hourSeparatorHeight, mHourSeparatorHeight);
            mEventTextSize = a.getDimensionPixelSize(R.styleable.WeekView_eventTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mEventTextSize, context.getResources().getDisplayMetrics()));
            mEventTextColor = a.getColor(R.styleable.WeekView_eventTextColor, mEventTextColor);
            mEventPadding = a.getDimensionPixelSize(R.styleable.WeekView_eventPadding, mEventPadding);
            mHeaderColumnBackgroundColor = a.getColor(R.styleable.WeekView_headerColumnBackground, mHeaderColumnBackgroundColor);
//            mDayNameLength = a.getInteger(R.styleable.WeekView_dayNameLength, mDayNameLength);
            mOverlappingEventGap = a.getDimensionPixelSize(R.styleable.WeekView_overlappingEventGap, mOverlappingEventGap);
            mEventMarginVertical = a.getDimensionPixelSize(R.styleable.WeekView_eventMarginVertical, mEventMarginVertical);
            mXScrollingSpeed = a.getFloat(R.styleable.WeekView_xScrollingSpeed, mXScrollingSpeed);
            mEventCornerRadius = a.getDimensionPixelSize(R.styleable.WeekView_eventCornerRadius, mEventCornerRadius);
            mShowDistinctPastFutureColor = a.getBoolean(R.styleable.WeekView_showDistinctPastFutureColor, mShowDistinctPastFutureColor);
            mShowDistinctWeekendColor = a.getBoolean(R.styleable.WeekView_showDistinctWeekendColor, mShowDistinctWeekendColor);
            mShowNowLine = a.getBoolean(R.styleable.WeekView_showNowLine, mShowNowLine);
            mHorizontalFlingEnabled = a.getBoolean(R.styleable.WeekView_horizontalFlingEnabled, mHorizontalFlingEnabled);
            mVerticalFlingEnabled = a.getBoolean(R.styleable.WeekView_verticalFlingEnabled, mVerticalFlingEnabled);
            mAllDayEventHeight = a.getDimensionPixelSize(R.styleable.WeekView_allDayEventHeight, mAllDayEventHeight);
            mScrollDuration = a.getInt(R.styleable.WeekView_scrollDuration, mScrollDuration);
        } finally {
            a.recycle();
        }

        init();
    }


    public void init() {
        // Scrolling initialization.
        mGestureDetector = new GestureDetectorCompat(mContext, mGestureListener);
        mScroller = new OverScroller(mContext, new FastOutLinearInInterpolator());
        mMinimumFlingVelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();
        mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setStyle(Paint.Style.FILL);
//        shadowPaint.setShadowLayer(12, 0, 0, Color.GRAY);

        // Important for certain APIs
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
        // Measure settings for time column.
        mTimeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTimeTextPaint.setTextSize(mTextSize);
        mTimeTextPaint.setColor(mHeaderColumnTextColor);
        Rect rect = new Rect();
        mTimeTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mTimeTextHeight = rect.height();
        mHeaderMarginBottom = mTimeTextHeight / 2;
        initTextTimeWidth();

        // Measure settings for header row.
        mHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderTextPaint.setColor(mHeaderColumnTextColor);
        mHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mHeaderTextPaint.setTextSize(mTextSize - 1);
//        mHeaderTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
//        mHeaderTextHeight = rect.height();

// Set up the paint object
        jheaderEventTextpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        jheaderEventTextpaint.setColor(Color.BLACK); // Set text color to black
        jheaderEventTextpaint.setTextAlign(Paint.Align.LEFT);
//        event size
        jheaderEventTextpaint.setTextSize(19); // Set text size to 16


        jtodayHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        jtodayHeaderTextPaint.setColor(Color.WHITE);
        jtodayHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        jtodayHeaderTextPaint.setTextSize(32);

        jHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        jHeaderTextPaint.setColor(Color.WHITE);
        jHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        jHeaderTextPaint.setTextSize(32);

        // Prepare header background paint.
        mHeaderBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderBackgroundPaint.setColor(mHeaderRowBackgroundColor);

        // Prepare day background color paint.
        mDayBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDayBackgroundPaint.setColor(mDayBackgroundColor);
        mFutureBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFutureBackgroundPaint.setColor(mFutureBackgroundColor);
        mPastBackgroundPaint = new Paint();
        mPastBackgroundPaint.setColor(mPastBackgroundColor);
        mFutureWeekendBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFutureWeekendBackgroundPaint.setColor(mFutureWeekendBackgroundColor);
        mPastWeekendBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPastWeekendBackgroundPaint.setColor(mPastWeekendBackgroundColor);

        // Prepare hour separator color paint.
        mHourSeparatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourSeparatorPaint.setStyle(Paint.Style.STROKE);
        mHourSeparatorPaint.setStrokeWidth(mHourSeparatorHeight);
        mHourSeparatorPaint.setColor(mHourSeparatorColor);

        // Prepare the "now" line color paint
        mNowLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNowLinePaint.setStrokeWidth(mNowLineThickness);
        mNowLinePaint.setColor(mNowLineColor);

        jNowbackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        jNowbackPaint.setColor(mNowLineColor);

        mEventBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mEventBackgroundPaint.setColor(Color.rgb(255, 255, 255));

        // Prepare header column background color.
        mHeaderColumnBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeaderColumnBackgroundPaint.setColor(mHeaderColumnBackgroundColor);

        // Prepare event text size and color.
        mEventTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mEventTextPaint.setStyle(Paint.Style.FILL);
        mEventTextPaint.setColor(Color.RED);
        mEventTextPaint.setTextSize(mEventTextSize);

        // Set default event color.
        mDefaultEventColor = Color.parseColor("#9fc6e7");

        mScaleDetector = new ScaleGestureDetector(mContext, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                mIsZooming = false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                mIsZooming = true;
                goToNearestOrigin();
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mNewHourHeight = Math.round(mHourHeight * detector.getScaleFactor());
                invalidate();
                return true;
            }
        });
    }

    // fix rotation changes
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mAreDimensionsInvalid = true;
    }

    /**
     * Initialize time column width. Calculate value with all possible hours (supposed widest text).
     */
    public  void initTextTimeWidth() {
        mTimeTextWidth = 0;
        for (int i = 0; i < 24; i++) {
            // Measure time string and get max width.
            String time = getDateTimeInterpreter().interpretTime(i);
            if (time == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null time");
            mTimeTextWidth = Math.max(mTimeTextWidth, mTimeTextPaint.measureText(time));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the header row.
        canvas_var= canvas;

        if (mNumberOfVisibleDays != 1) drawTimeColumnAndAxes(canvas);
        drawHeaderRowAndEvents(canvas);

    }

    public void calculateHeaderHeight() {
        //Make sure the header is the right size (depends on AllDay events)
        boolean containsAllDayEvent = false;
        int noofevent = 1;
        if (mEventRects != null && mEventRects.size() > 0) {
            for (int dayNumber = 0;
                 dayNumber < mNumberOfVisibleDays;
                 dayNumber++) {
                Calendar day = (Calendar) getFirstVisibleDay().clone();
                day.add(Calendar.DATE, dayNumber);
                for (int i = 0; i < mEventRects.size(); i++) {
                    if (isSameDay(mEventRects.get(i).event.getStartTime(), day) && mEventRects.get(i).event.isAllDay()) {
                        if (mEventRects.get(i).noofevent > noofevent)
                            noofevent = mEventRects.get(i).noofevent;
                        containsAllDayEvent = true;
                        break;
                    }
                }
//                if(containsAllDayEvent){
//                    break;
//                }
            }
        }
        //((noofevent-1)*5) where is 5 padding between event
        if (mNumberOfVisibleDays == 1) {
            if (containsAllDayEvent) {
                float without = mHeaderTextHeight + jHeaderTextHeight + mHeaderMarginBottom + 70 + mHeaderRowPadding / 3.0f;
                float with = (mAllDayEventHeight * noofevent) + ((noofevent - 1) * 5) + mHeaderMarginBottom;
                mHeaderHeight = Math.max(without, with);

//                shadow.setY(mHeaderHeight - 70 + mHeaderRowPadding * 3);

            } else {
                float without = mHeaderTextHeight + jHeaderTextHeight + mHeaderMarginBottom + 70 + mHeaderRowPadding / 3.0f;

                mHeaderHeight = without;
//                shadow.setY(mHeaderHeight - 70 + mHeaderRowPadding * 3);
            }


        } else {
            if (containsAllDayEvent) {
                mHeaderHeight = mHeaderTextHeight + jHeaderTextHeight + ((mAllDayEventHeight * noofevent) + ((noofevent - 1) * 5) + mHeaderMarginBottom) + 70;
//                shadow.setY(mHeaderHeight - 70 + mHeaderRowPadding * 3);
            } else {
                mHeaderHeight = mHeaderTextHeight + jHeaderTextHeight + mHeaderMarginBottom + 70;
//                shadow.setY(mHeaderHeight - 70 + mHeaderRowPadding * 3);
            }
        }


    }

    public void canvasclipRect(Canvas mCanvas, float left, float top, float right, float bottom, Region.Op op) {
        if (op == Region.Op.REPLACE) {
            mCanvas.restore();
            mCanvas.save();
            mCanvas.clipRect(left, top, right, bottom);
            return;
        }
    }

    public  void drawTimeColumnAndAxes(Canvas canvas) {
        // Draw the background color for the header column.
        canvas.drawRect(0, mHeaderHeight + mHeaderRowPadding * 3 - 70, mHeaderColumnWidth, getHeight(), mHeaderColumnBackgroundPaint);

        // Clip to paint in left column only.
        canvasclipRect(canvas, 0, mHeaderHeight + mHeaderRowPadding * 3 - 70, mHeaderColumnWidth, getHeight(), Region.Op.REPLACE);

        for (int i = 0; i < 24; i++) {
            float top = mHeaderHeight + mHeaderRowPadding * 3 + mCurrentOrigin.y + mHourHeight * i + mHeaderMarginBottom;

            // Draw the text if its y position is not outside of the visible area. The pivot point of the text is the point at the bottom-right corner.
            String time = getDateTimeInterpreter().interpretTime(i);
            Toast.makeText(mContext, "time "+time, Toast.LENGTH_SHORT).show();
            if (time == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null time");
            if (top < getHeight())
                canvas.drawText(time, mTimeTextWidth + mHeaderColumnPadding, top + mTimeTextHeight, mTimeTextPaint);
        }

    }

    public void drawTimeColumnAndAxes1day(Canvas canvas, float startx) {
        // Draw the background color for the header column.
        // canvas.drawRect(0, mHeaderHeight + mHeaderRowPadding * 3-70, getWidth(), getHeight(), mHeaderColumnBackgroundPaint);

        // Clip to paint in left column only.

        canvasclipRect(canvas, 0, mHeaderHeight + mHeaderRowPadding * 3 - 70, getWidth(), getHeight(), Region.Op.REPLACE);

        for (int i = 0; i < 24; i++) {
            float top = mHeaderHeight + mHeaderRowPadding * 3 + mCurrentOrigin.y + mHourHeight * i + mHeaderMarginBottom;

            // Draw the text if its y position is not outside of the visible area. The pivot point of the text is the point at the bottom-right corner.
            String time = getDateTimeInterpreter().interpretTime(i);
            if (time == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null time");
            if (top < getHeight())
                canvas.drawText(time, startx + mTimeTextWidth + mHeaderColumnPadding, top + mTimeTextHeight, mTimeTextPaint);
        }
        canvasclipRect(canvas, 0, 0, getWidth(), getHeight(), Region.Op.REPLACE);

    }

    public  void drawHeaderRowAndEvents(Canvas canvas) {
        // Calculate the available width for each day.
        mHeaderColumnWidth = mTimeTextWidth + mHeaderColumnPadding * 2;
        mWidthPerDay = getWidth() - mHeaderColumnWidth - mColumnGap * (mNumberOfVisibleDays - 1);
        if (mNumberOfVisibleDays == 1) mWidthPerDay = getWidth();//mWidthPerDay/mNumberOfVisibleDays
        else mWidthPerDay = mWidthPerDay / mNumberOfVisibleDays;

        calculateHeaderHeight(); //Make sure the header is the right size (depends on AllDay events)

        Calendar today = today();

        if (mAreDimensionsInvalid) {
            mEffectiveMinHourHeight = Math.max(mMinHourHeight, (int) ((getHeight() - mHeaderHeight - mHeaderRowPadding * 3 - mHeaderMarginBottom) / 24));

            mAreDimensionsInvalid = false;
            if (mScrollToDay != null)
                goToDate(mScrollToDay);

            mAreDimensionsInvalid = false;
            if (mScrollToHour >= 0)
                goToHour(mScrollToHour);

            mScrollToDay = null;
            mScrollToHour = -1;
            mAreDimensionsInvalid = false;
        }
        if (mIsFirstDraw) {
            mIsFirstDraw = false;

            // If the week view is being drawn for the first time, then consider the first day of the week.
            if (mNumberOfVisibleDays >= 7 && today.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek && mShowFirstDayOfWeekFirst) {
                int difference = (today.get(Calendar.DAY_OF_WEEK) - mFirstDayOfWeek);
                mCurrentOrigin.x += (mWidthPerDay + mColumnGap) * difference;
            }
        }

        // Calculate the new height due to the zooming.
        if (mNewHourHeight > 0) {
            if (mNewHourHeight < mEffectiveMinHourHeight)
                mNewHourHeight = mEffectiveMinHourHeight;
            else if (mNewHourHeight > mMaxHourHeight)
                mNewHourHeight = mMaxHourHeight;

            mCurrentOrigin.y = (mCurrentOrigin.y / mHourHeight) * mNewHourHeight;
            mHourHeight = mNewHourHeight;
            mNewHourHeight = -1;
        }

        // If the new mCurrentOrigin.y is invalid, make it valid.
        if (mCurrentOrigin.y < getHeight() - mHourHeight * 24 - mHeaderHeight - mHeaderRowPadding * 3 - mHeaderMarginBottom - mTimeTextHeight / 2)
            mCurrentOrigin.y = getHeight() - mHourHeight * 24 - mHeaderHeight - mHeaderRowPadding * 3 - mHeaderMarginBottom - mTimeTextHeight / 2;

        // Don't put an "else if" because it will trigger a glitch when completely zoomed out and
        // scrolling vertically.
        if (mCurrentOrigin.y > 0) {
            mCurrentOrigin.y = 0;
        }

        // Consider scroll offset.
        int leftDaysWithGaps = (int) -(Math.ceil(mCurrentOrigin.x / (mWidthPerDay + mColumnGap)));
        float startFromPixel = mCurrentOrigin.x + (mWidthPerDay + mColumnGap) * leftDaysWithGaps +
                mHeaderColumnWidth;
        float startPixel = startFromPixel;

        // Prepare to iterate for each day.
        Calendar day = (Calendar) today.clone();
        day.add(Calendar.HOUR, 6);

        // Prepare to iterate for each hour to draw the hour lines.
        int lineCount = (int) ((getHeight() - mHeaderHeight - mHeaderRowPadding * 3 -
                mHeaderMarginBottom) / mHourHeight) + 1;
        lineCount = (lineCount) * (mNumberOfVisibleDays + 1);
        float[] hourLines = new float[lineCount * 4];

        // Clear the cache for event rectangles.
        if (mEventRects != null) {
            for (EventRect eventRect : mEventRects) {
                eventRect.rectF = null;
            }
        }

        // Clip to paint events only.
        float stary = 0;
        canvasclipRect(canvas, 0, stary, getWidth(), getHeight(), Region.Op.REPLACE);

        // Iterate through each day.
        Calendar oldFirstVisibleDay = mFirstVisibleDay;
        mFirstVisibleDay = (Calendar) today.clone();
        mFirstVisibleDay.add(Calendar.DATE, -(Math.round(mCurrentOrigin.x / (mWidthPerDay + mColumnGap))));
        if (!mFirstVisibleDay.equals(oldFirstVisibleDay) && mScrollListener != null) {
//            Toast.makeText(mContext, "done", Toast.LENGTH_SHORT).show();
            mScrollListener.onFirstVisibleDayChanged(mFirstVisibleDay, oldFirstVisibleDay);
        }
        for (int dayNumber = leftDaysWithGaps + 1;
             dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1;
             dayNumber++) {

            // Check if the day is today.
            day = (Calendar) today.clone();
            mLastVisibleDay = (Calendar) day.clone();
            day.add(Calendar.DATE, dayNumber - 1);
            mLastVisibleDay.add(Calendar.DATE, dayNumber - 2);
            boolean sameDay = isSameDay(day, today);

            // Get more events if necessary. We want to store the events 3 months beforehand. Get
            // events only when it is the first iteration of the loop.
            if (mEventRects == null || mRefreshEvents ||
                    (dayNumber == leftDaysWithGaps + 1 && mFetchedPeriod != (int) mWeekViewLoader.toWeekViewPeriodIndex(day) &&
                            Math.abs(mFetchedPeriod - mWeekViewLoader.toWeekViewPeriodIndex(day)) > 0.5)) {
                getMoreEvents(day);
                mRefreshEvents = false;
                calculateHeaderHeight();
            }

            // Draw background color for each day.
            float start = (startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel);


            canvasclipRect(canvas, 0, stary, getWidth(), getHeight(), Region.Op.REPLACE);


            // Prepare the separator lines for hours.
            int i = 0;
            for (int hourNumber = 0; hourNumber < 24; hourNumber++) {
                float top = mHeaderHeight + mHeaderRowPadding * 3 + mCurrentOrigin.y + mHourHeight * hourNumber + mTimeTextHeight / 2 + mHeaderMarginBottom;
                if (top > mHeaderHeight - 70 + mHeaderRowPadding * 3 && top < getHeight() && startPixel + mWidthPerDay - start > 0) {
                    if (mNumberOfVisibleDays != 1) {
                        if (dayNumber == leftDaysWithGaps + 1) hourLines[i * 4] = start - 22;
                        else hourLines[i * 4] = start;
                        hourLines[i * 4 + 1] = top;
                        hourLines[i * 4 + 2] = startPixel + mWidthPerDay;

                        hourLines[i * 4 + 3] = top;
                        i++;
                    } else {
                        hourLines[i * 4] = startPixel - 22;
                        hourLines[i * 4 + 1] = top;
                        hourLines[i * 4 + 2] = startPixel + mWidthPerDay - mHeaderColumnWidth;
                        hourLines[i * 4 + 3] = top;
                        i++;
                    }


                }
            }

            // Draw the lines for hours.
            canvas.drawLines(hourLines, mHourSeparatorPaint);
            if (mNumberOfVisibleDays != 1)
                canvasclipRect(canvas, mHeaderColumnWidth, mHeaderHeight + (mHeaderRowPadding * 3) - 70, getWidth(), getHeight(), Region.Op.REPLACE);


            // Draw the events.
            if (mNumberOfVisibleDays != 1) drawEvents(day, startPixel, canvas);
            else drawEvents(day, startPixel, canvas);
            // Draw the line at the current time.


            // In the next iteration, start from the next day.
            startPixel += mWidthPerDay + mColumnGap;
        }

        if (mNumberOfVisibleDays != 1)
            canvasclipRect(canvas, mHeaderColumnWidth, 0, getWidth(), getHeight(), Region.Op.REPLACE);
        // Draw the header background.
        canvas.drawRect(0, 0, getWidth(), mHeaderHeight - 70 + mHeaderRowPadding * 3, mHeaderBackgroundPaint);
        mHourSeparatorPaint.setStrokeWidth(mHourSeparatorHeight * 2);
        if (mNumberOfVisibleDays != 1)
            canvas.drawLine(mHeaderColumnWidth, mHeaderHeight + mHeaderRowPadding * 3 - 105, mHeaderColumnWidth, getHeight(), mHourSeparatorPaint);
        mHourSeparatorPaint.setStrokeWidth(mHourSeparatorHeight);


        startPixel = startFromPixel;
        float begin = startPixel;
        Calendar start = (Calendar) today.clone();

        for (int dayNumber = leftDaysWithGaps + 1; dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1; dayNumber++) {
            // Check if the day is today.

            day = (Calendar) today.clone();
            day.add(Calendar.DATE, dayNumber - 1);
            boolean sameDay = isSameDay(day, today);
            int daycompare = day.compareTo(today);
            if (daycompare < 0) {
                jHeaderTextPaint.setColor(Color.parseColor("#606368"));
            } else {
                jHeaderTextPaint.setColor(Color.BLACK);

            }
            if (dayNumber == leftDaysWithGaps + 1) start = day;

            // Draw the day labels.
            String dayLabel1 = getDateTimeInterpreter().interpretDate(day);
            String dayLabel = getDateTimeInterpreter().interpretday(day);
            if (dayLabel == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null date");


            float x = startPixel + mWidthPerDay / 2;
            float xx = startPixel - mHeaderColumnWidth / 2.0f;
            float y = (mHeaderTextHeight + mHeaderRowPadding * 1.76f + jHeaderTextHeight) - jHeaderTextHeight / 2.0f;

            int size = getResources().getDimensionPixelSize(R.dimen.todaysize);
            if (mNumberOfVisibleDays != 1)
                canvas.drawLine(startPixel, mHeaderHeight + mHeaderRowPadding * 3 - 105, startPixel, getHeight(), mHourSeparatorPaint);
            else {
                canvas.drawLine(startPixel, mHeaderRowPadding / 3.0f, startPixel, getHeight(), mHourSeparatorPaint);

            }
            float marginLeft = 130; // Adjust the left margin as needed
            String inputDate = String.valueOf(MainActivity.lastdate);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd");

            SimpleDateFormat inputFormat_title = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat_title = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                Date date = inputFormat.parse(inputDate);
                Date date_title = inputFormat_title.parse(inputDate);
                String formattedDate = outputFormat.format(date);
                String formattedDate_title = outputFormat_title.format(date_title);
                MainActivity.title.setText(formattedDate_title);
                MainActivity.calender_date.setText(formattedDate);
                Calendar currentDate = Calendar.getInstance();
                // Get the day of the week and day of the month from the current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd", Locale.ENGLISH);
                String currentDateString = dateFormat.format(currentDate.getTime());
Log.d("dateeeeeee76876678768", formattedDate+"   date");
                // Check if the given date string matches the current date string
                if (formattedDate.equals(currentDateString)) {
                    // The given date is the current date
                    MainActivity.calender_date.setTextColor(Color.parseColor("#5C79FF"));
                    MainActivity.current_date.setTextColor(Color.parseColor("#5C79FF"));
                    TextView currentDateTextView = MainActivity.current_date;
                    int dateTextSizeDP = 15; // Size for the date
                    int todayTextSizeDP = 11; // Size for "Today"
                    float scale = getResources().getDisplayMetrics().density;
                    int dateTextSizePX = (int) (dateTextSizeDP * scale + 0.5f);
                    int todayTextSizePX = (int) (todayTextSizeDP * scale + 0.5f);
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(formattedDate);
                    String todayString = "\nToday";
                    SpannableString todaySpan = new SpannableString(todayString);
                    todaySpan.setSpan(new AbsoluteSizeSpan(todayTextSizePX), 0, todayString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(todaySpan);
                    currentDateTextView.setText(builder, TextView.BufferType.SPANNABLE);
                    currentDateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dateTextSizePX);
                } else {
                    // The given date is not the current date
                    MainActivity.calender_date.setTextColor(Color.parseColor("#000000"));
                    MainActivity.current_date.setTextColor(Color.parseColor("#000000"));
                    TextView currentDateTextView = MainActivity.current_date;
                    int dateTextSizeDP = 15; // Size for the date
                    float scale = getResources().getDisplayMetrics().density;
                    int dateTextSizePX = (int) (dateTextSizeDP * scale + 0.5f);
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.append(formattedDate);
                    currentDateTextView.setText(builder, TextView.BufferType.SPANNABLE);
                    currentDateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dateTextSizePX);
                }


            } catch (Exception e) {

            }
            String formattedDate = new SimpleDateFormat("EEEE, d", Locale.getDefault()).format(day.getTime());
            String nowDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(day.getTime());
            MainActivity.fun(mContext, nowDate);
            if (mNumberOfVisibleDays == 1) {
                if (sameDay) {
//                    canvas.drawRoundRect(xx - size, y - size, xx + size, y + size, size, size, mTodayBackgroundPaint);
                    // Set the color to 5C79FF if it's the same day
//                    mTodayHeaderTextPaint.setColor(Color.parseColor("#5C79FF"));

                    jtodayHeaderTextPaint.setColor(Color.parseColor("#5C79FF"));
                    jtodayHeaderTextPaint.setTextSize(32);
//                    MainActivity.calender_date.setTextColor(Color.parseColor("#5C79FF"));
                } else {
                    // Set the color to black if it's not the same day
//                    mTodayHeaderTextPaint.setColor(Color.BLACK);
                    jtodayHeaderTextPaint.setColor(Color.BLACK);
                    jtodayHeaderTextPaint.setTextSize(32);
//                    MainActivity.calender_date.setTextColor(Color.parseColor("#000000"));

                }

                // Format the date as "Wednesday, 27"
                // Draw the formatted date with left margin
//                canvas.drawText(formattedDate, startPixel - mHeaderColumnWidth / 2.0f + marginLeft, mHeaderTextHeight + mHeaderRowPadding / 3.0f, sameDay ? mTodayHeaderTextPaint : mHeaderTextPaint);
                canvas.drawText(formattedDate, startPixel - mHeaderColumnWidth / 2.0f + marginLeft, mHeaderTextHeight + mHeaderRowPadding * 1.76f + jHeaderTextHeight, sameDay ? jtodayHeaderTextPaint : jHeaderTextPaint);
                String date_ = MainActivity.lastdate + ""; // Example date
                EventDbHelper eventDbHelper = new EventDbHelper(mContext);
                List<Event> events = eventDbHelper.getCheckedEventsByDate(date_);
                for (Event event : events) {
                    long id = event.getId();
                    String title = event.getTitle();
                    String time = event.getTime();
                    String description = event.getDescription();
//                    Toast.makeText(mContext, ""+event.isChecked(), Toast.LENGTH_SHORT).show();
                    boolean checked = event.isChecked();
                    String date = event.getDate();
                    String[] times = time.split("-");
                    if (times.length == 2) {
                        String startTime = times[0]; // "04"
                        String endTime = times[1];   // "19"
//                        Toast.makeText(mContext, "log"+ id+"  -- "+ title+"  -- "+ time+"  -- "+ description+"  -- "+ checked+"  -- "+ date, Toast.LENGTH_SHORT).show();
                        Log.d("tesdffdfdft", event.getComplete() + "-----" + id + "  -- " + title + "  -- " + time + "  -- " + description + "  -- " + checked + "  -- " + date);
                        if (event.getComplete() == 0) {
                            draw_event(id, title, time, event.getExact_time(), description, false, date, startPixel, canvas, Integer.valueOf(startTime), Integer.valueOf(endTime), title);
                        }
                        else
                        {
                            draw_event_complete(id, title, time, event.getExact_time(), description, false, date, startPixel, canvas, Integer.valueOf(startTime), Integer.valueOf(endTime), title);
                        }
                    }
                }
                drawAllDayEvents(day, startPixel, canvas, dayNumber, false);
            } else {
                if (sameDay) {
//                    canvas.drawRoundRect(x - size, y - size, x + size, y + size, size, size, mTodayBackgroundPaint);
                    // Set the color to 5C79FF if it's the same day
//                    mTodayHeaderTextPaint.setColor(Color.parseColor("#5C79FF"));
                    jtodayHeaderTextPaint.setColor(Color.parseColor("#5C79FF"));
                    jtodayHeaderTextPaint.setTextSize(32);

                } else {
                    // Set the color to black if it's not the same day
//                    mTodayHeaderTextPaint.setColor(Color.BLACK);
                    jtodayHeaderTextPaint.setColor(Color.BLACK);
                    jtodayHeaderTextPaint.setTextSize(32);
//                    MainActivity.calender_date.setTextColor(Color.parseColor("#000000"));
                }

                // Format the date as "Wednesday, 27"

                // Draw the formatted date with left margin
//                canvas.drawText(formattedDate, startPixel + mWidthPerDay / 2 + marginLeft, mHeaderTextHeight + mHeaderRowPadding / 3.0f, sameDay ? mTodayHeaderTextPaint : mHeaderTextPaint);
                canvas.drawText(formattedDate, startPixel + mWidthPerDay / 2 + marginLeft, mHeaderTextHeight + mHeaderRowPadding * 1.76f + jHeaderTextHeight, sameDay ? jtodayHeaderTextPaint : jHeaderTextPaint);

                drawAllDayEvents(day, startPixel, canvas, dayNumber, false);
            }

            if (mNumberOfVisibleDays == 1)
                drawTimeColumnAndAxes1day(canvas, startPixel - mHeaderColumnWidth);

            if (mShowNowLine && sameDay) {
                Calendar now = Calendar.getInstance();
                LocalDate currentDate = LocalDate.now();
                if (day.get(Calendar.YEAR) == currentDate.getYear() &&
                        day.get(Calendar.MONTH) == currentDate.getMonthValue() - 1 &&
                        day.get(Calendar.DAY_OF_MONTH) == currentDate.getDayOfMonth()) {

                    float startY = mHeaderHeight + mHeaderRowPadding * 3 + mTimeTextHeight / 2 + mHeaderMarginBottom + mCurrentOrigin.y;
                    float beforeNow = (now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) / 60.0f) * mHourHeight;

                    float startat = startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel;
                    float wid = mWidthPerDay;
                    float per = 10 * (1.0f - (startat - startPixel) / wid);

                    // Draw rectangle background
                    Paint rectanglePaint = new Paint();
                    rectanglePaint.setColor(Color.parseColor("#3C5C9C")); // Set the background color
                    rectanglePaint.setStyle(Paint.Style.FILL);
                    float rectLeft = startat - per - 125; // Adjust width and position more to the left
                    float rectTop = startY + beforeNow - per - mTimeTextHeight / 2 + 3; // Adjust height and position more to the top
                    float rectRight = startat - per;
                    float rectBottom = startY + beforeNow + mTimeTextHeight / 2 + 6; // Adjust rectangle height based on text height
                    float cornerRadius = 30; // Set corner radius
                    canvas.drawRoundRect(rectLeft, rectTop, rectRight, rectBottom, cornerRadius, cornerRadius, rectanglePaint);

                    rectanglePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

                    // Draw the current time text
                    Paint textPaint = new Paint();
                    textPaint.setColor(Color.WHITE); // Set the text color to white
                    textPaint.setTextSize(mTimeTextHeight);
                    textPaint.setTextAlign(Paint.Align.CENTER);
                    float textX = (rectLeft + rectRight) / 2; // Center text horizontally within the rectangle
                    float textY = startY + beforeNow + mTimeTextHeight / 4; // Adjust the vertical position of the text
                    String currentTime = getTimeString(now);

                    canvas.drawText(currentTime, textX, textY, textPaint);

                    // Remove the Xfermode
                    rectanglePaint.setXfermode(null);

                    // Draw the now line after the time rectangle box
                    canvas.drawLine(rectRight, startY + beforeNow, startPixel + wid, startY + beforeNow, mNowLinePaint);


                }
            }
            startPixel += mWidthPerDay + mColumnGap;

        }

    }


    public  Calendar getTimeFromPoint(float x, float y) {
        int leftDaysWithGaps = (int) -(Math.ceil(mCurrentOrigin.x / (mWidthPerDay + mColumnGap)));
        float startPixel = mCurrentOrigin.x + (mWidthPerDay + mColumnGap) * leftDaysWithGaps +
                mHeaderColumnWidth;
        for (int dayNumber = leftDaysWithGaps + 1;
             dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1;
             dayNumber++) {
            float start = (startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel);
            if (mWidthPerDay + startPixel - start > 0 && x > start && x < startPixel + mWidthPerDay) {
                Calendar day = today();
                day.add(Calendar.DATE, dayNumber - 1);
                float pixelsFromZero = y - mCurrentOrigin.y - mHeaderHeight
                        - mHeaderRowPadding * 3 - mTimeTextHeight / 2 - mHeaderMarginBottom;
                int hour = (int) (pixelsFromZero / mHourHeight);
                int minute = (int) (60 * (pixelsFromZero - hour * mHourHeight) / mHourHeight);
                day.add(Calendar.HOUR, hour);
                day.set(Calendar.MINUTE, minute);
                return day;
            }
            startPixel += mWidthPerDay + mColumnGap;
        }
        return null;
    }

    public void drawEvents(Calendar date, float startFromPixel, Canvas canvas) {
        if (mEventRects != null && mEventRects.size() > 0) {
            for (int i = 0; i < mEventRects.size(); i++) {
                if (isSameDay(mEventRects.get(i).event.getStartTime(), date) && !mEventRects.get(i).event.isAllDay()) {

                    // Calculate top.
                    float top = mHourHeight * 24 * mEventRects.get(i).top / 1440 + mCurrentOrigin.y + mHeaderHeight + mHeaderRowPadding * 3 + mHeaderMarginBottom + mTimeTextHeight / 2 + mEventMarginVertical;

                    // Calculate bottom.
                    float bottom = mEventRects.get(i).bottom;
                    bottom = mHourHeight * 24 * bottom / 1440 + mCurrentOrigin.y + mHeaderHeight + mHeaderRowPadding * 3 + mHeaderMarginBottom + mTimeTextHeight / 2 - mEventMarginVertical;

                    // Calculate left and right.
                    float left = startFromPixel + mEventRects.get(i).left * (mWidthPerDay - (mNumberOfVisibleDays == 1 ? mHeaderColumnWidth : 0));
                    if (left < startFromPixel)
                        left += mOverlappingEventGap;
                    float right = left + mEventRects.get(i).width * (mWidthPerDay - (mNumberOfVisibleDays == 1 ? mHeaderColumnWidth : 0));
                    if (right < startFromPixel + (mWidthPerDay - (mNumberOfVisibleDays == 1 ? mHeaderColumnWidth : 0)))
                        right -= mOverlappingEventGap;

                    // Draw the event and the event name on top of it.
                    if (left < right &&
                            left < getWidth() &&
                            top < getHeight() &&
                            right > mHeaderColumnWidth &&
                            bottom > mHeaderHeight + mHeaderRowPadding * 3 + mTimeTextHeight / 2 + mHeaderMarginBottom
                    ) {
                        mEventRects.get(i).rectF = new RectF(left, top, right, bottom);
//                        mEventBackgroundPaint.setColor(mEventRects.get(i).event.getColor() == 0 ? mDefaultEventColor : mEventRects.get(i).event.getColor());
//                        canvas.drawRoundRect(mEventRects.get(i).rectF, mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);
                        drawEventTitle(mEventRects.get(i).event, mEventRects.get(i).rectF, canvas, top, left);
                    } else
                        mEventRects.get(i).rectF = null;
                }
            }
        }
    }

    public void drawAllDayEvents(Calendar date, float startFromPixel, Canvas canvas, int daynumber, boolean check) {
        if (mEventRects != null && mEventRects.size() > 0) {
            for (int i = 0; i < mEventRects.size(); i++) {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

                if (isSameDay(mEventRects.get(i).event.getStartTime(), date) && mEventRects.get(i).event.isAllDay()) {
                    // Calculate top.
                    float top = 0;
                    // Calculate bottom.
                    if (mNumberOfVisibleDays == 1) {
                        top = mHeaderRowPadding / 3.0f + mEventMarginVertical + mEventRects.get(i).top;
                    } else {
                        top = (3 * mHeaderRowPadding) + mHeaderTextHeight + jHeaderTextHeight + mEventMarginVertical + mEventRects.get(i).top;
                    }
                    float bottom = top + mEventRects.get(i).bottom;

                    // Calculate left and right.
                    float left = startFromPixel;

                    if (left < startFromPixel)
                        left += mOverlappingEventGap;

                    float right = left + (mWidthPerDay - (mNumberOfVisibleDays == 1 ? mHeaderColumnWidth : 0)) - 10;
                    if (right < startFromPixel + (mWidthPerDay - (mNumberOfVisibleDays == 1 ? mHeaderColumnWidth : 0)))
                        right -= mOverlappingEventGap;

                    // Draw the event and the event name on top of it.
                    if (left < right && left < getWidth() && top < getHeight() && right > mHeaderColumnWidth && bottom > 0) {
                        mEventRects.get(i).rectF = new RectF(left, top, right, bottom);

                        boolean mycheck = getNumberOfVisibleDays() != 1 && mEventRects.get(i).event.isIsmoreday();
                        mEventBackgroundPaint.setColor(mEventRects.get(i).event.getColor() == 0 ? mDefaultEventColor : mEventRects.get(i).event.getColor());

                        if (mycheck) {
                            if (true) {
                                float startat = startFromPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startFromPixel;
                                float wid = mWidthPerDay;
                                mEventRects.get(i).rectF.left = startat;
                                if (mEventRects.get(i).event.getDaytype() != 1 && startat > 200) {
                                    mEventRects.get(i).rectF.left = startat - mEventCornerRadius * 2;
                                }
                                mEventRects.get(i).rectF.right = startFromPixel + (wid);
                                RectF fd = mEventRects.get(i).rectF;
                                if (mEventRects.get(i).event.getDaytype() == mEventRects.get(i).event.getNoofday()) {
                                    fd.right = fd.right - 10;
                                }
                                // Draw the event background with white color
                                mEventBackgroundPaint.setColor(Color.WHITE);
                                canvas.drawRoundRect(fd, mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);

                                if (startFromPixel < mHeaderColumnWidth + 5 || mEventRects.get(i).event.getDaytype() == 1) {
                                    // Draw the event text with black color
                                    canvas.drawText(mEventRects.get(i).event.getName(), startat, mEventRects.get(i).rectF.centerY() - jheaderEventheight, jheaderEventTextpaint);
                                }
                            }
                        } else {
                            // Draw the event background with white color
                            mEventBackgroundPaint.setColor(Color.WHITE);
                            canvas.drawRoundRect(mEventRects.get(i).rectF, mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);

                            // Draw the event text with black color
                            canvas.drawText(mEventRects.get(i).event.getName(), mEventRects.get(i).rectF.left + 12, mEventRects.get(i).rectF.centerY() - jheaderEventheight, jheaderEventTextpaint);
                        }
                    } else {
                        mEventRects.get(i).rectF = null;
                    }
                }
            }
        }
    }

    public void drawEventTitle(WeekViewEvent event, RectF rect, Canvas canvas, float originalTop, float originalLeft) {
        if (rect.right - rect.left - mEventPadding * 2 < 0) return;
        if (rect.bottom - rect.top - mEventPadding * 2 < 0) return;

        // Prepare the name of the event.
        SpannableStringBuilder bob = new SpannableStringBuilder();
        if (event.getName() != null) {
            bob.append(event.getName());
            bob.setSpan(new StyleSpan(Typeface.BOLD), 0, bob.length(), 0);
            bob.append(' ');
        }

        // Prepare the location of the event.
        if (event.getLocation() != null) {
            bob.append(event.getLocation());
        }

        int availableHeight = (int) (rect.bottom - originalTop - mEventPadding * 2);
        int availableWidth = (int) (rect.right - originalLeft - mEventPadding * 2);

        // Get text dimensions.
        StaticLayout textLayout = new StaticLayout(bob, mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int lineHeight = textLayout.getHeight() / textLayout.getLineCount();

        if (availableHeight >= lineHeight) {
            // Calculate available number of line counts.
            int availableLineCount = availableHeight / lineHeight;
            do {
                // Ellipsize text to fit into event rect.
                textLayout = new StaticLayout(TextUtils.ellipsize(bob, mEventTextPaint, availableLineCount * availableWidth, TextUtils.TruncateAt.END), mEventTextPaint, (int) (rect.right - originalLeft - mEventPadding * 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                // Reduce line count.
                availableLineCount--;

                // Repeat until text is short enough.
            } while (textLayout.getHeight() > availableHeight);

            // Draw text.
            canvas.save();
            canvas.translate(originalLeft + mEventPadding, originalTop + mEventPadding);
            textLayout.draw(canvas);
            canvas.restore();
        }
    }

    /**
     * Gets more events of one/more month(s) if necessary. This method is called when the user is
     * scrolling the week view. The week view stores the events of three months: the visible month,
     * the previous month, the next month.
     *
     * @param day The day where the user is currently is.
     */
    public void getMoreEvents(Calendar day) {

        // Get more events if the month is changed.
        if (mEventRects == null)
            mEventRects = new ArrayList<EventRect>();
        if (mWeekViewLoader == null && !isInEditMode())
            throw new IllegalStateException("You must provide a MonthChangeListener");

        // If a refresh was requested then reset some variables.
        if (mRefreshEvents) {
            mEventRects.clear();
            mPreviousPeriodEvents = null;
            mCurrentPeriodEvents = null;
            mNextPeriodEvents = null;
            mFetchedPeriod = -1;
        }

        if (mWeekViewLoader != null) {
            int periodToFetch = (int) mWeekViewLoader.toWeekViewPeriodIndex(day);
            if (!isInEditMode() && (mFetchedPeriod < 0 || mFetchedPeriod != periodToFetch || mRefreshEvents)) {
                List<? extends WeekViewEvent> previousPeriodEvents = null;
                List<? extends WeekViewEvent> currentPeriodEvents = null;
                List<? extends WeekViewEvent> nextPeriodEvents = null;

                if (mPreviousPeriodEvents != null && mCurrentPeriodEvents != null && mNextPeriodEvents != null) {
                    if (periodToFetch == mFetchedPeriod - 1) {
                        currentPeriodEvents = mPreviousPeriodEvents;
                        nextPeriodEvents = mCurrentPeriodEvents;
                    } else if (periodToFetch == mFetchedPeriod) {
                        previousPeriodEvents = mPreviousPeriodEvents;
                        currentPeriodEvents = mCurrentPeriodEvents;
                        nextPeriodEvents = mNextPeriodEvents;
                    } else if (periodToFetch == mFetchedPeriod + 1) {
                        previousPeriodEvents = mCurrentPeriodEvents;
                        currentPeriodEvents = mNextPeriodEvents;
                    }
                }
                if (currentPeriodEvents == null)
                    currentPeriodEvents = mWeekViewLoader.onLoad(periodToFetch);
                if (previousPeriodEvents == null)
                    previousPeriodEvents = mWeekViewLoader.onLoad(periodToFetch - 1);
                if (nextPeriodEvents == null)
                    nextPeriodEvents = mWeekViewLoader.onLoad(periodToFetch + 1);


                // Clear events.
                mEventRects.clear();
                sortAndCacheEvents(previousPeriodEvents);
                sortAndCacheEvents(currentPeriodEvents);
                sortAndCacheEvents(nextPeriodEvents);
                calculateHeaderHeight();

                mPreviousPeriodEvents = previousPeriodEvents;
                mCurrentPeriodEvents = currentPeriodEvents;
                mNextPeriodEvents = nextPeriodEvents;
                mFetchedPeriod = periodToFetch;
            }
        }

        // Prepare to calculate positions of each events.
        List<EventRect> tempEvents = mEventRects;
        mEventRects = new ArrayList<EventRect>();

        // Iterate through each day with events to calculate the position of the events.
        while (tempEvents.size() > 0) {
            ArrayList<EventRect> eventRects = new ArrayList<>(tempEvents.size());

            // Get first event for a day.
            EventRect eventRect1 = tempEvents.remove(0);
            eventRects.add(eventRect1);

            int i = 0;
            while (i < tempEvents.size()) {
                // Collect all other events for same day.
                EventRect eventRect2 = tempEvents.get(i);
                if (isSameDay(eventRect1.event.getStartTime(), eventRect2.event.getStartTime())) {
                    tempEvents.remove(i);
                    eventRects.add(eventRect2);
                } else {
                    i++;
                }
            }
            computePositionOfEvents(eventRects);
        }
    }

    /**
     * Cache the event for smooth scrolling functionality.
     *
     * @param event The event to cache.
     */
    public void cacheEvent(WeekViewEvent event) {
        if (event.getStartTime().compareTo(event.getEndTime()) >= 0)
            return;
        List<WeekViewEvent> splitedEvents = event.splitWeekViewEvents();
        for (WeekViewEvent splitedEvent : splitedEvents) {
            mEventRects.add(new EventRect(splitedEvent, event, null));
        }
    }

    /**
     * Sort and cache events.
     *
     * @param events The events to be sorted and cached.
     */
    public void sortAndCacheEvents(List<? extends WeekViewEvent> events) {
        sortEvents(events);
        for (WeekViewEvent event : events) {
            cacheEvent(event);
        }
    }

    /**
     * Sorts the events in ascending order.
     *
     * @param events The events to be sorted.
     */
    public void sortEvents(List<? extends WeekViewEvent> events) {
        Collections.sort(events, new Comparator<WeekViewEvent>() {
            @Override
            public int compare(WeekViewEvent event1, WeekViewEvent event2) {
                Log.e("mecheck" + event1.getName() + "," + event2.getName(), event1.getMyday() + "," + event2.getMyday());
                return event1.getMyday() > event2.getMyday() ? -1 : event1.getMyday() < event2.getMyday() ? 1 : 0;
//                    long end2 = event2.getEndTime().getTimeInMillis();

//                long start1 = event1.getStartTime().getTimeInMillis();
//                long start2 = event2.getStartTime().getTimeInMillis();
//                int comparator = start1 > start2 ? 1 : (start1 < start2 ? -1 : 0);
//                if (comparator == 0) {
//                    long end1 = event1.getEndTime().getTimeInMillis();
//                    long end2 = event2.getEndTime().getTimeInMillis();
//                    comparator = end1 > end2 ? 1 : (end1 < end2 ? -1 : 0);
//                }
//                return comparator;
            }
        });
    }

    /**
     * Calculates the left and right positions of each events. This comes handy specially if events
     * are overlapping.
     *
     * @param eventRects The events along with their wrapper class.
     */
    public void computePositionOfEvents(List<EventRect> eventRects) {
        // Make "collision groups" for all events that collide with others.
        List<List<EventRect>> collisionGroups = new ArrayList<List<EventRect>>();
        for (EventRect eventRect : eventRects) {
            boolean isPlaced = false;

            outerLoop:
            for (List<EventRect> collisionGroup : collisionGroups) {
                for (EventRect groupEvent : collisionGroup) {
                    if (isEventsCollide(groupEvent.event, eventRect.event) && groupEvent.event.isAllDay() == eventRect.event.isAllDay()) {
                        collisionGroup.add(eventRect);
                        isPlaced = true;
                        break outerLoop;
                    }
                }
            }

            if (!isPlaced) {
                List<EventRect> newGroup = new ArrayList<EventRect>();
                newGroup.add(eventRect);
                collisionGroups.add(newGroup);
            }
        }

        for (List<EventRect> collisionGroup : collisionGroups) {
            expandEventsToMaxWidth(collisionGroup);
        }
    }

    /**
     * Expands all the events to maximum possible width. The events will try to occupy maximum
     * space available horizontally.
     *
     * @param collisionGroup The group of events which overlap with each other.
     */
    public void expandEventsToMaxWidth(List<EventRect> collisionGroup) {

        // Expand the events to maximum possible width.
        List<List<EventRect>> columns = new ArrayList<List<EventRect>>();
        columns.add(new ArrayList<EventRect>());
        for (EventRect eventRect : collisionGroup) {
            boolean isPlaced = false;
            for (List<EventRect> column : columns) {
                if (column.size() == 0) {
                    column.add(eventRect);
                    isPlaced = true;
                } else if (!isEventsCollide(eventRect.event, column.get(column.size() - 1).event)) {
                    column.add(eventRect);
                    isPlaced = true;
                    break;
                }
            }
            if (!isPlaced) {
                List<EventRect> newColumn = new ArrayList<EventRect>();
                newColumn.add(eventRect);
                columns.add(newColumn);
            }
        }


        // Calculate left and right position for all the events.
        // Get the maxRowCount by looking in all columns.
        int maxRowCount = 0;
        for (List<EventRect> column : columns) {
            maxRowCount = Math.max(maxRowCount, column.size());
        }

        for (int i = 0; i < maxRowCount; i++) {
            // Set the left and right values of the event.
            float j = 0;
            for (List<EventRect> column : columns) {
                if (column.size() >= i + 1) {

                    EventRect eventRect = column.get(i);
                    eventRect.width = 1f / columns.size();
                    eventRect.left = j / columns.size();
                    if (!eventRect.event.isAllDay()) {
                        eventRect.top = eventRect.event.getStartTime().get(Calendar.HOUR_OF_DAY) * 60 + eventRect.event.getStartTime().get(Calendar.MINUTE);
                        eventRect.bottom = eventRect.event.getEndTime().get(Calendar.HOUR_OF_DAY) * 60 + eventRect.event.getEndTime().get(Calendar.MINUTE);
                    } else {
                        eventRect.top = j * mAllDayEventHeight + j * 5;
                        eventRect.bottom = mAllDayEventHeight;
                        eventRect.noofevent = columns.size();
                    }
                    mEventRects.add(eventRect);
                }
                j++;
            }
        }
    }

    /**
     * Checks if two events overlap.
     *
     * @param event1 The first event.
     * @param event2 The second event.
     * @return true if the events overlap.
     */
    public static boolean isEventsCollide(WeekViewEvent event1, WeekViewEvent event2) {
        long start1 = event1.getStartTime().getTimeInMillis();
        long end1 = event1.getEndTime().getTimeInMillis();
        long start2 = event2.getStartTime().getTimeInMillis();
        long end2 = event2.getEndTime().getTimeInMillis();
        return !((start1 >= end2) || (end1 <= start2));
    }

 @Override
    public void invalidate() {
        super.invalidate();
        mAreDimensionsInvalid = true;
    }

    public void setOnEventClickListener(EventClickListener listener) {
        this.mEventClickListener = listener;
    }

    public void setshadow(View shadow) {
        this.shadow = shadow;
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to setting and getting the properties.
    //
    /////////////////////////////////////////////////////////////////

    public void setfont(Typeface typeface, int type) {
        if (type == 0) {
            mTimeTextPaint.setTypeface(typeface);
            jHeaderTextPaint.setTypeface(ResourcesCompat.getFont(mContext, R.font.latoregular));


        } else {
            mHeaderTextPaint.setTypeface(typeface);
            jheaderEventTextpaint.setTypeface(typeface);

        }
        invalidate();

    }

    public @Nullable
    MonthLoader.MonthChangeListener getMonthChangeListener() {
        if (mWeekViewLoader instanceof MonthLoader)
            return ((MonthLoader) mWeekViewLoader).getOnMonthChangeListener();
        return null;
    }

    public void setMonthChangeListener(MonthLoader.MonthChangeListener monthChangeListener) {
        this.mWeekViewLoader = new MonthLoader(monthChangeListener);
    }


    public void setEventLongPressListener(EventLongPressListener eventLongPressListener) {
        this.mEventLongPressListener = eventLongPressListener;
    }

    public EmptyViewClickListener getEmptyViewClickListener() {
        return mEmptyViewClickListener;
    }

    public void setEmptyViewClickListener(EmptyViewClickListener emptyViewClickListener) {
        this.mEmptyViewClickListener = emptyViewClickListener;
    }

    public EmptyViewLongPressListener getEmptyViewLongPressListener() {
        return mEmptyViewLongPressListener;
    }

    public void setEmptyViewLongPressListener(EmptyViewLongPressListener emptyViewLongPressListener) {
        this.mEmptyViewLongPressListener = emptyViewLongPressListener;
    }
   public void setScrollListener(ScrollListener scrolledListener) {
        this.mScrollListener = scrolledListener;
    }

    public DateTimeInterpreter getDateTimeInterpreter() {
        if (mDateTimeInterpreter == null) {
            mDateTimeInterpreter = new DateTimeInterpreter() {
                @Override
                public String interpretday(Calendar date) {
                    try {
                        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_NUMERIC_DATE;
                        String localizedDate = DateUtils.formatDateTime(getContext(), date.getTime().getTime(), flags);
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());

//                        SimpleDateFormat sdf = mDayNameLength == LENGTH_SHORT ? new SimpleDateFormat("EEEEE", Locale.getDefault()) : new SimpleDateFormat("EEE", Locale.getDefault());
                        return String.format("%s %s", sdf.format(date.getTime()).toUpperCase(), localizedDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }

                @Override
                public String interpretDate(Calendar date) {
                    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);


                    return dayOfMonth + "";
                }

                @Override
                public String interpretTime(int hour) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, 0);

                    try {
                        SimpleDateFormat sdf = DateFormat.is24HourFormat(getContext()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
                        return sdf.format(calendar.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            };
        }
        return mDateTimeInterpreter;
    }

    /**
     * Set the interpreter which provides the text to show in the header column and the header row.
     *
     * @param dateTimeInterpreter The date, time interpreter.
     */
    public void setDateTimeInterpreter(DateTimeInterpreter dateTimeInterpreter) {
        this.mDateTimeInterpreter = dateTimeInterpreter;

        // Refresh time column width.
        initTextTimeWidth();
    }

    /**
     * Get the number of visible days in a week.
     *
     * @return The number of visible days in a week.
     */
    public int getNumberOfVisibleDays() {
        return mNumberOfVisibleDays;
    }

    /**
     * Set the number of visible days in a week.
     *
     * @param numberOfVisibleDays The number of visible days in a week.
     */
    public void setNumberOfVisibleDays(int numberOfVisibleDays) {
        this.mNumberOfVisibleDays = numberOfVisibleDays;
        mCurrentOrigin.x = 0;
        mCurrentOrigin.y = 0;
        invalidate();
    }

    public int getHourHeight() {
        return mHourHeight;
    }

    public void setHourHeight(int hourHeight) {
        mNewHourHeight = hourHeight;
        invalidate();
    }

    public int getColumnGap() {
        return mColumnGap;
    }

    public void setColumnGap(int columnGap) {
        mColumnGap = columnGap;
        invalidate();
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    /**
     * Set the first day of the week. First day of the week is used only when the week view is first
     * drawn. It does not of any effect after user starts scrolling horizontally.
     * <p>
     * <b>Note:</b> This method will only work if the week view is set to display more than 6 days at
     * once.
     * </p>
     *
     * @param firstDayOfWeek The supported values are {@link Calendar#SUNDAY},
     *                       {@link Calendar#MONDAY}, {@link Calendar#TUESDAY},
     *                       {@link Calendar#WEDNESDAY}, {@link Calendar#THURSDAY},
     *                       {@link Calendar#FRIDAY}.
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
        invalidate();
    }
    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
//        mTodayHeaderTextPaint.setTextSize(mTextSize);
        mHeaderTextPaint.setTextSize(mTextSize);
        mTimeTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public Calendar getFirstVisibleDay() {
        return mFirstVisibleDay;
    }


    @Override
    public  boolean onTouchEvent(MotionEvent event) {

        if (isclosed()) return false;

        mScaleDetector.onTouchEvent(event);
        boolean val = mGestureDetector.onTouchEvent(event);
        float clickX = event.getX();
        float clickY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
Log.d("test", "!");
                // Check if the click event falls within the rectangle and show a toast
                boolean isClicked = isWithinRectangle(clickX, clickY);
                if (isClicked) {
                    Toast.makeText(getContext(), "Clicked on: " + "title", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        // Check after call of mGestureDetector, so mCurrentFlingDirection and mCurrentScrollDirection are set.
        if (event.getAction() == MotionEvent.ACTION_UP && !mIsZooming && mCurrentFlingDirection == Direction.NONE) {
            Log.d("test", "2");
            if (mCurrentScrollDirection == Direction.RIGHT || mCurrentScrollDirection == Direction.LEFT) {
                if (true) {
                    Log.d("test", "3");
                    float k = 0;
                    if (mCurrentScrollDirection == Direction.RIGHT && getNumberOfVisibleDays() == 1)
                        k = mHeaderColumnWidth;
                    int next = 0;
                    Log.d("test", "4");
                    if (getNumberOfVisibleDays() == 7) {
                        Log.d("test", "5");
                        if (mCurrentScrollDirection == Direction.LEFT) {
                            next = (int) (weekx - mWidthPerDay * 7);
                            Log.d("test", "6");
                        } else {
                            next = (int) (weekx + mWidthPerDay * 7);
                            Log.d("test", "7");
                        }
                    } else {
                        Log.d("test", "8");
                        if (mCurrentScrollDirection == Direction.LEFT) {
                            Log.d("test", "9");
                            next = (int) (weekx - mWidthPerDay);
                        } else {
                            Log.d("test", "10");
                            next = (int) (weekx + mWidthPerDay);
                        }
                    }

                    int previous = (int) weekx;
//                    if (mCurrentOrigin.x<0){
//                        next*=-1;
//                        previous*=-1;
//                    }
                    if (Math.abs(Math.abs(mCurrentOrigin.x + k) - Math.abs(next)) < Math.abs(Math.abs(mCurrentOrigin.x + k) - Math.abs(previous))) {

                        Log.d("test", "11");
                        mCurrentOrigin.x = next;

                        ViewCompat.postInvalidateOnAnimation(WeekView.this);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                invalidate();
                            }
                        }, 100);

                    } else {
                        Log.d("test", "12");
                        mCurrentOrigin.x = previous;

                        ViewCompat.postInvalidateOnAnimation(WeekView.this);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                invalidate();
                            }
                        }, 100);

                    }
                }
            }
            mCurrentScrollDirection = Direction.NONE;
        }

        return val;
    }

    public void goToNearestOrigin() {

        double leftDays = mCurrentOrigin.x / (mWidthPerDay + mColumnGap);
        if (getNumberOfVisibleDays() == 1 && mCurrentScrollDirection == Direction.RIGHT) {
            leftDays = (mCurrentOrigin.x + mHeaderColumnWidth) / (mWidthPerDay + mColumnGap);

        }

        if (mCurrentFlingDirection != Direction.NONE) {
            // snap to nearest day
            leftDays = Math.round(leftDays);
        } else if (mCurrentScrollDirection == Direction.LEFT) {
            // snap to last day
            Log.d("test", "112");

            leftDays = Math.floor(leftDays);
        } else if (mCurrentScrollDirection == Direction.RIGHT) {
            // snap to next day
            Log.d("test", "11211");

            leftDays = Math.ceil(leftDays);
        } else {
            // snap to nearest day
            Log.d("test", "112222");

            leftDays = Math.round(leftDays);
        }
        int nearestOrigin = (int) (mCurrentOrigin.x - leftDays * (mWidthPerDay + mColumnGap));

        if (nearestOrigin != 0) {
            // Stop current animation.
            mScroller.forceFinished(true);
            // Snap to date.
            mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, -nearestOrigin, 0, (int) (Math.abs(nearestOrigin) / mWidthPerDay * mScrollDuration));
            ViewCompat.postInvalidateOnAnimation(WeekView.this);
        }
        // Reset scrolling and fling direction.
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to scrolling.
    //
    /////////////////////////////////////////////////////////////////

    public void goToNearestOrigin1() {

        double leftDays = mCurrentOrigin.x / (mWidthPerDay + mColumnGap);
        if (getNumberOfVisibleDays() == 1 && mCurrentScrollDirection == Direction.RIGHT) {
            leftDays = (mCurrentOrigin.x + mHeaderColumnWidth) / (mWidthPerDay + mColumnGap);

        }

        if (mCurrentFlingDirection != Direction.NONE) {
            // snap to nearest day
            leftDays = Math.round(leftDays);
        } else if (mCurrentScrollDirection == Direction.LEFT) {
            // snap to last day
            leftDays = Math.ceil(leftDays);
        } else if (mCurrentScrollDirection == Direction.RIGHT) {
            // snap to next day
            leftDays = Math.floor(leftDays);
        } else {
            // snap to nearest day
            leftDays = Math.round(leftDays);
        }
        int nearestOrigin = (int) (mCurrentOrigin.x - leftDays * (mWidthPerDay + mColumnGap));

        if (nearestOrigin != 0) {
            // Stop current animation.
            mScroller.forceFinished(true);
            // Snap to date.
            mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, -nearestOrigin, 0, (int) (Math.abs(nearestOrigin) / mWidthPerDay * mScrollDuration));
            ViewCompat.postInvalidateOnAnimation(WeekView.this);
        }
        // Reset scrolling and fling direction.
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        if (mScroller.isFinished()) {
//            if (mCurrentFlingDirection != Direction.NONE) {
//                // Snap to day after fling is finished.
//                Log.e("flingcall","flh"+mScroller.getCurrVelocity());
//               goToNearestOrigin();
//            }
//        } else {
//
//            if (mCurrentFlingDirection != Direction.NONE && forceFinishScroll()) {
//                Log.e("flingcall","finish"+mScroller.getStartX()+","+mMinimumFlingVelocity);
//
//               goToNearestOrigin();
//            }
//            else if (mScroller.computeScrollOffset()) {
//                Log.e("flingcall","main"+mScroller.getStartX()+","+mMinimumFlingVelocity);
//                mCurrentOrigin.y = mScroller.getCurrY();
//                mCurrentOrigin.x = mScroller.getCurrX();
//                ViewCompat.postInvalidateOnAnimation(this);
//            }
//        }
    }

    /**
     * Check if scrolling should be stopped.
     *
     * @return true if scrolling should be stopped before reaching the end of animation.
     */
    public static boolean forceFinishScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // current velocity only available since api 14
            return mScroller.getCurrVelocity() <= mMinimumFlingVelocity;
        } else {
            return false;
        }
    }

    public boolean isclosed() {

        MainActivity mainActivity = (MainActivity) mContext;
        return mainActivity.isAppBarExpanded();
    }

    /**
     * Show today on the week view.
     */
    public void goToToday() {
        Calendar today = Calendar.getInstance();
        goToDate(today);
    }

    /**
     * Show a specific day on the week view.
     *
     * @param date The date to show.
     */
    public void goToDate(Calendar date) {

        if (getNumberOfVisibleDays() == 7) {
            int diff = date.get(Calendar.DAY_OF_WEEK) - getFirstDayOfWeek();
            Log.e("diff", diff + "");
            if (diff < 0) {
                date.add(Calendar.DAY_OF_MONTH, -(7 - Math.abs(diff)));
            } else {
                date.add(Calendar.DAY_OF_MONTH, -diff);
            }

        }

        mScroller.forceFinished(true);
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        if (mAreDimensionsInvalid) {
            mScrollToDay = date;
            return;
        }

        mRefreshEvents = true;

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long day = 1000L * 60L * 60L * 24L;
        long dateInMillis = date.getTimeInMillis() + date.getTimeZone().getOffset(date.getTimeInMillis());
        long todayInMillis = today.getTimeInMillis() + today.getTimeZone().getOffset(today.getTimeInMillis());
        long dateDifference = (dateInMillis / day) - (todayInMillis / day);
        mCurrentOrigin.x = -dateDifference * (mWidthPerDay + mColumnGap);
        invalidate();
    }


    /////////////////////////////////////////////////////////////////
    //
    //      Public methods.
    //
    /////////////////////////////////////////////////////////////////

    /**
     * Refreshes the view and loads the events again.
     */
    public void notifyDatasetChanged() {
        mRefreshEvents = true;
        invalidate();
    }

    /**
     * Vertically scroll to a specific hour in the week view.
     *
     * @param hour The hour to scroll to in 24-hour format. Supported values are 0-24.
     */
    public void goToHour(double hour) {
        if (mAreDimensionsInvalid) {
            mScrollToHour = hour;
            return;
        }

        int verticalOffset = 0;
        if (hour > 24)
            verticalOffset = mHourHeight * 24;
        else if (hour > 0)
            verticalOffset = (int) (mHourHeight * hour);

        if (verticalOffset > mHourHeight * 24 - getHeight() + mHeaderHeight + mHeaderRowPadding * 3 + mHeaderMarginBottom)
            verticalOffset = (int) (mHourHeight * 24 - getHeight() + mHeaderHeight + mHeaderRowPadding * 3 + mHeaderMarginBottom);
        mCurrentOrigin.y = -verticalOffset;
        invalidate();
    }



    public enum Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }

    public interface EventClickListener {
        /**
         * Triggered when clicked on one existing event
         *
         * @param event:     event clicked.
         * @param eventRect: view containing the clicked event.
         */
        void onEventClick(WeekViewEvent event, RectF eventRect);
    }

    public interface EventLongPressListener {
        /**
         * @param event:     event clicked.
         * @param eventRect: view containing the clicked event.
         */
        void onEventLongPress(WeekViewEvent event, RectF eventRect);
    }

    public interface EmptyViewClickListener {
        void onEmptyViewClicked(Calendar time);
    }

    public interface EmptyViewLongPressListener {

        void onEmptyViewLongPress(Calendar time);
    }

    public interface ScrollListener {

        void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay);
    }

    public static class EventRect {
        public WeekViewEvent event;
        public WeekViewEvent originalEvent;
        public RectF rectF;
        public float left;
        public float width;
        public float top;
        public float bottom;
        public int noofevent;

        public EventRect(WeekViewEvent event, WeekViewEvent originalEvent, RectF rectF) {
            this.event = event;
            this.rectF = rectF;
            this.originalEvent = originalEvent;
        }
    }

    public static String getTimeString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // Define the desired format, e.g., "03:27 PM"
        return sdf.format(calendar.getTime());
    }

    public void draw_event(long id, String s, String time, String event_excat_time, String description, boolean checked, String date, float startPixel, Canvas canvas, int hour, int minute, String title) {
        float startY = mHeaderHeight + mHeaderRowPadding * 3 + mTimeTextHeight / 2 + mHeaderMarginBottom + mCurrentOrigin.y;
        float startat = startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel;
        float canvasWidth = canvas.getWidth(); // Get the width of the canvas

        float hourHeight = mHourHeight; // Assuming 1 hour equals mHourHeight pixels
        float startPixelTime = (hour + minute / 60.0f) * hourHeight;

        Paint rectanglePaint = new Paint();
        rectanglePaint.setColor(Color.parseColor("#3C5C9C")); // Set the background color
        rectanglePaint.setStyle(Paint.Style.FILL);

        // Parse event_excat_time
        float rectTop, rectBottom;
        try {
            if (event_excat_time.contains("-")) {
                // Time range: e.g., "1:00 PM - 6:00 PM"
                String[] times = event_excat_time.split("-");
                float startHour = parseTimeToHour(times[0].trim());
                float endHour = parseTimeToHour(times[1].trim());

                rectTop = startY + (startHour * hourHeight);
                rectBottom = startY + (endHour * hourHeight);
            } else {
                // Single time: e.g., "01:00 AM"
                float exactHour = parseTimeToHour(event_excat_time.trim());

                rectTop = startY + (exactHour * hourHeight) - 20; // Adjust for single point
                rectBottom = rectTop + 40; // Height of 40dp
            }
        } catch (Exception e) {
            // Fallback in case of parsing error
            rectTop = startY + startPixelTime - 25;
            rectBottom = startY + startPixelTime + 25;
        }

        // Draw rectangle
        canvas.drawRoundRect(startat, rectTop, canvasWidth, rectBottom, 5, 5, rectanglePaint);

        // Add event to list
        Event event_model = new Event();
        event_model.top = rectTop;
        event_model.bottom = rectBottom;
        event_model.width = canvasWidth;
        event_model.left = startat;
        event_model.title = s;
        event_model.date = date;
        event_model.time = time;
        event_model.description = description;
        event_model.checked = checked;
        event_model.id = id;
        event_model.exact_time = event_excat_time;
        events.add(event_model);

        // Draw event text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE); // Set the text color to white
        textPaint.setTextSize(22);
        textPaint.setTextAlign(Paint.Align.LEFT);

        float textX = startat + 20; // Padding from the left
        float textY = rectTop + (rectBottom - rectTop) / 2 + 10; // Vertically center the text
        canvas.drawText(title + ", " + event_excat_time, textX, textY, textPaint);
    }

    // Helper method to parse time strings to fractional hours
    private float parseTimeToHour(String time) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date = format.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return hour + (minute / 60.0f);
    }

    public void draw_event_complete(long id, String s, String time, String event_excat_time, String description, boolean checked, String date, float startPixel, Canvas canvas, int hour, int minute, String title) {
        float startY = mHeaderHeight + mHeaderRowPadding * 3 + mTimeTextHeight / 2 + mHeaderMarginBottom + mCurrentOrigin.y;
        float beforeNow = (hour + minute / 60.0f) * mHourHeight;
        float startat = startPixel < mHeaderColumnWidth ? mHeaderColumnWidth : startPixel;
        float canvasWidth = canvas.getWidth(); // Get the width of the canvas
        float per = 1 * (1.0f - (startat - startPixel) / canvasWidth); // Adjust the width based on the canvas width

        // Set background color
        Paint rectanglePaint = new Paint();
        rectanglePaint.setColor(Color.parseColor("#EDF0F5")); // Set the background color to "#bc8f8f"
        rectanglePaint.setStyle(Paint.Style.FILL);
        float rectLeft = startat - per - 5;
        float rectTop = startY + beforeNow - per - 25; // Adjust height and position more to the top (subtract 20dp)
        float rectRight = canvasWidth; // Set the right edge of the rectangle to the width of the canvas
        float rectBottom = startY + beforeNow + 25; // Adjust rectangle height to approximately 40dp (add 20dp)
        canvas.drawRoundRect(rectLeft, rectTop, rectRight, rectBottom, 1, 1, rectanglePaint);

        // Set text properties
        Paint textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#75706B")); // Set the text color to black (#000000)
        textPaint.setTextSize(22);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setStrikeThruText(true); // Enable strike through

        // Draw text
        float textX = rectLeft + 20; // Set textX to rectLeft plus some padding (e.g., 10dp)
        float textY = startY + beforeNow + mTimeTextHeight / 4; // Adjust the vertical position of the text
        canvas.drawText(title + ", " + event_excat_time, textX, textY, textPaint);
    }

    public static boolean isWithinRectangle(float x, float y) {
        // Your logic to determine if the click is within the rectangle boundaries
        // For example:
        return (x >= rectLeft && x <= rectRight && y >= rectTop && y <= rectBottom);
    }





}