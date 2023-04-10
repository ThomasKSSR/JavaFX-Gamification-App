package Repository;

import Domain.Badge;
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

public class BadgeBDRepository implements BadgeRepository{

    private static final Logger logger = LogManager.getLogger();

    private JdbcUtils dbUtils;

    public BadgeBDRepository(Properties props) {
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Badge elem) {

    }

    @Override
    public void delete(Badge elem) {

    }

    @Override
    public void update(Badge elem, String id) {

    }

    @Override
    public Badge findById(String id) {
        return null;
    }

    @Override
    public Iterable<Badge> findAll() {
        return null;
    }

    @Override
    public Collection<Badge> getAll()
    {

        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Badge> badges= new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from badges")){

            try(ResultSet res = ps.executeQuery()){
                while(res.next()){
                    String name = res.getString("name");

                    Badge badge = new Badge(name);
                    badges.add(badge);
                }
            }
        }catch(SQLException se){
            logger.error(se);
            System.err.println("Error db "+se );
        }
        logger.traceExit(badges);
        return badges;
    }
}
