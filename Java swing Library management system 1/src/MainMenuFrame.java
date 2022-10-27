import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主菜单界面类，在登录成功时创建对象
 *
 * @see JFrame
 */
public class MainMenuFrame extends JFrame
{
    private static final int FRAMEWIDTH = 1000, FRAMEHEIGHT = 700;
    private int frameX, frameY;

    private final JButton booksManageBut = new JButton("图书管理");      //图书管理按钮
    private final JButton booksAddBut = new JButton("图书添加");         //图书添加按钮
    private final JButton categoryManageBut = new JButton("类别管理");   //类别管理按钮
    private final JButton categoryAddBut = new JButton("类别添加");      //类别添加按钮

    //初始化界面
    private void initFrame()
    {
        //设置窗口口属性
        setTitle("主菜单界面");      //设置窗口标题

        //确定窗口位置
        Point frameXY = FrameCommon.getScreenCentredPoint(FRAMEWIDTH, FRAMEHEIGHT);

        frameX = (int) frameXY.getX();
        frameY = (int) frameXY.getY();

        setBounds(frameX, frameY, FRAMEWIDTH, FRAMEHEIGHT);  //设置窗口位置、宽高
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      //设置点击关闭按钮是隐藏窗口，并停止程序
        setResizable(false);    //设置不可改变窗口大小
        setLayout(null);        //设置绝对布局

        //设置组件
        final int BUTTONWIDTH = 150, BUTTONHEIGHT = 75;     //每个按钮宽高
        final int BUTTONMARGIN = 20;                        //按钮之间的距离

        final int BUTTONALLWIDTH = (BUTTONWIDTH << 2) + (BUTTONMARGIN * 3); //按钮总宽度(包括间距)

        //得到所有按钮位于窗口居中的位置
        final Point buttonAllXY = FrameCommon.getFrameCentredPoint(FRAMEWIDTH, FRAMEHEIGHT, BUTTONALLWIDTH, BUTTONHEIGHT);

        final int buttonAllX = (int) buttonAllXY.getX();
        final int buttonALLY = (int) buttonAllXY.getY();

        AbstractButton[] buttons = {booksManageBut, booksAddBut, categoryManageBut, categoryAddBut};

        Font buttonFont = new Font("宋体", Font.BOLD, 18);
        //设置四个按钮的坐标和字体属性
        for(int i = 0; i < 4; i++) {
            buttons[i].setBounds(buttonAllX + (BUTTONWIDTH + BUTTONMARGIN) * i, buttonALLY, BUTTONWIDTH, BUTTONHEIGHT);
            buttons[i].setFont(buttonFont);
            add(buttons[i]);    //添加按钮组件
        }

        setVisible(true);
    }

    //组件事件初始化
    private void componentEventInit()
    {
        //图书管理按钮点击
        booksManageBut.addActionListener(e -> {
           new BooksManageFrame(); //图书管理界面
        });

        //图书添加按钮点击
        booksAddBut.addActionListener(e -> {
            new BooksAddFrame();
        });

        //类别管理按钮点击
        categoryManageBut.addActionListener(e -> {
            new CategoryManageFrame();  //类别管理界面
        });

        //类别添加按钮点击
        categoryAddBut.addActionListener(e -> {
            new CategoryAddFrame();
        });
    }

    /**
     * 构造函数，初始化界面
     */
    public MainMenuFrame() {
        initFrame();    //界面初始化
        componentEventInit();   //组件事件初始化
    }
}
