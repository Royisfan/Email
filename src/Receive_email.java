import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Receive_email {
    public static void receiveemail(){
        String email;
        String password;
        String send_email;
        String MailInfo = "";
        String subject;
        String content;
        String pop_serverHost;
        int    pop_serverPort = 110;
        LinkedList<String> MailList  = new LinkedList<>();  // 存储邮件的链表

        //用户键入邮箱密码
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入您的邮箱(163邮箱):");
        email = scanner.next();

        scanner = new Scanner(System.in);
        System.out.println("输入您的密码:");
        password = scanner.next();

        //解析pop服务器地址
        String Host_Address[] = email.split("@");
        pop_serverHost = "pop." + Host_Address[1];

        try{
            //打开连接通道
            Socket socket = new Socket(pop_serverHost, pop_serverPort);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //打印信息
            System.out.println(input.readLine()); //第一条信息

            output.println("user " + email);
            output.flush();
            System.out.println(input.readLine());

            output.println("pass " + password);
            output.flush();

            String wordByserver = input.readLine();
            System.out.println(wordByserver);

            //判断登录是否成功，失败重新选择
            if(!wordByserver.startsWith("+OK")){
                System.out.println("登录失败，请重新选择");
            }
            //邮件数量
            else {
                int mailCount = Integer.parseInt(wordByserver.substring(4, 5));
                for (int i = 1; i <= mailCount; i++) {
                    output.println("RETR " + i);
                    output.flush();

                    StringBuilder receive = new StringBuilder();
                    String temp;
                    while (true) {
                        temp = input.readLine();
                        receive.append("\r\n");
                        receive.append(temp);
                        if (temp.equals(".")) {
                            break;
                        }
                    }
                    MailInfo = receive.toString(); //
                    MailList.push(MailInfo);
                }
                output.println("quit");
                output.flush();
                System.out.println(input.readLine());
                System.out.println("接收邮件完成,开始打印邮件信息");

                //处理邮件信息
                for (int i = 1; i<= mailCount; i++) {
                    String mailinfo =MailList.pop();
                    int start;
                    int offsite;
                    int end;
                    String tempString;

                    start = mailinfo.indexOf("\nFrom:", 0);
                    end = mailinfo.indexOf("\nTo:", 0);
                    tempString = mailinfo.substring(start + "\nFrom:".length(), end);
                    if (tempString.contains("<")) {
                        start = tempString.indexOf("<");
                        end = tempString.indexOf(">");
                        send_email = tempString.substring(start + 1, end).trim();
                    } else {
                        send_email = tempString.trim();
                    }

                    start = mailinfo.indexOf("Subject:", end);
                    if (start == -1) {
                        start = MailInfo.indexOf("subject:", end);
                    }
                    offsite = "Subject:".length();
                    end = mailinfo.indexOf("\r\n", start + offsite);
                    subject = mailinfo.substring(start + offsite, end);
                    content = mailinfo.substring(end + 2, mailinfo.length() - 3);

                    //打印邮件信息
                    System.out.println("第" + i + "封邮件");
                    System.out.println("发件人: " + send_email);
                    System.out.println("邮件主题: " + subject);
                    System.out.println("邮件内容: " + content);
                    System.out.println("第" + i + "封邮件结束");
                    System.out.println("=============================================");
                    System.out.println("=============================================");
                }
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
