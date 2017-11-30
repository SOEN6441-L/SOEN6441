package gameviewsremote;

import javax.swing.*;

import java.awt.*;
import java.rmi.RemoteException;

/**
 * Class to display the domination status
 */
public class DominationViewRemote{
	JFrame frame;

	/**
	 * This is the constructor of class DominationView
	 */
	public DominationViewRemote(){      
    	frame = new JFrame();
        //frame.getContentPane().add(new DominationChart(null);
        frame.setSize(300, 300);
        frame.setTitle("Domination View");
        int screenWidth = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        //setLocation(2*screenWidth-300,0);  
        frame.setLocation(screenWidth-300,0);  
        frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
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
		
        DominationChart(int num,int[] size, Color[] colors) {
            int sliceNumber = num;
            slice = new Slice[sliceNumber];
            for (int i=0;i<size.length;i++) {
                slice[i] = new Slice(size[i],colors[i]);
            }
        }

        /**
         * This method is to paint graph
         * @param g the graphic object
         */
        public void paint(Graphics g) {
            drawPie((Graphics2D) g, getBounds(), slice);
        }

        /**
         * This is for update the state of the char
         * @param g the graphic object
         * @param area rectangle area
         * @param slice slice array
         */
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
     * @param num number of players
     * @param size array of country size
     * @param colors the game object array of colors
     * @throws RemoteException remote error
     */
    public void update(int num,int[] size, Color[] colors) throws RemoteException {
   		frame.getContentPane().removeAll();   		
   		if (num>0) {
   			frame.getContentPane().add(new DominationChart(num,size,colors));
   			frame.getContentPane().validate();
   		}	
    }
}
