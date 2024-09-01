import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Main {
    public static void main(String[] args) {
        new Login().launch();
    }
}

class Login extends JFrame implements ActionListener {
    private JLabel jl1, jl2, jl3;
    private JTextField Username;
    private JPasswordField Password;
    private JButton login;
    private JRadioButton radioButton1, radioButton2;
    public static String username;

    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        Login.username = username;
    }

    /*登录界面组件*/
    public Login() {
        /*--------创建文本对象--------*/
        jl1 = new JLabel("账号：");
        jl2 = new JLabel("密码：");
        jl3 = new JLabel("身份：");
        /*--------------------------*/

        Username = new JTextField(20);//账号栏
        Password = new JPasswordField(20);//密码栏
        login = new JButton("登录");

        /*创建两个单选按钮对象并添加至同一组中*/
        radioButton1 = new JRadioButton("管理员", true);//单选按钮1文本内容，默认选择按钮1
        radioButton2 = new JRadioButton("学生");//单选按钮2文本内容
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);
        /*-------------------------------*/
    }

    /*登录界面布局*/
    public void launch() {
        setLayout(null);//将容器的布局设为绝对布局
        login.addActionListener(this);
        /*--------------将文本对象和组件加入窗体并自定义位置&尺寸--------------*/
        add(jl1);
        jl1.setBounds(50, 10, 120, 30);//账号
        add(Username);
        Username.setBounds(90, 10, 250, 30);//账号框

        add(jl2);
        jl2.setBounds(50, 60, 120, 30);//密码
        add(Password);
        Password.setBounds(90, 60, 250, 30);//密码框

        add(jl3);
        jl3.setBounds(50, 100, 50, 30);//身份
        add(radioButton1);
        radioButton1.setBounds(90, 100, 70, 30);//管理员单选
        add(radioButton2);
        radioButton2.setBounds(160, 100, 100, 30);//学生单选

        add(login);
        login.setBounds(50, 140, 290, 30);//登录按钮
        /*----------------------------------------------------------------*/

        setTitle("在线心理测试系统");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(400, 220);//界面初始尺寸
        setLocationRelativeTo(null);//使界面居中
    }

    /*检查账号密码身份是否正确*/
    public boolean checkLogin(){
        if (radioButton1.isSelected()) {//若单选按钮1(管理员)被选中，则将"管理员"身份传给Account类
            new Account("管理员");
        } else if (radioButton2.isSelected()) {//若单选按钮2(学生)被选中，则将"学生"身份传给Account类
            new Account("学生");
        }

        String password=new String(Password.getPassword()/*获取密码框字符串，为char属性字符数组*/);//将char属性字符数组转化为字符串
        for (int i = 0; i < Account.getAccounts().size(); i++) {//for循环遍历集合比对输入框中和文件中的字符串是否相同
            if (Account.getAccounts().get(i).getUsername().equals(Username.getText()/*获取账号框字符串*/) /*比对账号字符串*/&& Account.getAccounts().get(i).getPassword().equals(password)/*比对密码字符串*/) {
                Account.getAccounts().clear();//清空列表元素
                Account.setAccounts(Account.getAccounts());
                return true;
            }
        }
        Account.getAccounts().clear();//清空列表元素
        Account.setAccounts(Account.getAccounts());
        return false;
    }

    /*监听登录按钮*/
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("登录")) {//若点击的是登录按钮
            if (checkLogin()) {//若检查登录通过(返回布尔值为真)
                dispose();//释放资源（关闭当前框）
                if (radioButton1.isSelected()){//如果选择了管理员身份，则跳转至管理员端界面
                    setUsername(Username.getText());//获取管理员账号字符串，命名心理测试需要
                    new TeacherMenu(getUsername());
                } else if (radioButton2.isSelected()){//如果选择了学生身份，则跳转至学生端界面
                    new StudentMenu();
                }
            } else {
                JOptionPane.showMessageDialog(null, "账号或密码错误！", "提示消息", JOptionPane.WARNING_MESSAGE);
                Username.setText("");//将账号栏清空
                Password.setText("");//将密码栏清空
            }
        }
    }
}