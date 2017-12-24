import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;
import java.util.Base64;

public class Send_email{
    public static void sendemail(){
        String send_email;
        String password;
        String received_email;
        String subject ;
        String content ;
        String Smtp_serverHost;
        int    Smtp_serverport = 25;

        //用户键入主要信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入您的邮箱（QQ邮箱除外):");
        send_email = scanner.next();

        scanner = new Scanner(System.in);
        System.out.println("输入您的密码:");
        password = scanner.next();

        scanner = new Scanner(System.in);
        System.out.println("输入接收的邮箱:");
        received_email = scanner.next();

        scanner = new Scanner(System.in);
        System.out.println("输入您邮件的主题:");
        subject = scanner.next();

        scanner = new Scanner(System.in);
        System.out.println("输入您邮件的内容");
        content = scanner.next();

        System.out.println("正在发送！");

        //解析smtp服务器地址
        String Host_Address[] = send_email.split("@");
        Smtp_serverHost = "smtp." + Host_Address[1];
        try {
            //将 邮箱和密码进行Base64转化
            String Base64_send_email = Base64.getEncoder().encodeToString(send_email.getBytes("utf-8"));
            String Base64_password = Base64.getEncoder().encodeToString(password.getBytes("utf-8"));

            //连接邮件服务器
            Socket socket = new Socket(Smtp_serverHost, Smtp_serverport);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(input.readLine());

            output.println("HELO 163");
            output.flush();
            System.out.println(input.readLine());

            output.println("auth login");
            output.flush();
            System.out.println(input.readLine());
            output.println(Base64_send_email);
            output.flush();
            System.out.println(input.readLine());
            output.println(Base64_password);
            output.flush();

            String word = input.readLine();
            System.out.println(word);

            output.println("mail from:<" + send_email + ">");
            output.flush();
            System.out.println(input.readLine());
            output.println("rcpt to:<" + received_email + ">");
            output.flush();
            System.out.println(input.readLine());

            output.println("data");
            output.flush();
            System.out.println(input.readLine());
            String email_con = "From:" + send_email + "\r\n";
            email_con += "To: " + received_email + "\r\n";
            email_con = email_con + "Subject:" + subject + "\r\n";
            email_con = email_con + "Content-Type: text/plain;charset=\"utf-8\"\r\n";
            email_con = email_con + "\r\n";
            email_con = email_con + content + "\r\n";
            email_con = email_con + ".\r\n";

            output.println(email_con);
            output.flush();
            System.out.println(input.readLine());

            output.println("quit");
            output.flush();
            System.out.println(input.readLine());

            //判断是否登录成功
            if (!word.startsWith("235")){
                System.out.println("发送失败，请重新选择");
            }
            socket.close();
            input.close();
            output.close();
        }catch (ConnectException e){
            System.out.println("Exception");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}