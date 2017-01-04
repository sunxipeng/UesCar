package db;

import java.util.List;

import bean.User;

/**
 * Created by sunxipeng on 2016/11/7.
 */
public interface UserInfoDao {

    public void insertUserInfo(String username,String password);

   // public void deleteUserInfo(String username);

    //public void updateUserInfo(String username);

   public List<User> getUserInfos();

    public boolean isExists(String username);
}
