package Domain;

import java.util.List;

public class UserDTO {
    private String username;
    private String password;
    private int points;
    private String badgesNames;
    private Rank rank;

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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getBadgesNames() {
        return badgesNames;
    }

    public void setBadgesNames(String badgesNames) {
        this.badgesNames = badgesNames;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public UserDTO(String username, String password, int points, String badgesNames, Rank rank) {
        this.username = username;
        this.password = password;
        this.points = points;
        this.badgesNames = badgesNames;
        this.rank = rank;
    }
}
