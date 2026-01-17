package groupe1.il3.app.gui.mainframe;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;

public class MainFrameModel {
    private final ObjectProperty<Region> centerContent = new SimpleObjectProperty<>();

    public ObjectProperty<Region> centerContentProperty() {
        return centerContent;
    }

    public Region getCenterContent() {
        return centerContent.get();
    }

    public void setCenterContent(Region content) {
        centerContent.set(content);
    }
}


