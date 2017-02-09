package control;

import alogrithm.AlphaBetaNode;
import alogrithm.SearchModel;
import chess.Board;
/**
 * Created by haochengqian on 2017/2/8.
 */
public class RobotTrain {

    public void RobotMoveChess(Board board){
        SearchModel seachModel = new SearchModel();
        AlphaBetaNode result = seachModel.search(board);
        board.updatePiece(result.piece,result.to);



        boolean isRedWin = board.pieces.get("bb0") == null;
        boolean isBlackWin = board.pieces.get("rb0") == null;
        if(isRedWin) {
            System.out.println("r win");
            System.exit(0);
        }
        else if(isBlackWin) {
            System.out.println("b win");
            System.exit(0);
        }
    }
}
