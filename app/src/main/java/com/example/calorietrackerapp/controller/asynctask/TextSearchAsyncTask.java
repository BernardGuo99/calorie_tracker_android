package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.restclient.google_custom_search.TextSearch;

public class TextSearchAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        return TextSearch.getSnippet(TextSearch.search(params[0], new String[]{"num"}, new String[]{"1"}));
    }
}
