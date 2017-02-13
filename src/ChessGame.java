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
    static public GameView view;

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
            PostApi text = new PostApi();
            view.showPlayer('r');

            /* User in. */
            while (board.player == 'r'){
                Thread.sleep(1000);
                if(view.isRestart==true){
                    break;
                }
            }

            if(view.isRestart == true){
                controller = new GameController();
                board = controller.playChess();
                view.controller=controller;
                view.init(board);
                view.isRestart=false;
                continue;
            }


            if (controller.hasWin(board) != 'x')
                view.showWinner('r');
            view.showPlayer('b');
            /* AI in. */
            controller.responseMoveChess(board, view);

            if(view.isRestart == true){
                view=null;
                controller = new GameController();
                board = controller.playChess();
                view.controller = controller;
                view.init(board);
                view.isRestart = false;
            }
        }
        view.showWinner('b');
    }
}