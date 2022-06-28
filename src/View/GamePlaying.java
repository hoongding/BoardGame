package View;

import Model.*;
import Controller.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class GamePlaying extends Map {
    private GameController controller;
    private Cell[][] mapdata;
    public static Map map;
    private int checkEndPerson = 0;
    public Scanner scanner = new Scanner(System.in);
    public GamePlaying(int playerNum) throws IOException, InterruptedException {
        System.out.println("플레이 할 map을 선택하세요");
        System.out.println("0 : default.map, 1 : another.map");
        this.initGame(playerNum);
        int i = 0;
        while(true)
        {
            if(this.playGame(((i%playerNum)))){
                i++;
            }
            else{//한명이 끝났을때
                controller.players[i%playerNum].setPlayerStatus(0);
                int maxScore = 0;
                int winner = 0;
                for(int j = 0; j< playerNum; j++){
                    if(maxScore < controller.players[j].getScore()){
                        maxScore = controller.players[j].getScore();
                        winner = j;
                    }
                }
                clearConsole();
                this.printMap();
                System.out.println("게임이 종료되었습니다.");
                System.out.println("승자는 "+ "플레이어"+winner+"님 입니다!! 축하드립니다!!!");
                break;
            }

        }

    }



    private void initGame(int playerNum) throws IOException {
        map = new Map();
        int map_num = scanner.nextInt();
        map.mapdata_num = map_num;
        map.readMap();
        this.controller = new GameController();
        this.mapdata = map.getMap();
        controller.createPlayer(playerNum);//플레이어 수만큼 인스턴스 만들기.

        for(int i =0; i<playerNum;i++){
            controller.initPlayerPos(i);
            controller.players[i].setPlayerStatus(1);// 상태 1로 init
            this.mapdata[map.start_x][map.start_y].setCellPlayer(i);
        }
    }

    private boolean playGame(int id) throws IOException, InterruptedException {
        clearConsole();
        this.printMap();
        if(controller.players[id].getPlayerStatus() == 1) {
            System.out.println("플레이어" + id + " 차례입니다.");
            //---------------------------------- STEP 1. 주사위 굴리기
            System.out.println("주사위를 굴리시겠습니까?(엔터키를 누르세요)");
            scanner.nextLine();
            controller.players[id].rollDice();
            //---------------------------------- STEP 2. 움직일 방향 정하기.
            System.out.println("움직일 수 있는 칸 수: " + controller.players[id].canMoveNum());
            System.out.print("움직이겠습니까(입력 1) 한 턴 쉬시겠습니까(입력 2) : ");
            String choice = scanner.nextLine();
            while (!Objects.equals(choice, "1") && !Objects.equals(choice, "2")) {
                System.out.println("1과 2중 한 숫자를 입력해주십시오. 다시 입력하세요 : ");
                choice = scanner.nextLine();
            }
            //---------------------------------STEP 3. 1을 입력받고 커멘드 입력받기.

            if (controller.players[id].atBridgeStart()) {//처음 시작이 다리일때
                if (controller.players[id].canMoveNum() < 2) {//입력가능한 칸 2칸보다 작으면. 무조건 generalMove
                    System.out.println("입력과 상관없이 이동할 수 있는 칸이 2칸 이하이므로 다리를 건널 수 없습니다.");
                    return generalMove(choice, id) != 0;
                } else {//입력가능한 칸 2칸보다 크면. 선택하게끔.
                    System.out.print("다리로 이동하시겠습니까? 이동하시려면 1, 이동하지 않으려면 2를 입력하세요. : ");
                    int bridgeChoice = scanner.nextInt();
                    if (bridgeChoice == 1) {
                        if (bridgeMove(choice, id) != 0) {
                            return true;
                        } else {
                            return false;
                        }
                    } else if (bridgeChoice == 2) {
                        System.out.println("다리를 건너지 않고 일반 Move로 이동합니다.");
                        return generalMove(choice, id) != 0;
                    }
                }
            } else if (!controller.players[id].atBridgeStart()) {//처음 시작이 다리시작부분이 아닐 때.
                return generalMove(choice, id) != 0;
            }
            return true;
        }
        else{
            System.out.println("플레이어"+id+"님은 게임이 끝났습니다. 아무키나 입력하세요.");
            scanner.nextLine();
            return true;
        }
    }





    private void updatePlayerPos(int id){
        this.mapdata[controller.players[id].positionX][controller.players[id].positionY].setCellPlayer(id);
    }
    private void clearPrevPos(int id)
    {
        this.mapdata[controller.players[id].positionX][controller.players[id].positionY].leavePlayer(id);
    }

    public void clearConsole() throws IOException {
        for (int i = 0; i < 80; i++)
            System.out.println("");
    }

    private void printMapFirstLine(int i) {
            for (int j = 0; j < mapdata[0].length; j++) {
                if (mapdata[i][j].getItem() != ' '&&mapdata[i][j].getItem() != '=') {
                    System.out.print(mapdata[i][j].getCellPlayer0() + " - " + mapdata[i][j].getCellPlayer1()+" ");
                } else if (mapdata[i][j].getItem() == '=') {
                    System.out.print("      ");
                }
                else
                {
                    System.out.print("      ");
                }
            }

    }
    private void printMapSecondLine(int i){
            for (int j = 0; j < mapdata[0].length; j++) {
                if (mapdata[i][j].getItem() != ' '&& mapdata[i][j].getItem() != '=') {
                    System.out.print("| " + mapdata[i][j].getItem()+" | ");
                }
                else if (mapdata[i][j].getItem() == '=') {
                    System.out.print("===== ");
                }
                else {
                    System.out.print("      ");
            }
        }
    }
    private void printMapThirdLine(int i) {

            for (int j = 0; j < mapdata[0].length; j++) {
                if (mapdata[i][j].getItem() != ' '&& mapdata[i][j].getItem() != '=') {
                    System.out.print(mapdata[i][j].getCellPlayer2() + " - " + mapdata[i][j].getCellPlayer3()+" ");
                }
                else if (mapdata[i][j].getItem() == '=') {
                    System.out.print("      ");
                }
                else {
                    System.out.print("      ");
                }

        }
    }
    private void printMap()
    {
        for (int i = 0; i < this.mapdata.length; i++)
        {
            this.printMapFirstLine(i);
            System.out.print("\n");
            this.printMapSecondLine(i);
            System.out.print("\n");
            this.printMapThirdLine(i);
            System.out.print("\n");
        }
        for(int i =0; i<controller.players.length;i++)
        {
            System.out.print("플레이어"+i+" -> ");
            System.out.println("브릿지카드: "+controller.players[i].getBridgeCard()+" 스코어: "+controller.players[i].getScore());
        }
        System.out.print("\n\n\n");
    }

    private int generalMove(String choice, int id) throws IOException, InterruptedException {
        if(checkEndPerson == 0) {//한명도 안끝났다면
            if(choice.equals("1"))
            {
                System.out.println("전반전입니다(뒤로도 이동 할 수 있습니다)");
                System.out.println("움직일 수 있는 칸 수만큼 움직일 방향을 입력하세요 : ");
                String input1 = scanner.nextLine();
                clearPrevPos(id);
                int return_move = controller.players[id].playerMoveInFirstHalf(input1);
                while(return_move == 0){
                    System.out.println(controller.players[id].message);
                    input1 = scanner.nextLine();
                    return_move = controller.players[id].playerMoveInFirstHalf(input1);
                }
                if (return_move == 2) {
                    controller.players[id].playerStay();
                }
                else if (return_move == 3)//만약 한명이 End에 도착하면
                {
                    this.checkEndPerson++;
                    System.out.println("플레이어"+id+"님 게임이 끝났습니다! 7점을 얻습니다.");
                    if(controller.players.length-checkEndPerson == 1)//2명중 1명일때만 여기 들어간다.
                    {
                        controller.players[id].setScore(7);
                        updatePlayerPos(id);
                        return 0;
                    }
                    else{
                        controller.players[id].setScore(7);
                    }
                    updatePlayerPos(id);
                    Thread.sleep(1000);
                }
                else {
                    updatePlayerPos(id);
                }
            }
            else if (choice.equals("2"))
            {
                System.out.println("한 턴 쉽니다. 만약 브릿지카드가 있다면 하나 줄어듭니다.");
                controller.players[id].playerStay();

            }
        } //전반전
        else if (checkEndPerson > 0) {//한명이라도 끝났다면
            if(choice.equals("1"))
            {
                System.out.println("후반전입니다.(뒤로는 이동 할 수 없습니다.)");
                System.out.println("움직일 수 있는 칸 수만큼 움직일 방향을 입력하세요 : ");
                String input1 = scanner.next();
                clearPrevPos(id);
                int return_move = controller.players[id].playerMoveInSecondHalf(input1);
                while(return_move == 0){
                    System.out.println(controller.players[id].message);
                    input1 = scanner.nextLine();
                    return_move = controller.players[id].playerMoveInSecondHalf(input1);
                }
                if (return_move == 2) {
                    controller.players[id].playerStay();
                }
                else if (return_move == 3)//만약 한명이 End에 도착하면
                {
                    this.checkEndPerson++;
                    System.out.println("플레이어"+id+"님 게임이 끝났습니다! 점을 얻습니다.");
                    if(controller.players.length-checkEndPerson == 1)//4명중 1명 , 3명중 1명
                    {
                        if(checkEndPerson == 2)//3명중 2명 즉 2등.
                        {
                            controller.players[id].setScore(3);
                        }
                        else if(checkEndPerson == 3)//4명중 3명 즉 3등
                        {
                            controller.players[id].setScore(1);
                        }
                        updatePlayerPos(id);
                        return 0;

                    } else if (controller.players.length-checkEndPerson == 2) {//4명중 2명 남은 경우 무조건 2등
                        controller.players[id].setScore(3);

                    }
                    updatePlayerPos(id);
                    Thread.sleep(1000);
                }
                else {
                    updatePlayerPos(id);
                }
            }
            else if (choice.equals("2"))
            {
                System.out.println("한 턴 쉽니다. 만약 브릿지카드가 있다면 하나 줄어듭니다.");
                controller.players[id].playerStay();
            }
        } //후반전
        //-------------------------------STEP 4. 2를 입력받고 STAY하기
        clearConsole();
        this.printMap();
        return 1;
    }
    private int bridgeMove(String choice, int id) throws IOException, InterruptedException {
        if(checkEndPerson == 0) {
            if(choice.equals("1"))
            {
                System.out.println("다리를 건넙니다. 다리도 한칸으로 칩니다. 전반전이기 때문에 다리를 건너고 뒤로도 이동할 수 있습니다.");
                System.out.print("움직일 수 있는 칸 수만큼 움직일 방향을 입력하세요 : ");
                String input1 = scanner.next();
                clearPrevPos(id);
                int return_move = controller.players[id].moveBridgeInFirstHalf(input1);

                while(return_move == 0){
                    System.out.println(controller.players[id].message);
                    input1 = scanner.nextLine();
                    return_move = controller.players[id].moveBridgeInFirstHalf(input1);
                }

                if (return_move == 2) {
                    controller.players[id].playerStay();
                }
                else if (return_move == 3)//만약 한명이 End에 도착하면
                {
                    this.checkEndPerson++;
                    System.out.println("플레이어"+id+"님 게임이 끝났습니다! 7점을 얻습니다.");
                    if(controller.players.length-checkEndPerson == 1)//2명중 1명일때만 여기 들어간다.
                    {
                        controller.players[id].setScore(7);
                        controller.players[id].setBridgeCard();
                        updatePlayerPos(id);
                        return 0;
                    }
                    else{
                        controller.players[id].setScore(7);
                    }
                    updatePlayerPos(id);
                    Thread.sleep(1000);
                }
                else {
                    updatePlayerPos(id);
                }
            }
            else if (choice.equals("2"))
            {
                System.out.println("한 턴 쉽니다. 만약 브릿지카드가 있다면 하나 줄어듭니다.");
                controller.players[id].playerStay();

            }
        } //전반전
        else if (checkEndPerson > 0) {//후반전.
            if(choice.equals("1"))
            {
                System.out.println("다리를 건넙니다. 후반전이기 때문에 다리를 건너고 뒤로는 가지 못합니다.");
                System.out.println("움직일 수 있는 칸 수만큼 움직일 방향을 입력하세요 : ");
                String input1 = scanner.next();
                clearPrevPos(id);
                int return_move = controller.players[id].moveBridgeInSecondHalf(input1);
                while(return_move == 0){
                    System.out.println(controller.players[id].message);
                    input1 = scanner.nextLine();
                    return_move = controller.players[id].moveBridgeInSecondHalf(input1);
                }
                if (return_move == 2) {
                    controller.players[id].playerStay();
                }
                else if (return_move == 3)//만약 한명이 End에 도착하면
                {
                    this.checkEndPerson++;
                    System.out.println("플레이어"+id+"님 게임이 끝났습니다! 점을 얻습니다.");
                    if(controller.players.length-checkEndPerson == 1)//4명중 1명 , 3명중 1명
                    {
                        if(checkEndPerson == 2)//3명중 2명 즉 2등.
                        {
                            controller.players[id].setBridgeCard();
                            controller.players[id].setScore(3);
                        }
                        else if(checkEndPerson == 3)//4명중 3명 즉 3등
                        {
                            controller.players[id].setBridgeCard();
                            controller.players[id].setScore(1);
                        }
                        updatePlayerPos(id);
                        return 0;

                    } else if (controller.players.length-checkEndPerson == 2) {//4명중 2명 남은 경우 무조건 2등
                        controller.players[id].setScore(3);
                    }
                    updatePlayerPos(id);
                    Thread.sleep(1000);
                }
                else {
                    updatePlayerPos(id);
                }
            }
            else if (choice.equals("2"))
            {
                System.out.println("한 턴 쉽니다. 만약 브릿지카드가 있다면 하나 줄어듭니다.");
                controller.players[id].playerStay();
            }
        } //후반전
        //-------------------------------STEP 4. 2를 입력받고 STAY하기
        clearConsole();
        controller.players[id].setBridgeCard();
        this.printMap();
        return 1;
    }
}


