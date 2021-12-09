package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType,
          final int piecePosition,final Alliance pieceAlliance,
          final boolean isFirstMove){
        this.pieceType=pieceType;
        this.piecePosition=piecePosition;
        this.pieceAlliance=pieceAlliance;

        this.isFirstMove=isFirstMove;
        this.cachedHashCode=computeHashCode();
    }

    private int computeHashCode() {
        int result= pieceType.hashCode();
        result = 31* result + pieceAlliance.hashCode();
        result = 31* result + piecePosition;
        result = 31* result + (isFirstMove? 1:0);
        return result;
    }


    @Override
    public boolean equals(final Object other){
        if ( this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType()&&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }

    public int hashcode(){
        return this.cachedHashCode;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    public  PieceType getPieceType(){
        return this.pieceType;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType{

        PAWN("P",1){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N",3){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B",3){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R",5){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q",9){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K",100){
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };


        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName,final int pieceValue){
            this.pieceName=pieceName;
            this.pieceValue=pieceValue;

        }
        public int getPieceValue(){
            return this.pieceValue;
        }

        public abstract boolean isKing();

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isRook();
    }
}
