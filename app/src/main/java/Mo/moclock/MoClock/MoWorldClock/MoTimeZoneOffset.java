package Mo.moclock.MoClock.MoWorldClock;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import Mo.moclock.MoDate.MoDate;
import Mo.moclock.MoIO.MoFile;
import Mo.moclock.MoIO.MoLoadable;
import Mo.moclock.MoIO.MoSavable;

public class MoTimeZoneOffset implements MoSavable, MoLoadable {

    private static final String REPLACE_TEXT = "Time zone in";
    private static final String SEP_KEY = "o&sjiabgu&";


    private String nameCity;
    private String timeZone;
    private String pureTimeZone;
    private int pureHourOffset;
    private int pureMinuteOffset;
    private boolean isAdd;
    private String parse;

    private TimeZone tz;

    public MoTimeZoneOffset(String parse){
        this.parse = parse;
        init();
    }

    // for loading
    public MoTimeZoneOffset(){}

    private void init(){
        config();
        configOffset();
        initTimeZone();
    }

    private void initTimeZone(){
        tz = TimeZone.getTimeZone(this.pureTimeZone);
    }


    private void config(){
        parse = parse.replace(REPLACE_TEXT,"");
        parse = parse.trim();
        StringBuilder nameCity = new StringBuilder();
        StringBuilder timeZone = new StringBuilder();
        boolean isTime = false;
        for(char c: parse.toCharArray()){
            if(c==')'){
                break;
            }else if(c == '('){
                // open
                isTime = true;
            }else if(isTime){
                timeZone.append(c);
            }else {
                nameCity.append(c);
            }
        }
        this.nameCity = nameCity.toString().trim();
        this.timeZone = timeZone.toString();
    }




    private void configOffset(){
        if(this.timeZone.contains("-")){
            // minus from that timezone
            this.isAdd = false;
            configPureComponents("\\-");
        }else if(this.timeZone.contains("+")){
            // we need to add pureHour and pureMinute
            this.isAdd = true;
            configPureComponents("\\+");
        }else{
            this.pureTimeZone = this.timeZone;
            this.pureHourOffset = 0;
            this.pureMinuteOffset =  0;
        }

    }

    private void configPureComponents(String splitter){
        String[] parts = this.timeZone.split(splitter);
        // [GMT, 4]
        if(parts.length >= 2){
            this.pureTimeZone = parts[0];
            if(parts[1].contains(":")){
                // then there is minutes as well
                String[] timeComps = parts[1].split(":");
                this.pureHourOffset = Integer.parseInt(timeComps[0]);
                this.pureMinuteOffset = Integer.parseInt(timeComps[1]);
            }else{
                this.pureHourOffset = Integer.parseInt(parts[1]);
                this.pureMinuteOffset = 0;
            }
        }
    }




    public void applyTimeZone(MoDate date){
        date.setTimeZone(tz);
        date.add(Calendar.HOUR_OF_DAY,isAdd?this.pureHourOffset:-1*this.pureHourOffset);
        date.add(Calendar.MINUTE,isAdd?this.pureMinuteOffset:-1*this.pureMinuteOffset);
    }

    public void applyTimeZone(Calendar date){
        date.setTimeZone(tz);
        date.add(Calendar.HOUR_OF_DAY,isAdd?this.pureHourOffset:-1*this.pureHourOffset);
        date.add(Calendar.MINUTE,isAdd?this.pureMinuteOffset:-1*this.pureMinuteOffset);
    }


    /**
     * loads a savable object into its class
     *
     * @param data
     * @param context
     */
    @Override
    public void load(String data, Context context) {
        String[] parts = MoFile.loadable(data);
        this.nameCity = parts[0];
        this.timeZone = parts[1];
        this.pureTimeZone = parts[2];
        this.pureHourOffset = Integer.parseInt(parts[3]);
        this.pureMinuteOffset = Integer.parseInt(parts[4]);
        this.isAdd = Boolean.parseBoolean(parts[5]);
        this.parse = parts[6];
        initTimeZone();
    }

    /**
     * @return the data that is going to be saved by the save method
     * inside the class which implements MoSavable
     */
    @Override
    public String getData() {
        return MoFile.getData(this.nameCity,this.timeZone,
                this.pureTimeZone,this.pureHourOffset,this.pureMinuteOffset,this.isAdd,this.parse);
    }


    public String getNameCity() {
        return nameCity;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getPureTimeZone() {
        return pureTimeZone;
    }

    public int getPureHourOffset() {
        return pureHourOffset;
    }

    public int getPureMinuteOffset() {
        return pureMinuteOffset;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public String getParse() {
        return parse;
    }

    public TimeZone getTz() {
        return tz;
    }
}
