package com.moutamid.sqlapp.activities.Calender.calenderapp.week;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeekPagerAdapter extends FragmentPagerAdapter {

    private List<Calendar> weeks = new ArrayList<>();

    public WeekPagerAdapter(@NonNull FragmentManager fm, List<Calendar> weeks) {
        super(fm);
        this.weeks = weeks;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Calendar weekStartDate = weeks.get(position);
        return WeekFragment.newInstance(weekStartDate);
    }

    @Override
    public int getCount() {
        return weeks.size();
    }
}
