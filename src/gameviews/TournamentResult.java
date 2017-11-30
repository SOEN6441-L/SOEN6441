package gameviews;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;



/**
 * This class is to show the result of tournament.
 *@see JDialog
 */
public class TournamentResult extends JDialog{

	private static final long serialVersionUID = 1L;

	//components in this window
    JLabel[] maps;
    JLabel[] games;
    JLabel[][] results;
    
    int width = 350;
    int height =600;
    
    /**
     * Constructor of class TournamentResult
     * @param mapstr maps
     * @param tourGameNum game number
     * @param result results of tournament
     */
	public TournamentResult(String[] mapstr, int tourGameNum, String[][] result){
        setTitle("Tournament result");
        setResizable(false);
        setLayout(null);
        setModal(true);
        //set exit program when close the window
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        //not capable adjust windows size
        Dimension size; 
        maps = new JLabel[mapstr.length];
        games = new JLabel[tourGameNum];
        results = new JLabel[maps.length][tourGameNum];
        
        int maxSize = 0;
        for (int i = 0;i<mapstr.length;i++){
        	maps[i] = new JLabel(mapstr[i]);
        	maps[i].setFont(new java.awt.Font("dialog",1,15));
        	add(maps[i]);
        	size = maps[i].getPreferredSize();
        	if (size.width>maxSize) maxSize = size.width;
        	maps[i].setBounds(15,55+i*40,size.width,size.height);
        }
        
        for (int j = 0;j<tourGameNum;j++){
        	games[j] = new JLabel("Game"+(j+1));
        	games[j].setFont(new java.awt.Font("dialog",1,15));
        	add(games[j]);
        	size = games[j].getPreferredSize();
        	games[j].setBounds(15+maxSize+20+(size.width+20)*j,15,size.width+20,size.height);       	
        }
        
        for (int i = 0;i<mapstr.length;i++){
        	for (int j = 0;j<tourGameNum;j++){
        		results[i][j] = new JLabel(result[i][j]);
        		results[i][j].setFont(new java.awt.Font("dialog",1,12));
        		add(results[i][j]);
        		size = results[i][j].getPreferredSize();
        		results[i][j].setBounds(games[j].getBounds().x+(games[j].getSize().width-20-size.width)/2,56+i*40,size.width,size.height);
        	}
        }	
        
        width = games[0].getSize().width*tourGameNum+maxSize+50;
        height = 90+40*mapstr.length;
        setSize(width,height);
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        setLocation((screenWidth-width)/2, (screenHeight-height)/2);
   
        setVisible(true);
    } 	
}
