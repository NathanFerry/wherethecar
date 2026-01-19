package groupe1.il3.app.gui.admin;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;
import java.util.UUID;

public class AdminPanelController {

    private final AdminPanelModel model;
    private final AdminPanelInteractor interactor;
    private final Builder<Region> viewBuilder;

    public AdminPanelController() {
        this.model = new AdminPanelModel();
        this.interactor = new AdminPanelInteractor();
        this.viewBuilder = new AdminPanelViewBuilder(
            model,
            this::loadVehicles,
            this::loadAgents,
            this::addVehicle,
            this::editVehicle,
            this::deleteVehicle,
            this::addAgent,
            this::editAgent,
            this::deleteAgent,
            this::loadPendingReservations,
            this::approveReservation,
            this::cancelReservation
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void loadVehicles() {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<List<Vehicle>> task = interactor.createLoadVehiclesTask();

        task.setOnSucceeded(event -> {
            model.vehiclesProperty().clear();
            model.vehiclesProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors du chargement des véhicules: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void loadAgents() {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<List<Agent>> task = interactor.createLoadAgentsTask();

        task.setOnSucceeded(event -> {
            model.agentsProperty().clear();
            model.agentsProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors du chargement des agents: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void addVehicle(Vehicle vehicle) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.createVehicleTask(vehicle);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Véhicule ajouté avec succès");
            loadVehicles();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors de l'ajout du véhicule: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void editVehicle(Vehicle vehicle) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.updateVehicleTask(vehicle);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Véhicule modifié avec succès");
            loadVehicles();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors de la modification du véhicule: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void deleteVehicle(UUID vehicleUuid) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.deleteVehicleTask(vehicleUuid);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Véhicule supprimé avec succès");
            loadVehicles();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors de la suppression du véhicule: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void addAgent(Agent agent) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.createAgentTask(agent);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Agent ajouté avec succès");
            loadAgents();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors de l'ajout de l'agent: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void editAgent(Agent agent) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.updateAgentTask(agent);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Agent modifié avec succès");
            loadAgents();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors de la modification de l'agent: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void deleteAgent(UUID agentUuid) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.deleteAgentTask(agentUuid);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Agent supprimé avec succès");
            loadAgents();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors de la suppression de l'agent: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void loadPendingReservations() {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<List<Reservation>> task = interactor.createLoadPendingReservationsTask();

        task.setOnSucceeded(event -> {
            model.pendingReservationsProperty().clear();
            model.pendingReservationsProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors du chargement des réservations en attente: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void approveReservation(UUID reservationUuid, UUID vehicleUuid) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.approveReservationTask(reservationUuid, vehicleUuid);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Réservation approuvée avec succès");
            loadPendingReservations();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors de l'approbation de la réservation: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void cancelReservation(UUID reservationUuid) {
        model.setErrorMessage("");
        model.setSuccessMessage("");

        Task<Void> task = interactor.cancelReservationTask(reservationUuid);

        task.setOnSucceeded(event -> {
            model.setSuccessMessage("Réservation refusée avec succès");
            loadPendingReservations();
        });

        task.setOnFailed(event -> {
            model.setErrorMessage("Erreur lors du refus de la réservation: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}
