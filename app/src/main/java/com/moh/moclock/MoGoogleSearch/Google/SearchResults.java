package com.moh.moclock.MoGoogleSearch.Google;

public class SearchResults {


    // takes html returns the string only containing the search results
    public static final String SEARCH_RESULTS = "Search Results";

    public static String getSearchResults(String html)
    {
        if (html != null && html.contains(SEARCH_RESULTS)) {
            return html.split(SEARCH_RESULTS)[1];
        }
        return "";

    }



}
