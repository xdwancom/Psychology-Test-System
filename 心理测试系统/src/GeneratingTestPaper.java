import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GeneratingTestPaper extends JFrame {
    private ArrayList<String[]> list = new ArrayList<>();
    private JTable table;
    private String name;
    private String fileName;

    public GeneratingTestPaper(String name, Container container) throws IOException {
        this.name = name;//将TeacherMenu.java中的name(管理员名字)传入

        read_QuestionBank();

        setTitle("心理测试题库");
        setResizable(false);//窗口大小不可变
        setSize(1000, 500);//界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中
        setLayout(null);//将容器的布局设为绝对布局

        JScrollPane scrollPane = new JScrollPane();//支持滚动
        scrollPane.setBounds(0, 0, 1000, 420);//滚动条面板大小
        add(scrollPane);

        String[] columnNames = {"题目", "选项A", "分数", "选项B", "分数", "选项C", "分数", "选项D", "分数"};
        table = new JTable();//自定义表格

        DefaultTableModel defaultTable = (DefaultTableModel) table.getModel();
        for (String columnName : columnNames) {
            defaultTable.addColumn(columnName);
        }

        for (String[] lists : list) {//循环添加题目到界面(表格)中
            defaultTable.addRow(lists);
        }

        TableColumnModel tableColumnModel = table.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(200);

        tableColumnModel.getColumn(1).setPreferredWidth(100);
        tableColumnModel.getColumn(2).setPreferredWidth(25);

        tableColumnModel.getColumn(3).setPreferredWidth(125);
        tableColumnModel.getColumn(4).setPreferredWidth(25);

        tableColumnModel.getColumn(5).setPreferredWidth(125);
        tableColumnModel.getColumn(6).setPreferredWidth(25);

        tableColumnModel.getColumn(7).setPreferredWidth(125);
        tableColumnModel.getColumn(8).setPreferredWidth(25);

        scrollPane.setViewportView(table);

        JPanel buttonPanel = new JPanel(null);//底部存放三个按钮的面板
        add(buttonPanel);//将该面板添加到container容器中
        buttonPanel.setBounds(0, 420, 1000, 80);

        JButton selectAllButton = new JButton("全部选择");
        selectAllButton.setBounds(300, 5, 120, 30);
        buttonPanel.add(selectAllButton);
        selectAllButton.addActionListener/*监听全部选择按钮*/(e -> {
            table.selectAll()/*选择所有的行*/;
        });

        JButton clearSelectionButton = new JButton("取消选择");
        clearSelectionButton.setBounds(450, 5, 120, 30);
        buttonPanel.add(clearSelectionButton);
        clearSelectionButton.addActionListener/*监听取消选择按钮*/(e -> {
            table.clearSelection()/*取消选择所有选定的列和行*/;
        });

        JButton determineGenerated = new JButton("确定生成");
        determineGenerated.setBounds(600, 5, 120, 30);
        buttonPanel.add(determineGenerated);
        determineGenerated.addActionListener/*监听确定生成按钮*/(e -> {
            if (table.getSelectedRowCount()/*获取选定的行数*/ > 20) {//若选择的题目>20道,则弹出警告
                JOptionPane.showMessageDialog(null, "最多选择20道题！");//弹出警告
            } else {
                if (createTestPaperResultsFolder() && createTestPaper()) {//若成功生成存放该心理测试试卷结果的文件夹&心理测试试卷txt文件,则将选定的题目写入新创建的心理测试试卷txt文件中
                    try {//一键生成try-catch环绕
                        writeTestPaper();//将选定的题目写入新创建的心理测试试卷txt文件中
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "生成成功！");
                    dispose();//释放资源（关闭当前框）
                    Examination.arrayList.clear();
                    Examination.names.clear();
                    container.setVisible(false);//使添加试卷前的管理员界面不可视
                    new TeacherMenu(name);//打开添加试卷后的管理员界面
                } else {
                    JOptionPane.showMessageDialog(null, "生成失败！");
                }
            }
        });
    }
    /*读取题库内容*/
    public void read_QuestionBank() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("./lib/QuestionBank.txt");//创建文件输入流用于获取指定文件数据
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));//创建缓冲字符输入流
        String line;//临时局部变量
        while ((line = br.readLine()/*读取一行字符串*/ ) != null) {
            String[] arr = line.split("\\s+");//用空格将其分割为字符串数组
            String[] a = new String[9];
            int j = 0;
            for (int i = 0; i < arr.length; i++) {
                if (i % 3 != 1) {//不将A,B,C,D四个字母作为字符串写入list集合
                    a[j] = arr[i];
                    j++;
                }
            }
            list.add(a);
        }
        br.close();
        fileInputStream.close();
    }
    /*生成存放该心理测试试卷结果的文件夹*/
    public boolean createTestPaperResultsFolder() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");//生成时间
        Date date = new Date(System.currentTimeMillis()/*以毫秒为单位返回当前时间与1970年1月1日0点之间的差值*/);
        fileName = "心理测试_" + name + "_" + simpleDateFormat.format(date)/*将Date类型格式化为日期时间字符串*/;//心理测试试卷(文件夹)名称
        File file = new File("./lib/Examination/" + fileName);//创建File对象
        if (!file.exists()) {//若此路径名表示的文件或目录不存在
            return file.mkdir()/*创建由此路径命名的文件夹*/;//当目录已创建时返回值为true
        }
        return false;
    }
    /*生成心理测试试卷txt文件*/
    public boolean createTestPaper() {
        File file = new File("./lib/TestPaper/" + fileName + ".txt");//创建File对象
        if (!file.exists()) {
            try {//一键生成try-catch环绕
                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*将选定的题目写入新创建的心理测试试卷txt文件中*/
    public void writeTestPaper() throws IOException {
        File file = new File("./lib/TestPaper/" + fileName + ".txt");//创建File对象
        FileOutputStream fileOutputStream = new FileOutputStream(file);//以覆盖的形式创建文件输出流
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");//指定以UTF-8格式写入文件
        int[] indexRow = table.getSelectedRows()/*返回所有选定行的索引*/;
        for (int i : indexRow) {
            for (int j = 0; j < table.getColumnCount/*返回列模型中的列数*/(); j++) {
                writer.write((String) table.getValueAt(i, j)/*返回行和列的单元格值,形参:(要查询其值的行,要查询其值的列)*/+" ");
            }
            writer.write("\n");
        }
        writer.close();
    }
}
