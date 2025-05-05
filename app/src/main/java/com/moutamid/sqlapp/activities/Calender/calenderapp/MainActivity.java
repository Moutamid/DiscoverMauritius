package com.moutamid.sqlapp.activities.Calender.calenderapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fxn.stash.Stash;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.moutamid.calenderapp.MyRecyclerView;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.Event;
import com.moutamid.sqlapp.activities.Calender.calenderapp.database.EventDbHelper;
import com.moutamid.sqlapp.activities.Calender.calenderapp.month.MonthAdapter;
import com.moutamid.sqlapp.activities.Calender.calenderapp.week.DateAdapter;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.AdEventDailogue;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.DateTimeInterpreter;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.MonthLoader;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.WeekView;
import com.moutamid.sqlapp.activities.Calender.calenderapp.weekview.WeekViewEvent;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.Fragment.DocumentFragment;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements MyRecyclerView.AppBarTracking, WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, WeekView.EmptyViewClickListener, WeekView.ScrollListener {
    private static final String TAG = "MainActivity";

    private final int DISTANCE = 1080;
//    private final int DISTANCE = 720;

    public static LocalDate lastdate = LocalDate.now();
    public static int topspace = 0;
    long lasttime;
    int mycolor;
    MyRecyclerView mNestedView;
    View weekviewcontainer;
    WeekView mWeekView;
    public static Activity activity;
    private String daysList[] = {"", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    private ViewPager monthviewpager;
    private static HashMap<LocalDate, EventInfo> alleventlist;
    private HashMap<LocalDate, EventInfo> montheventlist;
    private int mAppBarOffset = 0;
    private boolean mAppBarIdle = true;
    private int mAppBarMaxOffset = 0;
    private AppBarLayout mAppBar;
    private boolean mIsExpanded = false;
    public static TextView current_date;
    private ImageView mArrowImageView;
    private TextView monthname;
    private Toolbar toolbar;
    private int lastchangeindex = -1;
    private boolean isappbarclosed = true;
    private int month;
    private int expandedfirst;
    private View eventview, fullview;
    private GooglecalenderView calendarView;
    private ArrayList<EventModel> eventalllist;
    private boolean isgivepermission;
    private HashMap<LocalDate, Integer> indextrack;
    private HashMap<LocalDate, Integer> dupindextrack;

    public static TextView calender_date;
    private static List<EventInfo> eventList;
    private static RecyclerView recyclerView;
    private static EventAdapter eventAdapter;
    private static EventDbHelper dbHelper;
    public static TextView title;
    private TextView dayTextView, weekTextView, monthTextView;
    private TextView dayPressedTextView, weekPressedTextView, monthPressedTextView;
    ImageView backword_arrow, forward_arrow;

    RelativeLayout day_buttons, week_layout, month_view;
    private RecyclerView recyclerView_week;
    private ImageView previousButton, nextButton;
    private List<String> dates = new ArrayList<>();
    private List<String> search_dates = new ArrayList<>();
    private List<String> search_dates_local_event = new ArrayList<>();
    private List<String> search_dates_local_month = new ArrayList<>();
    private Calendar currentDate = Calendar.getInstance();
    private TextView dateRangeTextView, range;
    String search_date;
    private RecyclerView recyclerView_month;
    private ImageView previousButton_month, nextButton_month;
    private List<String> dates_month = new ArrayList<>();
    private List<String> search_dates_month = new ArrayList<>();
    private Calendar currentDate_month = Calendar.getInstance();
    private TextView dateRangeTextView_month, range_month;
    String search_date_month;
    float v = 0;
    BottomNavigationView bottomNavigationView;
    public static List<Event> events;

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = ResourcesCompat.getFont(this, R.font.googlesansmed);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calender);
        checkApp(MainActivity.this);

        Utils.loginBtnMenuListener(this);
        day_buttons = findViewById(R.id.button_days);
        week_layout = findViewById(R.id.week_layout);
        month_view = findViewById(R.id.month_view);
        current_date = findViewById(R.id.current_date);
        calender_date = findViewById(R.id.calender_date);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new DocumentFragment());
        Stash.put("first_time_right", "yes");
        Stash.put("first_time_left", "yes");
        bottomNavigationView.setSelectedItemId(R.id.calender_menu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.home) {
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                    return true;
                } else if (menuItemId == R.id.doc) {
                    findViewById(R.id.container).setVisibility(View.VISIBLE);
                    replaceFragment(new DocumentFragment());
                    return true;
                } else if (menuItemId == R.id.calender_menu) {
                    findViewById(R.id.container).setVisibility(View.GONE);

//                    startActivity(new Intent(MyDocsActivity.this, MainActivity.class));
                    return true;
                }

                return true;
            }
        });


        range = findViewById(R.id.range);
        dateRangeTextView = findViewById(R.id.dateRangeTextView);
        recyclerView_week = findViewById(R.id.recyclerView_week);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        updateDates(currentDate);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        dbHelper = new EventDbHelper(this);
        backword_arrow = findViewById(R.id.backword_arrow);
        forward_arrow = findViewById(R.id.forward_arrow);
        title = findViewById(R.id.title);
        dayTextView = findViewById(R.id.day);
        weekTextView = findViewById(R.id.week);
        monthTextView = findViewById(R.id.month);
        dayPressedTextView = findViewById(R.id.day_pressed);
        weekPressedTextView = findViewById(R.id.week_pressed);
        monthPressedTextView = findViewById(R.id.month_pressed);
        range_month = findViewById(R.id.range_month);
        dateRangeTextView_month = findViewById(R.id.dateRangeTextView_month);
        recyclerView_month = findViewById(R.id.recyclerView_month);
        previousButton_month = findViewById(R.id.previousButton_month);
        nextButton_month = findViewById(R.id.nextButton_month);
        updateDates_month(currentDate_month);
        MonthAdapter adapter_month = new MonthAdapter(MainActivity.this, dates_month, getDate_month(currentDate_month), search_dates_month, search_dates_local_month);
        recyclerView_month.setAdapter(adapter_month);
        recyclerView_month.setLayoutManager(new GridLayoutManager(this, 7));
        EventDbHelper eventDbHelper = new EventDbHelper(MainActivity.this);
        events = eventDbHelper.getEventsByDate(MainActivity.lastdate + "");

        List<String> checkedTitles = new ArrayList<>();
        RecyclerView recyclerView_local_event = findViewById(R.id.recyclerView_local_event);
        List<Event> checkedEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.isChecked()) {

                recyclerView_local_event.setVisibility(View.VISIBLE);
                checkedTitles.add(event.getTitle());
                checkedEvents.add(event);
            }
        }

        LocalEventAdapter local_adapter = new LocalEventAdapter(checkedEvents, MainActivity.this);
        recyclerView_local_event.setAdapter(local_adapter);
        recyclerView_local_event.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        forward_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float target = mWeekView.weekx - (mWeekView.mWidthPerDay * mWeekView.getNumberOfVisibleDays());
                Log.d("move", "left: " + mWeekView.weekx + " -> " + target);

                // Prevent animation if the target is the same as the current position
                if (mWeekView.mCurrentOrigin.x == target) {
                    Log.d("move", "Already at target position, no animation needed.");
                    return;
                }

                ValueAnimator va = ValueAnimator.ofFloat(mWeekView.mCurrentOrigin.x, target);
                va.setDuration(10);
                va.addUpdateListener(animation -> {
                    mWeekView.mCurrentOrigin.x = (float) animation.getAnimatedValue();
                    mWeekView.invalidate();
                });

                va.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mWeekView.weekx = mWeekView.mCurrentOrigin.x; // Update weekx after the animation
                        Log.d("move", "Animation ended. New weekx: " + mWeekView.weekx);
                    }
                });

                va.start();
            }
        });
        backword_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calculate the target position for moving forward
                float target = mWeekView.weekx + (mWeekView.mWidthPerDay * mWeekView.getNumberOfVisibleDays());
                Log.d("move", "right: " + mWeekView.weekx + " -> " + target);

                // Prevent animation if the target is the same as the current position
                if (mWeekView.mCurrentOrigin.x == target) {
                    Log.d("move", "Already at target position, no animation needed.");
                    return;
                }

                // Create and start the animation
                ValueAnimator va = ValueAnimator.ofFloat(mWeekView.mCurrentOrigin.x, target);
                va.setDuration(10);
                va.addUpdateListener(animation -> {
                    mWeekView.mCurrentOrigin.x = (float) animation.getAnimatedValue();
                    mWeekView.invalidate(); // Redraw the view
                });

                va.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mWeekView.weekx = mWeekView.mCurrentOrigin.x; // Update weekx after animation
                        Log.d("move", "Animation ended. New weekx: " + mWeekView.weekx);
                    }
                });

                va.start();
            }
        });
        previousButton_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousMonth();
                adapter_month.notifyDataSetChanged(); // Notify adapter after updating dates
            }
        });
        nextButton_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextMonth();
                adapter_month.notifyDataSetChanged();
            }
        });
        recyclerView_month.setOnTouchListener(new View.OnTouchListener() {
            private float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        if (startX - endX > 100) {
                            nextButton_month.performClick();
                        } else if (endX - startX > 100) {
                            previousButton_month.performClick();
                        }
                        return true;
                }
                return false;
            }
        });

        DateAdapter adapter = new DateAdapter(this, dates, getDate(currentDate), search_dates, search_dates_local_event);
        recyclerView_week.setAdapter(adapter);
        recyclerView_week.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_week.setOnTouchListener(new View.OnTouchListener() {
            private float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        if (startX - endX > 100) {
                            nextButton.performClick();
                        } else if (endX - startX > 100) {
                            previousButton.performClick();
                        }
                        return true;
                }
                return false;
            }
        });

        recyclerView_month.setOnTouchListener(new View.OnTouchListener() {
            private float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        if (startX - endX > 100) {
                            nextButton_month.performClick();
                        } else if (endX - startX > 100) {
                            previousButton_month.performClick();
                        }
                        return true;
                }
                return false;
            }
        });

        previousButton.setOnClickListener(v -> {
            currentDate.add(Calendar.DATE, -7);
            updateDates(currentDate);
            adapter.setCurrentDate(getDate(currentDate));
            adapter.notifyDataSetChanged();
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.DATE, 7);
                updateDates(currentDate);
                adapter.setCurrentDate(getDate(currentDate));
                adapter.notifyDataSetChanged();
            }
        });


        weekviewcontainer = findViewById(R.id.weekViewcontainer);
        dayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_buttons.setVisibility(View.VISIBLE);
                month_view.setVisibility(View.GONE);
                week_layout.setVisibility(View.GONE);
                dayPressedTextView.setVisibility(View.VISIBLE);
                dayTextView.setVisibility(View.INVISIBLE);
                weekPressedTextView.setVisibility(View.INVISIBLE);
                weekTextView.setVisibility(View.VISIBLE);
                monthPressedTextView.setVisibility(View.INVISIBLE);
                monthTextView.setVisibility(View.VISIBLE);
            }
        });
        weekTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_buttons.setVisibility(View.INVISIBLE);
                month_view.setVisibility(View.INVISIBLE);
                dayPressedTextView.setVisibility(View.INVISIBLE);
                week_layout.setVisibility(View.VISIBLE);
                dayTextView.setVisibility(View.VISIBLE);
                weekPressedTextView.setVisibility(View.VISIBLE);
                weekTextView.setVisibility(View.INVISIBLE);
                monthPressedTextView.setVisibility(View.INVISIBLE);
                monthTextView.setVisibility(View.VISIBLE);
            }
        });
        monthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_buttons.setVisibility(View.INVISIBLE);
                month_view.setVisibility(View.VISIBLE);
                dayPressedTextView.setVisibility(View.INVISIBLE);
                week_layout.setVisibility(View.INVISIBLE);
                dayTextView.setVisibility(View.VISIBLE);
                weekPressedTextView.setVisibility(View.INVISIBLE);
                weekTextView.setVisibility(View.VISIBLE);
                monthPressedTextView.setVisibility(View.VISIBLE);
                monthTextView.setVisibility(View.INVISIBLE);
            }
        });

        eventalllist = new ArrayList<>();
        indextrack = new HashMap<>();
        dupindextrack = new HashMap<>();
        mAppBar = findViewById(R.id.app_bar);
        calendarView = findViewById(R.id.calander);
        calendarView.setPadding(0, getStatusBarHeight(), 0, 0);
        mNestedView = findViewById(R.id.nestedView);
        monthviewpager = findViewById(R.id.monthviewpager);
        monthviewpager.setOffscreenPageLimit(1);
        monthviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if (monthviewpager.getVisibility() == View.GONE) return;
                if (isAppBarClosed()) {
                    Log.e("selected", i + "");
                    LocalDate localDate = new LocalDate();
                    MonthPageAdapter monthPageAdapter = (MonthPageAdapter) monthviewpager.getAdapter();
                    MonthModel monthModel = monthPageAdapter.getMonthModels().get(i);
                    String year = monthModel.getYear() == localDate.getYear() ? "" : monthModel.getYear() + "";
                    monthname.setText(monthModel.getMonthnamestr() + " " + year);
                    MainActivity.lastdate = new LocalDate(monthModel.getYear(), monthModel.getMonth(), 1);
                    Log.d("dat", MainActivity.lastdate + "   d1ate");
                    // EventBus.getDefault().post(new MessageEvent(new LocalDate(monthModel.getYear(),monthModel.getMonth(),1)));
                    // if (monthChangeListner!=null)monthChangeListner.onmonthChange(myPagerAdapter.monthModels.get(position));
                } else {
                    // calendarView.setCurrentmonth(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mNestedView.setAppBarTracking(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNestedView.setLayoutManager(linearLayoutManager);
//        DateAdapter dateAdapter = new DateAdapter();
//        mNestedView.setAdapter(dateAdapter);

//        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(dateAdapter);
//        mNestedView.addItemDecoration(headersDecor);
//        EventBus.getDefault().register(this);


        monthname = findViewById(R.id.monthname);
        calendarView.setMonthChangeListner(new MonthChangeListner() {
            @Override
            public void onmonthChange(MonthModel monthModel) {
                /**
                 * call when Googlecalendarview is open  scroll viewpager available inside GoogleCalendar
                 */
                LocalDate localDate = new LocalDate();
                String year = monthModel.getYear() == localDate.getYear() ? "" : monthModel.getYear() + "";
                monthname.setText(monthModel.getMonthnamestr() + " " + year);
                if (weekviewcontainer.getVisibility() == View.VISIBLE) {
                    Calendar todaydate = Calendar.getInstance();
                    todaydate.set(Calendar.DAY_OF_MONTH, 1);
                    todaydate.set(Calendar.MONTH, monthModel.getMonth() - 1);
                    todaydate.set(Calendar.YEAR, monthModel.getYear());
                    mWeekView.goToDate(todaydate);

                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, 200);
            }
        } else {
            isgivepermission = true;
            LocalDate mintime = new LocalDate().minusYears(5);
            LocalDate maxtime = new LocalDate().plusYears(5);
            alleventlist = Utility.readCalendarEvent(this, mintime, maxtime);
            eventList = new ArrayList<>();
            // Populate event list from alleventlist
            HashMap<LocalDate, EventInfo> alleventlist = Utility.readCalendarEvent(this, mintime, maxtime);
            for (Map.Entry<LocalDate, EventInfo> entry : alleventlist.entrySet()) {
                EventInfo eventInfo = entry.getValue();
                Instant instant = Instant.ofEpochMilli(eventInfo.starttime);
                java.time.LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

                Log.d("dataaaa", date.toString() + "  " + MainActivity.lastdate);
                if (date.toString().equals(MainActivity.lastdate)) {
                    Log.d("dataaaa", eventInfo.title + "  " + MainActivity.lastdate);
                    eventList.add(eventInfo);
                }
            }


            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            eventAdapter = new EventAdapter(MainActivity.this, eventList);
            recyclerView.setAdapter(eventAdapter);

            montheventlist = new HashMap<>();

            for (LocalDate localDate : alleventlist.keySet()) {
                EventInfo eventInfo = alleventlist.get(localDate);
                while (eventInfo != null) {
                    if (eventInfo.noofdayevent > 1) {

                        LocalDate nextmonth = localDate.plusMonths(1).withDayOfMonth(1);
                        LocalDate enddate = new LocalDate(eventInfo.endtime);
                        while (enddate.isAfter(nextmonth)) {
                            if (montheventlist.containsKey(nextmonth)) {
                                int firstday = nextmonth.dayOfMonth().withMinimumValue().dayOfWeek().get();
                                if (firstday == 7) firstday = 0;
                                int noofdays = Days.daysBetween(nextmonth, enddate).getDays() + firstday;
                                EventInfo newobj = new EventInfo();
                                newobj.title = eventInfo.title;
                                newobj.timezone = eventInfo.timezone;
                                newobj.isallday = eventInfo.isallday;
                                newobj.eventcolor = eventInfo.eventcolor;
                                newobj.endtime = eventInfo.endtime;
                                newobj.accountname = eventInfo.accountname;
                                newobj.isalreadyset = true;
                                newobj.starttime = eventInfo.starttime;
                                newobj.noofdayevent = noofdays;
                                newobj.id = eventInfo.id;
                                EventInfo beginnode = montheventlist.get(nextmonth);
                                newobj.nextnode = beginnode;
                                montheventlist.put(nextmonth, newobj);

                            } else {
                                int firstday = nextmonth.dayOfMonth().withMinimumValue().dayOfWeek().get();
                                if (firstday == 7) firstday = 0;
                                int noofdays = Days.daysBetween(nextmonth, enddate).getDays() + firstday;
                                EventInfo newobj = new EventInfo();
                                newobj.title = eventInfo.title;
                                newobj.timezone = eventInfo.timezone;
                                newobj.accountname = eventInfo.accountname;
                                newobj.isallday = eventInfo.isallday;
                                newobj.eventcolor = eventInfo.eventcolor;
                                newobj.endtime = eventInfo.endtime;
                                newobj.isalreadyset = true;
                                newobj.starttime = eventInfo.starttime;
                                newobj.noofdayevent = noofdays;
                                newobj.id = eventInfo.id;
                                montheventlist.put(nextmonth, newobj);

                            }
                            Log.e("nextmonth", nextmonth.toString());
                            Log.e("jdata" + localDate.toString() + "," + eventInfo.noofdayevent, eventInfo.title + "," + new LocalDate(eventInfo.starttime) + "," + new LocalDate(eventInfo.endtime));
                            nextmonth = nextmonth.plusMonths(1).withDayOfMonth(1);
                        }


                    }
                    eventInfo = eventInfo.nextnode;
                }

            }
            calendarView.init(alleventlist, mintime, maxtime);
            calendarView.setCurrentmonth(new LocalDate());
            calendarView.adjustheight();
            mIsExpanded = false;
            mAppBar.setExpanded(false, false);

        }
        toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
//        expandCollapse = findViewById(R.id.expandCollapseButton);
        mArrowImageView = findViewById(R.id.arrowImageView);
        if (monthviewpager.getVisibility() == View.VISIBLE) {
//            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
//            ((MyAppBarBehavior) layoutParams.getBehavior()).setScrollBehavior(false);
//            mAppBar.setElevation(0);
//            mArrowImageView.setVisibility(View.INVISIBLE);
        } else {
//            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
//            ((MyAppBarBehavior) layoutParams.getBehavior()).setScrollBehavior(true);
//            mAppBar.setElevation(20);
//            mArrowImageView.setVisibility(View.VISIBLE);
        }


        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        }


        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("dssddssddat", "   date3");
                DayOfWeek dayOfWeek = DayOfWeek.of(MainActivity.lastdate.getDayOfWeek());
                int dayOfMonth = MainActivity.lastdate.getDayOfMonth();
                if (mAppBarOffset != verticalOffset) {
                    mAppBarOffset = verticalOffset;
                    mAppBarMaxOffset = -mAppBar.getTotalScrollRange();
                    int totalScrollRange = appBarLayout.getTotalScrollRange();
                    float progress = (float) (-verticalOffset) / (float) totalScrollRange;
                    if ((monthviewpager.getVisibility() == View.GONE) && mNestedView.getVisibility() == View.VISIBLE)
                        mAppBar.setElevation(20 + (20 * Math.abs(1 - progress)));
                    if (weekviewcontainer.getVisibility() == View.VISIBLE) {
                        mAppBar.setElevation(20 - (20 * Math.abs(progress)));
                    }
                    mArrowImageView.setRotation(progress * 180);
                    mIsExpanded = verticalOffset == 0;
                    mAppBarIdle = mAppBarOffset >= 0 || mAppBarOffset <= mAppBarMaxOffset;
                    float alpha = (float) -verticalOffset / totalScrollRange;


                    if (mAppBarOffset == -appBarLayout.getTotalScrollRange()) {
                        isappbarclosed = true;
                        setExpandAndCollapseEnabled(false);
                    } else {
                        setExpandAndCollapseEnabled(true);
                    }

                    if (mAppBarOffset == 0) {
                        expandedfirst = linearLayoutManager.findFirstVisibleItemPosition();
                        if (mNestedView.getVisibility() == View.VISIBLE) {
                            topspace = linearLayoutManager.findViewByPosition(linearLayoutManager.findFirstVisibleItemPosition()).getTop();//uncomment jigs 28 feb
                        }
                        if (isappbarclosed) {
                            isappbarclosed = false;
                            mNestedView.stopScroll();
                            calendarView.setCurrentmonth(lastdate);
                        }
                    }

                }


            }
        });

        findViewById(R.id.backsupport).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (monthviewpager.getVisibility() == View.VISIBLE)
                            return;
                        mIsExpanded = !mIsExpanded;
                        mNestedView.stopScroll();

                        mAppBar.setExpanded(mIsExpanded, true);


                    }
                });
        mWeekView.setfont(ResourcesCompat.getFont(this, R.font.googlesans_regular), 0);
        mWeekView.setfont(ResourcesCompat.getFont(this, R.font.googlesansmed), 1);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        mWeekView.setScrollListener(this);
        setupDateTimeInterpreter(false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LocalDate mintime = new LocalDate().minusYears(5);
            LocalDate maxtime = new LocalDate().plusYears(5);
            alleventlist = Utility.readCalendarEvent(this, mintime, maxtime);
            montheventlist = new HashMap<>();

            for (LocalDate localDate : alleventlist.keySet()) {
                EventInfo eventInfo = alleventlist.get(localDate);
                while (eventInfo != null) {
                    if (eventInfo.noofdayevent > 1) {

                        LocalDate nextmonth = localDate.plusMonths(1).withDayOfMonth(1);
                        LocalDate enddate = new LocalDate(eventInfo.endtime);
                        while (enddate.isAfter(nextmonth)) {
                            if (montheventlist.containsKey(nextmonth)) {
                                int firstday = nextmonth.dayOfMonth().withMinimumValue().dayOfWeek().get();
                                if (firstday == 7) firstday = 0;
                                int noofdays = Days.daysBetween(nextmonth, enddate).getDays() + firstday;
                                EventInfo newobj = new EventInfo();
                                newobj.title = eventInfo.title;
                                newobj.timezone = eventInfo.timezone;
                                newobj.isallday = eventInfo.isallday;
                                newobj.eventcolor = eventInfo.eventcolor;
                                newobj.endtime = eventInfo.endtime;
                                newobj.isalreadyset = true;
                                newobj.starttime = eventInfo.starttime;
                                newobj.noofdayevent = noofdays;
                                newobj.id = eventInfo.id;
                                EventInfo beginnode = montheventlist.get(nextmonth);
                                newobj.nextnode = beginnode;
                                montheventlist.put(nextmonth, newobj);

                            } else {
                                int firstday = nextmonth.dayOfMonth().withMinimumValue().dayOfWeek().get();
                                if (firstday == 7) firstday = 0;
                                int noofdays = Days.daysBetween(nextmonth, enddate).getDays() + firstday;
                                EventInfo newobj = new EventInfo();
                                newobj.title = eventInfo.title;
                                newobj.timezone = eventInfo.timezone;
                                newobj.isallday = eventInfo.isallday;
                                newobj.eventcolor = eventInfo.eventcolor;
                                newobj.endtime = eventInfo.endtime;
                                newobj.isalreadyset = true;
                                newobj.starttime = eventInfo.starttime;
                                newobj.noofdayevent = noofdays;
                                newobj.id = eventInfo.id;
                                montheventlist.put(nextmonth, newobj);

                            }
                            Log.e("nextmonth", nextmonth.toString());
                            Log.e("jdata" + localDate.toString() + "," + eventInfo.noofdayevent, eventInfo.title + "," + new LocalDate(eventInfo.starttime) + "," + new LocalDate(eventInfo.endtime));
                            nextmonth = nextmonth.plusMonths(1).withDayOfMonth(1);
                        }


                    }
                    eventInfo = eventInfo.nextnode;
                }

            }
            calendarView.init(alleventlist, mintime, maxtime);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isgivepermission = true;
                    lastdate = new LocalDate();
                    calendarView.setCurrentmonth(new LocalDate());
                    calendarView.adjustheight();
                    mIsExpanded = false;
                    mAppBar.setExpanded(false, false);
                    mWeekView.notifyDatasetChanged();
//                    LinearLayoutManager linearLayoutManager= (LinearLayoutManager) mNestedView.getLayoutManager();
//                    if (indextrack.containsKey(new LocalDate())){
//                        smoothScroller.setTargetPosition(indextrack.get(new LocalDate()));
//                        linearLayoutManager.scrollToPositionWithOffset(indextrack.get(new LocalDate()),0);
//                    }
//                    else {
//                        for (int i=0;i<eventalllist.size();i++){
//                            if (eventalllist.get(i).getLocalDate().getMonthOfYear()==new LocalDate().getMonthOfYear()&&eventalllist.get(i).getLocalDate().getYear()==new LocalDate().getYear()){
//                                linearLayoutManager.scrollToPositionWithOffset(i,0);
//                                break;
//                            }
//                        }
//                    }
                }
            }, 10);
        }
    }

    /**
     * this call when user is scrolling on mNestedView(recyclerview) and month will change
     * or when toolbar top side current date button selected
     */
    @Subscribe
    public void onEvent(MonthChange event) {


        if (!isAppBarExpanded()) {

            LocalDate localDate = new LocalDate();
            String year = event.getMessage().getYear() == localDate.getYear() ? "" : event.getMessage().getYear() + "";
            monthname.setText(event.getMessage().toString("MMMM") + " " + year);


            long diff = System.currentTimeMillis() - lasttime;
            boolean check = diff > 600;
            if (check && event.mdy > 0) {
                monthname.setTranslationY(35);
                mArrowImageView.setTranslationY(35);
                lasttime = System.currentTimeMillis();
                monthname.animate().translationY(0).setDuration(200).start();
                mArrowImageView.animate().translationY(0).setDuration(200).start();

            } else if (check && event.mdy < 0) {

                monthname.setTranslationY(-35);
                mArrowImageView.setTranslationY(-35);
                lasttime = System.currentTimeMillis();
                monthname.animate().translationY(0).setDuration(200).start();
                mArrowImageView.animate().translationY(0).setDuration(200).start();
            }


        }

    }


    private int getDeviceHeight() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int height1 = size.y;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height1;
    }

    private int getDevicewidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return width;
    }


    /**
     * call only one time after googlecalendarview init() method is done
     */

    private void setExpandAndCollapseEnabled(boolean enabled) {

        if (mNestedView.isNestedScrollingEnabled() != enabled) {
            ViewCompat.setNestedScrollingEnabled(mNestedView, enabled);
        }

    }

    @Override
    public boolean isAppBarClosed() {
        return isappbarclosed;
    }

    @Override
    public int appbaroffset() {
        return expandedfirst;
    }

    public void selectdateFromMonthPager(int year, int month, int day) {
        MainActivity.lastdate = new LocalDate(year, month, day);
        LocalDate localDate = new LocalDate();
        String yearstr = MainActivity.lastdate.getYear() == localDate.getYear() ? "" : MainActivity.lastdate.getYear() + "";
        monthname.setText(MainActivity.lastdate.toString("MMMM") + " " + yearstr);
        calendarView.setCurrentmonth(MainActivity.lastdate);
        calendarView.adjustheight();
        mIsExpanded = false;
        mAppBar.setExpanded(false, false);
        EventBus.getDefault().post(new MessageEvent(new LocalDate(year, month, day)));
        monthviewpager.setVisibility(View.GONE);
        mNestedView.setVisibility(View.VISIBLE);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBar.getLayoutParams();
        ((MyAppBarBehavior) layoutParams.getBehavior()).setScrollBehavior(true);
        mAppBar.setElevation(20);
        mArrowImageView.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean isAppBarExpanded() {

        return mAppBarOffset == 0;
    }

    @Override
    public boolean isAppBarIdle() {
        return mAppBarIdle;
    }

    ///////////////////////////////////weekview implemention///////////////////////////////////////
    /* Function to reverse the linked list */
    EventInfo reverse(EventInfo node) {
        EventInfo prev = null;
        EventInfo current = node;
        EventInfo next = null;
        while (current != null) {
            next = current.nextnode;
            current.nextnode = prev;
            prev = current;
            current = next;
        }
        node = prev;
        return node;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {


        if (!isgivepermission) return new ArrayList<>();
        HashMap<LocalDate, EventInfo> jmontheventlist = new HashMap<>(montheventlist);
        LocalDate initial = new LocalDate(newYear, newMonth, 1);
        int length = initial.dayOfMonth().getMaximumValue();
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        for (int i = 1; i <= length; i++) {
            LocalDate localDate = new LocalDate(newYear, newMonth, i);
            if (alleventlist.containsKey(localDate) || jmontheventlist.containsKey(localDate)) {
                EventInfo eventInfo = null;

                if (alleventlist.containsKey(localDate)) {
                    eventInfo = alleventlist.get(localDate);
                }
                if (i == 1) {

                    if (jmontheventlist.containsKey(localDate)) {
                        HashMap<String, String> containevent = new HashMap<>();
                        EventInfo movecheck = jmontheventlist.get(localDate);
                        EventInfo newobj = new EventInfo(movecheck);
                        eventInfo = newobj;
                        containevent.put(movecheck.id + "", "1");
                        while (movecheck.nextnode != null) {
                            movecheck = movecheck.nextnode;
                            newobj.nextnode = new EventInfo(movecheck);
                            newobj = newobj.nextnode;
                            containevent.put(movecheck.id + "", "1");
                        }
                        List<EventInfo> infolist = new ArrayList<>();
                        EventInfo originalevent = alleventlist.get(localDate);
                        while (originalevent != null) {
                            if (!containevent.containsKey(originalevent.id + "")) {
                                infolist.add(originalevent);
                            }
                            originalevent = originalevent.nextnode;
                        }
                        for (EventInfo eventInfo1 : infolist) {
                            newobj.nextnode = new EventInfo(eventInfo1);
                            newobj = newobj.nextnode;

                        }
                        //  eventInfo=reverse(eventInfo);
                        Log.e("jeventinfo", eventInfo.title + "" + localDate);


                    }
                }

                while (eventInfo != null) {
                    Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone(eventInfo.timezone));
                    if (eventInfo.isalreadyset) {
                        startTime.setTimeInMillis(localDate.toDateTimeAtStartOfDay(DateTimeZone.forTimeZone(startTime.getTimeZone())).getMillis());
                    } else {
                        startTime.setTimeInMillis(eventInfo.starttime);

                    }
                    Calendar endTime = (Calendar) Calendar.getInstance(TimeZone.getTimeZone(eventInfo.timezone));
                    endTime.setTimeInMillis(eventInfo.endtime);
                    LocalDate enddate = new LocalDate(endTime);
                    LocalDate maxdate = new LocalDate(newYear, newMonth, length);

                    if (enddate.isAfter(maxdate)) {
                        LocalDateTime localDateTime = new LocalDateTime(newYear, newMonth, length, 23, 59, 59);

                        int f = eventInfo.isallday ? 0 : 1000;

                        endTime.setTimeInMillis(localDateTime.toDateTime().getMillis() + 1000);

                    }

                    Log.e("title:" + eventInfo.title, new LocalDate(eventInfo.starttime).toString());
                    int dau = Days.daysBetween(new LocalDate(eventInfo.endtime), new LocalDate(eventInfo.starttime)).getDays();

                    WeekViewEvent event = new WeekViewEvent(eventInfo.id, eventInfo.title, startTime, endTime, eventInfo.accountname);
                    event.setMyday(eventInfo.noofdayevent);


                    event.setAllDay(eventInfo.isallday);
                    event.setColor(eventInfo.eventcolor);
//                    if (eventInfo.isallday)event.setColor(getResources().getColor(R.color.event_color_04));
//                    else event.setColor(getResources().getColor(R.color.event_color_02));
                    events.add(event);
                    eventInfo = eventInfo.nextnode;
                }
            }
        }


        return events;
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretday(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (mWeekView.getNumberOfVisibleDays() == 7)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase();
            }

            @Override
            public String interpretDate(Calendar date) {
                int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);


                return dayOfMonth + "";
            }

            @Override
            public String interpretTime(int hour) {
                if (hour == 0 || hour == 12) {
                    return "12 " + (hour < 12 ? "AM" : "PM");
                } else {
                    return (hour % 12) + " " + (hour < 12 ? "AM" : "PM");
                }
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        int hour = time.get(Calendar.HOUR_OF_DAY); // Example: hour value
        int minute = time.get(Calendar.MINUTE); // Example: minute value
        int month = time.get(Calendar.MONTH); // Example: month value (0-based index)
        int dayOfMonth = time.get(Calendar.DAY_OF_MONTH); // Example: day of month value
        String formattedString = formatTime(hour, minute, month, dayOfMonth);
        return formattedString;
    }

    protected String getEventTime(Calendar time) {
        int hour = time.get(Calendar.HOUR_OF_DAY); // Example: hour value
        int minute = time.get(Calendar.MINUTE); // Example: minute value
        String formattedString = hour + "-" + minute;
        return formattedString;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        if (isAppBarExpanded()) {
            mIsExpanded = !mIsExpanded;
            mNestedView.stopScroll();

            mAppBar.setExpanded(mIsExpanded, true);
            return;
        }
        if (event.isAllDay() == false) {
            LocalDateTime start = new LocalDateTime(event.getStartTime().getTimeInMillis(), DateTimeZone.forTimeZone(event.getStartTime().getTimeZone()));
            LocalDateTime end = new LocalDateTime(event.getEndTime().getTimeInMillis(), DateTimeZone.forTimeZone(event.getEndTime().getTimeZone()));

            String sf = start.toString("a").equals(end.toString("a")) ? "" : "a";
            String rangetext = daysList[start.getDayOfWeek()] + ", " + start.toString("d MMM") + " · " + start.toString("h:mm " + sf + "") + " - " + end.toString("h:mm a");
        } else if (event.isIsmoreday()) {
            LocalDate localDate = new LocalDate(event.getActualstart().getTimeInMillis(), DateTimeZone.forTimeZone(event.getStartTime().getTimeZone()));
            LocalDate todaydate = LocalDate.now();
            LocalDate nextday = localDate.plusDays((int) (event.getNoofday() - 1));
            if (localDate.getYear() == todaydate.getYear()) {
                String rangetext = daysList[localDate.getDayOfWeek()] + ", " + localDate.toString("d MMM") + " - " + daysList[nextday.getDayOfWeek()] + ", " + nextday.toString("d MMM");
            } else {
                String rangetext = daysList[localDate.getDayOfWeek()] + ", " + localDate.toString("d MMM, YYYY") + " - " + daysList[nextday.getDayOfWeek()] + ", " + nextday.toString("d MMM, YYYY");
            }
        } else {
            LocalDate localDate = new LocalDate(event.getStartTime().getTimeInMillis());
            LocalDate todaydate = LocalDate.now();
            if (localDate.getYear() == todaydate.getYear()) {
                String rangetext = daysList[localDate.getDayOfWeek()] + ", " + localDate.toString("d MMM");
            } else {
                String rangetext = daysList[localDate.getDayOfWeek()] + ", " + localDate.toString("d MMM, YYYY");
            }
        }


        final View view = new View(this);
        ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams((int) eventRect.width(), (int) eventRect.height());
        view.setLeft((int) eventRect.left);
        view.setTop((int) eventRect.top);
        view.setRight((int) eventRect.right);
        view.setBottom((int) eventRect.bottom);
        view.setLayoutParams(layoutParams1);


        if (event.getColor() != 0) {
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.fourdp));
            mycolor = event.getColor();
            shape.setColor(mycolor);

        } else {
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.fourdp));
            mycolor = Color.parseColor("#009688");
            shape.setColor(mycolor);


        }


    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        String eventTitle = getEventTitle(time);
        String eventTime = getEventTime(time);
        Log.d("gfgfgfghh", eventTitle);

        //        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
        AdEventDailogue adEventDailogue = new AdEventDailogue(MainActivity.this, eventTitle, eventTime);
        adEventDailogue.show();
    }

    @Override
    public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {


        if (weekviewcontainer.getVisibility() == View.GONE || !isgivepermission) return;
        if (isAppBarClosed()) {

            LocalDate localDate = new LocalDate(newFirstVisibleDay.get(Calendar.YEAR), newFirstVisibleDay.get(Calendar.MONTH) + 1, newFirstVisibleDay.get(Calendar.DAY_OF_MONTH));
            MainActivity.lastdate = localDate;

            String year = localDate.getYear() == LocalDate.now().getYear() ? "" : localDate.getYear() + "";
            if (!monthname.getText().equals(localDate.toString("MMM") + " " + year)) {
                MainActivity.lastdate = localDate;
                calendarView.setCurrentmonth(localDate);
                calendarView.adjustheight();
                mIsExpanded = false;
                mAppBar.setExpanded(false, false);
                monthname.setText(localDate.toString("MMM") + " " + year);

            }

            // EventBus.getDefault().post(new MessageEvent(new LocalDate(monthModel.getYear(),monthModel.getMonth(),1)));
            // if (monthChangeListner!=null)monthChangeListner.onmonthChange(myPagerAdapter.monthModels.get(position));
        } else {
            // calendarView.setCurrentmonth(i);
        }
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();

    }

    public void BackPress(View view) {
        startActivity(new Intent(this, OrganizerActivity.class));
    }

    class MonthPageAdapter extends FragmentStatePagerAdapter {
        private ArrayList<MonthModel> monthModels;
        private int singleitemheight;

        // private ArrayList<MonthFragment> firstFragments=new ArrayList<>();

        public MonthPageAdapter(FragmentManager fragmentManager, ArrayList<MonthModel> monthModels, int singleitemheight) {

            super(fragmentManager);
            this.monthModels = monthModels;
            this.singleitemheight = singleitemheight;

//            for (int position=0;position<monthModels.size();position++){
//                firstFragments.add(MonthFragment.newInstance(monthModels.get(position).getMonth(), monthModels.get(position).getYear(), monthModels.get(position).getFirstday(), monthModels.get(position).getDayModelArrayList(), alleventlist, singleitemheight));
//            }
        }

//        public ArrayList<MonthFragment> getFirstFragments() {
//            return firstFragments;
//        }

        public ArrayList<MonthModel> getMonthModels() {
            return monthModels;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return monthModels.size();
        }


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            try {
                return MonthFragment.newInstance(monthModels.get(position).getMonth(), monthModels.get(position).getYear(), monthModels.get(position).getFirstday(), monthModels.get(position).getDayModelArrayList(), alleventlist, singleitemheight, montheventlist);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;

            }
        }


    }


    private static class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        private Context context;
        private List<EventInfo> eventList;

        public EventAdapter(Context context, List<EventInfo> eventList) {
            this.context = context;
            this.eventList = eventList;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            EventInfo event = eventList.get(position);
            Log.d("dataaaa", event.nextnode + "   " + event.endtime + event.eventtitles + event.starttime + event.accountname + "  " + event.timezone + "  " + event.id + " ");
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        public static class EventViewHolder extends RecyclerView.ViewHolder {
            TextView textViewTitle;

            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.event_name);
            }

            public void bind(EventInfo event) {
                textViewTitle.setText(event.title);
            }
        }
    }

    public static void fun(Context context, String cur_date) {
        LocalDate mintime = new LocalDate().minusYears(5);
        LocalDate maxtime = new LocalDate().plusYears(5);
        alleventlist = Utility.readCalendarEvent(context, mintime, maxtime);
        eventList = new ArrayList<>();
        HashMap<LocalDate, EventInfo> alleventlist = Utility.readCalendarEvent(context, mintime, maxtime);
        for (Map.Entry<LocalDate, EventInfo> entry : alleventlist.entrySet()) {
            EventInfo eventInfo = entry.getValue();
            Instant instant = Instant.ofEpochMilli(eventInfo.starttime);
            java.time.LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            if (date.toString().equals(cur_date)) {
                Log.d("dataaaa", eventInfo.title + "  " + MainActivity.lastdate);
                eventList.add(eventInfo);
            }
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        eventAdapter = new EventAdapter(context, eventList);
        recyclerView.setAdapter(eventAdapter);
    }

    public static void checkApp(Activity activity) {
        String appName = "My Trips2";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private String formatTime(int hour, int minute, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy   hh:mm a", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static List<EventInfo> func_week(Context context, String cur_date) {
        Log.d("week_dataaaa", cur_date);
        LocalDate mintime = new LocalDate().minusYears(5);
        LocalDate maxtime = new LocalDate().plusYears(5);
        alleventlist = Utility.readCalendarEvent(context, mintime, maxtime);
        eventList = new ArrayList<>();
        HashMap<LocalDate, EventInfo> alleventlist = Utility.readCalendarEvent(context, mintime, maxtime);
        for (Map.Entry<LocalDate, EventInfo> entry : alleventlist.entrySet()) {
            EventInfo eventInfo = entry.getValue();
            Instant instant = Instant.ofEpochMilli(eventInfo.starttime);
            java.time.LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            Log.d("week_data", date.toString() + "  " + cur_date);

            if (date.toString().equals(cur_date)) {
                eventList.add(eventInfo);
            }
        }
        return eventList;

    }

    private String getDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private String getSearch_date(Calendar calendar) {
        SimpleDateFormat search_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return search_date.format(calendar.getTime());
    }

    private void updateDates(Calendar currentDate) {
        dates.clear();
        search_dates.clear();
        search_dates_local_event.clear();
        SimpleDateFormat search_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat search_date_local_event = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd", Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd", Locale.getDefault());
        Calendar startDate = (Calendar) currentDate.clone();

        // Set startDate to the current date
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 7; i++) {
            dates.add(sdf.format(startDate.getTime()));
            search_dates.add(search_date.format(startDate.getTime()));
            search_dates_local_event.add(search_date_local_event.format(startDate.getTime()));
            startDate.add(Calendar.DATE, 1);
        }

        String startDateString = sdf1.format(currentDate.getTime());
        currentDate.add(Calendar.DATE, 6);
        String endDateString = sdf1.format(currentDate.getTime());
        currentDate.add(Calendar.DATE, -6); // Reset current date
        range.setText(getMonthName(currentDate.get(Calendar.MONTH)) + " " + startDateString + "-" + endDateString);
        dateRangeTextView.setText(getMonthName(currentDate.get(Calendar.MONTH)) + " " + startDateString + "-" + endDateString);
    }

    private String getMonthName(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    private String getDate_month(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private String getSearch_date_month(Calendar calendar) {
        SimpleDateFormat search_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return search_date.format(calendar.getTime());
    }

    private void updateDates_month(Calendar currentDate) {
        dates_month.clear();
        search_dates_month.clear();
        search_dates_local_month.clear();
        SimpleDateFormat search_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat search_date_local_event = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM ", Locale.getDefault());
        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE", Locale.getDefault());
        Calendar startDate = (Calendar) currentDate.clone();

        startDate.set(Calendar.DAY_OF_MONTH, 1);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        int maxDayOfMonth = startDate.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get the day of the week for the first day of the month
        int firstDayOfWeek = startDate.get(Calendar.DAY_OF_WEEK);

        // Add empty spaces to the beginning of the list based on the first day of the week
        for (int i = 1; i < firstDayOfWeek; i++) {
            dates_month.add("");
            search_dates_month.add("");
            search_dates_local_month.add("");
        }

        for (int i = 1; i <= maxDayOfMonth; i++) {
            dates_month.add(sdf.format(startDate.getTime()));
            search_dates_month.add(search_date.format(startDate.getTime()));
            search_dates_local_month.add(search_date_local_event.format(startDate.getTime()));
            startDate.add(Calendar.DATE, 1);
        }

        // Use a copy of the current date for display purposes
        Calendar displayDate = (Calendar) currentDate.clone();

        String startDateString = sdf1.format(displayDate.getTime());
        String year = sdf2.format(displayDate.getTime());
        displayDate.set(Calendar.DAY_OF_MONTH, 1); // Reset to the first day of the month
        displayDate.add(Calendar.MONTH, 1); // Move to the next month
        displayDate.add(Calendar.DATE, -1); // Move to the last day of the current month
        String endDateString = sdf1.format(displayDate.getTime());

        range_month.setText(startDateString);
        dateRangeTextView_month.setText(year);
    }

    private void goToNextMonth() {
        currentDate_month.add(Calendar.MONTH, 1); // Move to the next month
        updateDates_month(currentDate_month); // Update the dates
    }

    private void goToPreviousMonth() {
        currentDate_month.add(Calendar.MONTH, -1); // Move to the previous month
        updateDates_month(currentDate_month); // Update the dates
    }

    private String getMonthName_month(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();


    }

    public void menu(View view) {
        LinearLayout more_layout;
        more_layout = findViewById(R.id.more_layout);
        ImageView menu = findViewById(R.id.menu);
        ImageView close = findViewById(R.id.closebtn);
        menu.setVisibility(View.GONE);
        more_layout.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.VISIBLE);
                more_layout.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
            }
        });
    }

    public void explore(View view) {
        startActivity(new Intent(MainActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(MainActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(MainActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(MainActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(MainActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(MainActivity.this, AppInfoActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));

    }

    private void resetAllToCurrentDate() {
        // Get the current date
        Calendar today = Calendar.getInstance();
        LocalDate currentDate = LocalDate.now(); // Optional: If you need LocalDate for more control

        // Get the current day index (0 = Sunday, 1 = Monday, etc.)
        int currentDayIndex = today.get(Calendar.DAY_OF_WEEK) - 1;

        // Ensure the current day index is within the range of visible days (e.g., 0 to 6)
        int maxVisibleDays = 7;  // You can change this value based on how many days are visible in your view
        if (currentDayIndex >= maxVisibleDays) {
            currentDayIndex = maxVisibleDays - 1;
        }

        // Reset the horizontal position to ensure the current day is at the start of the visible range
        mWeekView.mCurrentOrigin.x = -(currentDayIndex * mWeekView.mWidthPerDay);

        // Optionally, reset the vertical position (if you want to reset the vertical scroll)
        mWeekView.mCurrentOrigin.y = 0;

        // Reset any other variables if needed (such as any flags, time or width variables)
        mWeekView.weekx = 0;  // Reset week position (example variable)


        // Invalidate the view to trigger a redraw
        mWeekView.invalidate();

        // Log the reset operation for debugging purposes
        Log.d("Reset", "All values reset to current date: " + today.getTime());
    }


    @Override
    protected void onResume() {
        super.onResume();

        resetAllToCurrentDate(); // Reset to the current date whenever the page is reopened
    }

}
