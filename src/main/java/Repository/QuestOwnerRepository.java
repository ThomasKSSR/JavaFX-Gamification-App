package Repository;

import Domain.QuestOwner;

import java.util.HashSet;

public interface QuestOwnerRepository extends Repository<QuestOwner, HashSet<String>> {
    String getUserIDByQuestID(String QuestID);

}
