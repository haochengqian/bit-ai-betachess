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
    private GameView view;
    private String filename="高山流水-女子十二乐坊.mp3";
    private Player player;

    public static void main(String[] args) throws InterruptedException {
        ChessGame game = new ChessGame();
        //game.play();
        game.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                game.play();
            }
        }).start();
        game.run();

    }
    public void play() {
        try {
            BufferedInputStream buffer = new BufferedInputStream(
                    new FileInputStream(filename));
            player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }

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

            while (board.player == 'r')
                Thread.sleep(1000);

            if (controller.hasWin(board) != 'x')
                view.showWinner('r');
            view.showPlayer('b');
            /* AI in. */
            controller.responseMoveChess(board, view);
        }
        view.showWinner('b');
    }
}