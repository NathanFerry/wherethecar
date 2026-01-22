package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.authentication.SessionManager;
import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class VehicleSelectorController {
    private final VehicleSelectorInteractor interactor;
    private final Builder<Region> viewBuilder;

    public VehicleSelectorController() {
        VehicleSelectorModel model = new VehicleSelectorModel();
        this.interactor = new VehicleSelectorInteractor(model);
        this.viewBuilder = new VehicleSelectorViewBuilder(model, this::loadVehicles, this::reserveVehicle, this::loadVehicleReservations);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void loadVehicles() {
        Task<List<Vehicle>> task = new Task<>() {
            @Override
            protected List<Vehicle> call() {
                return interactor.loadVehicles();
            }
        };

        task.setOnSucceeded(event -> {
            interactor.updateModelWithVehicles(task.getValue());
        });

        task.setOnFailed(event -> {
            System.err.println("Failed to load vehicles: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void loadVehicleReservations() {
        Task<List<Reservation>> task = new Task<>() {
            @Override
            protected List<Reservation> call() {
                if (!interactor.hasSelectedVehicle()) {
                    return null;
                }
                return interactor.loadVehicleReservations(interactor.getSelectedVehicleUuid());
            }
        };

        task.setOnSucceeded(event -> {
            if (task.getValue() != null) {
                interactor.updateModelWithReservations(task.getValue());
            } else {
                interactor.clearReservations();
            }
        });

        task.setOnFailed(event -> {
            System.err.println("Failed to load vehicle reservations: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void reserveVehicle() {
        if (!interactor.validateReservation()) {
            return;
        }

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return interactor.createReservation(
                    SessionManager.getInstance().getCurrentAgent().uuid(),
                    interactor.getSelectedVehicleUuid(),
                    interactor.getReservationStartDate(),
                    interactor.getReservationEndDate()
                );
            }
        };

        task.setOnSucceeded(event -> {
            if (task.getValue()) {
                interactor.setReservationSuccess();
                loadVehicles();
                loadVehicleReservations();
            } else {
                interactor.setReservationError("Ce véhicule est déjà réservé pour cette période");
            }
        });

        task.setOnFailed(event -> {
            interactor.setReservationError("Erreur lors de la création de la réservation");
            System.err.println("Failed to create reservation: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}
