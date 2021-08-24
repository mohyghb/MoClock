package com.moh.moclock.MoGoogleSearch.Google;


import android.os.AsyncTask;

import java.io.IOException;

import com.moh.moclock.MoGoogleSearch.Html.MoHtml;
import com.moh.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxManager;


// a class that can scrape data from google smart answers
public class GoogleSearch extends AsyncTask<Void,Void,Void> {

        public static String google = "https://www.google.com/search?q=";
        private String search;

        private String result;
        protected MoAnswerBoxManager answerBoxManager;
        private Runnable onResult;
        private Runnable onRateLimit;
        private boolean errorOccurred;


        public GoogleSearch() {}

        public void setOnResult(Runnable onResult) {
                this.onResult = onResult;
        }

        public void setOnRateLimit(Runnable onRateLimit) {
                this.onRateLimit = onRateLimit;
        }

        private String getUrl() {
                return google + search.replace(" ","+");
        }


        /**
         * searches for any google smart answer online and scrapes it
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
                try {
                        String html = MoHtml.getHtml(this.getUrl());
                        String results = SearchResults.getSearchResults(html);
                        this.answerBoxManager = new MoAnswerBoxManager();
                        answerBoxManager.process(results);
                } catch (IOException e) {
                       // e.printStackTrace();
                        onRateLimit.run();
                        errorOccurred = true;
                }
                return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
                if(!errorOccurred){
                        onResult.run();
                }

                super.onPostExecute(aVoid);
        }


        public void setSearch(String search) {
                this.search = search;
        }

        public MoAnswerBoxManager getAnswerBoxManager() {
                return answerBoxManager;
        }
}
