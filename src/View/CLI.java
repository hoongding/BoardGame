package View;


import java.io.IOException;
import java.util.Scanner;

public class CLI {
    public Scanner scanner = new Scanner(System.in);

    public CLI() throws IOException, InterruptedException {
        System.out.println("플레이 할 플레이어 수를 입력하세요 : ");

        while(true) {
            int playerNum = scanner.nextInt();
            if(1< playerNum && playerNum < 5){
                new GamePlaying(playerNum);
                break;
            }
            else{
                System.out.println("2에서 4사이의 숫자를 입력하세요: ");
            }
        }
    }
}
