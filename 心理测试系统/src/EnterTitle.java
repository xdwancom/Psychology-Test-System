import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EnterTitle extends JFrame implements ActionListener {
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4;
    private JTextField jtf1, jtf2, jtf3, jtf4;//选择题选项框
    private JComboBox jcb1, jcb2, jcb3, jcb4;//下拉列表框
    private JTextArea jTextArea;//题目框
    private JScrollPane jScrollPane;
    private Container container;
    private JButton jb1, jb2;//录入，重置按钮
    /*录入题目界面组件*/
    public  EnterTitle() {
        container = getContentPane();
        container.setLayout(null);//将容器的布局设为绝对布局
        jScrollPane = new JScrollPane();

        jLabel1 = new JLabel("A：");
        jLabel2 = new JLabel("B：");
        jLabel3 = new JLabel("C：");
        jLabel4 = new JLabel("D：");

        jtf1 = new JTextField();
        jtf2 = new JTextField();
        jtf3 = new JTextField();
        jtf4 = new JTextField();

        jcb1 = new JComboBox();
        jcb2 = new JComboBox();
        jcb3 = new JComboBox();
        jcb4 = new JComboBox();

        jcb1.addActionListener(this);
        jcb2.addActionListener(this);
        jcb3.addActionListener(this);
        jcb4.addActionListener(this);

        jb1 = new JButton("录入");
        jb1.setBorder(BorderFactory.createRaisedBevelBorder());
        jb1.addActionListener(this);

        jb2 = new JButton("重置");
        jb2.setBorder(BorderFactory.createRaisedBevelBorder());
        jb2.addActionListener(this);

        jTextArea = new JTextArea();
        jTextArea.setText("请输入题目");
        jScrollPane.setViewportView(jTextArea);
    }
    /*录入题目界面布局*/
    public void launch() {
        jcb1.addItem("请选择分数");
        jcb1.addItem("1");
        jcb1.addItem("2");
        jcb1.addItem("3");
        jcb1.addItem("4");
        jcb1.addItem("5");

        jcb2.addItem("请选择分数");
        jcb2.addItem("1");
        jcb2.addItem("2");
        jcb2.addItem("3");
        jcb2.addItem("4");
        jcb2.addItem("5");

        jcb3.addItem("请选择分数");
        jcb3.addItem("1");
        jcb3.addItem("2");
        jcb3.addItem("3");
        jcb3.addItem("4");
        jcb3.addItem("5");

        jcb4.addItem("请选择分数");
        jcb4.addItem("1");
        jcb4.addItem("2");
        jcb4.addItem("3");
        jcb4.addItem("4");
        jcb4.addItem("5");

        jScrollPane.setBounds(20, 20, 345, 150);

        jLabel1.setBounds(20, 180, 30, 30);
        jtf1.setBounds(40, 180, 225, 30);
        jcb1.setBounds(275, 180, 90, 30);

        jLabel2.setBounds(20, 220, 30, 30);
        jtf2.setBounds(40, 220, 225, 30);
        jcb2.setBounds(275, 220, 90, 30);

        jLabel3.setBounds(20, 260, 30, 30);
        jtf3.setBounds(40, 260, 225, 30);
        jcb3.setBounds(275, 260, 90, 30);

        jLabel4.setBounds(20, 300, 30, 30);
        jtf4.setBounds(40, 300, 225, 30);
        jcb4.setBounds(275, 300, 90, 30);

        jb1.setBounds(20, 360, 130, 30);
        jb2.setBounds(235, 360, 130, 30);

        container.add(jLabel1);
        container.add(jLabel2);
        container.add(jLabel3);
        container.add(jLabel4);

        container.add(jtf1);
        container.add(jtf2);
        container.add(jtf3);
        container.add(jtf4);

        container.add(jb1);
        container.add(jb2);

        container.add(jcb1);
        container.add(jcb2);
        container.add(jcb3);
        container.add(jcb4);
        container.add(jScrollPane);

        setTitle("录入题目");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(400, 450);//界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中
    }
    /*监听录入,重置按钮*/
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("重置")) {//若点击的是重置按钮
            jTextArea.setText("");//重置题目框
            /*--重置选项框--*/
            jtf1.setText("");
            jtf2.setText("");
            jtf3.setText("");
            jtf4.setText("");
            /*-----重置下拉列表框-----*/
            jcb1.setSelectedIndex(0);
            jcb2.setSelectedIndex(0);
            jcb3.setSelectedIndex(0);
            jcb4.setSelectedIndex(0);
        }
        if (e.getActionCommand().equals("录入")) {//若点击的是录入按钮
            if (jTextArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "问题不能为空！");
            } else if ( jcb1.getSelectedItem().equals("请选择分数") || jcb2.getSelectedItem().equals("请选择分数") || jcb3.getSelectedItem().equals("请选择分数") || jcb4.getSelectedItem().equals("请选择分数") ) {
                JOptionPane.showMessageDialog(null, "请选择分数！");
            } else {
                try {//一键生成try-catch环绕
                    outPut();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "录入成功！");
            }
        }
    }
    /*向题库中写入题目*/
    public void outPut() throws IOException {
        File file = new File("./lib/QuestionBank.txt");//创建File对象
        FileOutputStream fileOutputStream = null;

        if (!file.exists()) {//如果文件不存在，则创建该文件
            file.createNewFile();//创建该文件
            fileOutputStream = new FileOutputStream(file);//首次写入获取
        } else {//如果文件已存在，那么就在文件末尾追加写入
            fileOutputStream = new FileOutputStream(file, true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
        }

        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");//指定以UTF-8格式写入文件

        String line = jTextArea.getText() + " A " + jtf1.getText() + " " + jcb1.getSelectedItem() +
                                            " B " + jtf2.getText() + " " + jcb2.getSelectedItem() +
                                            " C " + jtf3.getText() + " " + jcb3.getSelectedItem() +
                                            " D " + jtf4.getText() + " " + jcb4.getSelectedItem() + "\n";
        writer.write(line);
        writer.close();
    }
}