package gameviews;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * This class is the implementation of Assign Strategy to players in the Risk.
 *@see JDialog
 */
public class AssignPlayerStrategy extends JDialog{

	private static final long serialVersionUID = 1L;
    JLabel[] players ;
    public JComboBox<?> [] strategies; 
    JButton enterBtn;

    int width = 350;
    int height =600;
    
    /**
     * Constructor of class AssignPlayerStrategy
     * @param playerNum the number of players
     */
	public AssignPlayerStrategy(int playerNum){
        setTitle("Assign players strategy");
        height = 100+playerNum*40;
        setSize(width,height);
        
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-width)/2, (screenHeight-height)/2);
        setResizable(false);
        setLayout(null);
        setModal(true);
        
        //set exit program when close the window
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        //not capable adjust windows size
        Dimension size;    
    	players = new JLabel[playerNum];
    	strategies = new JComboBox<?>[playerNum];
    	String [] strategy = new String[]{"Human","Aggressive","Benevolent","Random","Cheater"};
    	
    	for (int i=0;i<playerNum;i++){
    		players[i] = new JLabel("Choose player"+(i+1)+"'s strategy:");
    		add(players[i]);
    		players[i].setFont(new java.awt.Font("dialog",1,12));
            size = players[i].getPreferredSize();
            players[i].setBounds(15,13+i*40,size.width,size.height);
            strategies[i] = new JComboBox<String>(strategy);
            add(strategies[i]);
            size = strategies[i].getPreferredSize();
            strategies[i].setBounds(200,10+i*40,size.width,size.height);
        }
        
        enterBtn = new JButton("Confirm");
        add(enterBtn);
        size = enterBtn.getPreferredSize();
        
        enterBtn.setBounds((width-size.width)/2,20+playerNum*40,size.width,size.height);
        enterBtn.addActionListener(new ButtonHandler());  
        setVisible(true);
    }  
	
	/**
	 * Class to define action Listener.
	 * @see ActionListener
	 */
	private class ButtonHandler implements ActionListener { 
		/**
		 * Method to define action performed according to different users' action.
		 * @param e the action event of user.
		 */	
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	}	
	
}
