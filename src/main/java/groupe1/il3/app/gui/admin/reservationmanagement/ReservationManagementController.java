package groupe1.il3.app.gui.admin.reservationmanagement;

import groupe1.il3.app.domain.reservation.Reservation;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ReservationManagementController {

    private final ReservationManagementInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final BiConsumer<String, String> messageHandler;

    public ReservationManagementController(BiConsumer<String, String> messageHandler) {
        ReservationManagementModel model = new ReservationManagementModel();
        this.interactor = new ReservationManagementInteractor(model);
        this.messageHandler = messageHandler;
        this.viewBuilder = new ReservationManagementViewBuilder(
            model,
            this::loadPendingReservations,
            this::approveReservation,
            this::cancelReservation
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void loadPendingReservations() {
        messageHandler.accept("", "");

        Task<List<Reservation>> task = new Task<>() {
            @Override
            protected List<Reservation> call() {
                return interactor.fetchPendingReservations();
            }
        };

        task.setOnSucceeded(event -> {
            interactor.updatePendingReservationsList(task.getValue());
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors du chargement des réservations en attente: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void approveReservation(UUID reservationUuid, UUID vehicleUuid) {
        messageHandler.accept("", "");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                interactor.approveReservation(reservationUuid, vehicleUuid);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Réservation approuvée avec succès");
            loadPendingReservations();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de l'approbation de la réservation: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void cancelReservation(UUID reservationUuid) {
        messageHandler.accept("", "");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                interactor.cancelReservation(reservationUuid);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Réservation refusée avec succès");
            loadPendingReservations();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors du refus de la réservation: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}

