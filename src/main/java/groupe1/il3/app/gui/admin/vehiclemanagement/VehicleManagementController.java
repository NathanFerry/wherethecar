package groupe1.il3.app.gui.admin.vehiclemanagement;

import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class VehicleManagementController {

    private final VehicleManagementModel model;
    private final VehicleManagementInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final BiConsumer<String, String> messageHandler;

    public VehicleManagementController(BiConsumer<String, String> messageHandler) {
        this.model = new VehicleManagementModel();
        this.interactor = new VehicleManagementInteractor();
        this.messageHandler = messageHandler;
        this.viewBuilder = new VehicleManagementViewBuilder(
            model,
            this::loadVehicles,
            this::addVehicle,
            this::editVehicle,
            this::deleteVehicle,
            messageHandler
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void loadVehicles() {
        messageHandler.accept("", "");

        Task<List<Vehicle>> task = interactor.createLoadVehiclesTask();

        task.setOnSucceeded(event -> {
            model.vehiclesProperty().clear();
            model.vehiclesProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors du chargement des véhicules: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void addVehicle(Vehicle vehicle) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.createVehicleTask(vehicle);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Véhicule ajouté avec succès");
            loadVehicles();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de l'ajout du véhicule: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void editVehicle(Vehicle vehicle) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.updateVehicleTask(vehicle);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Véhicule modifié avec succès");
            loadVehicles();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de la modification du véhicule: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void deleteVehicle(UUID vehicleUuid) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.deleteVehicleTask(vehicleUuid);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Véhicule supprimé avec succès");
            loadVehicles();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de la suppression du véhicule: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}

