package groupe1.il3.app;

import groupe1.il3.app.gui.login.LoginController;
import groupe1.il3.app.gui.mainframe.MainFrameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Show login screen
        showLoginScreen();

        primaryStage.setTitle("WhereTheCar");
        primaryStage.show();
    }

    /**
     * Displays the login screen.
     */
    private void showLoginScreen() {
        LoginController loginController = new LoginController(agent -> {
            // On successful login, show the main frame
            showMainFrame();
        });

        Scene loginScene = new Scene(loginController.getView());
        primaryStage.setScene(loginScene);
    }

    /**
     * Displays the main application frame.
     */
    private void showMainFrame() {
        MainFrameController mainFrameController = new MainFrameController();
        Scene mainScene = new Scene(mainFrameController.getView());
        primaryStage.setScene(mainScene);
    }
}
