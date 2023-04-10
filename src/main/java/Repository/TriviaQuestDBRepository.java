package Repository;

import Domain.TriviaQuest;
import Domain.User;
import Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class TriviaQuestDBRepository implements TriviaQuestRepository{

    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    public TriviaQuestDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(TriviaQuest quest) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("insert into triviaquests(id,question,answer,points) values(?,?,?,?)")){
            ps.setInt(1,quest.getId());
            ps.setString(2, quest.getQuestion());
            ps.setString(3, quest.getAnswer());
            ps.setInt(4,quest.getPoints());

            int res = ps.executeUpdate();
            logger.trace("Saves {} instances",res);
        }catch (SQLException se){
            logger.error("Db error "+se);
            se.printStackTrace();
        }
        logger.traceExit();

    }

    @Override
    public void delete(TriviaQuest quest) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("delete from triviaquests where id=?")){
            ps.setInt(1,quest.getId());
            int res = ps.executeUpdate();
            logger.info("deleted "+res+" quests");


        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();

        }
    }

    @Override
    public void update(TriviaQuest elem, Integer id) {

    }

    @Override
    public TriviaQuest findById(Integer id)
    {
        Connection con =this.dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("select * from triviaquests where id=?")){
            ps.setInt(1,id);
            ResultSet res = ps.executeQuery();
            if(!res.next()){
                logger.info("No user with this id found ",id);
                return null;
            }
            else{
                String question = res.getString("question");
                String answer = res.getString("answer");
                int points = res.getInt("points");
                TriviaQuest triviaQuest = new TriviaQuest(id,question,answer,points);
                return triviaQuest;
            }
        }catch(SQLException se){
            logger.error(se);
            se.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<TriviaQuest> findAll() {
        return null;
    }

    @Override
    public Collection<TriviaQuest> getAll()
    {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<TriviaQuest> triviaQuests= new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from triviaquests")){

            try(ResultSet res = ps.executeQuery()){
                while(res.next()){
                    int id = res.getInt("id");
                    String question = res.getString("question");
                    String answer = res.getString("answer");
                    int points = res.getInt("points");

                    TriviaQuest triviaQuest = new TriviaQuest(id,question,answer,points);
                    triviaQuests.add(triviaQuest);
                }
            }
        }catch(SQLException se){
            logger.error(se);
            System.err.println("Error db "+se );
        }
        logger.traceExit(triviaQuests);
        return triviaQuests;
    }
}
