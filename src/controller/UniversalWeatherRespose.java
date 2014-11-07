package controller;

public interface UniversalWeatherRespose {

    /**
     * Gets the value of the success property.
     * 
     */
    public boolean isSuccess();

    /**
     * Gets the value of the responseText property.
     * 
     * @
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseText();


    /**
     * Gets the value of the state property.
     * 
     * @
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState();


    /**
     * Gets the value of the city property.
     * 
     * @
     *     possible object is
     *     {@link String }
     *     
     */
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
