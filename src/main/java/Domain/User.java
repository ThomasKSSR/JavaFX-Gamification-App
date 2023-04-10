package Domain;

public class User implements Entity<String>{

    private String username;
    private String password;

    private int points;

    private int completedQuests;

    private int createdQuests;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.points=0;
        this.completedQuests=0;
        this.createdQuests=0;
    }

    public int getCompletedQuests() {
        return completedQuests;
    }

    public void setCompletedQuests(int completedQuests) {
        this.completedQuests = completedQuests;
    }

    public int getCreatedQuests() {
        return createdQuests;
    }

    public void setCreatedQuests(int createdQuests) {
        this.createdQuests = createdQuests;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getId()
    {
        return username;
    }

    @Override
    public void setId(String id) {
        username=id;
    }
}
