package groupe1.il3.app.gui.reservations;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.SessionManager;
import groupe1.il3.app.domain.reservation.Reservation;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class ReservationsController {
    private final ReservationsInteractor interactor;
    private final Builder<Region> viewBuilder;

    public ReservationsController() {
        ReservationsModel model = new ReservationsModel();
        this.interactor = new ReservationsInteractor(model);
        this.viewBuilder = new ReservationsViewBuilder(model, this::loadReservations, this::returnVehicle);
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

        interactor.setLoading(true);

        Task<List<Reservation>> task = new Task<>() {
            @Override
            protected List<Reservation> call() {
                return interactor.loadReservations(currentAgent.uuid());
            }
        };

        task.setOnSucceeded(event -> {
            interactor.updateModelWithReservations(task.getValue());
        });

        task.setOnFailed(event -> {
            interactor.setLoading(false);
            System.err.println("Failed to load reservations: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void returnVehicle() {
        if (!interactor.validateReturn()) {
            return;
        }

        Reservation selectedReservation = interactor.getSelectedReservation();
        int newKilometers = interactor.getNewKilometers();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                interactor.returnVehicle(
                    selectedReservation.uuid(),
                    selectedReservation.vehicle().uuid(),
                    newKilometers
                );
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            interactor.clearReturnError();
            loadReservations();
        });

        task.setOnFailed(event -> {
            interactor.setReturnError("Erreur lors du retour du véhicule");
            System.err.println("Failed to return vehicle: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}
