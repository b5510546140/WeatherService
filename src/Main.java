
import view.Gui;

import com.cdyne.ws.weatherws.Weather;
import com.cdyne.ws.weatherws.WeatherReturn;
import com.cdyne.ws.weatherws.WeatherSoap;



public class Main {

	public static void main(String[] args) {

//		Weather factory = new Weather();
//		WeatherSoap proxy = factory.getWeatherSoap();
//		String zip = "01901";
//		WeatherReturn response =  proxy.getCityWeatherByZIP(zip);
//		System.out.println(response.getCity());
//		System.out.println(response.getDescription());
//		System.out.println(response.getPressure());
//		System.out.println(response.getState());
		Gui gui = new Gui();
		gui.run();
	
	}

}
