import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

//图书管理界面
class BooksManageFrame extends JFrame
{
    //图书名称文本区
    private final JTextArea booksNameTextArea = new JTextArea();
    //图书作者文本区
    private final JTextArea bookAuthorTextArea = new JTextArea();
    //图书类别下拉列表
    private final JComboBox<String> bookTypeComboBox = new JComboBox<String>();

    //用于图书相关的数据库操作对象
    private final DatabaseClass booksDataBase = new DatabaseClass() {
        @Override
        //重写getFieldData函数，用于在查询时得到结果
        protected String[] getFieldData(ResultSet resultSet) throws SQLException {
            return new String[] {
                    ((Integer) resultSet.getInt("book_id")).toString(), //id
                    resultSet.getString("book_name"),   //书名
                    resultSet.getString("book_author"),  //书作者
                    resultSet.getString("book_price"),  //书价格
                    resultSet.getString("book_desc"),   //书描述
                    ((Integer) resultSet.getInt("book_type_id")).toString(),    //类别id
            };
        }
    };

    //用于保存类别名称和类别id映射关系map
    private final HashMap<String, String> typeNameIdMap = new HashMap<String, String>();

    //查询按钮
    private final JButton queryButton = new JButton("查询");
    //删除按钮
    private final JButton deleteButton = new JButton("删除");

    //列名
    private final String[] tableColumnNames = {"图书id", "图书名", "图书作者", "图书价格", "图书描述", "图书类别id"};
    //表格
    private final MyTable myTable = new MyTable(new String[][]{}, tableColumnNames);
    private final JScrollPane scrollpane = new JScrollPane(myTable);    //显示表格的滚动窗格

    //界面初始化
    private void initFrame() {
        setTitle("图书管理窗口");     //设置标题
        FrameCommon.otFrameGeneralSettings(this);

        JLabel queryLable = new JLabel("搜索条件");   //搜索标签

        final int MODULE_ALL_WIDTH = 600, MODULE_ALL_HEIGHT = 500;      //全部组件宽度
        Point moduleAllXY = FrameCommon.getModuleCentredPoint(MODULE_ALL_WIDTH, MODULE_ALL_HEIGHT);     //全部组件居中位置

        //搜索标签位置、宽高
        queryLable.setBounds((int) moduleAllXY.getX(), (int) moduleAllXY.getY(), 80, 20);

        //图书名称标签
        JLabel booksNameLable = new JLabel("图书名称：");
        booksNameLable.setBounds(queryLable.getX(), queryLable.getY() + queryLable.getHeight() + 20, 80, 20);

        //图书名称文本区
        booksNameTextArea.setBounds(booksNameLable.getX() + booksNameLable.getWidth(), booksNameLable.getY() + 2, 140, 18);

        //图书作者标签
        JLabel booksAuthorLable = new JLabel("图书作者：");
        booksAuthorLable.setBounds(booksNameTextArea.getX() + booksNameTextArea.getWidth() + 40, booksNameTextArea.getY(), 80, 20);

        //图书作者文本区
        bookAuthorTextArea.setBounds(booksAuthorLable.getX() + booksAuthorLable.getWidth(), booksAuthorLable.getY() + 2, 140, 18);

        //图书类别标签
        JLabel booksTypeLable = new JLabel("图书类别：");
        booksTypeLable.setBounds(booksNameLable.getX(), booksNameLable.getY() + booksNameLable.getHeight() + 20, 80, 20);

        String[][] booksTypeQueryResult = FrameCommon.getBooksTypeInfo();

        //map
        bookTypeComboBox.addItem("全部"); //额外添加一个全部
        for (String[] strings : booksTypeQueryResult) {
            bookTypeComboBox.addItem(strings[1]);       //将类别名称添加进下拉列表选项中
            typeNameIdMap.put(strings[1], strings[0]);  //保存类别名称和id的映射关系，键为名称
        }

        bookTypeComboBox.setBounds(booksTypeLable.getX() + booksTypeLable.getWidth(), booksTypeLable.getY(), 120, 24);

        //查询按钮
        queryButton.setBounds(bookTypeComboBox.getX() + bookTypeComboBox.getWidth() + 60, bookTypeComboBox.getY(), 80, 26);

        //删除按钮
        deleteButton.setBounds(queryButton.getX() + queryButton.getWidth() + 20, queryButton.getY(), queryButton.getWidth(), queryButton.getHeight());

        //表格
        myTable.updateAllRow(booksDataBase.getQueryResult("SELECT * FROM book;"));
        myTable.setRowHeight(28);

        //滚动窗格
        final int SCROLL_WIDTH = 640, SCROLL_HEIGHT = 320;          //滚动窗格的宽高
        final int SCROLL_X = FrameCommon.getOperatingFrameCenteredX(SCROLL_WIDTH);     //滚动窗格X
        scrollpane.setBounds(SCROLL_X, booksTypeLable.getY() + booksTypeLable.getHeight() + 30, SCROLL_WIDTH, SCROLL_HEIGHT);  //设置滚动窗格的坐标、宽高

        add(queryLable);
        add(booksNameLable);
        add(booksNameTextArea);
        add(booksAuthorLable);
        add(bookAuthorTextArea);
        add(booksTypeLable);
        add(bookTypeComboBox);
        add(queryButton);
        add(deleteButton);
        add(scrollpane);

        setVisible(true);   //设置窗口显示
    }

