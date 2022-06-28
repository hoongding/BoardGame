package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.*;

public class Map {

    public int start_x = 0;
    public int start_y = 0;
    public int x_length ;
    public int y_length ;
    //    private int height = 0;
    private Cell[][] map;
    public int mapdata_num = 0;
    public String[] mapdata = {"src/inputData/default.map","src/inputData/another.map"};

    public Map() throws IOException {
//        this.readMap();
    }
    private void MapProperty() throws IOException{
        BufferedReader reader = new BufferedReader(
                new FileReader((mapdata[mapdata_num]))
        );
        int min_temp_x = 0;
        int min_temp_y = 0;
        int max_temp_x = 0;
        int max_temp_y = 0;
        int map_x = 0;
        int map_y = 0;
        this.x_length = 0;
        this.y_length = 0;

        String str;
        while((str = reader.readLine()) != null){
            String[] tempLine = str.split(" "); // 각 줄을 tempLine에 받아온다.
            List<String> list = new ArrayList<String>(Arrays.asList(tempLine));

            if(tempLine.length == 1){ // E이면 list추가
                list.add("0");
                list.add("0");
            }
            else if(tempLine.length == 2){ //S면 마지막 list 추가.
                list.add("0");
            }


            String ret1 = list.get(0); //ret1 : S C B b H S P
            String ret2 = list.get(1); //ret2 : D U L R
            String ret3 = list.get(2); //ret2 : D U L R or X


            if(tempLine.length == 3){//만약 첫글자가 E가 아니라면

                if(Objects.equals(ret3, "D")){
                    map_x += 1;
                    if(max_temp_x < map_x){
                        max_temp_x = map_x;
                    }
                } else if (Objects.equals(ret3, "U")) {
                    map_x -= 1;
                    if(min_temp_x > map_x){
                        min_temp_x = map_x;
                    }
                } else if (Objects.equals(ret3, "L")) {
                    map_y -= 1;
                    if(min_temp_y > map_y){
                        min_temp_y = map_y;
                    }
                } else if(Objects.equals(ret3,"R")){ // temp == "R"
                    map_y += 1;
                    if(max_temp_y < map_y){
                        max_temp_y = map_y;
                    }
                }

            } else if (tempLine.length == 2) {//만약 S라면

                if(Objects.equals(ret2, "D")){
                    map_x += 1;
                    if(max_temp_x < map_x){
                        max_temp_x = map_x;
                    }
                } else if (Objects.equals(ret2, "U")) {
                    map_x -= 1;
                    if(min_temp_x > map_x){
                        min_temp_x = map_x;
                    }
                } else if (Objects.equals(ret2, "L")) {
                    map_y -= 1;
                    if(min_temp_y>map_y){
                        min_temp_y = map_y;
                    }
                } else if(Objects.equals(ret2, "R")){ // temp == "R"
                    map_y += 1;
                    if(max_temp_y < map_y){
                        max_temp_y = map_y;
                    }
                }
            }
            else {
                break;
            }

        }

        this.x_length = max_temp_x - min_temp_x + 1;
        this.y_length = max_temp_y - min_temp_y + 1;

        if(min_temp_x < 0){
            this.start_x = this.start_x - min_temp_x;
        }
        if(min_temp_y < 0){
            this.start_y = this.start_y - min_temp_y;
        }

        map = new Cell[this.x_length][this.y_length];

    }



    public void readMap() throws IOException {
        this.MapProperty();
//        char[][] map = new char[this.x_length][this.y_length];
        BufferedReader reader = new BufferedReader(
                new FileReader((mapdata[mapdata_num]))
        );

        String str;

        for (int i = 0; i < this.map.length; i++) { //map 초기화.
            for (int k = 0; k < this.map[0].length; k++) {//length로 한 이유는 유지보수때문에.
                this.map[i][k] = new Cell();
            }
        }

        int x = this.start_x;
        int y = this.start_y;

        while ((str = reader.readLine()) != null) {
            String[] tempLine = str.split(" "); // 각 줄을 tempLine에 받아온다.
            List<String> list = new ArrayList<String>(Arrays.asList(tempLine));

            if(tempLine.length == 1){
                list.add("0");
                list.add("0");
            }
            else if(tempLine.length == 2){
                list.add("0");
            }

            String ret1 = list.get(0); //ret1 : S C B b H S P
            String ret2 = list.get(1); //ret2 : D U L R
            String ret3 = list.get(2); //ret2 : D U L R or X
            char item = ret1.charAt(0);
            char nextDirection = ret3.charAt(0);
            char prevDirection = ret2.charAt(0);


            if(tempLine.length == 3){//만약 첫글자가 E가 아니라면

                if(Objects.equals(ret3, "D")){
                    this.map[x][y].setCell(item, nextDirection, prevDirection);
                    x += 1;
                } else if (Objects.equals(ret3, "U")) {
                    this.map[x][y].setCell(item, nextDirection, prevDirection);
                    x -= 1;
                } else if (Objects.equals(ret3, "L")) {
                    this.map[x][y].setCell(item, nextDirection, prevDirection);
                    y -= 1;
                } else if (Objects.equals(ret3, "R")){ // temp == "R"
                    this.map[x][y].setCell(item, nextDirection, prevDirection);
                    y += 1;
                }

            } else if (tempLine.length == 2) {//만약 S라면

                if(Objects.equals(ret2, "D")){
                    this.map[x][y].setCell(item, ret2.charAt(0));
                    x += 1;
                } else if (Objects.equals(ret2, "U")) {
                    this.map[x][y].setCell(item, ret2.charAt(0));
                    x -= 1;
                } else if (Objects.equals(ret2, "L")) {
                    this.map[x][y].setCell(item, ret2.charAt(0));
                    y -= 1;
                } else{ // temp == "R"
                    this.map[x][y].setCell(item, ret2.charAt(0));
                    y += 1;
                }
            }
            else{//만약 E라면
                this.map[x][y].setCell(item);
            }

        }
        reader.close();

        for(int i =0;i<this.map.length;i++){ // Bridge 출력.
            for(int j =0;j< this.map[i].length;j++){
                if(this.map[i][j].getItem() == 'B'){
                    this.map[i][j+1].setCell('=','R','L');

                }
            }
        }




    }


    public Cell[][] getMap() {
        return map;
    }

}