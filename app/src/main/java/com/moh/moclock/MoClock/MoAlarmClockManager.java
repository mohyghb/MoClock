package com.moh.moclock.MoClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoFile;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoLoadable;
import com.moofficial.moessentials.MoEssentials.MoFileManager.MoIO.MoSavable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.moh.moclock.MainActivity;
import com.moh.moclock.MoClock.MoClockSugestions.MoClockSuggestionManager;
import com.moh.moclock.MoClock.MoSnooze.MoSnooze;
import com.moh.moclock.MoDate.MoDate;
import com.moh.moclock.MoId.MoId;
import com.moh.moclock.MoReadWrite.MoReadWrite;
import com.moh.moclock.MoRunnable.MoRunnableUtils;
import com.moh.moclock.MoVibration.MoVibration;
import com.moh.moclock.MoVibration.MoVibrationTypes;

public class MoAlarmClockManager implements Iterable<MoAlarmClock>, MoSavable, MoLoadable {


    public static String SET_ID = "id";
    private static final String FILE_NAME_ALARMS = "xcviui";

    private List<MoAlarmClock> clockList;
    private HashSet<Integer> reservedIds;
    public static Runnable refreshScreen;
    private int nextId;


    /**
     * a singleton responsible with most of the actions of
     * alarm clocks
     */

    private static MoAlarmClockManager ourInstance = new MoAlarmClockManager();

    public static MoAlarmClockManager getInstance() {
        return ourInstance;
    }

    private MoAlarmClockManager() {
        this.clockList = new ArrayList<>();
        this.reservedIds = new HashSet<>();
        this.nextId = 0;
    }


    public void onDestroy() {
        ourInstance = null;
        ourInstance = new MoAlarmClockManager();
        refreshScreen = null;
    }

    public ArrayList<MoAlarmClock> getAlarms() {
        return (ArrayList<MoAlarmClock>) this.clockList;
    }


    public void addAlarm(MoAlarmClock c, Context context) {
        addAlarm(c, context, true);
    }

    public void addAlarm(MoAlarmClock c, Context context, boolean toast) {
        this.loadIfNotLoaded(context);
        if (this.clockList.contains(c)) {
            // if there is a clock inside the list
            // that has the same date and time
            // we need to just turn that on
            turnOn(c, context);
        } else {
            this.clockList.add(c);
        }
        if (toast) {
            Toast.makeText(context, "Alarm set for " +
                    c.getDate().getReadableDifference(Calendar.getInstance()), Toast.LENGTH_LONG).show();
        }
        MoClockSuggestionManager.add(c, context);
        saveActivate(context);
    }


    /**
     * creates an alarm based on the values that are given
     * it also adds the alarm to the alarm list
     *
     * @param title
     * @param date
     * @param snooze
     * @param vibration
     * @param music
     */
    public void createAlarm(Context context, String title, MoDate date, boolean snooze, boolean vibration, boolean music) {
        MoAlarmClock c = new MoAlarmClock();
        // setting an id for this which is unique
        c.setId(MoAlarmClockManager.getInstance().getNextId());
        c.setTitle(title);
        c.setDateTime(date);
        c.setActive(true);
        c.setSnooze(new MoSnooze(context, snooze));
        c.setVibration(new MoVibration(MoVibrationTypes.BASIC, vibration));
        c.setPathToMusic(music);
        c.setRepeating(new MoRepeating());
        addAlarm(c, context);
    }


    public void addAlarmNoToast(MoAlarmClock c, Context context) {
        addAlarm(c, context, false);
    }


    public boolean isEmpty() {
        return this.clockList.isEmpty();
    }

    private void turnOn(MoAlarmClock c, Context context) {
        for (MoAlarmClock clock : this.clockList) {
            if (clock.equals(c)) {
                // just giving updating the old alarm
                clock.setActive(true);
                clock.setDateTime(c.getDateTime());
                clock.setTitle(c.getTitle());
                clock.setSnooze(c.getSnooze());
                clock.setVibration(c.getVibration());
                clock.setPathToMusic(c.hasMusic());
                clock.setRepeating(c.getRepeating());
                break;
            }
        }
    }

