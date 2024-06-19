package com.seohyun.kimseohyun2091052;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiaryListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    private List<Diary> diaryList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public DiaryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        diaryList = new ArrayList<>();
        adapter = new DiaryAdapter(diaryList);

        adapter.setOnDeleteClickListener(position -> {
            // Handle delete click
            showDeleteConfirmationDialog(position);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        // Fetch diaries from Firestore
        fetchDiaries();

        return view;
    }

    private void fetchDiaries() {
        String uid = currentUser.getUid();

        db.collection("users")
                .document(uid)
                .collection("diaries")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        diaryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Diary diary = document.toObject(Diary.class);
                            diary.setId(document.getId());
                            diaryList.add(diary);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("DiaryListFragment", "Error fetching diaries: ", task.getException());
                    }
                });
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("다이어리 지우❌")
                .setMessage("네를 누르면 다시 복구가 안됩니다!")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, delete the diary
                        deleteDiary(position);
                    }
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    private void deleteDiary(int position) {
        String diaryId = diaryList.get(position).getId();

        db.collection("users")
                .document(currentUser.getUid())
                .collection("diaries")
                .document(diaryId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DiaryListFragment", "Diary deleted successfully");

                    // Remove from local list and notify adapter
                    diaryList.remove(position);
                    adapter.notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    Log.e("DiaryListFragment", "Error deleting diary", e);
                });
    }
}
