package Model;


import View.GamePlaying;

import java.io.IOException;
import java.util.Random;

import static java.lang.Character.toUpperCase;

public class Player {
    // Player Class
    private final int playerId;
    private int playerStatus; //0: end Game 1:Playing

    private int playerNum;
    private int diceNum;
    private int score = 0; // 플레이어 점수
    private final boolean playing = true; // 끝났는지 안끝났는지
    private int bridgeCard = 0; //플레이어가 가지고 있는 브릿지카드개수
    private int move_num;
    private final int currentTurn = 0; // 현재 차례가 Player 몇번인가?
    public int positionX;
    public int positionY;
    public Map map = GamePlaying.map;
    public String message;
    public Player(int playerId) throws IOException { //생성자
        this.playerId = playerId;
    } // 생성자.

    //--------------------------------------------------------------------------------------------
    public void rollDice(){
        Random ranNum = new Random();
        this.diceNum = ranNum.nextInt(6) + 1; // diceNum을 바꾼다.
    }
    public int canMoveNum(){ // 움직일 수 있는 move 수 return.
        int canMove = this.diceNum - this.bridgeCard;
        this.move_num = canMove;

        if (this.move_num >= 0){
            return this.move_num;
        }
        else {
            this.move_num = 0;
            return this.move_num;
        }
    }

    public int playerMoveInFirstHalf(String input) throws IOException {
        //전반전에 움직이는 method 0 : 다시 입력  1:움직이기   2: 1명이 끝남. 이제 후반전으로 넘어가.
        int x = this.positionX;
        int y = this.positionY;

        if(this.move_num == 0){//움직일 수 있는 movenum이 0이면 STAY
            if (this.bridgeCard >0){//만약 브릿지카드가 1개이상 가지고있으면 하나 없애주기.
                this.bridgeCard -= 1;
            }
            this.message = "현재 움직일 수 있는 횟수가 0입니다. STAY 해야 합니다.";
            return 2; // STAY
        }

        else if(this.move_num == input.length()){//input길이와 move_num이 같다면 움직일 수 있다.
            for(int i = 0; i < input.length(); i++) {
                char temp = input.charAt(i);
                if (temp == 'D'||temp == 'd') {
                    x += 1;

                } else if (temp == 'U'||temp == 'u') {
                    x -= 1;

                } else if (temp == 'R'||temp == 'r') {
                    y += 1;

                } else if (temp == 'L'||temp == 'l') {
                    y -= 1;
                }
                //---------------------------------------
                //error Handling
                if(x>map.x_length - 1 || x<0){
                    this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                    return 0;
                }
                if(y> map.y_length - 1 || y<0){
                    this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                    return 0;
                }
                if(checkEndPoint(x,y)){
                    return 3;
                }
                if (map.getMap()[x][y].getItem() == ' ') { //빈칸으로 이동하는 경우 처음부터 다시 입력.
                    this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                    return 0;
                }

                if(map.getMap()[x][y].getItem() == '='){//시작지점이 Bridge가 아닐때 Bridge를 이용하면 다시 입력.
                    this.message = "Bridge는 시작지점이 Bridge이어야 이용가능합니다. 처음부터 다시 입력하세요";
                    return 0;
                }
            }

            this.positionX = x; //다 움직였으면 현재 Player 위치 넣어주기.
            this.positionY = y;
            this.calScore();//Score 계산해주기.
            return 1;
        }
        else {//입력 길이, 움직일 수 있는 num 같으면 0를 리턴. 다시 입력해야함
            this.message = "입력 길이가 일치하지 않습니다. 다시 입력하세요";
            return 0;
        }
    }

    public void playerStay(){
        if (this.bridgeCard >0){//만약 브릿지카드가 1개이상 가지고있으면 하나 없애주기.
            this.bridgeCard -= 1;
        }

    }


