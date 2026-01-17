package groupe1.il3.app.gui.reservations;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.SessionManager;
import groupe1.il3.app.domain.reservation.Reservation;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class ReservationsController {
    private final ReservationsModel model;
    private final ReservationsInteractor interactor;
    private final Builder<Region> viewBuilder;

    public ReservationsController() {
        this.model = new ReservationsModel();
        this.interactor = new ReservationsInteractor();
        this.viewBuilder = new ReservationsViewBuilder(model, this::loadReservations);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void loadReservations() {
        Agent currentAgent = SessionManager.getInstance().getCurrentAgent();

        if (currentAgent == null) {
            System.err.println("No agent logged in");
            return;
        }

        model.setLoading(true);
        Task<List<Reservation>> task = interactor.createLoadReservationsTask(currentAgent.getUuid());

        task.setOnSucceeded(event -> {
            model.setLoading(false);
            model.getReservations().clear();
            model.getReservations().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            model.setLoading(false);
            System.err.println("Failed to load reservations: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}
