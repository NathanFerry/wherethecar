package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.authentication.SessionManager;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class VehicleSelectorController {
    private final VehicleSelectorModel model;
    private final VehicleSelectorInteractor interactor;
    private final Builder<Region> viewBuilder;

    public VehicleSelectorController() {
        this.model = new VehicleSelectorModel();
        this.interactor = new VehicleSelectorInteractor();
        this.viewBuilder = new VehicleSelectorViewBuilder(model, this::loadVehicles, this::reserveVehicle);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void loadVehicles() {
        Task<List<Vehicle>> task = interactor.createLoadVehiclesTask();

        task.setOnSucceeded(event -> {
            model.vehiclesProperty().clear();
            model.vehiclesProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            System.err.println("Failed to load vehicles: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void reserveVehicle() {
        model.setReservationErrorMessage("");

        if (model.getSelectedVehicle() == null) {
            model.setReservationErrorMessage("Veuillez sélectionner un véhicule");
            return;
        }

        if (model.getReservationStartDate() == null || model.getReservationEndDate() == null) {
            model.setReservationErrorMessage("Veuillez sélectionner les dates de début et de fin");
            return;
        }

        if (model.getReservationEndDate().isBefore(model.getReservationStartDate())) {
            model.setReservationErrorMessage("La date de fin doit être après la date de début");
            return;
        }

        Task<Boolean> task = interactor.createReservationTask(
            SessionManager.getInstance().getCurrentAgent().getUuid(),
            model.getSelectedVehicle().getUuid(),
            model.getReservationStartDate(),
            model.getReservationEndDate()
        );

        task.setOnSucceeded(event -> {
            if (task.getValue()) {
                model.setReservationErrorMessage("");
                loadVehicles(); // Reload vehicles to update status
            } else {
                model.setReservationErrorMessage("Ce véhicule est déjà réservé pour cette période");
            }
        });

        task.setOnFailed(event -> {
            model.setReservationErrorMessage("Erreur lors de la création de la réservation");
            System.err.println("Failed to create reservation: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}
