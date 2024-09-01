import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*学生菜单界面布局*/
public class StudentMenu extends JFrame {
    public StudentMenu() {
        JLabel label1 = new JLabel();
        Container contentPane = getContentPane();//初始化容器并命名该容器为contentPane
        contentPane.add(label1);
        contentPane.setLayout(null);//将容器的布局设为绝对布局
        setTitle("在线心理测试系统-学生端");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(500, 500);//界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中
        new ExaminationTest(contentPane, this);
    }
}

/*学生菜单界面试卷事件*/
class ExaminationTest extends JFrame implements ActionListener {
    static ArrayList<String> arrayList = new ArrayList<>();//创建名为arrayList的集合对象，集合中存储的是String类型的元素，存放对应试卷评语文件夹的名称
    static ArrayList<String>     names = new ArrayList<>();//创建名为names的集合对象，集合中存储的是String类型的元素，存放对应试卷名称
    private Container container1;
    /*试卷布局*/
    public ExaminationTest(Container container, Container container1) {
        this.container1 = container1;
        ArrayList<JButton> buttons = new ArrayList<>();
        JPanel jPane = new JPanel(new GridLayout(0, 1)/*创建具有指定行数和列数的网格布局。布局中的所有组件都具有相同的大小。 rows 和 cols 中的一个（但不是两个）可以为零，
                                                                    这意味着可以将任意数量的对象放置在一行或一列中。形参：rows——行数，0表示任意行数。cols——列数，0表示任意数量的列*/);
        JScrollPane scrollPane = new JScrollPane();//创建一个带滚动条的面板容器对象
        scrollPane.setViewportView(jPane);

        File file = new File("./lib/Examination");//创建File对象
        File[] files = file.listFiles();//获取该目录下所有文件和目录的绝对路径，返回的是File数组
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()/*检查files[i]是否是文件夹，是则返回true*/) {
                arrayList.add(files[i].getName());//向集合中添加String属性的文件夹名称
            }
        }

        for (int i = 0; i < arrayList.size(); i++) {//for循环为每个试卷按钮命名
            String[] line = arrayList.get(i).split("_");//用下划线"_"将文件名分割为字符串数组
            String name = line[0] + " 发布管理员：" + line[1] + "发布时间：" + line[2].replace("-", "/") + " " + line[3].replace("-", ":");
            names.add(name);//向集合中添加上述格式化字符串
            JButton jButton = new JButton(name);//创建按钮并将按钮命名为上述格式化字符串
            buttons.add(jButton);//向集合中添加上述按钮
        }

        for (int i = 0; i < buttons.size(); i++) {//for循环为每个试卷按钮添加监听器
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
                File file = new File("./lib/Examination/"
                        + arrayList.get(i)
                        + "/" + Information.getSpeciality()
                        + "_" + Information.getClasses()
                        + "_" + Information.getSno()
                        + "_" + Information.getSname() + ".txt");//创建File对象
                if (file.exists()) {//若指定文件存在(点击了已答试卷)，则打开测试结果界面
                    try {//一键生成try-catch环绕
                        new TestCaseInHeart(arrayList.get(i)).launch();//打开测试结果界面
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    container1.setVisible(false);//使界面不可视
                    new Information(arrayList.get(i)).launch();//打开完善资料界面
                }
            }
        }
    }
}

/*在线心理测试答题界面*/
class OnlineTest extends JFrame {
    private String fileName;
    private ArrayList<String[]> lists = new ArrayList<>();
    private Container container;
    private JScrollPane scrollPane;
    private JPanel jPane;
    private ArrayList<JRadioButton[]> radioButtons = new ArrayList<>();
    private JButton jButton;
    private int score = 0;
    private Container company;
    private static ArrayList<String> comment = new ArrayList<>();
    private JLabel jl1;/*计时器*/

