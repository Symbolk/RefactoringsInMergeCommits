package ca.ualberta.smr.refactoring.analysis.database;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("project")
public class Project extends Model {

    static {
        validatePresenceOf("url", "name");
    }

    public Project() {
    }

    public Project(String url, String name) {
        set("url", url, "name", name);
    }

    public String getName() {
        return getString("name");
    }

    public int getID() {
        return getInteger("id");
    }

}