package groupe1.il3.app.gui.style;

import javafx.scene.Scene;
import javafx.scene.control.Dialog;

public class StyleApplier {

    private static final String[] STYLESHEETS = {
        "/styles/main.css",
        "/styles/header.css",
        "/styles/login.css",
        "/styles/navigation.css",
        "/styles/vehicles.css",
        "/styles/reservations.css",
        "/styles/admin.css"
    };

    private StyleApplier() {}

    public static void applyStylesheets(Scene scene) {
        if (scene != null) {
            for (String stylesheet : STYLESHEETS) {
                try {
                    var url = StyleApplier.class.getResource(stylesheet);
                    if (url != null) {
                        scene.getStylesheets().add(url.toExternalForm());
                    } else {
                        System.err.println("Warning: Could not find stylesheet: " + stylesheet);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading stylesheet " + stylesheet + ": " + e.getMessage());
                }
            }
        }
    }

    public static void applyStylesheets(Dialog<?> dialog) {
        if (dialog != null && dialog.getDialogPane() != null) {
            for (String stylesheet : STYLESHEETS) {
                try {
                    var url = StyleApplier.class.getResource(stylesheet);
                    if (url != null) {
                        dialog.getDialogPane().getStylesheets().add(url.toExternalForm());
                    } else {
                        System.err.println("Warning: Could not find stylesheet: " + stylesheet);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading stylesheet " + stylesheet + ": " + e.getMessage());
                }
            }
        }
    }
}