    static {
        try {//一键生成try-catch环绕
            readComment();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*在线测试界面组件*/
    OnlineTest(String fileName, Container company) {
        this.fileName = fileName;
        this.company = company;
        container = getContentPane();
        scrollPane = new JScrollPane();
        jButton = new JButton("提交");

        try {//一键生成try-catch环绕
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jl1 = new JLabel();
        jPane = new JPanel(new GridLayout(0, 1)/*在线测试界面布局*/);
    }
    /*在线测试界面布局*/
    public void launch() {
        scrollPane.setViewportView(jPane);
        setTitle("在线心理测试");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(420, 530);//在线心理测试界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中
        setLayout(null);//将容器的布局设为绝对布局

        new Timer().timer();/*启动计时器*/

        container.add(jl1);
        jl1.setBounds(7, 0, 200, 30);

        container.add(scrollPane);
        scrollPane.setBounds(7, 30, 390, 400);

        container.add(jButton);
        jButton.setBounds(127, 450, 150, 30);

        for (int i = 0; i < lists.size(); i++) {
            JPanel panel = new JPanel();
            JPanel panel1 = new JPanel(new GridLayout(0, 2));
            JPanel panel2 = new JPanel();
            JPanel panel3 = new JPanel(new BorderLayout());

            JLabel jl1 = new JLabel((i+1)+"."+lists.get(i)[0]);//第i+1个题目
            JRadioButton radioButton1 = new JRadioButton("A：" + lists.get(i)[1]);//选项A答案
            JRadioButton radioButton2 = new JRadioButton("B：" + lists.get(i)[3]);//选项B答案
            JRadioButton radioButton3 = new JRadioButton("C：" + lists.get(i)[5]);//选项C答案
            JRadioButton radioButton4 = new JRadioButton("D：" + lists.get(i)[7]);//选项D答案
            /*将四个单选按钮添加至同一组中*/
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(radioButton1);
            buttonGroup.add(radioButton2);
            buttonGroup.add(radioButton3);
            buttonGroup.add(radioButton4);
            /*------------------------*/
            panel1.add(radioButton1);
            panel1.add(radioButton2);
            panel1.add(radioButton3);
            panel1.add(radioButton4);

            radioButtons.add(new JRadioButton[]{radioButton1, radioButton2, radioButton3, radioButton4});
            panel2.add(jl1, BorderLayout.CENTER);
            panel2.setPreferredSize(new Dimension(280, 50));
            panel.add(panel3);
            panel3.add(panel2, BorderLayout.NORTH);
            panel3.add(panel1, BorderLayout.CENTER);
            jPane.add(panel);
        }
        /*监听提交按钮*/
        jButton.addActionListener(e -> {
            if (e.getActionCommand().equals("提交")) {
                boolean flag = true;
                for (int i = 0; i < radioButtons.size(); i++) {//循环检测每个选择题是否有未答
                    if ((!radioButtons.get(i)[0].isSelected()) && (!radioButtons.get(i)[1].isSelected()) && (!radioButtons.get(i)[2].isSelected()) && (!radioButtons.get(i)[3].isSelected())) {//isSelected方法:返回按钮的状态。如果选择了按钮，则为true，否则为false
                        JOptionPane.showMessageDialog(null, "还有题目未答！最开始一个未答题目为第"+(i+1)+"题");
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (radioButtons.get(i)[0].isSelected()) {//parseInt方法:字符串强转十进制数字
                            score += Integer.parseInt(lists.get(i)[2]);//选项A分值
                        }
                        if (radioButtons.get(i)[1].isSelected()) {
                            score += Integer.parseInt(lists.get(i)[4]);//选项B分值
                        }
                        if (radioButtons.get(i)[2].isSelected()) {
                            score += Integer.parseInt(lists.get(i)[6]);//选项C分值
                        }
                        if (radioButtons.get(i)[3].isSelected()) {
                            score += Integer.parseInt(lists.get(i)[8]);//选项D分值
                        }
                    }

                    JOptionPane.showMessageDialog(null, "提交完毕！");
                    try {//一键生成try-catch环绕
                        writeTest();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    score = 0;
                    dispose();//释放资源（关闭当前框）
                    ExaminationTest.names.clear();
                    ExaminationTest.arrayList.clear();
                    company.setVisible(false);//使界面不可视
                    new StudentMenu();//回到学生菜单
                }
            }
        });
    }
    /*写入(创建)测试结果文件*/
    public void writeTest() throws IOException {
        File file = new File("./lib/Examination/" + fileName + "/"
                + Information.getSpeciality()
                + "_" + Information.getClasses()
                + "_" + Information.getSno()
                + "_" + Information.getSname() + ".txt");//创建File对象

        if (!file.exists()) {//若指定文件不存在，则创建新文件
            try {//一键生成try-catch环绕
                file.createNewFile();//创建新文件，文件名为：专业_班级_学号_姓名.txt
                System.out.println("新测试结果(评语)路径为:"+file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");//指定以UTF-8格式写入文件
        String line = null;
        if (score >= 0 && score <= 16) {//若0≤分数≤16，则判定为可能有轻度的心理问题，可尝试着进行自我心理咨询。
            line = comment.get(0);
        } else if (score > 16 && score <= 60) {//若16<分数≤60，则判定为有较严重的心理问题，应考虑到医院进行心理咨询。
            line = comment.get(1);
        }
        writer.write(Information.getSpeciality() + " " + Information.getClasses() + " " + Information.getSno() + " " + Information.getSname() + " " + score + " " + line);
        writer.close();
    }
    /*读取测试试卷文件*/
    public void readFile() throws IOException {
        File file = new File("./lib/TestPaper/" + fileName + ".txt");//创建File对象
        FileInputStream fileInputStream = new FileInputStream(file);//创建文件输入流用于获取指定文件数据
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));//创建缓冲字符输入流
        String line;//临时局部变量
        while ((line = br.readLine()/*读取一行字符串*/ ) != null) {
            String[] lines = line.split("\\s+");//用空格将其分割为字符串数组
            String[] a = new String[9];
            a[0] = lines[0];//问题
            a[1] = lines[1];//选项A答案
            a[2] = lines[2];//选项A分值
            a[3] = lines[3];//选项B答案
            a[4] = lines[4];//选项B分值
            a[5] = lines[5];//选项C答案
            a[6] = lines[6];//选项C分值
            a[7] = lines[7];//选项D答案
            a[8] = lines[8];//选项D分值
            lists.add(a);
        }
    }
    /*读取测试评语文件*/
    public static void readComment() throws IOException {
        File file = new File("./lib/comment.txt");//创建File对象
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));//创建缓冲字符输入流
        String line;//临时局部变量
        while ((line = br.readLine()/*读取一行字符串*/ ) != null) {
            comment.add(line);
        }
    }
    /*定时器*/
    public class Timer {
        private ScheduledThreadPoolExecutor scheduled;

        public void timer() {
            final long Initial_time=System.currentTimeMillis()/*以毫秒为单位返回当前时间与1970年1月1日0点之间的差值*/+1200000;//该值为:开始答题时间+20分钟
            scheduled.scheduleAtFixedRate(new Runnable() {//周期性动作
                public void run() {
                    long time = (Initial_time - System.currentTimeMillis()) / 1000;//(开始答题时间+20分钟)-当前时间
                    if (time <= 0) {//若剩余时间≤0
                        if (scheduled != null) {
                            scheduled.shutdownNow();//取消计时任务(结束周期性动作)
                            scheduled = null;

                                for (int i = 0; i < lists.size(); i++) {//按目前答题进度计算得分，未完成的选择题为0分
                                    if (radioButtons.get(i)[0].isSelected()) {
                                        score += Integer.parseInt(lists.get(i)[2]);//选项A分值
                                    }
                                    if (radioButtons.get(i)[1].isSelected()) {
                                        score += Integer.parseInt(lists.get(i)[4]);//选项B分值
                                    }
                                    if (radioButtons.get(i)[2].isSelected()) {
                                        score += Integer.parseInt(lists.get(i)[6]);//选项C分值
                                    }
                                    if (radioButtons.get(i)[3].isSelected()) {
                                        score += Integer.parseInt(lists.get(i)[8]);//选项D分值
                                    }
                                }

                                JOptionPane.showMessageDialog(null, "测试时间到！已自动提交\n未完成的选择题记为0分");
                                try {//一键生成try-catch环绕
                                    writeTest();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                score = 0;//重置分数以便下次测试使用
                                dispose();//释放资源（关闭当前框）
                                ExaminationTest.names.clear();
                                ExaminationTest.arrayList.clear();
                                //company.setVisible(false);//使界面不可视
                                new StudentMenu();//回到学生菜单
                        }
                        return ;
                    }

                    long minute = time / 60;
                    long seconds = time - minute * 60;
                    String stringBuilder = "<html>距离测试结束还有" + minute + "分 " + seconds + "秒 " + "<html>";
                    jl1.setText(stringBuilder);
                }
            }, 0/*首次执行的延迟时间*/, 1/*周期执行的间隔时间*/, TimeUnit.SECONDS);
        }

        public Timer() {
            scheduled = new ScheduledThreadPoolExecutor(2);
        }
    }
}
