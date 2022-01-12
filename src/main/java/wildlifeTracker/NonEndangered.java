package wildlifeTracker;

import java.util.List;
import org.sql2o.*;
public class NonEndangered extends Animals{
    public NonEndangered(String name){
        this.name = name;
        endangered = false;
    }
    public static List<NonEndangered> all() {
        String sql = "SELECT * FROM animals WHERE endangered = 'false'";
        try(Connection connect = DB.sql2o.open()) {
            return connect.createQuery(sql).throwOnMappingFailure(false).executeAndFetch(NonEndangered.class);
        }
    }
    public static NonEndangered find(int id){
        try(Connection connect = DB.sql2o.open()){
            String sql = "SELECT * FROM animals WHERE id= :id;";
            NonEndangered animal = connect.createQuery(sql)
                    .addParameter("id", id)
                    .throwOnMappingFailure(false)
                    .executeAndFetchFirst(NonEndangered.class);
            if (animal == null) {
                throw new IndexOutOfBoundsException("Sorry, this animal cannot be found.");
            }
            return animal;
        }
    }
}
