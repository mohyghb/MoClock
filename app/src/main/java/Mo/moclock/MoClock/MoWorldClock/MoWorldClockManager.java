package Mo.moclock.MoClock.MoWorldClock;

import android.content.Context;
import android.os.Parcelable;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoFileManager;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoFileManagerUtils;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoLog.MoLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import Mo.moclock.MainActivity;
import Mo.moclock.MoClock.MoWorldClock.MoCities.MoCityCoordinate;

public class MoWorldClockManager {

    private static final String FILE_NAME = "mwcmfile";
    private static final String WORLD_CLOCK_FILE = "citiesWithZoneId.csv";
    public static ArrayList<MoWorldClock> worldClocks = new ArrayList<>();
    public static ArrayList<MoCityCoordinate> cities = new ArrayList<>();
//    private static List<MoOnWorldClockAvailableObserver> observers = new ArrayList<>();

    public static void init(Context context) {
//        new Thread() {
//            @Override
//            public void run() {
//                MoWorldClockManager.loadCities(context);
//                observers.forEach(MoOnWorldClockAvailableObserver::onWorldClockAvailable);
//            }
//        }.start();
    }

    public static void subscribe(MoOnWorldClockAvailableObserver observer) {
//        if (!observers.contains(observer)) {
//            observers.add(observer);
//        }
//        MoLog.print("(sub) observers size = " + observers.size());
    }

    public static void unsubscribe(MoOnWorldClockAvailableObserver observer) {
//        observers.remove(observer);
//        MoLog.print("(unsub) observers size = " + observers.size());
    }

    /**
     * loads in the cities that we have along with their coordinates into a list so that it can be
     * searchable and selectable by the user
     * @param context
     */
    public static void loadCities(Context context) {
        if (!cities.isEmpty())
            return;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(WORLD_CLOCK_FILE), StandardCharsets.UTF_8))) {
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String[] elements = mLine.split(",");
                MoCityCoordinate coordinate = new MoCityCoordinate()
                        .setName(elements[0])
                        .setZoneId(elements[1]);
                cities.add(coordinate);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * todo handle duplicates
     * adds a world clock
     * @param context
     * @param worldClock
     */
    public static synchronized void add(Context context, MoWorldClock worldClock) {
        worldClocks.add(worldClock);
        save(context);
    }

    /**
     * removes a list of items from the world clock list
     * @param context
     * @param worldClockList
     */
    public static synchronized void removeSelected(Context context, List<MoWorldClock> worldClockList) {
        worldClocks.removeAll(worldClockList);
        save(context);
    }

    /**
     * saves the world clock list so it can be loaded later on
     * @param context
     */
    public static synchronized void save(Context context) {
        try {
            MoFileManagerUtils.write(context, FILE_NAME, worldClocks);
        } catch (IOException e) {
            // ignore
        }
    }

    public static synchronized void load(Context context) {
        if (!worldClocks.isEmpty())
            return;
        try {
            String data = MoFileManager.readInternalFile(context, FILE_NAME);
            String[] components = MoFile.loadable(data);
            if (MoFile.isValidData(components)) {
                String[] worldClockData = MoFile.loadable(components[0]);
                for (String w : worldClockData) {
                    MoWorldClock worldClock = new MoWorldClock();
                    worldClock.load(w, context);
                    worldClocks.add(worldClock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface MoOnWorldClockAvailableObserver {
        void onWorldClockAvailable();
    }

}