    //组件事件初始化
    private void componentEventInit() {
        //查询按钮点击事件
        queryButton.addActionListener(e -> {
            //要执行的sql语句 (部分)
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM book");

            /*
              查询规律：按照图书名称、图书作者、图书类别进行查询
              不填的项视为对其字段无要求
            */

            boolean flag_WHERE = false; //是否添加WHERE标记

            //图书名文本区不为空
            if (!booksNameTextArea.getText().equals("")) {
                sqlBuilder.append(" WHERE " + "book_name LIKE '%"+ booksNameTextArea.getText() +"%'");
                flag_WHERE = true;
            }

            //图书作者文本区不为空
            if (!bookAuthorTextArea.getText().equals("")) {
                if(!flag_WHERE) {
                    sqlBuilder.append(" WHERE ");
                    flag_WHERE = true;
                }
                else
                    sqlBuilder.append(" AND ");

                sqlBuilder.append("book_author LIKE '%"+ bookAuthorTextArea.getText() +"%'");
            }

            //下拉列表框
            String selectbookType = (String) bookTypeComboBox.getSelectedItem();
            if(selectbookType == null) return;
            //不为全部时
            if (!selectbookType.equals("全部")) {
                if(!flag_WHERE) {
                    sqlBuilder.append(" WHERE ");
                    flag_WHERE = true;
                }
                else
                    sqlBuilder.append(" AND ");

                sqlBuilder.append("book_type_id = '"+ typeNameIdMap.get(selectbookType) +"'");
            }

            sqlBuilder.append(";");     //最后添加分号

            String sql = sqlBuilder.toString();     //得到需要执行的sql语句

            try {
                myTable.updateAllRow(booksDataBase.getQueryResult(sql));
            }
            catch (Exception se) {      //更新表格时出现异常说明要查询的图书类别名称不存在。将表格清空
                myTable.clearRow();
            }

            System.out.println(sql);
        });

        //删除按钮点击事件
        deleteButton.addActionListener(e -> {
            //将所选行的id的数据从数据库中删除
            final int selectRow = myTable.getSelectedRow();
            try {
                if(selectRow != -1) //选中行为-1代表没有选中
                {
                    //执行删除数据sql语句
                    booksDataBase.dExecuteUpdate("DELETE FROM `book` WHERE `book_id` = "+ myTable.getValueAt(selectRow, 0) +";");
                    //从表格中删除该行
                    myTable.removeRow(selectRow);
                }
            }
            catch (SQLException se) {
                throw new RuntimeException(se.toString());
            }
        });

        //显示列名和真实列名的map
        final HashMap<String, String> columnNameMap = new HashMap<String, String>();
        //真实列名数组
        final String[] tableRealColumnNameStrings = new String[]{"book_id", "book_name", "book_author", "book_price", "book_desc", "book_type_id"};
        //循环设置列名的映射关系
        for(int i = 0, arrLen = tableRealColumnNameStrings.length; i < arrLen; i++)
            columnNameMap.put(tableColumnNames[i], tableRealColumnNameStrings[i]);

        //表格修改单元格事件
        myTable.getModel().addTableModelListener(e -> {
            if(e.getColumn() == -1 || e.getFirstRow() == -1)
                return;

            final String updatecCurrentColumnName;
            //得到改变行的列名
            updatecCurrentColumnName = myTable.getColumnName(e.getColumn());
            //得到修改列的真实列名
            final String updatecRealColumnName = columnNameMap.get(updatecCurrentColumnName);
            //执行sql修改语句
            try {
                final String sql = "UPDATE `book` SET "+
                        updatecRealColumnName +" = '"+
                        myTable.getValueAt(e.getFirstRow(), e.getColumn()) +"' WHERE book_id = '"+
                        myTable.getValueAt(e.getFirstRow(), 0) +"';";
                System.out.println(sql);

                booksDataBase.dExecuteUpdate(sql);
            }
            catch (Exception se) {
                new JDialog() {
                    {
                        setTitle("异常");
                        setModal(true);
                        setLayout(null);
                        JLabel label = new JLabel("fail to edit");
                        label.setBounds(8, 20, 250, 50);
                        label.setFont(new Font(null, Font.BOLD, 14));
                        add(label);

                        Point point = FrameCommon.getScreenCentredPoint(250, 150);

                        setBounds(point.x, point.y, 250, 150);
                    }
                }.setVisible(true);
            }
        });
    }

    public BooksManageFrame() {
        initFrame();    //界面初始化
        componentEventInit();
    }
}
