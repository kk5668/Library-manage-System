import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {
    private Connection connection = null;  //连接引用

    //URL指向要访问的数据库名mydata
    // private static final String url = "jdbc:mysql://localhost:3306/books_management";
    private static final String url = "jdbc:mysql://localhost:3306/books_management?characterEncoding=utf-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    //MySQL配置时的用户名
    private static final String user = "root";
    //MySQL配置时的密码
    private static final String password = "root";

    //"声明"对象，用来执行SQL语句
    private Statement statement = null;

    //结果集对象，用于存放数据库执行查询语句后的结果集
    private ResultSet result = null;

    /**
     * 用于连接数据库和初始化和执行sql语句相关的对象
     */
    public DatabaseTest()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  //反射加载驱动程序
            //连接mysql数据库
            connection = DriverManager.getConnection(url,user,password);

            // if(connection.isClosed())      //检查数据库是否连接成功
            //     throw new RuntimeException("连接数据库失败");

            statement = connection.createStatement();   //声明对象
            String sql = "SELECT * FROM book";
            result = statement.executeQuery(sql);
            System.out.println(result);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString() + "初始化部分");
        }
    }
    public static void main(String[] args) {
        new DatabaseTest();
    }
}
