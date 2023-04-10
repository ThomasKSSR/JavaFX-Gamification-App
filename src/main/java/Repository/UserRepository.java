package Repository;

import Domain.User;

public interface UserRepository extends Repository<User,String> {

    User findMatchingUser(String username,String password);

}
