package controller;

import com.cdyne.ws.weatherws.Weather;
import com.cdyne.ws.weatherws.WeatherReturn;
import com.cdyne.ws.weatherws.WeatherSoap;

public class Controller {
	private WeatherSoap proxy;
	private WeatherReturn response;

	public Controller() {
		Weather factory = new Weather();
		proxy = factory.getWeatherSoap();
	}
	
	public UniversalWeatherRespose sendZip(String zip){
		response =  proxy.getCityWeatherByZIP(zip);
		return response;
	}
	
	public UniversalWeatherRespose getWeatherReturn(){
		return response;
	}
}
