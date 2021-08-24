package com.moh.moclock.MoHotWordDetection;

import android.content.Context;

import com.moofficial.moessentials.MoEssentials.MoLog.MoLog;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public abstract class MoHotWordDetector implements RecognitionListener {

    public static final String HOT_WORD_STOP = "stop";

    private static final String WAKEWORD_SEARCH = "WAKEWORD_SEARCH";
    private static final float SENSITIVITY = 0.5f;
    private SpeechRecognizer mRecognizer;



    private Context context;
    private String hotWord;
    private float sensitivity;
    private int detected;

    public MoHotWordDetector(Context a,String hw){
        this.context = a;
        this.hotWord = hw;
        this.sensitivity  = SENSITIVITY;
    }

    public MoHotWordDetector(Context a,String hw,float s){
        this.context = a;
        this.hotWord = hw;
        this.sensitivity = s;
    }

    /**
     * Setup the Recognizer with a sensitivity value in the range [1..100]
     * Where 1 means no false alarms but many true matches might be missed.
     * and 100 most of the words will be correctly detected, but you will have many false alarms.
     */
    private void setup() {
        // we assume that you have the recording permission already
        onDestroy();
        try {
            final Assets assets = new Assets(this.context);
            final File assetDir = assets.syncAssets();
            mRecognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetDir, "models/en-us-ptm"))
                    .setDictionary(new File(assetDir, "models/lm/words.dic"))
                    .setKeywordThreshold(sensitivity)
                    .getRecognizer();
            mRecognizer.addKeyphraseSearch(WAKEWORD_SEARCH, this.hotWord);
            mRecognizer.addListener(this);
            mRecognizer.startListening(WAKEWORD_SEARCH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onResume(){
        this.setup();
    }

    public void onDestroy(){
        if (mRecognizer != null) {
            mRecognizer.removeListener(this);
            mRecognizer.cancel();
            mRecognizer.shutdown();
            mRecognizer = null;
            MoLog.print("on destroy was called ");
        }
    }

    public void onPause(){
        if (mRecognizer != null) {
            mRecognizer.removeListener(this);
            mRecognizer.cancel();
            mRecognizer.shutdown();
        }
    }


    @Override
    public void onBeginningOfSpeech() {
        MoLog.print("Beginning Of Speech");
    }

    @Override
    public void onEndOfSpeech() {
        MoLog.print("END Of Speech");
    }

    @Override
    public void onPartialResult(final Hypothesis hypothesis) {
        if (hypothesis != null && mRecognizer!=null) {
            final String text = hypothesis.getHypstr();
             MoLog.print("On partial (" + text + ")");
           // Log.d(LOG_TAG, "on partial: " + text);
            if (text.toLowerCase().equals(this.hotWord)) {
                if(detected == 0) {
                    onWakeWordDetected();
                }
                detected++;
                //mVibrator.vibrate(100);
              //  MoLog.print("DETECTED (" + text + ")");
               // onWakeWordDetected();
            }
        }
    }

    @Override
    public void onResult(final Hypothesis hypothesis) {
//        if (hypothesis != null) {
//            MoLog.print("on Result: " + hypothesis.getHypstr() + " : " + hypothesis.getBestScore());
//        }
    }

    @Override
    public void onError(final Exception e) {
        //MoLog.print(" ON ERROR " + e);
    }

    @Override
    public void onTimeout() {
       // MoLog.print("ON TIME OUT");
    }


    public abstract void onWakeWordDetected();


}
