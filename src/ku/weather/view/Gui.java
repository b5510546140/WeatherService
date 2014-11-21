package ku.weather.view;
/**
 * 
 * @author wat wattanagaroon
 * @version 2014/9/10
 */
import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JProgressBar;
import javax.xml.ws.WebServiceException;

import ku.weather.controller.Controller;
import ku.weather.controller.UniversalWeatherRespose;


public class Gui extends JFrame{
	private JTextField textField;
	private Controller controller;
	private JTable tablePane;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;
	private Working work;
	private JProgressBar progressBar;
	private boolean isRun = false;
	private boolean isCancle = false;
	private JButton submitButt;
//	private UniversalWeatherRespose weatherResponse;
	
	public Gui() {
		super("Zip code for weather");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("GUI start");

			controller = new Controller();
		System.out.println("out controller");
		JPanel panel = new JPanel();
		panel.setLayout(null);
		getContentPane().add(panel);
		
		submitButt = new JButton("Submit");
		submitButt.setBounds(257, 53, 117, 29);
		panel.add(submitButt);
		
		JLabel lblNewLabel_1 = new JLabel("Zip code of US only!");
		lblNewLabel_1.setBounds(110, 0, 166, 34);
		panel.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(56, 52, 189, 28);
		panel.add(textField);
		textField.setColumns(10);
		
		String col[] = {"Type","Data"};
		tableModel = new DefaultTableModel(col,0);
		tablePane = new JTable(tableModel);
		scrollPane = new JScrollPane(tablePane);
		scrollPane.setBounds(56, 121, 318, 107);
		panel.add(scrollPane);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(66, 92, 146, 20);
		panel.add(progressBar);
		
		JLabel lblProgress = new JLabel("Progress");
		lblProgress.setBounds(227, 93, 61, 16);
		panel.add(lblProgress);
		
		submitButt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isRun){// false gonna work
					String zipcode = textField.getText().toString();	
					work = new Working( zipcode );
					progressBar.setValue(0);
					clearLocationData();
					work.execute();
					System.out.println("Work execute");
					submitButt.setText("Cancel");
					isRun = true;
					isCancle = false;
				}
				else{// cancel
					submitButt.setText("Submit");
					progressBar.setValue(0); 
					isRun = false;
					isCancle = true;
					work.cancel(true);
				}
				
			}
		});
	}
	/**
	 * If it catch it will come to this method and notify 
	 */
	private void NotiEror() {
		JOptionPane.showMessageDialog(null, "No internet Connection \n ","Error",JOptionPane.ERROR_MESSAGE);
		setEnabled(false);
	}
	
	
	/**
	 * Clear the data displayed about a location
	 */
	private void clearLocationData() {
		tableModel.setRowCount(0);
	}
	
	/**
	 * result of soap will show
	 */
	private void displayLocationDetail(UniversalWeatherRespose weatherResponse){
		try{
			tableModel.setRowCount(0);
			progressBar.setValue(80);
	
			System.out.println("is cancle ="+isCancle);
			
			if (weatherResponse == null) {
				// nothing to update.  TableModel is empty.
				setButtonToDefault();
				isCancle = false;
			}
			else if ( isCancle )
			{
					System.out.println("Cancelled");
					setButtonToDefault();
					isCancle = false;
			}
			else {
				if(weatherResponse.isSuccess()){
					String[][] array = manageArray(weatherResponse);
					for (String[] u: array) {
					    	tableModel.addRow(u);
					}
					isRun = false;
					progressBar.setValue(100);
					submitButt.setText("Submit");
				}
				else{
					setButtonToDefault();
					JOptionPane.showMessageDialog(null, "No Result ,\n Webservice of close,\n Time out","Error",JOptionPane.ERROR_MESSAGE);
				}
	
			}
		}catch (WebServiceException e){
			NotiEror();
		}catch(NullPointerException e){
			System.out.println("catch null!!!");
			setButtonToDefault();
			JOptionPane.showMessageDialog(null, "No internet connection","Error",JOptionPane.ERROR_MESSAGE);
			
		}
	}
	/**
	 * Set the button to submit and set progressbar to 0
	 */
	private void setButtonToDefault(){
		isRun = false;
		progressBar.setValue(0);
		submitButt.setText("Submit");
	}
	/**
	 * set the array to get topic
	 * @param response the response from web service
	 * @return array after managae array
	 */
	private String[][] manageArray( UniversalWeatherRespose response){
		String[][] array = new String[11][2];
		array[0][0] = "State";
		array[0][1] = response.getState();
		array[1][0] = "City";
		array[1][1] = response.getCity();
		array[2][0] = "Pressure";
		array[2][1] = response.getPressure();
		array[3][0] = "Humidity";
		array[3][1] = response.getRelativeHumidity();
		array[4][0] = "Temperature";
		array[4][1] = response.getTemperature();		
		array[5][0] = "Wind";
		array[5][1] = response.getWind();
		array[6][0] = "Description";
		array[6][1] = response.getDescription();
		return array;
	}
	
	public void run() {
		setSize(new Dimension(450, 350));
		setVisible(true);
	}
	
	class Working extends SwingWorker<UniversalWeatherRespose, String>{
		
		private String postalcode;

		public Working(String postalcode) {
			this.postalcode = postalcode;
		}

		@Override
		protected UniversalWeatherRespose doInBackground() throws Exception {
			progressBar.setValue(50);
			UniversalWeatherRespose response = controller.sendZip(postalcode);
			return response;
		}

		@Override
		protected void done() {
			try {
				UniversalWeatherRespose response = get();
				System.err.println("Worker response = " + response);
				displayLocationDetail( response );
			} catch (Exception e) {
				System.err.println("SwingWorker.get() threw exceptin: " + e);
				displayLocationDetail( null );
			}
			
			super.done();
		}
		
	}
}