    public int playerMoveInSecondHalf(String input) throws IOException { //후반전에 움직이는 method
        this.canMoveNum();

        int x = this.positionX;
        int y = this.positionY;

        if(this.move_num == 0){//움직일 수 있는 movenum이 0이면 STAY
            this.message = "현재 움직일 수 있는 횟수가 0입니다. STAY합니다.";
            return 2; // STAY
        }

        else if(this.move_num == input.length()){//input길이와 move_num이 같다면 움직일 수 있다.
            for(int i = 0; i < input.length(); i++) {
                char temp = input.charAt(i);
                //현재 위치가 Bridge가 아닐때

                if(checkMoveSecondHalf(temp, x, y)) {//매 입력마다 뒤로가는지 안가는지 확인.
                    if (temp == 'D'||temp == 'd') {
                        x += 1;
                    } else if (temp == 'U'||temp == 'u') {
                        x -= 1;
                    } else if (temp == 'R'||temp == 'r') {
                        y += 1;
                    } else if (temp == 'L'||temp == 'l') {
                        y -= 1;
                    }
                    //-------------------------------------------------
                    //error Handling
                    if(x>map.x_length || x<0){
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }
                    if(y> map.y_length || y<0){
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }
                    if(checkEndPoint(x,y)){
                        return 3;
                    }
                    if (map.getMap()[x][y].getItem() == ' ') { //빈칸으로 이동하는 경우 처음부터 다시 입력.
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }

                    if(map.getMap()[x][y].getItem() == '='){//시작지점이 Bridge가 아닐때 Bridge를 이용하면 다시 입력.
                        this.message = "Bridge는 시작지점이 Bridge이어야 이용가능합니다. 처음부터 다시 입력하세요";
                        return 0;
                    }
                }
                else{//뒤로가는 입력받으면 다시 입력받아야함.
                    this.message = "후반전입니다. 뒤로는 이동할 수 없습니다. 다시 입력하세요 : ";
                    return 0;
                }
            }

        }
        else {//입력 길이, 움직일 수 있는 num 같으면 0를 리턴. 다시 입력해야함
            this.message = "입력 길이가 일치하지 않습니다. 다시 입력하세요";
            return 0;
        }

        this.positionX = x; //다 움직였으면 현재 Player 위치 넣어주기.
        this.positionY = y;
        this.calScore();//Score 계산해주기.
        return 1;
    }

