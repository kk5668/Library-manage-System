import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

//图书类别管理界面
class CategoryManageFrame extends JFrame
{
    private final JTextField booksTypeNameTextField = new JTextField(); //图书类别文本区
    private final JButton inquiryButton = new JButton("查询");     //查询按钮
    private final JButton deleteButton = new JButton("删除");  //删除按钮

    private final String[] tableColumnNames = {"图书类别id", "图书类别名称", "图书类别描述"};
    private final MyTable myTable = new MyTable(new String[][]{}, tableColumnNames);
    private final JScrollPane scrollpane = new JScrollPane(myTable);    //显示表格的滚动窗格

    //数据库操作对象
    DatabaseClass database = new DatabaseClass() {
        @Override
        //重写getFieldData函数，用于在查询时得到结果
        protected String[] getFieldData(ResultSet resultSet) throws SQLException {
            return new String[] {
                    ((Integer) resultSet.getInt("book_type_id")).toString(),
                    resultSet.getString("book_type_name"),
                    resultSet.getString("book_type_desc")
            };
        }
    };

    //表格列名
    private void initFrame() {
        setTitle("类别管理界面");
        FrameCommon.otFrameGeneralSettings(this);   //设置窗口的一般属性

        //设置组件
//        table.setValueAt("你好", 0, 1);   //设置单元格的值
        JLabel booksTypeNameLable = new JLabel("图书类别名称：");   //图书类别名称标签

        final int MODULE_ALL_WIDTH = 580, MODULE_ALL_HEIGHT = 420;      //全部组件宽度
        Point moduleAllXY = FrameCommon.getModuleCentredPoint(MODULE_ALL_WIDTH, MODULE_ALL_HEIGHT);     //全部组件居中位置

        //图书类别名称标签位置、宽高
        booksTypeNameLable.setBounds((int) moduleAllXY.getX(), (int) moduleAllXY.getY(), 100, 30);

        //图书类别名称文本区
        booksTypeNameTextField.setBounds(booksTypeNameLable.getX() + booksTypeNameLable.getWidth() + 40, booksTypeNameLable.getY() + 2, 200, 26);

        //查询按钮
        final int INQUIRY_BUTTON_WIDTH = 80;        //查询按钮宽度
        inquiryButton.setBounds( MODULE_ALL_WIDTH + (int) moduleAllXY.getX() - INQUIRY_BUTTON_WIDTH, booksTypeNameLable.getY(), INQUIRY_BUTTON_WIDTH, 30);

        //删除按钮
        deleteButton.setBounds(inquiryButton.getX() - INQUIRY_BUTTON_WIDTH - 30, inquiryButton.getY(), inquiryButton.getWidth(), inquiryButton.getHeight());

        //表格初始化为查询所有状态
        myTable.updateAllRow(database.getQueryResult("SELECT * FROM books_type;"));
        myTable.setRowHeight(28);

        final int SCROLL_WIDTH = 580, SCROLL_HEIGHT = 360;          //滚动窗格的宽高
        final int SCROLL_X = FrameCommon.getOperatingFrameCenteredX(SCROLL_WIDTH);  //滚动窗格X
        scrollpane.setBounds(SCROLL_X, booksTypeNameLable.getY() + booksTypeNameLable.getHeight() + 30, SCROLL_WIDTH, SCROLL_HEIGHT);  //设置滚动窗格的坐标、宽高

        add(booksTypeNameLable);
        add(booksTypeNameTextField);
        add(scrollpane);
        add(inquiryButton);
        add(deleteButton);

        setVisible(true);
    }

    //组件事件初始化
    private void componentEventInit() {

        //查询按钮点击
        inquiryButton.addActionListener(e -> {
            //如果文本区中没有内容点击查询按钮则查询全部数据
            if(booksTypeNameTextField.getText().equals("")) {
                myTable.updateAllRow(database.getQueryResult("SELECT * FROM books_type;"));
            }
            else {
                try {
                    myTable.updateAllRow(database.getQueryResult("SELECT * FROM books_type WHERE book_type_name LIKE \"%"+ booksTypeNameTextField.getText() +"%\";"));
                }
                catch (Exception se) {      //更新表格时出现异常说明要查询的图书类别名称不存在。将表格清空
                    myTable.clearRow();
                }
            }
        });

        //删除按钮点击
        deleteButton.addActionListener(e -> {
            try {
                //将所选行的id的数据从数据库中删除
                final int selectRow = myTable.getSelectedRow();
                if(selectRow != -1) //选中行为-1代表没有选中
                {
                    //执行删除数据sql语句
                    database.dExecuteUpdate("DELETE FROM `books_type` WHERE `book_type_id` = "+ myTable.getValueAt(selectRow, 0) +";");
                    //从表格中删除该行
                    myTable.removeRow(selectRow);
                }
            }
            catch (SQLException se) {
                //提示类别下有图书，不可删除该类别
                new JDialog() {
                    {
                        setTitle("提示");
                        setModal(true);
                        setLayout(null);
                        JLabel label = new JLabel("There are books in this category");
                        label.setBounds(4, 20, 250, 50);
                        label.setFont(new Font(null, Font.BOLD, 14));
                        add(label);

                        Point point = FrameCommon.getScreenCentredPoint(250, 150);

                        setBounds(point.x, point.y, 250, 150);
                    }
                }.setVisible(true);
            }
        });

        //显示列名和真实列名的map
        final HashMap<String, String> columnNameMap = new HashMap<String, String>();
        //真实列名数组
        final String[] tableRealColumnNameStrings = new String[]{"book_type_id", "book_type_name", "book_type_desc"};
        //循环设置列名的映射关系
        for(int i = 0, arrLen = tableRealColumnNameStrings.length; i < arrLen; i++)
            columnNameMap.put(tableColumnNames[i], tableRealColumnNameStrings[i]);

//        可以通过e.getColumn()，e.getFirstRow()，e.getLastRow()，e.getType()来获取变更发生的位置和变更的类型（插入、更新或删除）
        myTable.getModel().addTableModelListener(e -> {
            //如果要改变的行号或列号为-1则返回
            if(e.getColumn() == -1 || e.getFirstRow() == -1)
                return;

            final String updatecCurrentColumnName;
            //得到改变行的列名
            updatecCurrentColumnName = myTable.getColumnName(e.getColumn());
            //得到修改列的真实列名
            final String updatecRealColumnName = columnNameMap.get(updatecCurrentColumnName);
            //执行sql修改语句
            try {
                database.dExecuteUpdate("UPDATE books_type SET "+
                        updatecRealColumnName +" = '"+
                        myTable.getValueAt(e.getFirstRow(), e.getColumn()) +"' WHERE book_type_id = '"+
                        myTable.getValueAt(e.getFirstRow(), 0) +"';");
            }
            catch (SQLException se) {
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

    public CategoryManageFrame() {
        initFrame();    //界面初始化
        componentEventInit();   //组件事件初始化
    }
}
