package groupe1.il3.app.gui.navigation;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class NavigationViewBuilder implements Builder<Region> {
    @Override
    public Region build() {
        VBox navigationPane = new VBox();

        navigationPane.setPrefWidth(200);
        navigationPane.getChildren().add(this.generateShowVehiculesListButton());
        navigationPane.getChildren().add(this.generateShowAgentReservationsButton());

        return navigationPane;
    }

    private Button generateShowVehiculesListButton() {
        return new Button("Véhicules");
    }

    private Button generateShowAgentReservationsButton() {
        return new Button("Mes réservations");
    }
}
