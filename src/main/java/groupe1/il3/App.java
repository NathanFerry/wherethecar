package groupe1.il3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label = new Label("Hello, World!");

        Scene root = new Scene(label, 400, 200);

        primaryStage.setScene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }
}
