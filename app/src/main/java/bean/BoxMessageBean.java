package bean;

import java.util.List;

/**
 * Created by sunxipeng on 2016/12/13.
 */
public class BoxMessageBean extends BeseBean {

    public String boxname;
    public List<String> boarddata;


    @Override
    public String toString() {
        return "BoxMessageBean{" +
                "boxname='" + boxname + '\'' +
                ", boarddata=" + boarddata +
                '}';
    }
}
