package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;


import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;
import com.moh.moclock.MoGoogleSearch.Google.MoConstants;


public class WeatherAnswerBox extends AnswerBox {


    public static final String PLACE_CODE = "<div class=\"vk_gy vk_h\" id=\"wob_loc\">";
    public static final String DATE_TIME_CODE = "<div class=\"vk_gy vk_sh\" id=\"wob_dts\">";
    public static final String WEATHER_STATE_CODE = "<span class=\"vk_gy vk_sh\" id=\"wob_dc\">";

    public static final String TEMPERATURE_CODE = "<span class=\"wob_t\" id=\"wob_tm\" style=\"display:inline\">";
    public static final String TEMPERATURE1_CODE = "<span class=\"wob_t\" id=\"wob_ttm\" style=\"display:none\">";

    public static final String PRECIPITATION_PERCENTAGE_CODE = "<span id=\"wob_pp\">";
    public static final String HUMIDITY_PERCENTAGE_CODE = "<span id=\"wob_hm\">";
    public static final String WIND_SPEED_CODE = "<span class=\"wob_t\" id=\"wob_ws\">";

    public static final String IMAGE_URI_CODE = "<img style=\"float:left;height:64px;width:64px\" alt=\"";
    public static final String SOURCE_CODE = "src=\"";


    public static final String UNIT_TEMPERATURE = "<span aria-label=\"";



    private String place;
    private String dateTime;
    private String weatherState;

    private String temperature;
    private String temperature1;
    private String temperatureUnit;
    private String temperatureUnit1;
    private boolean showingFirstTemperature;



    private String precipitationPercentage;
    private String humidityPercentage;
    private String windSpeed;

    private String imageUri;

    // absolutely for nothing
    // just to show that I know composite pattern
    ArrayList<AnswerBox> otherAnswerBoxes;

    public WeatherAnswerBox(String data)
    {
        super(data);
        this.initValues();
    }

    private void initValues()
    {
        this.showingFirstTemperature = true;

        this.place = super.extractData(WeatherAnswerBox.PLACE_CODE);
        this.dateTime = super.extractData(WeatherAnswerBox.DATE_TIME_CODE);
        this.weatherState = super.extractData(WeatherAnswerBox.WEATHER_STATE_CODE);

        this.temperature = super.extractData(WeatherAnswerBox.TEMPERATURE_CODE);
        this.temperature1 = super.extractData(WeatherAnswerBox.TEMPERATURE1_CODE);

        this.precipitationPercentage = super.extractData(WeatherAnswerBox.PRECIPITATION_PERCENTAGE_CODE);
        this.humidityPercentage = super.extractData(WeatherAnswerBox.HUMIDITY_PERCENTAGE_CODE);
        this.windSpeed = super.extractData(WeatherAnswerBox.WIND_SPEED_CODE);


        this.imageUri = this.extractURL(super.extractData(IMAGE_URI_CODE));
        this.extractUnits();

    }

    private String extractURL(String data)
    {
        String[] sep = data.split(MoConstants.SPACE);
        for(int i = 0;i<sep.length;i++){
            if(sep[i].startsWith(SOURCE_CODE)){
                String[] sepp = sep[i].split("\"");
                return "https:" + sepp[1];
            }
        }
        return "";
    }



    private void extractUnits()
    {
        super.setDivisions(new String[]{"\""});
        ArrayList<String> units = super.extractDataList(UNIT_TEMPERATURE);
        if(units.size()==2){
            this.temperatureUnit = units.get(0).substring(0,2);
            this.temperatureUnit1 = units.get(1).substring(0,2);
        }
    }

    @Override
    public String toString() {
        return "WeatherAnswerBox{" + "\n"+
                "place='" + place + '\n' +
                ", dateTime='" + dateTime + '\n' +
                ", weatherState='" + weatherState + '\n' +
                ", temperature='" + temperature + '\n' +
                ", temperature1='" + temperature1 + '\n' +
                ", temperatureUnit='" + temperatureUnit + '\n' +
                ", temperatureUnit1='" + temperatureUnit1 + '\n' +
                ", showingFirstTemperature=" + showingFirstTemperature +
                ", precipitationPercentage='" + precipitationPercentage + '\n' +
                ", humidityPercentage='" + humidityPercentage + '\n' +
                ", windSpeed='" + windSpeed + '\n' +
                ", imageUri='" + imageUri + '\n' +
                '}';
    }

