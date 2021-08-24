package com.moh.moclock.MoIntents;

import android.app.Activity;
import android.content.Intent;

public class MoIntents {

    public static void openMusicPicker(Activity a,int requestCode) {
        Intent audio_picker_intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        a.startActivityForResult(audio_picker_intent, requestCode);
    }
}
