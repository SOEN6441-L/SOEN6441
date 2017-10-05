package risk.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by liarthur on 2017/10/5.
 */
public class DoDrawing {
    public void doDrawing(GraphicsContext gc) {

        gc.setStroke(Color.FORESTGREEN.brighter());
        gc.setLineWidth(1);
        gc.strokeOval(30, 30, 80, 80);
        gc.setFill(Color.FORESTGREEN);
        gc.fillOval(130, 30, 80, 80);
    }
}
