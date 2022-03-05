package create;

import objects.OneCallObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OneCall {
    private static final String URL = "https://api.openweathermap.org/data/2.5/onecall";
    private final String apiKey;
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

    public OneCall(String apiKey, double latitude, double longitude) {
        this.apiKey = apiKey;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public OneCall(String apiKey, double latitude, double longitude, String lang) {
        this.apiKey = apiKey;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lang = lang;
    }

    private String getJSON(String url1) {
        try {
            java.net.URL url = new URL(url1);
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

    public OneCallObject execute(){
        String urlParameters = urlParameters="?lat="+this.latitude+"&lon="+this.longitude+"&appid="+this.apiKey+"&units="+this.units+"&lang="+this.lang;
        JSONObject jsonObject = new JSONObject(getJSON(URL+urlParameters));
        if(jsonObject.toString().equals("{}")){
            return null;
        }
        OneCallObject o = new OneCallObject();
        o.setLat(jsonObject.getDouble("lat"));
        o.setLon(jsonObject.getDouble("lon"));
        o.setTimezone(jsonObject.getString("timezone"));
        o.setTimezone_offset(jsonObject.getInt("timezone_offset"));
        ArrayList<OneCallObject.Weather> cWeather = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONObject("current").getJSONArray("weather").length();i++){
            OneCallObject.Weather weather = new OneCallObject.Weather(
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getInt("id"),
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getString("main"),
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getString("description"),
                    jsonObject.getJSONObject("current").getJSONArray("weather").getJSONObject(i).getString("icon")
            );
            cWeather.add(weather);
        }
        o.setCurrent(new OneCallObject.Current(
                jsonObject.getJSONObject("current").getLong("dt"),
                jsonObject.getJSONObject("current").getLong("sunrise"),
                jsonObject.getJSONObject("current").getLong("sunset"),
                jsonObject.getJSONObject("current").getDouble("temp"),
                jsonObject.getJSONObject("current").getDouble("feels_like"),
                jsonObject.getJSONObject("current").getInt("pressure"),
                jsonObject.getJSONObject("current").getInt("humidity"),
                jsonObject.getJSONObject("current").getDouble("dew_point"),
                jsonObject.getJSONObject("current").getDouble("uvi"),
                jsonObject.getJSONObject("current").getInt("clouds"),
                jsonObject.getJSONObject("current").getInt("visibility"),
                jsonObject.getJSONObject("current").getInt("wind_speed"),
                jsonObject.getJSONObject("current").getInt("wind_deg"),
                cWeather
        ));
        ArrayList<OneCallObject.Minutely> minutelies = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("minutely").length();i++){
            OneCallObject.Minutely minutely = new OneCallObject.Minutely(
                    jsonObject.getJSONArray("minutely").getJSONObject(i).getLong("dt"),
                    jsonObject.getJSONArray("minutely").getJSONObject(i).getDouble("precipitation")
            );
            minutelies.add(minutely);
        }
        o.setMinutely(minutelies);
        ArrayList<ArrayList<OneCallObject.Weather>> hWeather = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("hourly").length();i++){
            ArrayList<OneCallObject.Weather> weather1 = new ArrayList<>();
            for (int j = 0; j < jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").length(); j++) {
                OneCallObject.Weather weather = new OneCallObject.Weather(
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getInt("id"),
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("main"),
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("description"),
                        jsonObject.getJSONArray("hourly").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("icon")
                );
                weather1.add(weather);
            }
            hWeather.add(weather1);
        }
        ArrayList<OneCallObject.Hourly> hourlies = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("hourly").length();i++){
            OneCallObject.Hourly hourly = new OneCallObject.Hourly(
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getLong("dt"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("temp"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("feels_like"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("pressure"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("humidity"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("dew_point"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("uvi"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("clouds"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("visibility"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("wind_speed"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("wind_deg"),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getDouble("wind_gust"),
                    hWeather.get(i),
                    jsonObject.getJSONArray("hourly").getJSONObject(i).getInt("pop")
            );
            hourlies.add(hourly);
        }
        o.setHourly(hourlies);
        ArrayList<ArrayList<OneCallObject.Weather>> dWeather = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("daily").length();i++){
            ArrayList<OneCallObject.Weather> weather1 = new ArrayList<>();
            for (int j = 0; j <jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").length(); j++) {
                OneCallObject.Weather weather = new OneCallObject.Weather(
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getInt("id"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("main"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("description"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getJSONArray("weather").getJSONObject(j).getString("icon")
                );
                weather1.add(weather);
            }
            dWeather.add(weather1);
        }
        ArrayList<OneCallObject.Daily> dailies = new ArrayList<>();
        for (int i = 0;i<jsonObject.getJSONArray("daily").length();i++){
            try {
                OneCallObject.Daily daily = new OneCallObject.Daily(
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("dt"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("moon_phase"),
                        new OneCallObject.Temp(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("min"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("max"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("morn")
                        ),
                        new OneCallObject.FeelsLike(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("morn")
                        ),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("pressure"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("humidity"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("dew_point"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("wind_speed"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("wind_deg"),
                        dWeather.get(i),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("clouds"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("pop"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("rain"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("uvi")
                );
                dailies.add(daily);
            }catch (Exception e){
                OneCallObject.Daily daily = new OneCallObject.Daily(
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("dt"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("sunset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonrise"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getLong("moonset"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("moon_phase"),
                        new OneCallObject.Temp(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("min"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("max"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("temp").getDouble("morn")
                        ),
                        new OneCallObject.FeelsLike(
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("day"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("night"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("eve"),
                                jsonObject.getJSONArray("daily").getJSONObject(i).getJSONObject("feels_like").getDouble("morn")
                        ),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("pressure"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("humidity"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("dew_point"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("wind_speed"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("wind_deg"),
                        dWeather.get(i),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getInt("clouds"),
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("pop"),
                        0,
                        jsonObject.getJSONArray("daily").getJSONObject(i).getDouble("uvi")
                );
                dailies.add(daily);
            }
        }
        o.setDaily(dailies);
        try {
            ArrayList<OneCallObject.Alerts> alerts = new ArrayList<>();
            ArrayList<ArrayList<String>> tags = new ArrayList<>();
            for (int i = 0;i<jsonObject.getJSONArray("alerts").length();i++){
                ArrayList<String> tag = new ArrayList<>();
                for (int j = 0;j<jsonObject.getJSONArray("alerts").getJSONObject(i).getJSONArray("tags").length();j++){
                    tag.add(jsonObject.getJSONArray("alerts").getJSONObject(i).getJSONArray("tags").getString(j));
                }
                tags.add(tag);
            }
            for (int i = 0;i<jsonObject.getJSONArray("alerts").length();i++){
                OneCallObject.Alerts alert = new OneCallObject.Alerts(
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getString("sender_name"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getString("event"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getLong("start"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getLong("end"),
                        jsonObject.getJSONArray("alerts").getJSONObject(i).getString("description"),
                        tags.get(i)
                );
                alerts.add(alert);
            }
            o.setAlerts(alerts);
        }catch (Exception e){
            o.setAlerts(null);
        }
        return o;
    }

    @Override
    public String toString() {
        return "OneCall{" +
                "apiKey='" + apiKey + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", units='" + units + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }
}
