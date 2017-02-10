package control;

import algorithm.ANN;
import algorithm.AlphaBetaNode;
import algorithm.SearchModel;
import chess.Board;
/**
 * Created by haochengqian on 2017/2/8.
 */
public class RobotTrain {

    public ANN ann = null;
    int times = 0;

    public RobotTrain(ANN ann) {
        this.ann = ann;
        this.times = 0;
    }

    public int simulate(int flag_ann) {
        System.out.println("Robot starts to train");
        GameController robotGameController = new GameController();

        Board trainBoard = robotGameController.playChess();

        char winner = 'x';

        while (winner == 'x' && times < 1000) {
            SearchModel seachModel = new SearchModel();
            AlphaBetaNode result = seachModel.search(trainBoard, this.ann,flag_ann);

 //           System.out.println(trainBoard.player);
 //           System.out.println("The time is " + times);

            trainBoard.updatePiece(result.piece, result.to);

            winner = robotGameController.hasWin(trainBoard);

            times++;
        }
        if (winner == 'b') {
            times = 1000 - times;
        }
        if (winner == 'x') {
            times = 0;
        }
        System.out.println(winner);
        return times;
    }

//    public void RobotMoveChess(Board board){
//        SearchModel seachModel = new SearchModel();
//        AlphaBetaNode result = seachModel.search(board);
//        board.updatePiece(result.piece, result.to);
//
//        boolean isRedWin = board.pieces.get("bb0") == null;
//        boolean isBlackWin = board.pieces.get("rb0") == null;
//        if(isRedWin) {
//            System.out.println("r win");
//            //System.exit(0);
//        }
//        else if(isBlackWin) {
//            System.out.println("b win");
//            //System.exit(0);
//        }
//    }
}
