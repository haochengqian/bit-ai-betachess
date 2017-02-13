package control;

import algorithm.ANN;
import algorithm.AlphaBetaNode;
import algorithm.PostApi;
import algorithm.SearchModel;
import chess.Board;
import chess.Piece;

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

    public int simulate() {
        System.out.println("Robot starts to train");
        GameController robotGameController = new GameController();
        Board trainBoard = robotGameController.playChess();
        trainBoard.changeAIorCut();
        SearchModel seachModel;
        AlphaBetaNode result;
        char winner = 'x';

        while (winner == 'x' && times < 1000) {


            seachModel = new SearchModel();
            result = seachModel.search(trainBoard, this.ann);
            trainBoard.updatePiece(result.piece, result.to);
            winner = robotGameController.hasWin(trainBoard);

            times++;
            if(winner != 'x' || times >= 1000)
                break;

            synchronized (this) {
                PostApi postapi = new PostApi();
                String resultGet = "";
                resultGet = postapi.sendGet("http://api.chessdb.cn:81/chessdb.php?action=query&egtbmetric=dtc&egtbmetric=dtm&board=", trainBoard.fetchFen());
                int[] pos = postapi.processInf(resultGet);
                if (pos[0] != Integer.MAX_VALUE) {
                    Piece piece = trainBoard.getPiece(pos[1], pos[0]);
                    pos[0] = pos[3];
                    pos[1] = pos[2];
                    trainBoard.updatePiece(piece.key, pos);
                } else {
                    seachModel = new SearchModel();
                    result = seachModel.search(trainBoard, this.ann);
                    trainBoard.updatePiece(result.piece, result.to);
                }
            }
            winner = robotGameController.hasWin(trainBoard);
            times++;
        }
        if (winner == 'r') {
            times = 1000 - times;
        }
        if (winner == 'x') {
            times = 500;
        }
        System.out.println(winner);
        return times;
    }
}
