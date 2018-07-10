package app.com.dc_books.Expand;

import java.util.ArrayList;

/**
 * Created by User on 12/27/2017.
 */

public class ExpandableCollectiveClass {
    String mainparent_id;
    ArrayList <ExpandableClass> collection=new ArrayList<>();

    public ExpandableCollectiveClass(String mainparent_id, ArrayList<ExpandableClass> collection) {
        this.mainparent_id = mainparent_id;
        this.collection = collection;
    }

    public String getMainparent_id() {
        return mainparent_id;
    }

    public ArrayList<ExpandableClass> getCollection() {
        return collection;
    }
}
