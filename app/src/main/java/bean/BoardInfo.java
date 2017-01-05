package bean;

import java.io.Serializable;

public class BoardInfo
        implements Serializable
{
    private boolean isshow;
    private boolean selected;
    private String 分类1;
    private String 分类2;
    private String 分类3;
    private String 分类4;
    private String 分类5;
    private String 售价;
    private String 备注;
    private String 姓名;
    private String 库位;
    private String 操作;
    private String 日期;
    private String 时间;
    private String 机器码;
    private String 钥匙编码;

    public String get分类1()
    {
        return this.分类1;
    }

    public String get分类2()
    {
        return this.分类2;
    }

    public String get分类3()
    {
        return this.分类3;
    }

    public String get分类4()
    {
        return this.分类4;
    }

    public String get分类5()
    {
        return this.分类5;
    }

    public String get售价()
    {
        return this.售价;
    }

    public String get备注()
    {
        return this.备注;
    }

    public String get姓名()
    {
        return this.姓名;
    }

    public String get库位()
    {
        return this.库位;
    }

    public String get操作()
    {
        return this.操作;
    }

    public String get日期()
    {
        return this.日期;
    }

    public String get时间()
    {
        return this.时间;
    }

    public String get机器码()
    {
        return this.机器码;
    }

    public String get钥匙编码()
    {
        return this.钥匙编码;
    }

    public boolean isSelected()
    {
        return this.selected;
    }

    public boolean isshow()
    {
        return this.isshow;
    }

    public void setIsshow(boolean paramBoolean)
    {
        this.isshow = paramBoolean;
    }

    public void setSelected(boolean paramBoolean)
    {
        this.selected = paramBoolean;
    }

    public void set分类1(String paramString)
    {
        this.分类1 = paramString;
    }

    public void set分类2(String paramString)
    {
        this.分类2 = paramString;
    }

    public void set分类3(String paramString)
    {
        this.分类3 = paramString;
    }

    public void set分类4(String paramString)
    {
        this.分类4 = paramString;
    }

    public void set分类5(String paramString)
    {
        this.分类5 = paramString;
    }

    public void set售价(String paramString)
    {
        this.售价 = paramString;
    }

    public void set备注(String paramString)
    {
        this.备注 = paramString;
    }

    public void set姓名(String paramString)
    {
        this.姓名 = paramString;
    }

    public void set库位(String paramString)
    {
        this.库位 = paramString;
    }

    public void set操作(String paramString)
    {
        this.操作 = paramString;
    }

    public void set日期(String paramString)
    {
        this.日期 = paramString;
    }

    public void set时间(String paramString)
    {
        this.时间 = paramString;
    }

    public void set机器码(String paramString)
    {
        this.机器码 = paramString;
    }

    public void set钥匙编码(String paramString)
    {
        this.钥匙编码 = paramString;
    }

    public String toString()
    {
        return "BoardInfo{库位='" + this.库位 + '\'' + ", 时间='" + this.时间 + '\'' + ", 日期='" + this.日期 + '\'' + ", 姓名='" + this.姓名 + '\'' + ", 操作='" + this.操作 + '\'' + ", 分类1='" + this.分类1 + '\'' + ", 分类2='" + this.分类2 + '\'' + ", 分类3='" + this.分类3 + '\'' + ", 分类4='" + this.分类4 + '\'' + ", 分类5='" + this.分类5 + '\'' + ", 售价='" + this.售价 + '\'' + ", 备注='" + this.备注 + '\'' + '}';
    }
}