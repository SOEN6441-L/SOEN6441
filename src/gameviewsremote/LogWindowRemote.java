package gameviewsremote;

import java.awt.Dimension;
import java.rmi.RemoteException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * Class to define log window
 * @see JFrame
 */

public class LogWindowRemote extends JFrame{
	private static final long serialVersionUID = 1L;
	JTextArea outputArea;
	/**
	 * Constructor of class
	 */
	public LogWindowRemote (){
		outputArea = new JTextArea(10,10);  
		outputArea.setEditable(true);
		outputArea.setLineWrap(true);
		Dimension windowSize =  java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setTitle("Log window");
		setSize(windowSize.width-10,windowSize.height-610);
		//setLocation(windowSize.width+5,565);
		setLocation(5,575);        
		//set exit program when close the window
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        //not capable adjust windows size
        //setResizable(false);
		setResizable(false);  
		setLayout(null); 
    
		JScrollPane outputPane= new JScrollPane(outputArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(outputPane);  
		outputPane.setBounds(5,5,windowSize.width-25,windowSize.height-648);
		setVisible(true);
	}
	
	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param newLog new log string.
	 * @throws RemoteException remote error
	 */	
	public void update(String newLog) throws RemoteException{
		// TODO Auto-generated method stub
		outputArea.append((String)newLog);
		outputArea.setCaretPosition(outputArea.getText().length());
	}
}
