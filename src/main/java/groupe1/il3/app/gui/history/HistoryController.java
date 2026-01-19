package groupe1.il3.app.gui.history;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.SessionManager;
import groupe1.il3.app.domain.reservation.Reservation;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class HistoryController {
    private final HistoryModel model;
    private final HistoryInteractor interactor;
    private final Builder<Region> viewBuilder;

    public HistoryController() {
        this.model = new HistoryModel();
        this.interactor = new HistoryInteractor();
        this.viewBuilder = new HistoryViewBuilder(model, this::loadHistory);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void loadHistory() {
        Agent currentAgent = SessionManager.getInstance().getCurrentAgent();

        if (currentAgent == null) {
            System.err.println("No agent logged in");
            return;
        }

        model.setLoading(true);
        Task<List<Reservation>> task = interactor.createLoadHistoryTask(currentAgent.getUuid());

        task.setOnSucceeded(event -> {
            model.setLoading(false);
            model.reservationsProperty().clear();
            model.reservationsProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            model.setLoading(false);
            System.err.println("Failed to load history: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}

