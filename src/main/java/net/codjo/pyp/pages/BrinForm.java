package net.codjo.pyp.pages;
import net.codjo.pyp.model.Brin;
import net.codjo.pyp.model.Status;
import net.codjo.pyp.model.Team;
import net.codjo.pyp.model.UnblockingType;
import net.codjo.pyp.services.BrinService;
import net.codjo.pyp.services.MailService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
/**
 *
 */
public class BrinForm extends Form<Brin> {
    private Brin brin;
    private MailService mailService;
    private Status initialStatus;
    private boolean creationMode;


    public BrinForm(String formId, final Brin brin, boolean creationMode) {
        super(formId, new CompoundPropertyModel<Brin>(brin));
        this.brin = brin;
        this.creationMode = creationMode;
        initialStatus = brin.getStatus();
        mailService = new MailService(getContextUrl(((WebRequest)getRequest()).getHttpServletRequest()).toString());

        add(new TextField("title").setRequired(true));
        add(createDataField(brin, "creationDate").setRequired(true));

        TextArea description = new TextArea("description");
        add(description);

        add(createDataField(brin, "unblockingDate"));
        add(new TextArea("unblockingDescription"));

        add(new TextArea("rootCause"));

        final RadioGroup<UnblockingType> group = new RadioGroup<UnblockingType>("unblockingType");
        add(group);

        ListView<UnblockingType> radioList = new ListView<UnblockingType>("radios",
                                                                          Arrays.asList(UnblockingType.values())) {

            @Override
            protected void populateItem(ListItem<UnblockingType> item) {
                Radio<UnblockingType> radio = new Radio<UnblockingType>("radio", item.getModel(), group);
                radio.setLabel(new PropertyModel<String>(item.getModelObject(), "type"));
                item.add(radio);
                item.add(new SimpleFormComponentLabel("type", radio));
            }
        };
        group.add(radioList);

        final DropDownChoice<Status> dropDownChoice = new DropDownChoice<Status>("status",
                                                                                 Arrays.asList(Status.values()));
        add(dropDownChoice);

        //Teams
        CheckGroup<String> checks = new CheckGroup<String>("affectedTeams");
        add(checks);

        ListView<String> checksList = new ListView<String>("teams", buildTeamList()) {
            @Override
            protected void populateItem(ListItem<String> item) {
                Check<String> check = new Check<String>("check", item.getModel());
                check.setLabel(item.getModel());
                item.add(check);
                item.add(new SimpleFormComponentLabel("team", check));
            }


            ;
        }.setReuseItems(true);

        checks.add(checksList);

        SubmitLink submitLink = new SubmitLink("save", this);
        add(submitLink);
    }


    private List<String> buildTeamList() {
        Team[] values = Team.values();
        List<String> strTeams = new ArrayList<String>();
        for (Team value : values) {
            strTeams.add(value.getTeam());
        }
        return strTeams;
    }


    @Override
    public void onSubmit() {
        if (creationMode) {
            BrinService.getBrinService(this).addBrin(brin);
            sendMail(brin);
        }
        else {
            BrinService.getBrinService(this).updateBrin(brin);

            if (!initialStatus.equals(brin.getStatus())) {
                sendMail(brin);
            }
        }

        setResponsePage(new HomePage());
    }


    private void sendMail(Brin theBrin) {
        try {
            mailService.sendMail(theBrin);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private DateTextField createDataField(Brin theBrin, String dateFieldName) {
        return new DateTextField(dateFieldName,
                                 new PropertyModel<Date>(theBrin, dateFieldName), "yyyy-MM-dd");
    }


    private StringBuffer getContextUrl(final HttpServletRequest req) {
        String protocol = req.isSecure() ? "https://" : "http://";
        String hostname = req.getServerName();
        int port = req.getServerPort();
        StringBuffer url = new StringBuffer(128);
        url.append(protocol);
        url.append(hostname);
        if ((port != 80) && (port != 443)) {
            url.append(":");
            url.append(port);
        }
        String ctx = req.getSession().getServletContext().getContextPath();
        if (!ctx.startsWith("/")) {
            url.append('/');
        }
        url.append(ctx);
        if (!ctx.endsWith("/")) {
            url.append('/');
        }
        return url;
    }
}