    public void save(Context context) {
        MoReadWrite.saveFile(FILE_NAME_ALARMS, this.getData(), context);
    }

    // saves and activates the next alarm
    public void saveActivate(Context context) {
        save(context);
        activateNextAlarm(context);
    }

    // saves and activates the next alarm
    // and refreshes the screen if it is not null
    public void saveActivateRefresh(Context context) {
        save(context);
        activateNextAlarm(context);
        MoRunnableUtils.runIfNotNull(refreshScreen);
    }

    /**
     * snoozes the alarm based on the
     * amount that is passed to it or by
     * the snooze specification made on the
     * creation of clock
     *
     * @param id
     * @param minutes
     * @param context
     */
    public void snoozeAlarm(int id, int minutes, Context context) {
        try {
            // finding the clock
            MoAlarmClock c = getAlarm(id);
            // snoozing it
            c.snooze(context);
            // adding it to suggestions
            MoClockSuggestionManager.add(c, context);
            // saving and activating the next alarm
            saveActivateRefresh(context);
        } catch (MoEmptyAlarmException e) {
            Toast.makeText(context, "Failed to snooze", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    /**
     * @return
     * @throws MoEmptyAlarmException if there is no clock, or no active clock, we throw empty alarm exception
     */
    public int getNextAlarmIndex() throws MoEmptyAlarmException {
        if (this.clockList.isEmpty() || !this.hasAnActiveClock())
            throw new MoEmptyAlarmException();
        int index = -1;
        for (int i = 0; i < this.clockList.size(); i++) {
            if (index == -1 && this.clockList.get(i).isActive()) {
                index = i;
            } else if (this.clockList.get(i).isActive()) {
                boolean iIsBeforeIndex = this.clockList.get(i).getDateTime()
                        .before(this.clockList.get(index).getDateTime());
                if (iIsBeforeIndex)
                    index = i;
            }
        }

        return index;
    }

    private boolean hasAnActiveClock() {
        for (MoAlarmClock c : this.clockList) {
            if (c.isActive()) {
                return true;
            }
        }
        return false;
    }

    public MoAlarmClock getNextAlarm() throws MoEmptyAlarmException {
        return this.clockList.get(this.getNextAlarmIndex());
    }


    public MoAlarmClock getAlarm(int id) throws MoEmptyAlarmException {
        for (MoAlarmClock c : this.clockList) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new MoEmptyAlarmException();
    }


    /**
     * removes alarm based on its id
     *
     * @param id
     */
    public void removeAlarm(MoId id, Context context) {
        this.loadIfNotLoaded(context);
        try {
            MoAlarmClock c = this.getAlarm(id.getId());
            this.clockList.remove(c);
            MoReadWrite.saveFile(FILE_NAME_ALARMS, this.getData(), context);
        } catch (MoEmptyAlarmException e) {
            // if it didn't find one dont do anything
            System.out.println("we did not find an alarm with id: " + id);
        }
        this.activateNextAlarm(context);
    }


    /**
     * removes an alarm based on the index inside the array
     *
     * @param index
     */
    public void removeAlarm(int index, Context context) {
        this.loadIfNotLoaded(context);
        this.cancelAlarm(this.clockList.get(index), context);
        this.clockList.remove(index);
        saveActivate(context);
    }

    /**
     * removes an alarm based on the index inside the array
     *
     * @param index
     */
    public void removeAlarm(int index, Context context, boolean save) {
        this.loadIfNotLoaded(context);
        this.cancelAlarm(this.clockList.get(index), context);
        this.clockList.remove(index);
        if (save) {
            saveActivate(context);
        }
    }

    /**
     * if an alarm is selected, remove it
     * we go from back to front
     * since index need to be preserved
     */
    public void removeSelectedAlarms(Context context) {
        for (int i = this.clockList.size() - 1; i >= 0; i--) {
            if (this.clockList.get(i).isSelected()) {
                removeAlarm(i, context, false);
            }
        }
        saveActivate(context);
    }


    /**
     * @return number of selected alarms
     */
    public int getSelectedCount() {
        int count = 0;
        for (MoAlarmClock c : clockList) {
            if (c.isSelected()) {
                count++;
            }
        }
        return count;
    }


    /**
     * activates the next alarm
     * or if there is no next alarm
     * it throws an MoEmptyAlarmException
     *
     * @param c Context required for application purposes and to activate an alarm
     */
    public void activateNextAlarm(Context c) {
        this.loadIfNotLoaded(c);
        for (MoAlarmClock clock : clockList) {
            if (clock.isActive()) {
                this.activateAlarm(clock, c);
            } else {
                this.cancelPendingIntent(clock, c);
            }
        }
    }


    /**
     * activates an alarm which makes the alarm to ring next
     *
     * @param c       An alarm clock
     * @param context needed to perform certain tasks
     */
    private void activateAlarm(MoAlarmClock c, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MoAlarmReceiver.class);
        intent.putExtra(SET_ID, c.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, c.getId(), intent, 0);
        Intent i2 = new Intent(context, MainActivity.class);
        PendingIntent pi2 = PendingIntent.getActivity(context, 0, i2, 0);
        AlarmManager.AlarmClockInfo ac =
                new AlarmManager.AlarmClockInfo(c.getDateTime().getTimeInMillis(),
                        pi2);
        assert alarmMgr != null;
        alarmMgr.setAlarmClock(ac, pendingIntent);
    }


    /**
     * stops the alarm before ringing
     *
     * @param c        An MoAlarmClock
     * @param activity required for application purposes
     */
    public void cancelAlarm(MoAlarmClock c, Context activity) {
        AlarmManager alarmMgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, MoAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, c.getId(), intent, 0);
        // canceling alarm only if it's not repeating
        c.cancel();
        // ***
        assert alarmMgr != null;
        alarmMgr.cancel(pendingIntent);
    }


    public void cancelAlarm(int id, Context context) throws MoEmptyAlarmException {
        loadIfNotLoaded(context);
        this.cancelAlarm(this.getAlarm(id), context);
        saveActivate(context);
    }

    public void cancelPendingIntent(MoAlarmClock c, Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MoAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, c.getId(), intent, 0);
        alarmMgr.cancel(pendingIntent);
    }


