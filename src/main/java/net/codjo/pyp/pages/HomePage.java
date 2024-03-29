package net.codjo.pyp.pages;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.services.BrinService;
import net.codjo.pyp.services.CsvService;
import net.codjo.pyp.services.GithubServiceAgi;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.StringResourceStream;
import org.eclipse.egit.github.core.PullRequest;
/**
 *
 */
public class HomePage extends RootPage {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    public HomePage() {
        GithubServiceAgi service = GithubServiceAgi.getGithubService(this);

        try {
            add(new DataView<PullRequest>("brinList",
                                          new ListDataProvider<PullRequest>(service.buildPullRequestList())) {
                @Override
                protected void populateItem(Item<PullRequest> listItem) {
                    final PullRequest brin = listItem.getModelObject();
                    listItem.add(new Label("project", "Project to be filled "));
                    listItem.add(new Label("title", brin.getTitle()));
                    listItem.add(new Label("creationDate", formatDate(brin.getCreatedAt())));
                    listItem.add(new Label("status", brin.getState().toString()));

/*
                    Link editionLink = new Link("editBrin") {
                        @Override
                        public void onClick() {
                            responseWithEdit(brin, false);
                        }
                    };
                    editionLink.add(new ExternalImage("editBrinLogo", "images/brin_form_edit.png"));
                    listItem.add(editionLink);
                    listItem.add(new AjaxEventBehavior("ondblclick") {
                        @Override
                        protected void onEvent(AjaxRequestTarget target) {
                            responseWithEdit(brin, false);
                        }
                    });
*/
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();  // Todo
        }
    }


    @Override
    protected void initRightPanel(String id) {
        add(new EmptyPanel(id));  
/*

        CallBack buttonCallBack = new CallBack() {
            public void onClickCallBack(Brin brin) {
                responseWithEdit(brin, true);
            }


            public String getLabel() {
                return "Add new BRIN";
            }


            public String getImagePath() {
                return "images/brin_form_add.png";
            }
        };

        CallBack exportCallBack = new CallBack() {
            public void onClickCallBack(Brin brin) {
                //TODO On pourrait creer un DownloadLink pour encapsuler ce comportement
                StringBuilder content = CsvService.export(BrinService.getBrinService(HomePage.this).getAllBrins());
                StringResourceStream stream = new StringResourceStream(content.toString(), "text/csv");
                ResourceStreamRequestTarget requestTarget = new ResourceStreamRequestTarget(stream);
                requestTarget.setFileName("ExportBrin.csv");
                getRequestCycle().setRequestTarget(requestTarget);
            }


            public String getLabel() {
                return "Export BRINs (csv)";
            }


            public String getImagePath() {
                return "images/export.png";
            }
        };

        add(new RightPanel(id, buttonCallBack, exportCallBack));
*/
    }


    @Override
    protected void initLeftPanel(String id) {
//        BrinService service = BrinService.getBrinService(this);
//        add(new LeftPanel(id, service.calculateBrinNumber(service.getAllBrins())));
        add(new EmptyPanel(id));
    }


    private void responseWithEdit(Brin brin, boolean creationMode) {
        if (creationMode) {
            setResponsePage(new BrinEditPage(brin, creationMode));
        }
        else {
            setResponsePage(BrinEditPage.class, new PageParameters("id=" + brin.getUuid()));
        }
    }


    private String formatDate(Date dateToFormat) {
        if (dateToFormat == null) {
            return "";
        }
        return format.format(dateToFormat);
    }


    public interface CallBack extends Serializable {
        void onClickCallBack(Brin brin);


        String getLabel();


        String getImagePath();
    }
}