    public int moveBridgeInFirstHalf(String input){
        //전반전에 움직이는 method 0 : 다시 입력  1:움직이기   2: 1명이 끝남. 이제 후반전으로 넘어가.
        int x = this.positionX;
        int y = this.positionY;
        int bridgecardnum = this.bridgeCard;
        if(this.move_num == input.length()){//input길이와 move_num이 같다면 움직일 수 있다.
            if(this.move_num > 2){ // 2보다 크다면 endcheck까지 해줘야함.
                char firstInput = input.charAt(0);
                char secondInput = input.charAt(1);
                if((firstInput == 'R'&& secondInput == 'R')||(firstInput == 'r'&& secondInput == 'r')){//첫번째, 두번째 인풋이 무조건 RR이어야지 다리를 건널 수 있음.
                    y+=2;
                }
                else{
                    this.message = "다리를 건너려면 R을 두번 입력해야 이동할 수 있습니다. 다시 처음부터 입력하세요.";
                    return 0;
                }
                for(int i = 2; i < input.length(); i++) {
                    char temp = input.charAt(i);
                    if (temp == 'D'||temp == 'd') {
                        x += 1;

                    } else if (temp == 'U'||temp == 'u') {
                        x -= 1;

                    } else if (temp == 'R'||temp == 'r') {
                        y += 1;

                    } else if (temp == 'L'||temp == 'l') {
                        y -= 1;
                    }
                    //------------------------------------------
                    //Error Handling
                    if(x>map.x_length-1 || x<0){
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }
                    if(y> map.y_length-1 || y<0){
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }
                    if(checkEndPoint(x,y)){
                        return 3;
                    }
                    if (map.getMap()[x][y].getItem() == '=') {
                        this.message = "Bridge는 반대로 건널수 없습니다. 다시 처음부터 입력하세요";
                    }
                    if (map.getMap()[x][y].getItem() == ' ') { //빈칸으로 이동하는 경우 처음부터 다시 입력.
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }

                }
            }
            else if(this.move_num == 2){ //무조건 RR이어야함.
                char firstInput = input.charAt(0);
                char secondInput = input.charAt(1);
                if((firstInput == 'R'&& secondInput == 'R')||(firstInput == 'r'&& secondInput == 'r')){//첫번째, 두번째 인풋이 무조건 RR이어야지 다리를 건널 수 있음.
                    y+=2;

                }
                else{
                    this.message = "다리를 건너려면 R을 두번 입력해야 이동할 수 있습니다. 다시 처음부터 입력하세요.";
                    return 0;
                }
            } // 2면 RR만 받으면됨.
            this.positionX = x; //다 움직였으면 현재 Player 위치 넣어주기.
            this.positionY = y;
            this.bridgeCard = bridgecardnum;
            this.calScore();//Score 계산해주기.
            return 1;
        }


        else {//입력 길이, 움직일 수 있는 num 같으면 0를 리턴. 다시 입력해야함
            this.message = "입력 길이가 일치하지 않습니다. 다시 입력하세요";
            return 0;
        }
    }
    public int moveBridgeInSecondHalf(String input){
        int x = this.positionX;
        int y = this.positionY;
        int bridgecardnum = this.bridgeCard;
        if(this.move_num == input.length()){//input길이와 move_num이 같다면 움직일 수 있다.
            if(this.move_num > 2){ // 2보다 크다면 endcheck까지 해줘야함.
                char firstInput = input.charAt(0);
                char secondInput = input.charAt(1);
                if((firstInput == 'R'&& secondInput == 'R')||(firstInput == 'r'&& secondInput == 'r')){//첫번째, 두번째 인풋이 무조건 RR이어야지 다리를 건널 수 있음.
                    y+=2;
//                        bridgecardnum++;
                }
                else{
                    this.message = "다리를 건너려면 R을 두번 입력해야 이동할 수 있습니다. 다시 처음부터 입력하세요.";
                    return 0;
                }
                for(int i = 2; i < input.length(); i++) {
                    char temp = input.charAt(i);
                    if(checkMoveSecondHalf(temp, x, y)) {//매 입력마다 뒤로가는지 안가는지 확인.
                        if (temp == 'D'||temp == 'd') {
                            x += 1;
                        } else if (temp == 'U'||temp == 'u') {
                            x -= 1;
                        } else if (temp == 'R'||temp == 'r') {
                            y += 1;
                        } else if (temp == 'L'||temp == 'l') {
                            y -= 1;
                        }
                    }
                    else{
                        this.message = "후반전이기 때문에 다리를 건너고 뒤로는 이동하지 못합니다.";
                        return 0;
                    }
                    //------------------------------------------
                    //error Handling
                    if(x > map.x_length - 1 || x < 0){
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }
                    if(y > map.y_length - 1 || y < 0){
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }
                    if(checkEndPoint(x,y)){
                        return 3;
                    }
                    if (map.getMap()[x][y].getItem() == '=') {
                        this.message = "Bridge는 반대로 건널수 없습니다. 다시 처음부터 입력하세요";
                    }
                    if (map.getMap()[x][y].getItem() == ' ') { //빈칸으로 이동하는 경우 처음부터 다시 입력.
                        this.message = "이동하지 못하는 Cell입니다. 처음부터 다시 입력하세요";
                        return 0;
                    }


                }
            } //2이상이면 다음 인풋 받고 end check
            else if(this.move_num == 2){ //무조건 RR이어야함.
                char firstInput = input.charAt(0);
                char secondInput = input.charAt(1);
                if((firstInput == 'R'&& secondInput == 'R')||(firstInput == 'r'&& secondInput == 'r')){//첫번째, 두번째 인풋이 무조건 RR이어야지 다리를 건널 수 있음.
                    y+=2;

                }
                else{
                    this.message = "다리를 건너려면 R을 두번 입력해야 이동할 수 있습니다. 다시 처음부터 입력하세요.";
                    return 0;
                }
            } // 2면 RR만 받으면됨.
            this.positionX = x; //다 움직였으면 현재 Player 위치 넣어주기.
            this.positionY = y;
            this.bridgeCard = bridgecardnum;
            this.calScore();//Score 계산해주기.
            return 1;
        }


        else {//입력 길이, 움직일 수 있는 num 같으면 0를 리턴. 다시 입력해야함
            this.message = "입력 길이가 일치하지 않습니다. 다시 입력하세요";
            return 0;
        }

    }


    public void calScore(){//이동 끝났을때 Score계산해주기.
        int x = this.positionX;
        int y = this.positionY;
        if(map.getMap()[x][y].getItem() == 'P'){
            this.score += 1;
        }
        else if(map.getMap()[x][y].getItem() == 'H'){
            this.score += 2;
        }
        else if (map.getMap()[x][y].getItem() == 'S') {
            this.score += 3;
        }
    }

    public boolean checkMoveSecondHalf(char temp, int x, int y){
        temp = toUpperCase(temp);
        return map.getMap()[x][y].getPrevDirection() != temp;
    }

    public boolean checkEndPoint(int x, int y)
    {

        if (map.getMap()[x][y].getItem() == 'E') {
            this.playerStatus = 0;//Status를 End로 바꿔줌.
            this.positionX = x;
            this.positionY = y;
            return true;
        }
        else{
            return false;
        }
    }
    public int getBridgeCard(){ return this.bridgeCard; }

    public int getScore(){ return this.score; }

    public void setScore(int score){
        this.score += score;
    }

    public boolean atBridgeStart(){
        return map.getMap()[this.positionX][this.positionY].getItem() == 'B';
    }
    public void setBridgeCard(){
        this.bridgeCard += 1;
    }

    public void setPlayerStatus(int status){
        this.playerStatus = status;
    }
    public int getPlayerStatus(){
        return this.playerStatus;
    }

}

