package view;
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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import controller.Controller;
import controller.UniversalWeatherRespose;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JProgressBar;
import javax.xml.ws.WebServiceException;


public class Gui extends JFrame{
	private JTextField textField;
	private String input;
	private Controller controller;
	private JTable tablePane;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;
	private Working work;
	private JProgressBar progressBar;
	private boolean isRun = false;
	private JButton submitButt;
	private UniversalWeatherRespose weatherResponse;
	
	public Gui() {
		super("Zip code for weather");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try{

			controller = new Controller();
		} catch (WebServiceException e){

			NotiEror();
		}
		
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
//		tablePane.setBounds(56, 121, 318, 107);
		scrollPane = new JScrollPane(tablePane);
		scrollPane.setBounds(56, 121, 318, 107);
		panel.add(scrollPane);
//		panel.add(tablePane);
		
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
				progressBar.setValue(0);
				input = textField.getText().toString();	
				work = new Working();
				work.execute();
				submitButt.setText("Cancel");
				isRun = true;
				}
				else{// cancel
					submitButt.setText("Submit");
					progressBar.setValue(0);
					work.cancel(true);
					isRun = false;
				}
			System.out.println(isRun);
				
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
	 * result of soap will show
	 */
	private void response(){
		try{
		tableModel.setRowCount(0);
		progressBar.setValue(80);
		weatherResponse = controller.getWeatherReturn();
		if(weatherResponse.isSuccess()){
			String[][] array = manageArray();
			for (String[] u: array) {
			   
			    	tableModel.addRow(u);
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "No Result ,\n Webservice of close,\n Time out","Error",JOptionPane.ERROR_MESSAGE);
		}
//		if(controller.getList() != null){
//			Table[] list = controller.getList();
//			for(Table table : list){
//				String row[] = {table.getCity()};
//				tableModel.addRow(row);
//			}
//		}
//		else{
//			String result[] = {"No Result"};
//			tableModel.addRow(result);
//		}
		isRun = false;
		progressBar.setValue(100);
		submitButt.setText("Submit");
		}catch (WebServiceException e){
			NotiEror();
		}
	}
	
	private String[][] manageArray(){
		String[][] array = new String[11][2];
		array[0][0] = "State";
		array[0][1] = weatherResponse.getState();
		array[1][0] = "City";
		array[1][1] = weatherResponse.getCity();
		array[2][0] = "Pressure";
		array[2][1] = weatherResponse.getPressure();
		array[3][0] = "Humidity";
		array[3][1] = weatherResponse.getRelativeHumidity();
		array[4][0] = "Temperature";
		array[4][1] = weatherResponse.getTemperature();		
		array[5][0] = "Wind";
		array[5][1] = weatherResponse.getWind();
		array[6][0] = "Description";
		array[6][1] = weatherResponse.getDescription();
		return array;
	}
	
	public void run() {
		setSize(new Dimension(450, 350));
		setVisible(true);
	}
	
	class Working extends SwingWorker<List<String>, String>{

		@Override
		protected List<String> doInBackground() throws Exception {
			progressBar.setValue(50);
			controller.sendZip(input);
			return null;
		}

		@Override
		protected void done() {
			response();
			super.done();
		}
		
	}
}
