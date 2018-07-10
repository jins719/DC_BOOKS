package app.com.dc_books.Expand;

/**
 * Created by User on 12/27/2017.
 */

public class ExpandableClass {
    String parent_id,parent_value,main_id;

    public ExpandableClass(String parent_id, String parent_value,String main_id) {
        this.parent_id = parent_id;
        this.parent_value = parent_value;
        this.main_id = main_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getParent_value() {
        return parent_value;
    }

    public String getMain_id(){
        return main_id;
    }
}
