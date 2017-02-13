import algorithm.PostApi;
import chess.Board;
import control.GameController;
import view.GameView;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

/**
 * Created by Tong on 12.08.
 * Main process of Chinese Chess Game.
 */
public class ChessGame {
    private Board board;
    private GameController controller;
    static private GameView view;

    public static void main(String[] args) throws InterruptedException {
        ChessGame game = new ChessGame();
        //game.play();
        game.init();
        game.run();
    }

    public void init() {
        controller = new GameController();
        board = controller.playChess();
        view = new GameView(controller);
        view.init(board);
    }

    public void run() throws InterruptedException {
        while (controller.hasWin(board) == 'x') {

            System.out.println(board.fetchFen());
            PostApi text = new PostApi();
            System.out.println(text.sendGet("http://api.chessdb.cn:81/chessdb.php?action=query&egtbmetric=dtc&egtbmetric=dtm&board=",board.fetchFen()));


            view.showPlayer('r');
            /* User in. */
//            controller.responseMoveChess(board, view);

            while (board.player == 'r'){
                Thread.sleep(1000);
                if(view.reStart==true){
                    break;
                }
            }

            if(view.reStart==true){

                System.out.println("*");
                //init();
                view=null;
                controller = new GameController();
                board = controller.playChess();
                view = new GameView(controller);
                view.init(board);
                continue;
            }


            if (controller.hasWin(board) != 'x')
                view.showWinner('r');
            view.showPlayer('b');
            /* AI in. */
            controller.responseMoveChess(board, view);

            if(view.reStart==true){

                System.out.println("*");
                //init();
                view=null;
                controller = new GameController();
                board = controller.playChess();
                view = new GameView(controller);
                view.init(board);
            }
        }
        view.showWinner('b');
    }
}