package groupe1.il3;

import groupe1.il3.app.App;
import groupe1.il3.app.persistence.DatabaseConfig;
import groupe1.il3.app.persistence.DatabaseConnectionManager;
import javafx.application.Application;

public class Launcher {
    static void main(String[] args) {
        //TODO: FOR TESTING PURPOSES ONLY, REMOVE BEFORE DEPLOYMENT
        DatabaseConnectionManager.getInstance().configure(
                DatabaseConfig.createLocal(
                        "wherethecar",
                        "wherethecar",
                        "wherethecar"
                )
        );

        Application.launch(App.class, args);
    }
}
