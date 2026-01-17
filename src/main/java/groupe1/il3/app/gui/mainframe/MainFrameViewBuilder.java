package groupe1.il3.app.gui.mainframe;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class MainFrameViewBuilder implements Builder<Region> {

    private MainFrameModel model;

    public MainFrameViewBuilder(MainFrameModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        BorderPane panel = new BorderPane();
        panel.setPrefSize(1200, 800);

        panel.setLeft(this.navigationPanel());

        return panel;
    }

    private Region navigationPanel() {
        VBox navPanel = new VBox();
        navPanel.setPrefWidth(200);

        navPanel.getChildren().add(this.navigationButton("Liste des véhicules"));
        navPanel.getChildren().add(this.navigationButton("Mes reservations"));

        return navPanel;
    }

    private Button navigationButton(String title) {
        Button btn = new Button(title);
        return btn;
    }
}