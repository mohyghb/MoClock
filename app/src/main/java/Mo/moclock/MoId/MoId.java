package Mo.moclock.MoId;

import android.content.Context;

import java.util.Random;

import Mo.moclock.MoIO.MoLoadable;
import Mo.moclock.MoIO.MoSavable;

public class MoId implements MoSavable, MoLoadable {


    private int id;

    public MoId(){
        this.id = getRandomId();
    }


    public MoId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSId(){
        return id+"";
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getRandomId()
    {
        return randInt(12,1003122340);
    }


    public static int randInt(int min, int max) {

        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        this.id = Integer.parseInt(data);
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return this.id+"";
    }
}
