import java.util.Scanner;

public class Main {
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);

        exit:
        while (true){
            System.out.println("输入1发送邮件，输入2接收邮件，输入q退出程序。");

            String answer = scanner.nextLine();

            switch (answer){
                case "1":
                    Send_email send_email = new Send_email();
                    send_email.sendemail();
                    break ;
                case "2":
                    Receive_email receive_email = new Receive_email();
                    receive_email.receiveemail();
                    break ;
                case "q":
                    break exit;
                default:
                    break ;
            }
        }
        scanner.close();
    }
}
