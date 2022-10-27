import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

//图书类别添加界面
class CategoryAddFrame extends JFrame
{
    //组件数组，1、3为文本区
    JComponent[] components = new JComponent[]{new JLabel("类别名称："), new JTextArea(), new JLabel("类别描述："), new JTextArea(), new JButton("添加")};
    //数据库操作对象，这里不需要获得查询后的结果，不需要重写其中的函数
    private final DatabaseClass database = new DatabaseClass();

    //界面初始化
    private void initFrame()
    {
        setTitle("类别添加界面");
        FrameCommon.otFrameGeneralSettings(this);   //操作窗口一般设置

        final int MODEL_X = 300, MODEL_Y = 100, MODEL_WIDTH = 200, MODEL_HEIGHT = 26;
        final Font font = new Font(null, Font.PLAIN, 18);

        components[0].setBounds(MODEL_X, MODEL_Y, MODEL_WIDTH, MODEL_HEIGHT);
        for(int i = 0, compsLength = components.length; i < compsLength; i++) {
            if(i != 0)
                components[i].setBounds(components[i - 1].getX(), components[i - 1].getY() + components[i - 1].getHeight() + 40,
                                        components[i - 1].getWidth(), components[i - 1].getHeight());

            components[i].setFont(font);
            add(components[i]);
        }

        setVisible(true);
    }

    //组件事件初始化
    private void componentEventInit() {
        //添加按钮点击事件
        ((JButton) components[4]).addActionListener(e -> {
            //类别名文本区
            JTextArea categoryNameTextArea = (JTextArea) components[1];
            //类别描述文本区
            JTextArea categoryDescTextArea = (JTextArea) components[3];

            try {
                //使用两个文本区中的内容作为sql语句的参数向数据库中添加图书类别
                database.dExecuteUpdate(
                        "INSERT INTO `books_type` (`book_type_name`, `book_type_desc`)\n" +
                            "VALUES (\""+ categoryNameTextArea.getText() +"\", \""+ categoryDescTextArea.getText() +"\");");
                //添加完毕后弹出提示框
                new JDialog() {
                    {
                        setTitle("提示");
                        setModal(true);
                        setLayout(null);
                        JLabel label = new JLabel("Added successfully");
                        label.setBounds(8, 20, 250, 50);
                        label.setFont(new Font(null, Font.BOLD, 14));
                        add(label);

                        Point point = FrameCommon.getScreenCentredPoint(250, 150);

                        setBounds(point.x, point.y, 250, 150);
                    }
                }.setVisible(true);
            }
            catch (SQLException se) {
                throw new RuntimeException(se.toString() + "添加数据");
            }
        });
    }

    public CategoryAddFrame() {
        initFrame();        //初始化界面
        componentEventInit();   //组件事件初始化
    }
}
