package db;

import java.util.List;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public interface UserDao {

    public void insertSqlName(String username,String lv,String sqlname);

   // public void deleteGameInfo(String game_id, String game_name);

    public void updateUser(String sqlname);

    public List<String> getParents();

    public List<List<String>> getChild();

    public boolean isExists(String username,String lv,String sqlname);

    public List<String> getAllCount(String account);
}
