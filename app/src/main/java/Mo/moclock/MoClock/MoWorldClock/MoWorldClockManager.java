package Mo.moclock.MoClock.MoWorldClock;

import android.content.Context;
import android.widget.Toast;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import Mo.moclock.MoLog.MoLog;
import Mo.moclock.MoReadWrite.MoReadWrite;
import Mo.moclock.MoReadWrite.MoSave;
import Mo.moclock.R;

public class MoWorldClockManager implements MoSave, MoSavable, MoLoadable {

    private static final String FILE_NAME = "mwcmfile";
    private static final String WORLD_CLOCK_FILE = "world_clock.csv";
    private static ArrayList<MoWorldClock> worldClocks = new ArrayList<>();
    public static ArrayList<MoCountryCityCoordinate> coordinates = new ArrayList<>();

    public static MoWorldClockManager manager = new MoWorldClockManager();

    // load the world clock data into our objects
    public static void loadCountryCityCoordinates(Context context) {
        if (coordinates.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(WORLD_CLOCK_FILE), StandardCharsets.UTF_8))) {
                String mLine;
                int i = 0;
                while ((mLine = reader.readLine()) != null) {
                    String[] elements = mLine.split(",");
                    MoCountryCityCoordinate coordinate = new MoCountryCityCoordinate()
                            .setName(elements[0])
                            .setLat(Double.parseDouble(elements[1]))
                            .setLon(Double.parseDouble(elements[2]));
                    coordinates.add(coordinate);
                    i++;
                }
            } catch (Exception e) {
                // ignore
                e.printStackTrace();
            }
        }
        MoLog.print("size " + coordinates.size());
    }

    /**
     * adds a world clock and saves it
     *
     * @param context
     * @param c
     */
    public void add(Context context, MoWorldClock c) {
        loadIfNotLoaded(context);
        if (worldClocks.contains(c)) {
            Toast.makeText(context, context.getString(R.string.error_already_added), Toast.LENGTH_SHORT).show();
        } else {
            c.setSearchMode(false);
            worldClocks.add(c);
        }
        save(context);
    }


    public void remove(int index, Context context) {
        loadIfNotLoaded(context);
        worldClocks.remove(index);
        save(context);
    }


    /**
     * deletes the selected world clocks
     *
     * @param context
     */
    public void deleteSelected(Context context) {
        for (int i = worldClocks.size() - 1; i >= 0; i--) {
            if (worldClocks.get(i).isSelected()) {
                worldClocks.remove(i);
            }
        }
        MoWorldClockManager.manager.save(context);
    }


    public void deselectAll() {
        for (MoWorldClock worldClock : worldClocks) {
            worldClock.setSelected(false);
        }
    }

    public void selectAll() {
        for (MoWorldClock worldClock : worldClocks) {
            worldClock.setSelected(true);
        }
    }

    public int getSelectedSize() {
        int t = 0;
        for (MoWorldClock clock : worldClocks) {
            if (clock.isSelected())
                t++;
        }
        return t;
    }


    public int size() {
        return worldClocks.size();
    }

    private void loadIfNotLoaded(Context context) {
        if (worldClocks.isEmpty()) {
            load("", context);
        }
    }


    public ArrayList<MoWorldClock> getWorldClocks() {
        return worldClocks;
    }


    @Override
    public void save(Context context) {
        MoReadWrite.saveFile(FILE_NAME, getData(), context);
    }

    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        worldClocks.clear();
        String[] components = MoFile.loadable(MoReadWrite.readFile(FILE_NAME, context));
        for (String s : components) {
            if (!s.isEmpty()) {
                MoWorldClock c = new MoWorldClock();
                c.load(s, context);
                worldClocks.add(c);
            }
        }
//        if(worldClocks.isEmpty()){
//            // if it is still empty
//            // add the local one to their list
//            MoWorldClock c  = new MoWorldClock();
//            c.load("",context);
//            worldClocks.add(c);
//        }
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(worldClocks);
    }
}
