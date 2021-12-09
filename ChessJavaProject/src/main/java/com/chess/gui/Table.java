package com.chess.gui;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION= new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION= new Dimension(10,10);

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    public Table(){
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();

        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.boardPanel=new BoardPanel();
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setVisible(true);


    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar=new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open up a pgn file!");
            }});
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem=new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles= new ArrayList<>();
            for(int i=0;i< BoardUtils.NUM_TILES;i++){
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }
    private class TilePanel extends JPanel{
        private final int tileID;
        TilePanel(final BoardPanel boardPanel,
                  final int tileID){
            super(new GridLayout());
            this.tileID=tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            validate();

        }

        private void assignTileColor() {
            if(BoardUtils.EIGHTH_RANK[this.tileID] ||
                    BoardUtils.SIXTH_RANK[this.tileID] ||
                    BoardUtils.FOURTH_RANK[this.tileID] ||
                    BoardUtils.SECOND_RANK[this.tileID]){
                setBackground(this.tileID%2==0?lightTileColor:darkTileColor);
            } else if(BoardUtils.SEVENTH_RANK[this.tileID] ||
                    BoardUtils.FIFTH_RANK[this.tileID] ||
                    BoardUtils.THIRD_RANK[this.tileID] ||
                    BoardUtils.FIRST_RANK[this.tileID] ){
                setBackground(this.tileID%2!=0?lightTileColor:darkTileColor);
            }

        }
    }
}
