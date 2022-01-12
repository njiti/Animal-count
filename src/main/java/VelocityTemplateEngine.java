import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Template Engine based on Apache Velocity.
 */
public class VelocityTemplateEngine<VelocityEngine> extends TemplateEngine {

    private final VelocityEngine velocityEngine;

    /**
     * Constructor
     */
    public VelocityTemplateEngine() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty(
                "class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine = (VelocityEngine) new org.apache.velocity.app.VelocityEngine(properties);
    }

    /**
     * Constructor
     *
     * @param velocityEngine The velocity engine, must not be null.
     */
    public VelocityTemplateEngine(VelocityEngine velocityEngine) {
        if (velocityEngine == null) {
            throw new IllegalArgumentException("velocityEngine must not be null");
        }
        this.velocityEngine = velocityEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(ModelAndView modelAndView) {
        Template template = velocityEngine.getTemplate(modelAndView.getViewName());
        Object model = modelAndView.getModel();
        if (model instanceof Map) {
            Map<?, ?> modelMap = (Map<?, ?>) model;
            VelocityContext context = new VelocityContext(modelMap);
            StringWriter writer = new StringWriter();
            try {
                template.merge(context, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writer.toString();
        } else {
            throw new IllegalArgumentException("modelAndView must be of type java.util.Map");
        }
    }

}