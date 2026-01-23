package groupe1.il3.app.gui.history;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.SessionManager;
import groupe1.il3.app.domain.reservation.Reservation;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class HistoryController {
    private final HistoryInteractor interactor;
    private final Builder<Region> viewBuilder;

    public HistoryController() {
        HistoryModel model = new HistoryModel();
        this.interactor = new HistoryInteractor(model);
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

        Task<List<Reservation>> task = new Task<>() {
            @Override
            protected List<Reservation> call() {
                return interactor.loadHistory(currentAgent.uuid());
            }
        };

        task.setOnSucceeded(event -> {
            interactor.updateModelWithReservations(task.getValue());
        });

        task.setOnFailed(event -> {
            System.err.println("Failed to load history: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}

