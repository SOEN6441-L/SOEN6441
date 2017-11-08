package gameviews;



import javax.swing.*;

import gamemodels.PlayerModel;
import gamemodels.RiskGameModel;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by liarthur on 03/11/2017.
 */
public class DominationView implements Observer{
    private PlayerModel[] players;
    JFrame frame;

    /**
     * This is the constructor of class DominationView
     */
    public DominationView(){      
        frame = new JFrame();
        //frame.getContentPane().add(new DominationChart(null);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    /**
     * This is the slice class for the pie chart
     */
    class Slice {
        double value;
        Color color;

        public Slice(double value, Color color) {
            this.value = value;
            this.color = color;
        }
    }

    /**
     * The slices detail in chart class DominationChart
     */
    class DominationChart extends JComponent {
		private static final long serialVersionUID = 1L;
		Slice[] slice;
        DominationChart(PlayerModel[] players) {
            int sliceNumber = players.length;
            int i = 0;
            slice = new Slice[sliceNumber];
            for (PlayerModel player:players) {
                slice[i] = new Slice(player.getCountries().size(),player.getMyColor());
                i++;
            }
        }

        public void paint(Graphics g) {
            drawPie((Graphics2D) g, getBounds(), slice);
        }

        void drawPie(Graphics2D g, Rectangle area, Slice[] slice) {
            double total = 0.0D;

            for (int i = 0; i < slice.length; i++) {
                total += slice[i].value;
            }
            double curValue = 0.0D;
            int startAngle = 0;
            for (int i = 0; i < slice.length; i++) {
                startAngle = (int) (curValue * 360 / total);
                int arcAngle = (int) (slice[i].value * 360 / total);
                g.setColor(slice[i].color);
                g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);
                curValue += slice[i].value;
            }
        }
    }

    /**
     * This is for update the state of the chart
     * @param ob the notifying Observable from model
     * @param arg the notifying object from model
     */
    public void update(Observable ob, Object arg) {
    	if ((int)arg==111){
    		players = ((RiskGameModel) ob).getPlayers();
    		frame.getContentPane().removeAll();   		
    		if (players!=null&&players.length>0) {
    			frame.getContentPane().add(new DominationChart(players));
    			frame.getContentPane().validate();
    		}	
    	}	
        //frame.setSize(300, 300);
        //frame.setVisible(true);
    }
}
