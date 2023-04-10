package Domain;

import java.util.HashSet;

public class BadgeOwner implements Entity<HashSet<String>>{
    private String userid;
    private String badgeid;

    public BadgeOwner(String userid, String badgeid) {
        this.userid = userid;
        this.badgeid = badgeid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }

    @Override
    public HashSet<String> getId() {
        HashSet<String > ids= new HashSet<>();
        ids.add(userid);
        ids.add(badgeid);
        return ids;
    }

    @Override
    public void setId(HashSet<String> id) {

    }
}
