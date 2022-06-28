package Model;

public class Cell {

    private char item = ' ';
    private char nextDirection = 'N';
    private char prevDirection = 'N';
    private char player0 = '-'; // cell1
    private char player1 = '-';//cell3
    private char player2 = '-';
    private char player3 = '-';

    public Cell(){}

    public char getItem() {
        return this.item;
    }
    public char getPrevDirection(){
        return this.prevDirection;
    }

    public void setCell(char item, char nextDirection, char prevDirection){ // C R L 같은 Cell을 위한..
        this.item = item;
        this.nextDirection = nextDirection;
        this.prevDirection = prevDirection;
    }

    public void setCell(char item) { // E를 위한
        this.item = item;
    }

    public void setCell(char item, char nextDirection) { // S를 위한..
        this.item = item;
        this.nextDirection = nextDirection;
    }
    public void setCellPlayer(int id){
        if (id == 0){
        this.player0 = '0';
        } else if (id == 1 ) {
            this.player1 = '1';
        } else if (id == 2) {
            this.player2 = '2';
        } else if (id == 3) {
            this.player3 = '3';
        }
    }
    public void leavePlayer(int id){
        if (id == 0){
            this.player0 = '-';
        } else if (id == 1 ) {
            this.player1 = '-';
        } else if (id == 2) {
            this.player2 = '-';
        } else if (id == 3) {
            this.player3 = '-';
        }
    }

    public char getCellPlayer0(){
        return this.player0;
    }
    public char getCellPlayer1(){
        return this.player1;
    }
    public char getCellPlayer2(){
        return this.player2;
    }
    public char getCellPlayer3(){
        return this.player3;
    }





}
