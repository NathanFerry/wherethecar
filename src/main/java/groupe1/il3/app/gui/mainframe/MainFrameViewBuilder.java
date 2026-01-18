package groupe1.il3.app.gui.mainframe;

import groupe1.il3.app.domain.authentication.SessionManager;
import groupe1.il3.app.gui.header.HeaderController;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class MainFrameViewBuilder implements Builder<Region> {

    private final MainFrameModel model;
    private final Runnable showVehicleListAction;
    private final Runnable showReservationsAction;
    private final Runnable showAdminPanelAction;

    public MainFrameViewBuilder(MainFrameModel model, Runnable showVehicleListAction, Runnable showReservationsAction, Runnable showAdminPanelAction) {
        this.model = model;
        this.showVehicleListAction = showVehicleListAction;
        this.showReservationsAction = showReservationsAction;
        this.showAdminPanelAction = showAdminPanelAction;
    }

    @Override
    public Region build() {
        BorderPane panel = new BorderPane();
        panel.setPrefSize(1200, 800);

        HeaderController headerController = new HeaderController();
        panel.setTop(headerController.getView());

        panel.setLeft(this.navigationPanel());

        model.centerContentProperty().addListener((obs, oldVal, newVal) -> {
            panel.setCenter(newVal);
        });

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

        Button adminBtn = this.navigationButton("Administration");
        adminBtn.setOnAction(e -> showAdminPanelAction.run());

        boolean isAdmin = SessionManager.getInstance().getCurrentAgent() != null &&
                         SessionManager.getInstance().getCurrentAgent().isAdmin();
        adminBtn.setVisible(isAdmin);
        adminBtn.setManaged(isAdmin);

        navPanel.getChildren().addAll(vehiclesBtn, reservationsBtn, adminBtn);

        return navPanel;
    }

    private Button navigationButton(String title) {
        Button btn = new Button(title);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }
}
