package net.codjo.pyp.pages;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.pages.HomePage.CallBack;
import net.codjo.pyp.services.BrinService;
import java.io.IOException;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
public class BrinEditPage extends RootPage {

    public BrinEditPage() throws IOException {
        BrinService service = BrinService.getBrinService(this);
        String idString = getRequest().getParameterMap().get("id")[0];
        Brin brin = service.getBrin(idString);
        if (brin == null) {
            setResponsePage(new HomePage());
        }
        else {
            buildPage(brin, false);
        }
    }


    public BrinEditPage(Brin brin, boolean creationMode) {
        buildPage(brin, creationMode);
    }


    @Override
    protected void initRightPanel(String id) {
        CallBack buttonCallBack = new CallBack() {
            public void onClickCallBack(Brin brin) {
                setResponsePage(new HomePage());
            }


            public String getLabel() {
                return "Back to the List";
            }


            public String getImagePath() {
                return "../../images/backToList.png";
            }
        };

        add(new RightPanel(id, buttonCallBack));
    }


    private void buildPage(Brin brin, boolean creationMode) {
        add(new FeedbackPanel("feedback").setOutputMarkupId(true));

        add(new BrinForm("brinForm", brin, creationMode));
    }
}
