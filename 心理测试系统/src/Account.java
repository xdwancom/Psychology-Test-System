import java.io.*;
import java.util.ArrayList;

public class Account {
    private String username;
    private String password;
    private String authority;
    private static ArrayList<Account> accounts = new ArrayList<Account>();//创建名为accounts的集合对象，集合中存储的是Account类型的元素

    public static ArrayList<Account> getAccounts() {
        return accounts;
    }
    public static void setAccounts(ArrayList<Account> accounts) {
        Account.accounts = accounts;
    }

    public Account()/*无参构造方法*/{
    }
    public Account(String authority)/*带参构造方法*/{
        this.setAuthority(authority);
        try {//一键生成try-catch环绕
            readUser();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void readUser() throws IOException {
        FileInputStream fileInputStream = null;

        if (getAuthority().equals("管理员")) {//若选择管理员身份
            fileInputStream = new FileInputStream("./lib/teacher.txt");//创建字节输入流读取管理员账户密码文件
        } else if (getAuthority().equals("学生")) {//若选择学生身份
            fileInputStream = new FileInputStream("./lib/student.txt");//创建字节输入流读取学生账户密码文件
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));//创建缓冲字符输入流
        String line;//临时局部变量
        while ((line = br.readLine())/*读取一行字符串*/ != null) {
            String[] arr = line.split("\\s+");//用空格将其分割为字符串数组
            Account account = new Account();//创建Accounts对象
            account.setUsername(arr[0]);//将文件中账号放入集合
            account.setPassword(arr[1]);//将文件中密码放入集合
            accounts.add(account);
        }
        setAccounts(accounts);
        br.close();//关闭输入流
        fileInputStream.close();//关闭输入流
    }
}
