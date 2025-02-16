package cardyb.newOne.models;

public class ActionEntity {

    //requires inputs to be defined i.e. 1 string, 1 int, or
    private String name;

    public ActionEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
