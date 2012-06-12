package net.codjo.pyp.pages;
import net.codjo.pyp.model.Status;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
/**
 *
 */
public class LeftPanel extends Panel {
    public LeftPanel(String id, final Map<Status, Integer> statusIntegerMap) {
        super(id);
        WebMarkupContainer myContainer = new WebMarkupContainer("leftContainer");
        add(myContainer);

        List<Status> callBackList = Arrays.asList(Status.values());

        myContainer.add(new ListView<Status>("infoList", callBackList) {
            @Override
            protected void populateItem(ListItem<Status> statusListItem) {
                Status status = statusListItem.getModelObject();
                statusListItem.add(new Label("statusLabel", status.toString()));
                statusListItem.add(new Label("nbBrin", statusIntegerMap.get(status).toString()));
            }
        });
    }
}
