package com.seohyun.kimseohyun2091052;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteDiaryDialogFragment extends DialogFragment {

    private int year, month, dayOfMonth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String currentUserUid;

    // newInstance 메서드를 통해 날짜 정보 전달
    public static WriteDiaryDialogFragment newInstance(int year, int month, int dayOfMonth) {
        WriteDiaryDialogFragment fragment = new WriteDiaryDialogFragment();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("dayOfMonth", dayOfMonth);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            year = getArguments().getInt("year");
            month = getArguments().getInt("month");
            dayOfMonth = getArguments().getInt("dayOfMonth");
        }

        // 현재 사용자 UID 가져오기
        currentUserUid = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_diary_bottom_sheet, container, false);

        // 선택한 날짜 표시
        EditText dateEditText = view.findViewById(R.id.dateEditText);
        dateEditText.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");

        // 일기 작성 완료 버튼
        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일기 내용 가져오기
                EditText diaryEditText = view.findViewById(R.id.contentEditText);
                String diaryContent = diaryEditText.getText().toString();

                // Firestore에 데이터 저장하기
                saveDiaryToFirestore(year, month, dayOfMonth, diaryContent);

                // TODO: 일기 저장 후 처리할 로직 추가

                dismiss(); // 다이얼로그 닫기
            }
        });

        return view;
    }

    private void saveDiaryToFirestore(int year, int month, int dayOfMonth, String diaryContent) {
        // 현재 시간을 가져와서 문자열로 변환하여 저장
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        // Firestore에 저장할 데이터 생성
        Map<String, Object> diary = new HashMap<>();
        diary.put("day", year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");
        diary.put("content", diaryContent);
        diary.put("timestamp", currentTime); // 시간 정보 추가 (옵션)

        // Firestore에 데이터 추가 (사용자 UID를 기반으로 저장)
        db.collection("users")
                .document(currentUserUid)  // 사용자 UID로 문서 경로 설정
                .collection("diaries")    // 'diaries' 서브컬렉션에 저장
                .add(diary)    // 자동 생성된 문서 ID를 사용하여 데이터 추가
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // 성공적으로 저장된 경우 처리할 로직 추가
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 저장 실패 시 처리할 로직 추가
                    }
                });
    }
}
