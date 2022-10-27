import javax.swing.*;
import java.awt.*;

//登录界面类，继承JFrame
class LoginFrame extends JFrame {

    private JTextField usernameTextField = null;        //用户名文本区
    private JTextField passwordTextField = null;        //密码文本区
    private JButton loginButton = null;                 //登录按钮

    private static final int FRAMEWIDTH = 700, FRAMEHEIGHT = 520;     //窗口宽高
    private static int frameX, frameY;                   //窗口位置

    //数据库操作对象
    //这里不需要进行获取查询结果的操作，所以不需要重写其中的函数
    private final DatabaseClass database = new DatabaseClass();

    //窗口初始化
    private void initFrame() {
        //设置窗口属性
        setTitle("登录界面");   //设置窗口标题

        //得到窗口位于屏幕居中的位置
        Point frameXY = FrameCommon.getScreenCentredPoint(FRAMEWIDTH, FRAMEHEIGHT);
        frameX = (int)frameXY.getX();
        frameY = (int)frameXY.getY();

        setBounds(frameX, frameY, FRAMEWIDTH, FRAMEHEIGHT);  //设置窗口位置、宽高
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置点击关闭按钮是隐藏窗口，并停止程序
        setResizable(false);    //设置不可改变窗口大小
        setLayout(null);        //设置绝对布局

        final int usernameLableWidth = 80, usernameLableHeight = 30;   //保存用户名标签宽高
        final int textFieldWidth = 200;                                //文本区宽度
        final int LabletextFieldSpacing = 20;                          //标签和文本区的间距
        final int usernameLableX =                                     //用户名标签位置
                (FRAMEWIDTH >> 1) - ((usernameLableWidth + LabletextFieldSpacing + textFieldWidth) >> 1), usernameLableY = 180;

        Font lableFont = new Font(null, Font.PLAIN, 18);    //标签文本属性

        //添加和设置组件
        JLabel usernameLable = new JLabel("用户名：");              //用户名标签组件
        usernameLable.setFont(lableFont);                               //设置文字属性
        usernameLable.setBounds(usernameLableX, usernameLableY, usernameLableWidth, usernameLableHeight);  //设置位置、宽高

        JLabel passwordLable = new JLabel("密码：");                //密码标签组件
        passwordLable.setFont(lableFont);
        passwordLable.setBounds(usernameLableX, usernameLableY + 56, usernameLableWidth, usernameLableHeight);

        Font textFieldFont = new Font(null, Font.BOLD, 16);   //文本区文本属性

        usernameTextField = new JTextField();        //用户名文本区
        usernameTextField.setFont(textFieldFont);
        usernameTextField.setBounds(usernameLableX + usernameLableWidth + LabletextFieldSpacing, usernameLableY, textFieldWidth, usernameLableHeight);

        passwordTextField = new JTextField();        //密码文本区
        passwordTextField.setFont(textFieldFont);
        passwordTextField.setBounds(usernameTextField.getX(), passwordLable.getY(), textFieldWidth, usernameLableHeight);

        loginButton = new JButton("登录");       //登录按钮
        final int buttonWidth = 100;    //按钮宽度
        loginButton.setFont(new Font("宋体", Font.BOLD, 22));
        loginButton.setBounds(usernameLableX + ((usernameLableWidth + LabletextFieldSpacing + textFieldWidth) >> 1) - (buttonWidth >> 1),
                passwordTextField.getY() + 70, buttonWidth, 50);

        JLabel MSLable = new JLabel("图书管理系统");  //"图书管理系统"标签
        MSLable.setFont(new Font("黑体", Font.BOLD, 46));
        final int MSLableWidth = 293;       //图书管理系统标签宽度
        MSLable.setBounds((FRAMEWIDTH >> 1) - (MSLableWidth >> 1), 60, MSLableWidth, 60);

        //将组件添加进窗口容器
        add(usernameLable);
        add(passwordLable);
        add(usernameTextField);
        add(passwordTextField);
        add(loginButton);
        add(MSLable);

        setVisible(true);       //设置显示窗口
    }

    //组件事件初始化
    private void componentEventInit() {
        //监听登录按钮点击
        loginButton.addActionListener(e -> {
            //将两个文本区中的内容作为要执行的sql语句的一部分，然后得到数据库中是否存在文本区内容输入的用户信息
            boolean flag = database.isResultExist(
                "SELECT * FROM `user` WHERE user_name = \""+ usernameTextField.getText() +"\" AND `password` = \""+ passwordTextField.getText() +"\";");
            // boolean flag=true;
            //如果输入信息不对则弹出提示对话框
            if(!flag) {
                new JDialog() { {
                        setTitle("警告");
                        setModal(true);
                        setLayout(null);
                        JLabel label = new JLabel("wrong user name or password!");
                        label.setBounds(8, 20, 250, 50);
                        label.setFont(new Font(null, Font.BOLD, 14));
                        add(label);

                        Point point = FrameCommon.getScreenCentredPoint(250, 150);

                        setBounds(point.x, point.y, 250, 150);
                    }
                }.setVisible(true);
            }
            else {
                new MainMenuFrame();
                dispose();  //关闭当前窗口
            }
        });
    }

    /**
     * 构造函数用于调用初始化函数，初始化程序
     */
    public LoginFrame() {
        initFrame();            //窗口初始化
        componentEventInit();   //组件事件初始化

    }

}
