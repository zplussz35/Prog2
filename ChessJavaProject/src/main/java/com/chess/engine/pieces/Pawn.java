package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES={8,16,7,9};

    Pawn(final int piecePosition,final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate=this.piecePosition+
                    (this.pieceAlliance.getDirection()*currentCandidateOffset);
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if(currentCandidateOffset==8&&!board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                //TODO more work here
                legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));

            }
            else if(currentCandidateOffset==16&&this.isFirstMove&&
                    BoardUtils.SECOND_ROW[this.piecePosition]&&this.getPieceAlliance().isBlack()||
                    BoardUtils.SEVENTH_ROW[this.piecePosition]&&this.getPieceAlliance().isWhite()){
                final int behindCandidateDestinationCoordinate=this.piecePosition+(this.getPieceAlliance().getDirection()*8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()&&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                }
            }
            else if(currentCandidateOffset==7&&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.pieceAlliance.isWhite())||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                        //Todo more things here
                        legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                    }
                }

            }
            else if(currentCandidateOffset==9&&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition]&&this.pieceAlliance.isWhite())||
                      (BoardUtils.EIGHTH_COLUMN[this.piecePosition]&&this.pieceAlliance.isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()){
                        //Todo more things here
                        legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                    }
                }

            }
        }



        return ImmutableList.copyOf(legalMoves);
    }
}
