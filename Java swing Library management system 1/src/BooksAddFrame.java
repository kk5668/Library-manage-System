import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;

//图书添加界面
class BooksAddFrame extends JFrame
{
    private final JTextArea[] textAreaArr = new JTextArea[4];     //文本区数组
    private final JButton addButton = new JButton("添加");   //添加按钮

    //用于保存类别名称和类别id映射关系map
    private final HashMap<String, String> typeNameIdMap = new HashMap<String, String>();
    //图书类别下拉列表
    private final JComboBox<String> bookTypeComboBox = new JComboBox<String>();

    //初始化界面
    private void initFrame()
    {
        setTitle("图书添加窗口");     //设置标题
        FrameCommon.otFrameGeneralSettings(this);   //操作界面的一般设置

        final int AREA_WIDTH = 200; //文本区宽

        final int MODEL_X = FrameCommon.getOperatingFrameCenteredX(AREA_WIDTH), LABEL_Y = 60, AREA_Y = LABEL_Y + 35;

        //标签数组
        final JLabel[] labelArr = new JLabel[]{
                new JLabel("图书名"), new JLabel("图书作者"), new JLabel("图书价格"),
                new JLabel("图书描述"), new JLabel("图书类别id")
        };

        for(int i = 0, arrLen = labelArr.length; i < arrLen; i++) {
            labelArr[i].setBounds(MODEL_X, LABEL_Y + i * 70, 100, 30);
            labelArr[i].setFont(new Font(null, Font.PLAIN, 18));
            add(labelArr[i]);
        }

        //文本区数组设置
        for(int i = 0, arrLen = textAreaArr.length; i < arrLen; i++) {
            textAreaArr[i] = new JTextArea();
            textAreaArr[i].setBounds(MODEL_X, AREA_Y + i * 70, AREA_WIDTH, 22);
            textAreaArr[i].setFont(new Font(null, Font.PLAIN, 16));
            add(textAreaArr[i]);
        }

        String[][] typeInfos = FrameCommon.getBooksTypeInfo();  //获得图书类别信息

        for(String[] info : typeInfos) {
            bookTypeComboBox.addItem(info[1]);  //1是类别名
            typeNameIdMap.put(info[1], info[0]);    //保存和id的映射关系，key为类别名
        }
        bookTypeComboBox.setBounds(MODEL_X, textAreaArr[textAreaArr.length - 1].getY() + 70, 140, 26);

        final int BUT_WIDTH = 120;  //按钮宽
        addButton.setBounds(FrameCommon.getOperatingFrameCenteredX(BUT_WIDTH), bookTypeComboBox.getY() + 60, BUT_WIDTH, 38);
        addButton.setFont(new Font(null, Font.BOLD, 16));

        add(addButton);
        add(bookTypeComboBox);
        setVisible(true);
    }

    //组件事件初始化
    private void componentEventInit()
    {
        //添加按钮点击
        addButton.addActionListener(e -> {
            DatabaseClass dataBase = new DatabaseClass();   //数据库操作对象，不需要获得查询结果所以不用重写函数

            try {
                //确定sql语句
                String sql = "INSERT INTO `book` " +
                        "(`book_name`, `book_author`, book_price, book_desc, book_type_id) " +
                        "VALUES " +
                        "('" + textAreaArr[0].getText() + "', '" + textAreaArr[1].getText() + "', '" +
                        textAreaArr[2].getText() + "', '" + textAreaArr[3].getText() +
                        "', " + typeNameIdMap.get((String) bookTypeComboBox.getSelectedItem()) + ");";

                System.out.println(sql);

                //执行添加语句
                dataBase.dExecuteUpdate(sql);
            }
            catch (SQLException se) {
                throw new RuntimeException(se.toString() + "添加失败");
            }

        });
    }


    public BooksAddFrame() {
        initFrame();
        componentEventInit();
    }
}
