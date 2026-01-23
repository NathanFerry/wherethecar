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
    private final Runnable showHistoryAction;
    private final Runnable showAdminPanelAction;
    private final Runnable onLogout;

    public MainFrameViewBuilder(MainFrameModel model, Runnable showVehicleListAction, Runnable showReservationsAction, Runnable showHistoryAction, Runnable showAdminPanelAction, Runnable onLogout) {
        this.model = model;
        this.showVehicleListAction = showVehicleListAction;
        this.showReservationsAction = showReservationsAction;
        this.showHistoryAction = showHistoryAction;
        this.showAdminPanelAction = showAdminPanelAction;
        this.onLogout = onLogout;
    }

    @Override
    public Region build() {
        BorderPane panel = new BorderPane();
        panel.setPrefSize(1200, 800);

        this.setupHeader(panel);
        this.setupNavigation(panel);
        this.setupContentListener(panel);
        this.showInitialView();

        return panel;
    }

    private void setupHeader(BorderPane panel) {
        HeaderController headerController = new HeaderController(onLogout);
        panel.setTop(headerController.getView());
    }

    private void setupNavigation(BorderPane panel) {
        panel.setLeft(this.navigationPanel());
    }

    private void setupContentListener(BorderPane panel) {
        model.centerContentProperty().addListener((obs, oldVal, newVal) -> {
            panel.setCenter(newVal);
        });
    }

    private void showInitialView() {
        showVehicleListAction.run();
    }

    private Region navigationPanel() {
        VBox navPanel = new VBox(10);
        navPanel.setPrefWidth(200);
        navPanel.getStyleClass().add("navigation-panel");

        Button vehiclesBtn = this.createVehiclesButton();
        Button reservationsBtn = this.createReservationsButton();
        Button historyBtn = this.createHistoryButton();
        Button adminBtn = this.createAdminButton();

        navPanel.getChildren().addAll(vehiclesBtn, reservationsBtn, historyBtn, adminBtn);

        return navPanel;
    }

    private Button createVehiclesButton() {
        Button vehiclesBtn = this.navigationButton("Liste des véhicules");
        vehiclesBtn.setOnAction(e -> showVehicleListAction.run());
        return vehiclesBtn;
    }

    private Button createReservationsButton() {
        Button reservationsBtn = this.navigationButton("Mes réservations");
        reservationsBtn.setOnAction(e -> showReservationsAction.run());
        return reservationsBtn;
    }

    private Button createHistoryButton() {
        Button historyBtn = this.navigationButton("Historique");
        historyBtn.setOnAction(e -> showHistoryAction.run());
        return historyBtn;
    }

    private Button createAdminButton() {
        Button adminBtn = this.navigationButton("Administration");
        adminBtn.setOnAction(e -> showAdminPanelAction.run());
        this.configureAdminButtonVisibility(adminBtn);
        return adminBtn;
    }

    private void configureAdminButtonVisibility(Button adminBtn) {
        boolean isAdmin = this.isCurrentUserAdmin();
        adminBtn.setVisible(isAdmin);
        adminBtn.setManaged(isAdmin);
    }

    private boolean isCurrentUserAdmin() {
        return SessionManager.getInstance().getCurrentAgent() != null &&
               SessionManager.getInstance().getCurrentAgent().isAdmin();
    }

    private Button navigationButton(String title) {
        Button btn = new Button(title);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("nav-button");
        return btn;
    }
}
