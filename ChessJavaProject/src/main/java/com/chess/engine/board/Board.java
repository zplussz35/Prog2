package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;


public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    private Board(Builder builder){
        this.gameBoard=createGameBoard(builder);
        this.whitePieces=calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.blackPieces=calculateActivePieces(this.gameBoard,Alliance.BLACK);
        this.enPassantPawn= builder.enPassantPawn;
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        this.whitePlayer = new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }
    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }
    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }



    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);

    }
    //--------------------------
    public Board reBuildBoard(int from, int to ,Piece piece) {
        Piece p = piece;
        boolean success=false;
        Builder builder = new Builder();
        builder.setMoveMaker(currentPlayer().getAlliance());
        for (Tile tile : gameBoard) {
            if (tile.getTileCoordinate() == from){
                if( tile.isTileOccupied()){
                    if(p.getPieceType() != Piece.PieceType.PAWN){
                        if(gameBoard.get(to).isTileOccupied()){
                            if(gameBoard.get(to).getPiece().getPieceAlliance()!=p.getPieceAlliance()){
                                Piece attackedPiece=gameBoard.get(to).getPiece();
                                if(p.calculateLegalMoves(this).contains(new Move.MajorAttackMove(this,p,to,attackedPiece))){
                                    if(p.getPieceAlliance()==this.currentPlayer.getAlliance()){
                                        builder.setPiece(p.movePiece(new Move.MajorAttackMove(this, p, to,attackedPiece))); //p.movePiece(new Move.MajorAttackMove(this, p, to,attackedPiece))
                                        success=true;
                                        break;
                                    }
                                    else{
                                        System.out.println("Dont correct alliance!");
                                    }
                                }
                                else{
                                    System.out.println("Illegal Move!");
                                }
                            }
                            else{
                                System.out.println("Cannot attack your own piece!");
                            }
                        }
                        else{
                            if(p.calculateLegalMoves(this).contains(new Move.MajorMove(this,p,to))){
                                if(p.getPieceAlliance()==this.currentPlayer.getAlliance()){
                                    builder.setPiece(p.movePiece(new Move.MajorMove(this, p, to)));
                                    success=true;
                                    break;
                                }
                                else{
                                    System.out.println("Dont correct alliance!");
                                }
                            }
                            else{
                                System.out.println("Illegal Move!");
                            }
                        }

                    }
                    else{
                        if(gameBoard.get(to).isTileOccupied()){
                            Piece attackedPiece=gameBoard.get(to).getPiece();
                            if(p.calculateLegalMoves(this).contains(new Move.PawnAttackMove(this,p,to,attackedPiece))){
                                if(p.getPieceAlliance()==this.currentPlayer.getAlliance()){
                                    builder.setPiece(p.movePiece(new Move.PawnAttackMove(this, p, to,attackedPiece)));
                                    success=true;
                                    break;
                                }
                                else{
                                    System.out.println("Dont correct alliance!");
                                }

                            }else{
                                System.out.println("Illegal Move!");
                            }

                        }
                        else{
                            if(Math.abs(to-from)==16&&((!gameBoard.get(from+8).isTileOccupied()&&currentPlayer().getAlliance()==Alliance.BLACK)||
                               (!gameBoard.get(from-8).isTileOccupied()&&currentPlayer().getAlliance()==Alliance.WHITE))){

                                if(p.calculateLegalMoves(this).contains(new Move.PawnJump(this,p,to))){
                                    if(p.getPieceAlliance()==this.currentPlayer.getAlliance()){
                                        builder.setPiece(p.movePiece(new Move.PawnJump(this, p, to)));
                                        success=true;
                                        break;
                                    }
                                    else{
                                        System.out.println("Dont correct alliance!");
                                    }
                                }else{
                                    System.out.println("Illegal Move!");
                                }
                            }
                            else{
                                if(p.calculateLegalMoves(this).contains(new Move.PawnMove(this,p,to))){
                                    if(p.getPieceAlliance()==this.currentPlayer.getAlliance()){
                                        builder.setPiece(p.movePiece(new Move.PawnMove(this, p, to)));
                                        success=true;
                                        break;
                                    }
                                    else{
                                        System.out.println("Dont correct alliance!");
                                    }

                                }else{
                                    System.out.println("Illegal Move!");
                                }
                            }
                        }
                    }
                }
                else{
                    System.out.println("This tile is not occupied!");
                }
            }
        }
        if(success){
            for (Tile tile : gameBoard) {
                if (tile.isTileOccupied() && !tile.getPiece().equals(p)) {
                    builder.setPiece(tile.getPiece());
                }
            }
            builder.setMoveMaker(currentPlayer().getOpponent().getAlliance());
        }
        else{
            for (Tile tile : gameBoard) {
                if (tile.isTileOccupied()) {
                    builder.setPiece(tile.getPiece());
                }
            }
        }
        return builder.build();
    }
    //----------------------------------

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        //Black layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        //white layout
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));

    }

    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        public Builder(){
            this.boardConfig = new HashMap<>();

        }
        public  Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }
        public  Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker=nextMoveMaker;
            return this;
        }

        //---
        public Alliance getNextMoveMaker(){
            return nextMoveMaker;
        }
        //---
        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn=enPassantPawn;
        }
    }


    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard,final  Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile:gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAlliance()==alliance){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    private static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0;i<BoardUtils.NUM_TILES;i++){
            tiles[i]=Tile.createTile(i,builder.boardConfig.get(i));

        }
        return ImmutableList.copyOf(Arrays.asList(tiles)); //'tiles' nem jó?? Arrays.aslist korrigálás
    }


    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i=0;i<BoardUtils.NUM_TILES;i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i+1)%BoardUtils.NUM_TILES_PER_ROW==0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
