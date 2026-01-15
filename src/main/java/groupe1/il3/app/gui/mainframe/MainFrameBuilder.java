package groupe1.il3.app.gui.mainframe;

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
        panel.setLeft(this.createTestColoredRegion(new Color(1,1,1,1)));

        return panel;
    }

    //TODO add color attribute support
    private Region createTestColoredRegion(Color color) {
        Pane pane = new Pane();
        pane.setPrefWidth(100);
        pane.setStyle("-fx-background-color: #FF0000;");
        return pane;
    }
}