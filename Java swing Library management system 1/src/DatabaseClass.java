
import java.sql.*;
import java.util.ArrayList;

/*
数据库类，用于封装数据库的连接和操作数据库的方法
如果需要用到获取查询结果的函数，则需要重写getFieldData函数才可以正常获得
*/
class DatabaseClass
{
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
    private ResultSet resultSet = null;

    /**
     * 用于连接数据库和初始化和执行sql语句相关的对象
     */
    public DatabaseClass()
    {
        try {
            // System.out.println("2");
            Class.forName("com.mysql.jdbc.Driver");  //反射加载驱动程序
            // System.out.println("3");
            //连接mysql数据库
            connection = DriverManager.getConnection(url,user,password);

            if(connection.isClosed())      //检查数据库是否连接成功
                throw new RuntimeException("连接数据库失败");

            System.out.println("heelo");
            statement = connection.createStatement();   //声明对象

        }
        catch (Exception e) {
            throw new RuntimeException(e.toString() + "初始化部分");
        }
    }

    /**
     *
     * @param sql 需要执行的sql查询语句
     * @return 返回查询的结果是否存在，存在为<code>true</code>不存在为<code>false</code>
     */
    public boolean isResultExist(String sql) {
        boolean flag;
        try {
            resultSet = statement.executeQuery(sql);
            flag = resultSet.isBeforeFirst();
        }
        catch (SQLException e) {
            throw new RuntimeException(e.toString());
        }

        return flag;
    }

    /**
     * 将参数sql查询到的结果集转换为字符串数组然后返回
     *
     * @param sql 需要执行的sql查询语句
     * @return 结果集的字符串二维数组
     */
    public String[][] getQueryResult(final String sql) {

        final ArrayList<String[]> resultList = new ArrayList<String[]>();

        try {
            resultSet = statement.executeQuery(sql);

            while (resultSet.next())
                resultList.add(getFieldData(resultSet));
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

        //将resultList转换为Stringp[][]返回
        return resultList.toArray(new String[resultList.size()][]);
    }

    /**
     * 从结果集中得到对应字段的查询结果，然后添加到Stringp[]并返回，用于该类的getQueryResult调用
     *
     * @param resultSet 执行查询语句后的结果集，通过get方法获得集合中对应字段的内容
     * @return 从数据库获取到的每行数据
     * @throws SQLException sql异常
     */
    protected String[] getFieldData(final ResultSet resultSet) throws SQLException {
        return null;
    }

    /**
     * 执行更新语句
     *
     * @param sql 要执行的sql更新语句
     */
    public void dExecuteUpdate(String sql) throws SQLException{
            statement.executeUpdate(sql);
    }

}
