package groupe1.il3.app.gui.mainframe;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MainFrameController {

    private final Builder<Region> viewBuilder;

    public MainFrameController() {
        this.viewBuilder = new MainFrameBuilder();
    }

    public Region getView() {
        return this.viewBuilder.build();
    }
}
