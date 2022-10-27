import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
界面共同类
拥有界面都需要用到的函数和变量常量
*/
class FrameCommon
{
    //保存屏幕的宽高
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * 电脑屏幕的宽度
     */
    public static final int SCREENWIDTH = (int)screenSize.getWidth();

    /**
     * 电脑屏幕的高度
     */
    public static final int SCREENHEIGHT = (int)screenSize.getHeight();

    /**
     * 操作界面的宽高常量
     */
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 600;


    /**
     * 提供要对准界面和目标界面的信息，得到目标界面基于要对准界面居中的位置
     *
     * @param frameWidth 要对准界面的宽度
     * @param frameHeight 要对准界面的高度
     * @param targetWidth 目标界面的宽度
     * @param targetHeight 目标界面的高度
     * @return 目标界面的位置
     */
    public static Point getFrameCentredPoint(int frameWidth, int frameHeight,
                                             int targetWidth, int targetHeight)
    {
        return new Point((frameWidth >> 1) - (targetWidth >> 1),
                            (frameHeight >> 1) - (targetHeight >> 1));
    }

    /**
     * 获得操作界面居中X
     *
     * @param targetWidth 要居中组件的宽
     * @return 使组件在操作界面上水平居中的X坐标
     */
    public static int getOperatingFrameCenteredX(int targetWidth) {
        return (int) getFrameCentredPoint(FRAME_WIDTH, FRAME_HEIGHT, targetWidth, 0).getX();
    }

    /**
     * 提供目标界面的宽高，得到目标界面位于电脑屏幕居中的位置
     *
     * @param targetWidth 目标界面宽度
     * @param targetHeight 目标界面高度
     * @return 目标界面位于屏幕居中的位置
     */
    public static Point getScreenCentredPoint(int targetWidth, int targetHeight) {
        return getFrameCentredPoint(SCREENWIDTH, SCREENHEIGHT, targetWidth, targetHeight);
    }

    /**
     * 得到操作窗口中组件位于窗口居中的位置
     *
     * @param targetWidth   要居中组件的宽度
     * @param targetHeight  要居中组件的高度
     * @return 要居中组件位于操作窗口居中的位置
     */
    public static Point getModuleCentredPoint(int targetWidth, int targetHeight) {
        return getFrameCentredPoint(FRAME_WIDTH, FRAME_HEIGHT, targetWidth, targetHeight);
    }

    /**
     * 操作界面的一般设置
     * @param frame 要设置的JFrame
     */
    public static void otFrameGeneralSettings(JFrame frame) {
        //确定窗口位置
        Point frameXY = FrameCommon.getScreenCentredPoint(FRAME_WIDTH, FRAME_HEIGHT);

        frame.setBounds((int) frameXY.getX(), (int) frameXY.getY(), FRAME_WIDTH, FRAME_HEIGHT);  //设置窗口位置、宽高
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   //关闭窗口释放窗口资源
        frame.setResizable(false);    //设置不可改变窗口大小
        frame.setLayout(null);        //设置绝对布局
    }

    //获得图书类别
    public static String[][] getBooksTypeInfo() {
        //图书类别下拉列表
        //用于得到图书类别的查询结果的数据库操作对象
        DatabaseClass booksTypeDataBase = new DatabaseClass() {
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

        return booksTypeDataBase.getQueryResult("SELECT * FROM books_type;");
    }
}
