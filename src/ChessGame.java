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
        System.out.println("当前难度为简单\n点击难度升级按钮升级难度\n点击播放音乐开始播放音乐\n点击训练AI开始学习\n结果存在根目录annOut.txt文件\n点击重新开始重置棋盘\n点击棋子点击目标位置开始下棋");
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