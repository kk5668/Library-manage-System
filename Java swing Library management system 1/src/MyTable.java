
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * 我的表格类
 * <p>
 * 封装了程序中需要用到的操作表格方法
 */
public class MyTable extends JTable
{
    private DefaultTableModel tableModel = null;    //默认表格模型引用
    private Object[][] tRowData = null;
    private Object[] tColumnNames = null;

    /**
     * 构造函数
     * @param rowData 新表的数据
     * @param columnNames 每列的名称
     */
    public MyTable(final Object[][] rowData, final Object[] columnNames)
    {
        tRowData = rowData;         //将参数赋给成员
        tColumnNames = columnNames;

        //表格模型初始化
        tableModel = new DefaultTableModel(tRowData, tColumnNames) {
            //将列为0的列设为不可编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        //设置表格的表格模型
        setModel(tableModel);

        setFont(new Font(null, Font.PLAIN, 16));

        getTableHeader().setReorderingAllowed(false); //不可整列移动
    }

    /**
     * 为表格添加一行
     *
     * @param objects 要添加的内容
     */
    public void addRow(final Object[] objects) {
        tableModel.addRow(objects);
        setModel(tableModel);
    }

    /**
     * 为表格添加多行
     * @param objects 要添加的多行内容（二维数组）
     */
    public void addRows(final Object[][] objects) {
        for (Object[] object : objects)
            tableModel.addRow(object);
        setModel(tableModel);
    }

    /**
     * 为表格删除一行
     *
     * @param row 要删除行的行号
     */
    public void removeRow(final int row) {
        tableModel.removeRow(row);
    }

    /**
     * 清空所有行
     */
    public void clearRow() {
        int rowCount = tableModel.getRowCount() - 1;
        for(int i = rowCount; i >= 0; i--)
            removeRow(i);
    }

    /**
     * 更新所有行函数（在添加多行前清空所有行）
     */
    public void updateAllRow(Object[][] objects) {
        clearRow();
        addRows(objects);
    }


}
