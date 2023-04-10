package Repository;

import Domain.BadgeOwner;
import Domain.QuestOwner;
import Domain.TriviaQuest;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class QuestOwnerDBRepository implements QuestOwnerRepository{

    private static final Logger logger = LogManager.getLogger();

    JdbcUtils dbUtils;
    public QuestOwnerDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(QuestOwner questOwner) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("insert into questowners(questid,userid) values(?,?)")){
            ps.setString(1,questOwner.getQuestId());
            ps.setString(2, questOwner.getUsername());


            int res = ps.executeUpdate();
            logger.trace("Saves {} instances",res);
        }catch (SQLException se){
            logger.error("Db error "+se);
            se.printStackTrace();
        }
        logger.traceExit();
    }

    @Override
    public void delete(QuestOwner quest) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("delete from questowners where questid=? and userid=?")){
            ps.setString(1,quest.getQuestId());
            ps.setString(2,quest.getUsername());
            int res = ps.executeUpdate();
            logger.info("deleted "+ res+" questowners");

        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();

        }
    }

    @Override
    public void update(QuestOwner elem, HashSet<String> id) {

    }

    @Override
    public QuestOwner findById(HashSet<String> id) {
        return null;
    }

    @Override
    public Iterable<QuestOwner> findAll() {
        return null;
    }

    @Override
    public Collection<QuestOwner> getAll()
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<QuestOwner> questOwners= new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from questowners")){

            try(ResultSet res = ps.executeQuery()){
                while(res.next()){
                    String questid = res.getString("questid");
                    String userid=res.getString("userid");

                    QuestOwner questOwner = new QuestOwner(userid,questid);
                    questOwners.add(questOwner);
                }
            }
        }catch(SQLException se){
            logger.error(se);
            System.err.println("Error db "+se );
        }
        logger.traceExit(questOwners);
        return questOwners;

    }

    @Override
    public String getUserIDByQuestID(String questID) {
        Connection con =this.dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("select * from questowners where questid=?")){
            ps.setString(1,questID);
            ResultSet res = ps.executeQuery();
            if(!res.next()){
                logger.info("No user with the quest  ",questID);
                return null;
            }
            else{
                String userID = res.getString("userid");
                return userID;
            }
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
            return null;
        }
    }
}
