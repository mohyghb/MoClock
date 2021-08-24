package com.moh.moclock;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.moh.moclock.MoClock.MoAlarmSession.MoAlarmSessionBroadCast;
import com.moh.moclock.MoClock.MoSmartCancel.MoSmartCancel;
import com.moh.moclock.MoClock.MoSmartCancel.MoTapCancelAlarm;

import com.moh.moclock.R;

public class MoTapActivity extends AppCompatActivity {

    private final int MAX_TAPS = 40;
    private final int MIN_TAPS = 25;

    private TextView counter;
    private Button nextTest;
    private ConstraintLayout layout;
    private int counterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_tap);
        MoAlarmSessionBroadCast.activityList.add(this);
        init();
    }

    private void init(){
        this.counter = findViewById(R.id.tap_counter);
        this.nextTest = findViewById(R.id.Another_test_btn);
        this.layout = findViewById(R.id.tap_constrained_layout);
        this.generateValue();


        this.counter.setText(this.counterValue+"");

        this.nextTest.setOnClickListener((b)->{
           // next test
            MoSmartCancel.setResultAndFinish(MoTapActivity.this,MoSmartCancel.NEXT_TEST);
        });

        this.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // decreament counter
                decAndSet();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void decAndSet() {
        this.counterValue--;
        if(this.counterValue <= 0){
            // end this activity
            MoSmartCancel.setResultAndFinish(this,MoSmartCancel.SUCCESS);
        }
        this.counter.setText(this.counterValue+"");
    }

    private void generateValue(){
        this.counterValue = MIN_TAPS + MoTapCancelAlarm.getRandom(MAX_TAPS);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

}
