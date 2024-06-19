package com.seohyun.kimseohyun2091052;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private TextView quoteTextView;
    private String[] quotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Inflating layout");
        View rootView = inflater.inflate(R.layout.fragment_quote_display, container, false);
        quoteTextView = rootView.findViewById(R.id.quoteTextView);
        if (quoteTextView != null) {
            Log.d(TAG, "onCreateView: quoteTextView found");
        } else {
            Log.e(TAG, "onCreateView: quoteTextView not found");
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Displaying random quote");
        // Display a random quote
        displayRandomQuote();
    }

    private void loadQuotesFromJson() {
        Log.d(TAG, "loadQuotesFromJson: Loading quotes from JSON file");
        String json;
        try {
            InputStream is = getActivity().getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            quotes = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                quotes[i] = jsonArray.getString(i);
            }

            Log.d(TAG, "loadQuotesFromJson: Successfully loaded quotes from JSON");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "loadQuotesFromJson: Error loading quotes from JSON", e);
        }
    }

    private void displayRandomQuote() {
        Log.d(TAG, "displayRandomQuote: Displaying random quote");

        // Load quotes from JSON file if not loaded yet
        if (quotes == null) {
            Log.d(TAG, "displayRandomQuote: Loading quotes from JSON file");
            loadQuotesFromJson();
        }

        if (quotes != null && quotes.length > 0) {
            Random random = new Random();
            int index = random.nextInt(quotes.length);
            String randomQuote = quotes[index];
            Log.d(TAG, "displayRandomQuote: Random quote: " + randomQuote);
            quoteTextView.setText(randomQuote);
        } else {
            Log.e(TAG, "displayRandomQuote: Quotes array is null or empty");
        }
    }
}
