package game;

import View.CLI;

import java.io.IOException;
import java.util.Scanner;


public class Game {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try {
            new CLI();
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
