package com.seohyun.kimseohyun2091052;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DiaryFragment extends Fragment {

    private CalendarView calendarView;

    public DiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 날짜를 선택하면 일기 작성 다이얼로그 표시
                showWriteDiaryDialog(year, month, dayOfMonth);
            }
        });

        return view;
    }

    private void showWriteDiaryDialog(int year, int month, int dayOfMonth) {
        // DialogFragment를 이용하여 일기 작성 다이얼로그 표시
        WriteDiaryDialogFragment dialogFragment = WriteDiaryDialogFragment.newInstance(year, month, dayOfMonth);
        dialogFragment.show(getChildFragmentManager(), "WriteDiaryDialog");
    }
}
