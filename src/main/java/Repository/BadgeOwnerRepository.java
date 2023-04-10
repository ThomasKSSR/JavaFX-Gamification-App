package Repository;

import Domain.BadgeOwner;
import Domain.User;

import java.util.HashSet;
import java.util.List;

public interface BadgeOwnerRepository extends Repository<BadgeOwner, HashSet<String>> {
    List<BadgeOwner> findAllByUser(User user);

    void deleteUsersBadges(User user);

}
