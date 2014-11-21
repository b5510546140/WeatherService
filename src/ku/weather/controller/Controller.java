package ku.weather.controller;
/**
 * This class connect to the webservice.
 * @author wat wattanagaroon
 * @version 2014/11/15
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.xml.ws.WebServiceException;

import jdk.nashorn.internal.ir.ContinueNode;

import com.cdyne.ws.weatherws.Weather;
import com.cdyne.ws.weatherws.WeatherReturn;
import com.cdyne.ws.weatherws.WeatherSoap;

public class Controller {
	private WeatherSoap proxy;
	private WeatherReturn response;
	private Timer timer;
	public Controller() {
		create();
	}
/**
 * This method create factory and proxy.
 * It will try to create again and again until it can create.
 */
	public void create(){
		System.out.println("Create");
		while(true){
			try{
			setTimeout(5000);
			Weather factory = new Weather();
			proxy = factory.getWeatherSoap();
			timer.stop();
			break;
			}catch(WebServiceException e){
				System.out.println("catch the ");

				int choose = dialog();
			    if (choose == JOptionPane.CLOSED_OPTION || choose == JOptionPane.NO_OPTION) {
			    	System.exit(0);
			    	break;
				}
			    timer.stop();
			}
		}
	}
	
	/**
	 * The dialog show the error and ask user to want toretry again or exit it
	 * @return number of user choose for retry and exit
	 */
	private int dialog(){
		timer.stop();
		String[] options = { "Retry","Exit" } ;
        int choose = JOptionPane.showOptionDialog( 
                                null, "The device is not ready",
                                "Device Error",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                options,
                                options[1] );
        
        return choose;
	}
	/**
	 * set time out of the response if after timeout the method will show dialog
	 * @param timeout time to timeout
	 */
	private void setTimeout(int timeout) {
		timer = new Timer(timeout, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("time out already");
				int choose = dialog();
			    if (choose == JOptionPane.CLOSED_OPTION || choose == JOptionPane.NO_OPTION) {
			    	System.exit(0);

				}else{
					//this for user click retry but it is time out
				}
			    timer.stop();
			}
		});
		timer.start();
	}
	/**
	 * set string of zip code
	 * @param zip string of zip
	 * @return response
	 */
	public UniversalWeatherRespose sendZip(String zip){
		response =  proxy.getCityWeatherByZIP(zip);
		return response;
	}
	
}
