import javax.swing.*;
import java.io.*;

public class SetUpTestResult extends JFrame {
    private JLabel jLabel;
    private JTextField jtf;
    private JButton jButton1, jButton2;

    public SetUpTestResult() {
        jLabel = new JLabel("请输入测评结果：");
        jtf = new JTextField();
        jButton1 = new JButton("提交");
        jButton2 = new JButton("重置");
    }

    public void launch() {
        setTitle("设置测试结果");
        setResizable(false);//窗口大小不可变
        setVisible(true);//使界面可视
        setSize(400, 120);//界面初始尺寸
        setLocationRelativeTo(getOwner());//使界面居中
        setLayout(null);//将容器的布局设为绝对布局

        jLabel.setBounds(30, 10, 100, 30);
        add(jLabel);
        jtf.setBounds(130, 10, 200, 30);
        add(jtf);

        jButton1.setBounds(30, 50, 130, 30);
        jButton1.addActionListener/*监听提交按钮*/(e -> {
            if (jtf.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "不能为空！");
            }else {
                File file = new File("./lib/comment.txt");//创建File对象
                FileOutputStream fileOutputStream = null;

                if (!file.exists()) {//若文件不存在，则创建该文件
                    try {//一键生成try-catch环绕
                        file.createNewFile();//创建该文件
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    try {//一键生成try-catch环绕
                        fileOutputStream = new FileOutputStream(file);//首次写入获取
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                } else {//若文件已存在，则在文件末尾追加写入
                    try {//一键生成try-catch环绕
                        fileOutputStream = new FileOutputStream(file, true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }

                OutputStreamWriter writer = null;//指定以UTF-8格式写入文件，避免乱码
                try {//一键生成try-catch环绕
                    writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                }

                String line = jtf.getText() + "\n";

                try {//一键生成try-catch环绕
                    writer.write(line);
                    writer.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, "提交成功");
                dispose();//释放资源（关闭当前框）
            }
        });
        add(jButton1);

        jButton2.setBounds(200, 50, 130, 30);
        jButton2.addActionListener/*监听重置按钮*/(e -> jtf.setText(""));
        add(jButton2);
    }
}
