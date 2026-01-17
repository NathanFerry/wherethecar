package groupe1.il3.app.gui.mainframe;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MainFrameController {

    private final Builder<Region> viewBuilder;
    private final MainFrameModel model;

    public MainFrameController() {
        this.model = new MainFrameModel();
        this.viewBuilder = new MainFrameViewBuilder(this.model);
    }

    public Region getView() {
        return this.viewBuilder.build();
    }


}
