import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Information extends JFrame implements ActionListener {
    private JLabel jl2, jl3, jl4, jl1;
    private JTextField jtf1, jtf2, jtf3, jtf4;
    private JButton jb1, jb2;
    private Container container;
    private String name;

    static String Speciality;
    static String Classes;
    static String Sno;
    static String Sname;

    public static String getSpeciality() {
        return Speciality;
    }
    public static void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public static String getClasses() {
        return Classes;
    }
    public static void setClasses(String classes) {
        Classes = classes;
    }

    public static String getSno() {
        return Sno;
    }
    public static void setSno(String sno) {
        Sno = sno;
    }

    public static String getSname() {
        return Sname;
    }
    public static void setSname(String sname) {
        Sname = sname;
    }

    /*完善资料界面布局*/
    public Information(String name) {
        this.name = name;
        container = getContentPane();
        jtf1 = new JTextField();
        jtf2 = new JTextField();
        jtf3 = new JTextField();
        jtf4 = new JTextField();
        jl1 = new JLabel("姓名：");
        jl2 = new JLabel("学号：");
        jl3 = new JLabel("专业：");
        jl4 = new JLabel("班级：");

        jb1 = new JButton("提交");
        jb2 = new JButton("重置");
    }

    public void launch() {
        container.setLayout(null);//将容器的布局设为绝对布局
        setTitle("完善资料");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(250, 300);//界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中

        jtf1.setBounds(70, 10, 140, 30);
        jtf2.setBounds(70, 60, 140, 30);
        jtf3.setBounds(70, 110, 140, 30);
        jtf4.setBounds(70, 160, 140, 30);

        jl1.setBounds(30, 10, 40, 30);
        jl2.setBounds(30, 60, 40, 30);
        jl3.setBounds(30, 110, 40, 30);
        jl4.setBounds(30, 160, 40, 30);

        jb1.setBounds(30, 210, 80, 30);
        jb2.setBounds(130, 210, 80, 30);

        jb1.addActionListener(this);
        jb2.addActionListener(this);

        container.add(jtf1);
        container.add(jtf2);
        container.add(jtf3);
        container.add(jtf4);

        container.add(jl1);
        container.add(jl2);
        container.add(jl3);
        container.add(jl4);

        container.add(jb1);
        container.add(jb2);
    }
    /*监听提交&重置按钮*/
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("提交")) {
            if (jtf1.getText().isEmpty() || jtf2.getText().isEmpty() || jtf3.getText().isEmpty() || jtf4.getText().isEmpty()) {//若四个信息有留空
                JOptionPane.showMessageDialog(null, "信息不能为空！");
            } else {
                setSname(jtf1.getText());//写入姓名
                setSno(jtf2.getText());//写入学号
                setSpeciality(jtf3.getText());//写入专业
                setClasses(jtf4.getText());//写入班级
                this.setVisible(false);//使界面不可视
                new OnlineTest(name,this).launch();//打开在线心理测试界面
            }
        }
        if (e.getActionCommand().equals("重置")) {
            jtf1.setText("");
            jtf2.setText("");
            jtf3.setText("");
            jtf4.setText("");
        }
    }
}
