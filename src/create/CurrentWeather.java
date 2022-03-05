package create;

import objects.CurrentWeatherObjects;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CurrentWeather {
    private static final String URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String apiKey;
    private String cityName;
    private double latitude;
    private double longitude;

    private String units = "standard";
    private String lang = "EN";

    public void setUnitStandard(){
        this.units="standard";
    }
    public void setUnitMetric(){
        this.units="metric";
    }
    public void setUnitImperial(){
        this.units="imperial";
    }

    public String getUnits() {
        return units;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public CurrentWeather(String apiKey, String cityName) {
        this.apiKey = apiKey;
        this.cityName = cityName;
    }

    public CurrentWeather(String apiKey, double latitude, double longitude) {
        this.apiKey = apiKey;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private String getJSON(String url1) {
        try {
            URL url = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                String inLine;
                StringBuilder result = new StringBuilder();
                while ((inLine=in.readLine())!=null){
                    result.append(inLine);
                }
                return result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CurrentWeatherObjects execute(){
        String urlParameters = "";
        if(this.cityName==null){
            urlParameters="?lat="+this.latitude+"&lon="+this.longitude+"&appid="+this.apiKey+"&units="+this.units+"&lang="+this.lang;
        }else {
            urlParameters="?q="+this.cityName+"&appid="+this.apiKey+"&units="+this.units+"&lang="+this.lang;
        }
        JSONObject jsonObject = new JSONObject(getJSON(URL+urlParameters));
        if(jsonObject.toString().equals("{}")){
            return null;
        }
        CurrentWeatherObjects currentWeatherObjects = new CurrentWeatherObjects();
        currentWeatherObjects.setCoord(new CurrentWeatherObjects.Coord(
                jsonObject.getJSONObject("coord").getDouble("lon")
                ,jsonObject.getJSONObject("coord").getDouble("lat")));
        ArrayList<CurrentWeatherObjects.Weather> weathers = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("weather").length();i++){
            CurrentWeatherObjects.Weather weather = new CurrentWeatherObjects.Weather(
                    jsonObject.getJSONArray("weather").getJSONObject(i).getLong("id"),
                    jsonObject.getJSONArray("weather").getJSONObject(i).getString("main"),
                    jsonObject.getJSONArray("weather").getJSONObject(i).getString("description"),
                    jsonObject.getJSONArray("weather").getJSONObject(i).getString("icon")
                    );
            weathers.add(weather);
        }
        currentWeatherObjects.setWeather(weathers);
        currentWeatherObjects.setBase(jsonObject.getString("base"));
        currentWeatherObjects.setMain(new CurrentWeatherObjects.Main(
                jsonObject.getJSONObject("main").getDouble("temp"),
                jsonObject.getJSONObject("main").getDouble("feels_like"),
                jsonObject.getJSONObject("main").getDouble("temp_min"),
                jsonObject.getJSONObject("main").getDouble("temp_max"),
                jsonObject.getJSONObject("main").getInt("pressure"),
                jsonObject.getJSONObject("main").getInt("humidity")
        ));
        currentWeatherObjects.setVisibility(jsonObject.getInt("visibility"));
        currentWeatherObjects.setWind(new CurrentWeatherObjects.Wind(
                jsonObject.getJSONObject("wind").getDouble("speed"),
                jsonObject.getJSONObject("wind").getInt("deg")
        ));
        currentWeatherObjects.setClouds(new CurrentWeatherObjects.Clouds(
                jsonObject.getJSONObject("clouds").getInt("all")
        ));
        currentWeatherObjects.setDt(jsonObject.getLong("dt"));
        try {
            currentWeatherObjects.setSys(new CurrentWeatherObjects.Sys(
                    jsonObject.getJSONObject("sys").getInt("type"),
                    jsonObject.getJSONObject("sys").getLong("id"),
                    0.0,
                    jsonObject.getJSONObject("sys").getString("country"),
                    jsonObject.getJSONObject("sys").getLong("sunrise"),
                    jsonObject.getJSONObject("sys").getLong("sunset")
            ));
        }catch (Exception e){
            currentWeatherObjects.setSys(new CurrentWeatherObjects.Sys(
                    0,
                    0,
                    0.0,
                    jsonObject.getJSONObject("sys").getString("country"),
                    jsonObject.getJSONObject("sys").getLong("sunrise"),
                    jsonObject.getJSONObject("sys").getLong("sunset")
            ));
        }
        currentWeatherObjects.setTimezone(jsonObject.getInt("timezone"));
        currentWeatherObjects.setId(jsonObject.getLong("id"));
        currentWeatherObjects.setName(jsonObject.getString("name"));
        currentWeatherObjects.setCod(jsonObject.getInt("cod"));
        return currentWeatherObjects;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "apiKey='" + apiKey + '\'' +
                ", cityName='" + cityName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
