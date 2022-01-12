package wildlifeTracker;
import org.sql2o.*;

import java.util.List;

public class Endangered extends Animals{
    private String health;
    private String age;

    public static final String HEALTHY = "Top Notch Health";
    public static final String ILL = "Neither 100% Nor Ill";
    public static final String OKAY = "Ver Sick";

    public static final String NEWBORN = "Child";
    public static final String YOUNG = "MiddleAged";
    public static final String ADULT = "Adult";

    public Endangered(String name, String health, String age) {
        this.name = name;
        this.health = health;
        this.age = age;
        endangered = true;
    }
    public String getName(){return name;}
    public String getHealth(){
        return health;
    }
    public String getAge(){
        return age;
    }

    @Override
    public void save(){
        super.save();
        try(Connection connect= DB.sql2o.open()){
            String sql = "UPDATE animals SET health=:health, age=:age WHERE id=:id";
            connect.createQuery(sql, true)
                    .addParameter("health", this.health)
                    .addParameter("age", this.age)
                    .addParameter("id", this.id)
                    .executeUpdate();
        }
    }
    public static List<Endangered> all(){
        String sql = "SELECT * FROM animals;";
        try(Connection connect= DB.sql2o.open()){
            return connect.createQuery(sql).executeAndFetch(Endangered.class);
        }
    }
    public static Endangered find(int id){
        try(Connection connect = DB.sql2o.open()){
            String sql = "SELECT * FROM animals WHERE id= :id;";
            Endangered animal = connect.createQuery(sql)
                    .addParameter("id", id)
                    .throwOnMappingFailure(false)
                    .executeAndFetchFirst(Endangered.class);
            if (animal == null) {
                throw new IndexOutOfBoundsException("Sorry, this animal cannot be found.");
            }
            return animal;
        }
    }
    public void update(String name, String health){
        try(Connection connect = DB.sql2o.open()){
            String sql = "UPDATE animals SET (name, health) = (:name, :health) WHERE id = :id;";
            connect.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("health", health)
                    .executeUpdate();
        }
    }
}
