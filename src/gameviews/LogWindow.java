package gameviews;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * 
 * Class to define log window
 * @see JFrame
 *
 */

public class LogWindow extends JFrame implements Observer{
	private static final long serialVersionUID = 1L;
	JTextArea outputArea;
	/**
	 * Constructor of class
	 */
	public LogWindow (){
		outputArea = new JTextArea(10,10);  
		outputArea.setEditable(false);
		outputArea.setLineWrap(true);
		Dimension windowSize =  java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setTitle("Log window");
		setSize(windowSize.width-10,windowSize.height-600);
		//setLocation(windowSize.width+5,565);
		setLocation(5,565);        
		//set exit program when close the window
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        //not capable adjust windows size
        //setResizable(false);
		setResizable(false);  
		setLayout(null); 
    
		JScrollPane outputPane= new JScrollPane(outputArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(outputPane);  
		outputPane.setBounds(5,5,windowSize.width-37,windowSize.height-648);
		setVisible(true);
	}
	/**
	 * Method to be called by Observable's notifyObservers method.
	 * @param arg0 the observable object
	 * @param arg1 an argument passed by the notifyObservers method.
	 */	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		outputArea.append((String)arg1);
		outputArea.setCaretPosition(outputArea.getText().length());
	}
}
