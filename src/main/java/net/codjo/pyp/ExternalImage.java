package net.codjo.pyp;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
/**
 * TODO copie from magic
 */
public class ExternalImage extends Image {
    public ExternalImage(String s, String url) {
        super(s);
        add(new AttributeModifier("src", true, new Model<String>(url)));
    }
}