    @Override
    public void getResult() {
        System.out.print(this.toString());
        //System.out.print("Weather = " + this.temperature);


//        final View view = MoView.getView(R.layout.conversation_weather,super.getContext());
//
//        CardView cardView = view.findViewById(R.id.Conversation_Weather_CardView);
//
//        TextView temperatureTV = view.findViewById(R.id.Conversation_Weather_Temperature);
//        temperatureTV.setText(this.temperature);
//
//        TextView temperatureUnitTV = view.findViewById(R.id.Conversation_Weather_Temperature_Unit);
//        temperatureUnitTV.setText(this.temperatureUnit);
//
//
//        // making it so that when the click on the
//        // text view, it changes the temperature shown
//        // to another
//        // e.g. celsius to farhenhite
//        // or vice versa
//        temperatureTV.setOnClickListener(v -> {
//            if(showingFirstTemperature)
//            {
//                showingFirstTemperature = false;
//                temperatureTV.setText(this.temperature1);
//                temperatureUnitTV.setText(this.temperatureUnit1);
//            }else{
//                showingFirstTemperature = true;
//                temperatureTV.setText(this.temperature);
//                temperatureUnitTV.setText(this.temperatureUnit);
//            }
//        });
//
//
//        TextView placeTV = view.findViewById(R.id.Conversation_Weather_Place);
//        placeTV.setText(this.place);
//
//        TextView dateTimeTV = view.findViewById(R.id.Conversation_Weather_DateTime);
//        dateTimeTV.setText(this.dateTime);
//
//        TextView stateTV = view.findViewById(R.id.Conversation_Weather_State);
//        stateTV.setText(this.weatherState);
//
//        TextView precipitationTV = view.findViewById(R.id.Conversation_Weather_Percitipation);
//        precipitationTV.setText("Precipitation: "+this.precipitationPercentage);
//
//        TextView humidityTV = view.findViewById(R.id.Conversation_Weather_Humidity);
//        humidityTV.setText("Humidity: "+this.humidityPercentage);
//
//        TextView windTV = view.findViewById(R.id.Conversation_Weather_WindSpeed);
//        windTV.setText("Wind: "+this.windSpeed);
//
//        ImageView imageView = view.findViewById(R.id.Conversation_Weather_Image);
//
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getContext()).build();
//        imageLoader.init(imageLoaderConfiguration);
//
//
//
//        imageLoader.loadImage(imageUri, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view1, Bitmap loadedImage) {
//
//
//                /// we are getting the dominant color of the
//                /// image just so that we can make the
//                /// results more stylish
//                imageView.setImageBitmap(loadedImage);
////                Palette palette = MoPalette.createPaletteSync(loadedImage);
////                Palette.Swatch swatch = palette.getDominantSwatch();
//                int backgroundColor = ContextCompat.getColor(getContext(),
//                R.color.white);
////                if(swatch!=null){
////                     backgroundColor = swatch.getRgb();
////                }
//
//
//                /// setting the background of the view to
//                /// the dominant color of the bitmap image
//                /// !!! this should be changed to the background of the card
//                /// not the view
//
//                //backgroundColor = ColorUtils.setAlphaComponent(backgroundColor,15);
//                GradientDrawable shape =  new GradientDrawable();
//                shape.setCornerRadius( 20);
//                //shape.setStroke(2,ContextCompat.getColor(WeatherAnswerBox.super.getContext(),R.color.black));
//                shape.setColor(backgroundColor);
//                cardView.setBackground(shape);
//                cardView.setElevation(5f);
//                //cardView.setMaxCardElevation(20f);
//
//
//
//
//
//                /// showing the result
//                /// since everything has been loaded and now
//                /// we are ready to show the current weather to the user
//                Conversation conversation = new Conversation();
//                conversation.setView(view);
//                Reply.reply(conversation);
//
//            }
//        });

    }





}
