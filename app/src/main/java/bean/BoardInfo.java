package bean;

/**
 * Created by sunxipeng on 2016/12/12.
 */
public class BoardInfo {

    public String get库位() {
        return 库位;
    }

    public void set库位(String 库位) {
        this.库位 = 库位;
    }


    public String get时间() {
        return 时间;
    }

    public void set时间(String 时间) {
        this.时间 = 时间;
    }

    public String get姓名() {
        return 姓名;
    }

    public void set姓名(String 姓名) {
        this.姓名 = 姓名;
    }

    public String get操作() {
        return 操作;
    }

    public void set操作(String 操作) {
        this.操作 = 操作;
    }

    public String get分类1() {
        return 分类1;
    }

    public void set分类1(String 分类1) {
        this.分类1 = 分类1;
    }

    public String get分类2() {
        return 分类2;
    }

    public void set分类2(String 分类2) {
        this.分类2 = 分类2;
    }

    public String get分类3() {
        return 分类3;
    }

    public void set分类3(String 分类3) {
        this.分类3 = 分类3;
    }

    public String get分类4() {
        return 分类4;
    }

    public void set分类4(String 分类4) {
        this.分类4 = 分类4;
    }

    public String get分类5() {
        return 分类5;
    }

    public void set分类5(String 分类5) {
        this.分类5 = 分类5;
    }

    public String get售价() {
        return 售价;
    }

    public void set售价(String 售价) {
        this.售价 = 售价;
    }

    public String get日期() {
        return 日期;
    }

    public void set日期(String 日期) {
        this.日期 = 日期;
    }


    public String get备注() {
        return 备注;
    }

    public void set备注(String 备注) {
        this.备注 = 备注;
    }

    private String 库位;
    private String 时间;
    private String 日期;
    private String 姓名;
    private String 操作;
    private String 分类1;
    private String 分类2;
    private String 分类3;
    private String 分类4;
    private String 分类5;
    private String 售价;
    private String 备注;

    @Override
    public String toString() {
        return "BoardInfo{" +
                "库位='" + 库位 + '\'' +
                ", 时间='" + 时间 + '\'' +
                ", 日期='" + 日期 + '\'' +
                ", 姓名='" + 姓名 + '\'' +
                ", 操作='" + 操作 + '\'' +
                ", 分类1='" + 分类1 + '\'' +
                ", 分类2='" + 分类2 + '\'' +
                ", 分类3='" + 分类3 + '\'' +
                ", 分类4='" + 分类4 + '\'' +
                ", 分类5='" + 分类5 + '\'' +
                ", 售价='" + 售价 + '\'' +
                ", 备注='" + 备注 + '\'' +
                '}';
    }
}
