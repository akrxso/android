package com.seohyun.kimseohyun2091052;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuoteDisplayFragment extends Fragment {

    private TextView quoteTextView;

    public QuoteDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quote_display, container, false);
        quoteTextView = view.findViewById(R.id.quoteTextView);

        // 인자로 전달된 명언 텍스트 설정
        Bundle args = getArguments();
        if (args != null) {
            String quote = args.getString("quote", "");
            quoteTextView.setText(quote);
        }

        return view;
    }
}

