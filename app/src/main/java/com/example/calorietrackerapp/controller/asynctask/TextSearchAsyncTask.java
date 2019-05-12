package com.example.calorietrackerapp.controller.asynctask;

import android.os.AsyncTask;

import com.example.calorietrackerapp.model.google_custom_search.TextSearch;

public class TextSearchAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        return TextSearch.getSnippet(TextSearch.search(params[0], new String[]{"num"}, new String[]{"1"}));
    }
}