    // returns the id of next alarm
    // and places that id inside the reserved ones
    public MoId getNextId() {
        nextId++;
        while (reservedIds.contains(nextId)) {
            nextId++;
        }
        reservedIds.add(nextId);
        return new MoId(nextId);
    }


    @NonNull
    @Override
    public Iterator<MoAlarmClock> iterator() {
        return this.clockList.iterator();
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(this.clockList);
    }

    /**
     * loads a savable object into its class
     */
    @Override
    public void load(String data, Context context) {
        this.clockList.clear();
        String[] components = MoFile.loadable(MoReadWrite.readFile(FILE_NAME_ALARMS, context));
        if (MoFile.isValidData(components)) {
            String[] alarms = MoFile.loadable(components[0]);
            for (String a : alarms) {
                if (!a.isEmpty()) {
                    MoAlarmClock c = new MoAlarmClock();
                    // loading its data back to itself
                    c.load(a, context);
                    if (c.getId() > nextId) {
                        // updating the next id of the clock
                        nextId = c.getId();
                    }
                    // adding the id of this clock to reserved ones
                    reservedIds.add(c.getId());
                    // adding the clock to clock list
                    this.clockList.add(c);
                }
            }
        }
    }

    public void loadIfNotLoaded(Context c) {
        if (this.clockList.isEmpty()) {
            this.load("", c);
        }
    }

}
