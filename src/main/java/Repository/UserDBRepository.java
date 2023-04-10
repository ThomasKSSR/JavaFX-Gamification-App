package Repository;

import Domain.Badge;
import Domain.User;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements UserRepository{

    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    public UserDBRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(User user) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("insert into users(username,password,points,completedquests,createdquests) values(?,?,?,?,?)")){
            ps.setString(1, user.getId());
            ps.setString(2, user.getPassword());
            ps.setInt(3,user.getPoints());
            ps.setInt(4,user.getCompletedQuests());
            ps.setInt(5,user.getCreatedQuests());
            int res =ps.executeUpdate();
            logger.info("added "+res+" users");
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("delete from users where username=?")){
            ps.setString(1,user.getUsername());
            int res = ps.executeUpdate();
            logger.info("deleted "+res+" users");
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
        }
    }

    @Override
    public void update(User user, String id) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps =con.prepareStatement("update users set points=?, completedquests=?, createdquests=? where username=?")){
            ps.setInt(1,user.getPoints());
            ps.setInt(2,user.getCompletedQuests());
            ps.setInt(3,user.getCreatedQuests());
            ps.setString(4,id);

            int rez =ps.executeUpdate();
            logger.info(rez+" user updated");
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();

        }
    }

    @Override
    public User findById(String id)
    {
        Connection con =this.dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("select * from users where username=?")){
            ps.setString(1,id);
            ResultSet res = ps.executeQuery();
            if(!res.next()){
                logger.info("No user with this id found ",id);
                return null;
            }
            else{
                String password = res.getString("password");
                int points = res.getInt("points");
                int completedquests = res.getInt("completedquests");
                int createdquests = res.getInt("createdquests");
                User user = new User(id,password);
                user.setPoints(points);
                user.setCompletedQuests(completedquests);
                user.setCreatedQuests(createdquests);
                return user;
            }
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Collection<User> getAll()
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users= new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from users")){

            try(ResultSet res = ps.executeQuery()){
                while(res.next()){
                    String username= res.getString("username");
                    String password = res.getString("password");
                    int points =res.getInt("points");
                    int completedQuests = res.getInt("completedquests");
                    int createdquests = res.getInt("createdquests");


                    User user = new User(username,password);
                    user.setPoints(points);
                    user.setCompletedQuests(createdquests);
                    user.setCompletedQuests(completedQuests);
                    users.add(user);
                }
            }
        }catch(SQLException se){
            logger.error(se);
            System.err.println("Error db "+se );
        }
        logger.traceExit(users);
        return users;

    }

    @Override
    public User findMatchingUser(String username, String password) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("select * from users where username=? and password=?")){
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rez = ps.executeQuery();

            if(!rez.next()){
                logger.info("No matching client found");
                return null;
            }
            int points=rez.getInt("points");
            int completedquests =rez.getInt("completedquests");
            int createdquests=rez.getInt("createdquests");
            logger.info("Matching client found");

            User user = new User(username,password);
            user.setPoints(points);
            user.setCompletedQuests(completedquests);
            user.setCreatedQuests(createdquests);
            return user;
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
            return null;
        }
    }
}
