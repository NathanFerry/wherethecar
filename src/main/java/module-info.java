module wherethecar.main {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires org.postgresql.jdbc;
    requires java.sql;

    opens groupe1.il3.app to javafx.graphics, javafx.fxml;
    exports groupe1.il3.app;
}