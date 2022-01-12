package wildlifeTracker;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;

public class Sightings{
    private String location;
    private String rangerName;
    private Timestamp spotted;
    private int animalId;
    private int id;

    public Sightings(String location, String rangerName, int animalId) {
        this.location = location;
        this.rangerName = rangerName;
        this.animalId = animalId;
    }

    public String getLocation() {
        return location;
    }

    public String getRangerName() {
        return rangerName;
    }

    public Timestamp getSpotted() {
        return spotted;
    }

    public int getAnimalId() {
        return animalId;
    }

    public int getId() {
        return id;
    }

    public String getFormattedDate() {
        return DateFormat.getDateTimeInstance().format(spotted);
    }

    @Override
    public boolean equals(Object anotherSighting){
        if(!(anotherSighting instanceof Sightings)){
            return false;
        }
        else{
            Sightings newSighting = (Sightings) anotherSighting;
            return this.getId() == newSighting.getId() &&
                    this.getLocation().equals(newSighting.getLocation()) &&
                    this.getRangerName().equals(newSighting.getRangerName());
        }
    }

    public static List<Sightings> all(){
        String sql = "SELECT * FROM sightings;";
        try(Connection connect= DB.sql2o.open()){
            return connect.createQuery(sql).executeAndFetch(Sightings.class);
        }
    }
    public void save(){
        try(Connection connect= DB.sql2o.open()){
            String sql ="INSERT INTO sightings (location, rangerName, spotted, animalId) VALUES (:location, :rangerName, now(), :animalId)";
            this.id = (int) connect.createQuery(sql, true)
                    .addParameter("location", this.location)
                    .addParameter("rangerName", this.rangerName)
                    .addParameter("animalId", this.animalId)
                    .executeUpdate()
                    .getKey();
        }
    }
    public static Sightings find(int id){
        try(Connection connect= DB.sql2o.open()){
            String sql ="SELECT * FROM sightings WHERE id=:id;";
            Sightings sighting = connect.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Sightings.class);
            return sighting;
        }
    }
}
