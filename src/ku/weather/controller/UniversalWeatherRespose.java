package ku.weather.controller;
/**
 * This use for adapter class all of response have to have this method
 * @author Aof
 *
 */
public interface UniversalWeatherRespose {

    public boolean isSuccess();

    public String getResponseText();

    public String getState();

    public String getCity();

    public String getWeatherStationCity();
    
    public short getWeatherID();

    public String getDescription();
    
    public String getTemperature();
    
    public String getRelativeHumidity();
    
    public String getWind();

    public String getPressure();

    public String getVisibility();

    public String getWindChill();

    public String getRemarks();


}
