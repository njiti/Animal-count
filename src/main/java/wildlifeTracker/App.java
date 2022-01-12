package wildlifeTracker;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;


public class App {


    public static void main(String[] args) {
        staticFileLocation("/public");
        String layout = "templates/layout.vtl";


        ProcessBuilder process = new ProcessBuilder();
        int port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }
        port(port);

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/index.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());
        get("/animals/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("Top Notch Health", Endangered.HEALTHY);
            model.put("Neither 100% Nor Ill", Endangered.OKAY);
            model.put("Very Sick", Endangered.ILL);
            model.put("Child", Endangered.NEWBORN);
            model.put("MiddleAged", Endangered.YOUNG);
            model.put("Adult", Endangered.ADULT);
            model.put("rangerName", request.session().attribute("rangerName"));
            model.put("template", "templates/animal-form.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/animals/new", (request, response) -> {
            String name = request.queryParams("name");
            boolean endangered = request.queryParamsValues("endangered") != null;
            if (endangered) {
                String health = request.queryParams("health");
                String age = request.queryParams("age");
                Endangered endangeredAnimal = new Endangered(name, age, health);
                endangeredAnimal.save();
            } else {
                NonEndangered notEndangered = new NonEndangered(name);
                notEndangered.save();
            }
            response.redirect("/animals");
            return null;
        });

        get("/animals", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("nonendangered", NonEndangered.all());
            model.put("endangered", Endangered.all());
            model.put("template", "templates/animals.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/sightings/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            List<Endangered> animals = Endangered.all();
            model.put("animals", animals);
            model.put("template", "templates/sightings-form.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        post("/sightings/new", (request, response) ->{
            Map<String, Object> model = new HashMap<>();
            Endangered animal = Endangered.find(Integer.parseInt(request.queryParams("animalid")));
            String location = request.queryParams("location");
            String rangername= request.queryParams("rangername");
            Sightings newSighting = new Sightings(location, rangername, animal.getId());
            newSighting.save();
            model.put("template", "templates/sightings.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/sightings", (request, response) ->{
            Map<String, Object> model = new HashMap<>();
            model.put("sightings", Sightings.all());
            model.put("template", "templates/sightings.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());
    }
}
