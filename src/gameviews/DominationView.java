package gamecontroller;


import gameelements.Player;
import gameelements.Players;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by liarthur on 03/11/2017.
 */
public class DominationView implements Observer{
    private Players players;

    /**
     * This is the consturtor of class DominationView
     * @param newPlayers The players are in the game
     */
    public DominationView(Players newPlayers){

        this.players = newPlayers;

        JFrame frame = new JFrame();
        frame.getContentPane().add(new DominationChart(players));
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

        Slice[] slice;
        DominationChart(Players players) {
            int sliceNumber = players.getPlayers().size();
            int i = 0;
            slice = new Slice[sliceNumber];
            for (Player player:players.getPlayers()) {
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
        players = (Players) ob;

        JFrame frame = new JFrame();
        frame.getContentPane().add(new DominationChart(players));
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
