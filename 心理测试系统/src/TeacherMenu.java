import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
/*管理员菜单界面*/
public class TeacherMenu extends JFrame implements ActionListener {
    public String name;
    /*管理员菜单界面组件&布局*/
    public TeacherMenu(String name) {
        this.name = name;//将Main.java中的name(管理员名字)传入
        JLabel label1 = new JLabel();
        Container contentPane = getContentPane();
        contentPane.setLayout(null);//将容器的布局设为绝对布局;
        contentPane.add(label1);
        setTitle("在线心理测试系统-管理员端");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(500, 500);//界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中
        /*-----------功能菜单栏布局-----------*/
        JMenuBar menuBar1 = new JMenuBar();//创建菜单栏
        JMenu menu=new JMenu("功能菜单");//创建菜单
        JMenuItem menuItem1 = new JMenuItem("录入题目");menuItem1.addActionListener(this);//创建下拉菜单项并添加监听
        JMenuItem menuItem2 = new JMenuItem("生成试卷");menuItem2.addActionListener(this);//创建下拉菜单项并添加监听
        JMenuItem menuItem3 = new JMenuItem("设置测试等级及结果");menuItem3.addActionListener(this);//创建下拉菜单项并添加监听
        menu.add(menuItem1);
        menu.add(menuItem2);
        menu.add(menuItem3);
        menuBar1.add(menu);
        setJMenuBar(menuBar1);//将菜单栏添加至界面
        /*----------------------------------*/
        new Examination(contentPane);
    }
    /*监听录入题目,生成试卷,设置测试等级及结果按钮*/
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("录入题目")) {
            new EnterTitle().launch();
        }
        if (e.getActionCommand().equals("生成试卷")) {
            try {//一键生成try-catch环绕
                new GeneratingTestPaper(name, this).setVisible(true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (e.getActionCommand().equals("设置测试等级及结果")) {
            new SetUpTestResult().launch();
        }
    }
}
/*管理员菜单界面试卷事件*/
class Examination implements ActionListener {
    static ArrayList<String> arrayList = new ArrayList<>();//创建名为arrayList的集合对象，集合中存储的是String类型的元素
    static ArrayList<String>     names = new ArrayList<>();//创建名为names的集合对象，集合中存储的是String类型的元素

    public Examination(Container container) {
        ArrayList<JButton> buttons = new ArrayList<>();//创建名为buttons的集合对象，集合中存储的是JButton类型的元素
        JPanel jPane = new JPanel(new GridLayout(0, 1)/*创建具有指定行数和列数的网格布局。布局中的所有组件都具有相同的大小。 rows 和 cols 中的一个（但不是两个）可以为零，
                                                                    这意味着可以将任意数量的对象放置在一行或一列中。形参：rows——行数，零表示任意行数。cols – 列，零值表示任意数量的列*/);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(jPane);

        File file = new File("./lib/Examination");//创建File对象
        File[] files = file.listFiles();//获取该目录下所有文件和目录的路径，返回的是File数组类型
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()/*检查files[i]是否是文件夹，是则返回true*/) {
                arrayList.add(files[i].getName());//向集合中添加String属性的文件夹名称
            }
        }

        for (int i = 0; i < arrayList.size(); i++) {
            String[] line = arrayList.get(i).split("_");//用下划线"_"将其分割为字符串数组
            String name = line[0] + " 发布管理员：" + line[1] + "发布时间：" + line[2].replace("-", "/") + " " + line[3].replace("-", ":");
            names.add(name);//向集合中添加上述格式化字符串
            JButton jButton = new JButton(name);//创建按钮并将按钮命名为上述格式化字符串
            buttons.add(jButton);//向集合中添加上述按钮
        }

        for (int i = 0; i < buttons.size(); i++) {//循环向每个试卷按钮添加监听
            buttons.get(i).addActionListener(this);
        }

        for (int i = 0; i < buttons.size(); i++) {
            JPanel panel = new JPanel();
            panel.add(buttons.get(i),BorderLayout.CENTER);
            jPane.add(panel);
        }

        scrollPane.setBounds(50, 30, 400, 380);//滚动条面板(试卷窗口)尺寸
        container.add(scrollPane);//添加滚动条面板
    }
    /*监听各个试卷按钮*/
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < names.size(); i++) {
            if (e.getActionCommand().equals(names.get(i))) {
                try {//一键生成try-catch环绕
                    new TestCaseInHeart(arrayList.get(i)).launch();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
/*心理测试结果统计界面*/
class TestCaseInHeart extends JFrame {
    private ArrayList<String[]> lists = new ArrayList<>();
    private String name;//=arrayList.get(i)
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel defaultTable;

    public TestCaseInHeart(String name) {
        this.name = name;
        scrollPane = new JScrollPane();
        table = new JTable();
        defaultTable = (DefaultTableModel) table.getModel();
        scrollPane.setViewportView(table);
    }
    /*心理测试结果统计*/
    public void launch() throws IOException {
        File file = new File("./lib/Examination/" + name);//创建File对象
        File[] files = file.listFiles();//获取Examination/心理健康测试文件夹 下所有文件和目录的路径，返回的是File数组类型
        System.out.println("点击的试卷测试结果(评语)文件夹下所有文件和目录的路径:");
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
            FileInputStream fileInputStream = new FileInputStream(files[i]);//创建文件输入流用于获取指定文件数据
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));//创建缓冲字符输入流
            String line;//临时局部变量
            while ((line = br.readLine())/*读取一行字符串*/ != null) {
                String[] lines = line.split("\\s+");//用空格将其分割为字符串数组
                String[] a = new String[6];
                a[0] = lines[0]/*专业*/;
                a[1] = lines[1]/*班级*/;
                a[2] = lines[2]/*学号*/;
                a[3] = lines[3]/*测试人*/;
                a[4] = lines[4]/*分数*/;
                a[5] = lines[5]/*评语*/;
                lists.add(a);
            }
        }
        /*-----------------向表格添加六列-----------------*/
        defaultTable.addColumn("专业");
        defaultTable.addColumn("班级");
        defaultTable.addColumn("学号");
        defaultTable.addColumn("测试人");
        defaultTable.addColumn("分数");
        defaultTable.addColumn("心理健康评语");
        /*----------------------------------------------*/

        for (int i = 0; i < lists.size(); i++) {
            defaultTable.addRow(lists.get(i));
        }
        TableColumnModel tableColumnModel = table.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(30);//将此列的初始宽度设置为30
        tableColumnModel.getColumn(1).setPreferredWidth(30);
        tableColumnModel.getColumn(2).setPreferredWidth(30);
        tableColumnModel.getColumn(3).setPreferredWidth(30);
        tableColumnModel.getColumn(4).setPreferredWidth(30);
        tableColumnModel.getColumn(5).setPreferredWidth(330);
        add(scrollPane);

        setTitle("心理测试结果统计");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(800, 400);//界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中
    }
}