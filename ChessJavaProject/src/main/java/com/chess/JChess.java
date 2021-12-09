package com.chess;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.BlackPlayer;
import com.chess.gui.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class JChess {

    final private static Map<String,Integer> hm = new HashMap<>();



    public static void main(String[] args) {
        //new JChess();
        fillCoords();
        Board board =Board.createStandardBoard();
        System.out.println(board);

        Scanner sc = new Scanner(System.in);
        boolean run=true;
        while(run) {
            System.out.print("Adja meg a lépést (pl b1 c3): ");
            String[] moveCoord=sc.nextLine().split(" ");
            int from = hm.get(moveCoord[0]);
            int to = hm.get(moveCoord[1]);
            board=board.reBuildBoard(from,to,board.getTile(from).getPiece());
            System.out.println(board);
            if(board.blackPlayer().isInCheckMate()){
                System.out.println("Checkmate! White win!");
                run=false;
            }
            else if(board.whitePlayer().isInCheckMate()){
                System.out.println("Checkmate! Black win!");
                run=false;
            }
        }
        //Table table = new Table();
    }

    public static void fillCoords(){
        hm.put("a8",0);  hm.put("b8",1); hm.put("c8",2); hm.put("d8",3); hm.put("e8",4); hm.put("f8",5); hm.put("g8",6); hm.put("h8",7);
        hm.put("a7",8);  hm.put("b7",9); hm.put("c7",10); hm.put("d7",11); hm.put("e7",12); hm.put("f7",13); hm.put("g7",14); hm.put("h7",15);
        hm.put("a6",16); hm.put("b6",17); hm.put("c6",18); hm.put("d6",19); hm.put("e6",20); hm.put("f6",21); hm.put("g6",22); hm.put("h6",23);
        hm.put("a5",24); hm.put("b5",25); hm.put("c5",26); hm.put("d5",27); hm.put("e5",28); hm.put("f5",29); hm.put("g5",30); hm.put("h5",31);
        hm.put("a4",32); hm.put("b4",33); hm.put("c4",34); hm.put("d4",35); hm.put("e4",36); hm.put("f4",37); hm.put("g4",38); hm.put("h4",39);
        hm.put("a3",40); hm.put("b3",41); hm.put("c3",42); hm.put("d3",43); hm.put("e3",44); hm.put("f3",45); hm.put("g3",46); hm.put("h3",47);
        hm.put("a2",48); hm.put("b2",49); hm.put("c2",50); hm.put("d2",51); hm.put("e2",52); hm.put("f2",53); hm.put("g2",54); hm.put("h2",55);
        hm.put("a1",56); hm.put("b1",57); hm.put("c1",58); hm.put("d1",59); hm.put("e1",60); hm.put("f1",61); hm.put("g1",62); hm.put("h1",63);

    }
}


