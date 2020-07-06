package Mo.moclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import Mo.moclock.MoClock.MoWorldClock.MoTimeZoneOffset;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClock;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClockArrayAdapter;
import Mo.moclock.MoClock.MoWorldClock.MoWorldClockManager;
import Mo.moclock.MoGoogleSearch.Google.GoogleSearch;
import Mo.moclock.MoGoogleSearch.MoGoogle.MoAnswerBox;
import Mo.moclock.MoGoogleSearch.MoGoogle.MoAnswerBoxManager;
import Mo.moclock.MoListView.MoListView;
import Mo.moclock.MoLog.MoLog;

public class MoAddWorldClockActivity extends AppCompatActivity {


    private static final String FAILED = "Sorry, something went wrong at this moment, please try again later.";

    private ArrayList<MoWorldClock> citiesWorldClocks = new ArrayList<>();


    private Button cancel;

    private ListView listView;
    private MoListView<MoWorldClock> moListView;
    private TextInputEditText search;
    private ProgressBar progressBar;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_add_world_clock);
        this.init();

    }

    private void init(){
        //loadIfNotLoaded();
        this.progressBar = findViewById(R.id.world_clock_progress_bar);
        this.cancel = findViewById(R.id.cancel_world_clock_search);
        this.cancel.setOnClickListener((v)-> finish());
        this.listView = findViewById(R.id.cities_list_view);
        this.search = findViewById(R.id.name_world_text_field);




        search.setOnEditorActionListener((textView, i, keyEvent) -> {
            onSearch(textView,i,keyEvent);
            return false;
        });



        this.moListView = new MoListView<>(listView,
                new MoWorldClockArrayAdapter(this,0, citiesWorldClocks,null),
                citiesWorldClocks,this);
        hideProgress();
    }

    private void onSearch(TextView textView, int actionId, KeyEvent event){
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                (actionId == EditorInfo.IME_ACTION_GO) || actionId == EditorInfo.IME_ACTION_DONE) {
            // then we need to search
            showProgressLoading();
            GoogleSearch googleSearch = new GoogleSearch();
            googleSearch.setOnResult(()-> this.onGoogleSearchResult(googleSearch.getAnswerBoxManager()));
            googleSearch.setOnRateLimit(this::onGoogleSearchError);
            googleSearch.setSearch("time zone of " + this.search.getText().toString());
            googleSearch.execute();
        }
    }


    private void onGoogleSearchResult(MoAnswerBoxManager answerBoxManager){
        hideProgress();
        answerBoxManager.display(2);
        List<MoAnswerBox> boxes = answerBoxManager.getAnswerBoxes();
        if(!boxes.isEmpty()){
            ArrayList<String> results = boxes.get(0).getWholeData();
            if(results.size() > 1) {
                String parsableData = results.get(1);
                MoTimeZoneOffset timeZoneOffset = new MoTimeZoneOffset(parsableData);
                MoWorldClock worldClock = new MoWorldClock(timeZoneOffset);
                citiesWorldClocks.add(worldClock);
                moListView.setArrayAdapter(new MoWorldClockArrayAdapter(MoAddWorldClockActivity.this,
                    0,citiesWorldClocks,null));
            }else {
                // error
                onGoogleSearchError();
            }
        }else {
            // error
            onGoogleSearchError();
        }
    }

    private void onGoogleSearchError(){
        hideProgress();
        runOnUiThread(() -> {
            Snackbar.make(search,FAILED,Snackbar.LENGTH_LONG)
                    .setAnchorView(search)
                    .setBackgroundTint(getColor(R.color.error_color))
                    .setTextColor(getColor(R.color.snack_bar_text))
                    .show();
        });
    }


    private void hideProgress(){
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressLoading(){
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }


}
