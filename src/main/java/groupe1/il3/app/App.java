package groupe1.il3.app;

import groupe1.il3.app.gui.login.LoginController;
import groupe1.il3.app.gui.mainframe.MainFrameController;
import groupe1.il3.app.gui.style.StyleApplier;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        showLoginScreen();

        primaryStage.setTitle("WhereTheCar");
        primaryStage.show();
    }

    private void showLoginScreen() {
        LoginController loginController = new LoginController(agent -> {
            showMainFrame();
        });

        Scene loginScene = new Scene(loginController.getView());
        StyleApplier.applyStylesheets(loginScene);
        primaryStage.setScene(loginScene);
    }

    private void showMainFrame() {
        MainFrameController mainFrameController = new MainFrameController();
        Scene mainScene = new Scene(mainFrameController.getView());
        StyleApplier.applyStylesheets(mainScene);
        primaryStage.setScene(mainScene);
    }
}
