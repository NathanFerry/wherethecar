package groupe1.il3.app.gui.mainframe;

import groupe1.il3.app.gui.widgets.placeholders.PlaceHolderPane;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Builder;

public class MainFrameBuilder implements Builder<Region> {

    public MainFrameBuilder() {

    }

    @Override
    public Region build() {
        BorderPane panel = new BorderPane();

        panel.setPrefSize(800, 600);
        panel.setLeft(PlaceHolderPane.generate(Color.rgb(255,0,0), 100, 100));

        return panel;
    }
}