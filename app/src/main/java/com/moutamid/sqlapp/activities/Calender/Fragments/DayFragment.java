package com.moutamid.sqlapp.activities.Calender.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Calender.Adapter.DayPagerAdapter;

public class DayFragment extends Fragment {

    private ViewPager2 viewPager;
    private DayPagerAdapter dayPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        viewPager = view.findViewById(R.id.viewPager);

        dayPagerAdapter = new DayPagerAdapter(getChildFragmentManager(), getLifecycle());
        for (int i = -5; i <= 5; i++) {
            DayPageFragment dayPageFragment = new DayPageFragment();
            Bundle args = new Bundle();
            args.putInt("dayOffset", i);
            dayPageFragment.setArguments(args);
            dayPagerAdapter.addFragment(dayPageFragment);
        }

        viewPager.setAdapter(dayPagerAdapter);
        int centralPageIndex = 5;
        viewPager.setCurrentItem(centralPageIndex, false);

        return view;
    }
}
