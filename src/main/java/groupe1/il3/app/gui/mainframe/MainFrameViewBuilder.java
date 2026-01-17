package groupe1.il3.app.gui.mainframe;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class MainFrameViewBuilder implements Builder<Region> {

    private final MainFrameModel model;
    private final Runnable showVehicleListAction;
    private final Runnable showReservationsAction;

    public MainFrameViewBuilder(MainFrameModel model, Runnable showVehicleListAction, Runnable showReservationsAction) {
        this.model = model;
        this.showVehicleListAction = showVehicleListAction;
        this.showReservationsAction = showReservationsAction;
    }

    @Override
    public Region build() {
        BorderPane panel = new BorderPane();
        panel.setPrefSize(1200, 800);

        panel.setLeft(this.navigationPanel());

        // Bind center content to model property
        model.centerContentProperty().addListener((obs, oldVal, newVal) -> {
            panel.setCenter(newVal);
        });

        // Show vehicle list by default
        showVehicleListAction.run();

        return panel;
    }

    private Region navigationPanel() {
        VBox navPanel = new VBox(10);
        navPanel.setPrefWidth(200);

        Button vehiclesBtn = this.navigationButton("Liste des véhicules");
        vehiclesBtn.setOnAction(e -> showVehicleListAction.run());

        Button reservationsBtn = this.navigationButton("Mes réservations");
        reservationsBtn.setOnAction(e -> showReservationsAction.run());

        navPanel.getChildren().addAll(vehiclesBtn, reservationsBtn);

        return navPanel;
    }

    private Button navigationButton(String title) {
        Button btn = new Button(title);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }
}
