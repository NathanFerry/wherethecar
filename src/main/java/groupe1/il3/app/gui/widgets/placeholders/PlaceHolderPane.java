package groupe1.il3.app.gui.widgets.placeholders;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class PlaceHolderPane {
    public static Pane generate(Color color, int prefWidth, int prefHeight) {
        Pane pane = new Pane();

        pane.setPrefSize(prefWidth, prefHeight);
        BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(backgroundFill));

        return pane;
    }
}
