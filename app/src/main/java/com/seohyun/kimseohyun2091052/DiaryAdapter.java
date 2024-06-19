package com.seohyun.kimseohyun2091052;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<Diary> diaryList;
    private OnDeleteClickListener deleteClickListener;

    public DiaryAdapter(List<Diary> diaryList) {
        this.diaryList = diaryList;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        Diary diary = diaryList.get(position);
        holder.bind(diary);
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder {

        private TextView dateTextView;
        private TextView contentTextView;
        private View deleteButton;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }

        public void bind(Diary diary) {
            dateTextView.setText(diary.getDay());
            contentTextView.setText(diary.getContent());
        }
    }
}
