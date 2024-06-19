package com.seohyun.kimseohyun2091052;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

public class StartActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 탭바 설정
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        // 프래그먼트 어댑터 설정
        DiaryPagerAdapter adapter = new DiaryPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiaryFragment(), "일기 작성");
        adapter.addFragment(new DiaryListFragment(), "일기 목록");
        adapter.addFragment(new SettingsFragment(), "명언");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
