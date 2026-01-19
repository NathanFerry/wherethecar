package groupe1.il3.app.gui.admin.maintenancemanagement;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class MaintenanceManagementModel {

    private final ListProperty<MaintenanceOperation> maintenanceOperations = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<MaintenanceOperation> selectedOperation = new SimpleObjectProperty<>();

    public ListProperty<MaintenanceOperation> maintenanceOperationsProperty() {
        return maintenanceOperations;
    }

    public ObjectProperty<MaintenanceOperation> selectedOperationProperty() {
        return selectedOperation;
    }

    public MaintenanceOperation getSelectedOperation() {
        return selectedOperation.get();
    }

    public void setSelectedOperation(MaintenanceOperation operation) {
        selectedOperation.set(operation);
    }
}
