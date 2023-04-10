package Repository;

import Domain.Badge;
import Domain.BadgeOwner;
import Domain.User;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BadgeOwnerDBRepository implements BadgeOwnerRepository{

    private static final Logger logger = LogManager.getLogger();


    private JdbcUtils dbUtils;

    public BadgeOwnerDBRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);

    }

    @Override
    public void add(BadgeOwner badgeOwner) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("insert into badgeowners(userid,badgeid) values(?,?)")){
            ps.setString(1,badgeOwner.getUserid());
            ps.setString(2, badgeOwner.getBadgeid());
            int res =ps.executeUpdate();
            logger.info("added "+res+" badgeOwner");
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
        }
    }

    @Override
    public void delete(BadgeOwner badgeOwner) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("delete from badgeowners where userid=? and badgeid=?")){
            ps.setString(1,badgeOwner.getUserid());
            ps.setString(2,badgeOwner.getBadgeid());
            int res = ps.executeUpdate();
            logger.info("deleted "+res+" badgeOwners");
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
        }
    }

    @Override
    public void update(BadgeOwner elem, HashSet<String> id) {

    }

    @Override
    public BadgeOwner findById(HashSet<String> id) {
        return null;
    }

    @Override
    public Iterable<BadgeOwner> findAll() {
        return null;
    }

    @Override
    public Collection<BadgeOwner> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<BadgeOwner> badgeowners= new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from badgeowners")){

            try(ResultSet res = ps.executeQuery()){
                while(res.next()){
                    String userid = res.getString("userid");
                    String badgeid=res.getString("badgeid");

                    BadgeOwner badgeOwner = new BadgeOwner(userid,badgeid);
                    badgeowners.add(badgeOwner);
                }
            }
        }catch(SQLException se){
            logger.error(se);
            System.err.println("Error db "+se );
        }
        logger.traceExit(badgeowners);
        return badgeowners;
    }

    @Override
    public List<BadgeOwner> findAllByUser(User user) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<BadgeOwner> badgeowners= new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from badgeowners where userid=?")){
            ps.setString(1,user.getUsername());
            try(ResultSet res = ps.executeQuery()){
                while(res.next()){
                    String userid = res.getString("userid");
                    String badgeid=res.getString("badgeid");

                    BadgeOwner badgeOwner = new BadgeOwner(userid,badgeid);
                    badgeowners.add(badgeOwner);
                }
            }
        }catch(SQLException se){
            logger.error(se);
            System.err.println("Error db "+se );
        }
        logger.traceExit(badgeowners);
        return badgeowners;
    }

    @Override
    public void deleteUsersBadges(User user) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("delete from badgeowners where userid=?")){
            ps.setString(1, user.getId());
            int res = ps.executeUpdate();
            logger.info("deleted "+res+" badgeOwners");
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
        }
    }
}
