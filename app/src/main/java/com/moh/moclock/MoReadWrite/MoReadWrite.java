package com.moh.moclock.MoReadWrite;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MoReadWrite {

    public static void saveFile(String file, String text, Context context) {
        try{

            FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
        }catch(Exception e){

        }
    }

    public static String readFile(String filename, Context context) {
        String text = "";
        try{
            if(fileExist(filename,context)) {
                FileInputStream fis = context.openFileInput(filename);
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();
                text = new String(buffer);
            }
        }catch(Exception e) {
            Toast.makeText(context,"There was an ERROR in loading the functions", Toast.LENGTH_LONG).show();
        }
        return text;
    }

    public static boolean fileExist(String fileName, Context context){
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }
}
