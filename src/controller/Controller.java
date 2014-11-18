package controller;

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

	public void create(){
		System.out.println("Create");
		while(true){
			try{
			setTimeout(5000);
			System.out.println("settime");
			Weather factory = new Weather();
			proxy = factory.getWeatherSoap();
			timer.stop();
			break;
			}catch(WebServiceException e){
				System.out.println("catch the ");
//				String[] options = { "Retry","Exit" } ;
//		        int choose = JOptionPane.showOptionDialog( 
//		                                null, "The device is not ready",
//		                                "Device Error",
//		                                JOptionPane.YES_NO_OPTION,
//		                                JOptionPane.ERROR_MESSAGE,
//		                                null,
//		                                options,
//		                                options[1] );
				int choose = dialog();
			    if (choose == JOptionPane.CLOSED_OPTION || choose == JOptionPane.NO_OPTION) {
			    	System.exit(0);
			    	break;
				}
			}
		}
	}
	
	private int dialog(){
		String[] options = { "Retry","Exit" } ;
        int choose = JOptionPane.showOptionDialog( 
                                null, "The device is not ready",
                                "Device Error",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE,
                                null,
                                options,
                                options[1] );
//	    if (choose == JOptionPane.CLOSED_OPTION || choose == JOptionPane.NO_OPTION) {
//	    	System.exit(0);
//	    	break;
//		}
        return choose;
	}
	
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
	
	public UniversalWeatherRespose sendZip(String zip){
		response =  proxy.getCityWeatherByZIP(zip);
		return response;
	}
	
	public UniversalWeatherRespose getWeatherReturn(){
		return response;
	}
}
