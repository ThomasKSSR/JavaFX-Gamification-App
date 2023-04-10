package Domain;

public class Badge implements Entity<String>{

    private String name;

    public Badge(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        this.name=id;
    }
}
