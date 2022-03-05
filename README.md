# OpenWeatherMapApi

Java библиотека для использвания https://openweathermap.org

## Как использовать?

### Call current weather data 

#### Создать класс CurrentWeather:

   CurrentWeather currentWeather = new CurrentWeather(API_KEY,CITY_NAME);
   
   CurrentWeather currentWeather = new CurrentWeather(API_KEY,latitude,longitude);
   
#### При желаний можно изменить язык, по стандарту английский https://openweathermap.org/current#multi

   currentWeather.setLang("RU");
   
#### Можно изменить тип измерений:

  currentWeather.setUnitMetric();
  
  currentWeather.setUnitImperial();
  
  currentWeather.setUnitStandard();

#### Создать класс CurrentWeatherObjects:

  CurrentWeatherObjects currentWeatherObjects = currentWeather.execute();
 
 CurrentWeatherObjects загрузил все данные о погоде по методу currentWeather.execute();
 
 ![image](https://user-images.githubusercontent.com/74965405/156893018-f14cd25d-4657-4077-949f-88fd55d6804a.png)


### Current and forecast weather data
Current weather

Minute forecast for 1 hour

Hourly forecast for 48 hours

Daily forecast for 7 days

National weather alerts

#### Создать класс CurrentWeather:

   OneCall oneCall = new OneCall(API_KEY,latitude,longitude);
   
#### При желаний можно изменить язык, по стандарту английский https://openweathermap.org/current#multi

   oneCall.setLang("RU");
   
#### Можно изменить тип измерений:

  oneCall.setUnitMetric();
  
  oneCall.setUnitImperial()
  
  oneCall.setUnitStandard();

#### Создать класс OneCallObject:

  OneCallObject currentWeatherObjects = oneCall.execute();
  
OneCallObject загрузил все данные о погоде по методу oneCall.execute();

![image](https://user-images.githubusercontent.com/74965405/156893207-9b307aa3-5f83-4a1c-a35c-b2d45c1da9fe.png)
