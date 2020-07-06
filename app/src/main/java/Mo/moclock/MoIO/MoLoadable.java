package Mo.moclock.MoIO;

import android.content.Context;

public interface MoLoadable {

    /**
     * loads a savable object into its class
     */
    void load(String data,Context context);


}
