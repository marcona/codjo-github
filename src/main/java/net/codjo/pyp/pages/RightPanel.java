package net.codjo.pyp.pages;
import net.codjo.pyp.ExternalImage;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.pages.HomePage.CallBack;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
/**
 *
 */
public class RightPanel extends Panel {
    WebMarkupContainer myContainer;


    public RightPanel(String id, final CallBack... buttonCallBack) {
        super(id);
        myContainer = new WebMarkupContainer("myContainer");
        add(myContainer);

        List<CallBack> callBackList = Arrays.asList(buttonCallBack);
        myContainer.add(new ListView<CallBack>("menuList", callBackList) {
            @Override
            protected void populateItem(ListItem<CallBack> callBackListItem) {
                addLink(callBackListItem, callBackListItem.getModelObject());
            }
        });
    }


    private void addLink(ListItem<CallBack> callBackListItem, final CallBack buttonCallBack) {
        Link link = new Link("imageLink") {
            @Override
            protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                getResponse().write(buttonCallBack.getLabel());
                super.onComponentTagBody(markupStream, openTag);
            }


            @Override
            public void onClick() {
                buttonCallBack.onClickCallBack(new Brin());
            }
        };
        link.add(new ExternalImage("imageLogo", buttonCallBack.getImagePath()));
        callBackListItem.add(link);
    }
}

