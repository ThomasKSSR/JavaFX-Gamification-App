package Domain;

import java.util.HashSet;

public class QuestOwner implements Entity<HashSet<String>>{
    private String username;
    private String questId;

    public QuestOwner(String username, String questId) {
        this.username = username;
        this.questId = questId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    @Override
    public HashSet<String> getId() {
        HashSet<String> ids = new HashSet<>();
        ids.add(username);
        ids.add(questId);
        return ids;
    }

    @Override
    public void setId(HashSet<String> id) {

    }



}
