package groupe1.il3.app;

import groupe1.il3.app.gui.mainframe.MainFrameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        Scene root = new Scene(new MainFrameController().getView());

        primaryStage.setScene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }
}
