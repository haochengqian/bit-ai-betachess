package view;

import algorithm.EvolutionaryProgramming.EvolutionaryProgramming;
import chess.Board;
import chess.Piece;
import chess.Rules;
import control.GameController;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tong on 12.10.
 * Dealing with graphics logic in the chess game. Render with j2d.
 */
public class GameView {
    private static final int VIEW_WIDTH = 700, VIEW_HEIGHT = 712;
    private static final int PIECE_WIDTH = 67, PIECE_HEIGHT = 67;
    private static final int SY_COE = 68, SX_COE = 68;
    private static final int SX_OFFSET = 50, SY_OFFSET = 15;
    private Map<String, JLabel> pieceObjects = new HashMap<String, JLabel>();
    private Board board;
    private Board trainBoard;
    private String selectedPieceKey;
    private String prePieceFrameKey;
    private String pieceFrameKey;
    public JFrame frame;
    private JLayeredPane pane;
    public GameController controller;
    private JLabel lblPlayer;
    public JScrollPane jsp;
    public JTextArea consoleLabel;
    private ConsoleToTextArea consoleToTextArea=null;
    private JButton lblTrainButton;
    private JButton lblInputANNButton;
    private JButton lblMusicButton;
    private JButton lblRestartButton;

    private Player player;
    private String musicName = "高山流水-女子十二乐坊.mp3";
    public int SetAi = 0;

    public boolean isPlayingMusic = false;
    public boolean isRestart = false;
    public boolean isTrainingAI = false;

    private Runnable1 r;
    private Thread t;
    public GameView(GameController gameController) {
        this.controller = gameController;
    }

    public void init(final Board board) {

        this.board = board;
        selectedPieceKey = null;
        prePieceFrameKey = null;
        pieceFrameKey = null;
        isPlayingMusic = false;
        isTrainingAI = false;
        SetAi = 0;

        if (isRestart == false) {
            frame = new JFrame("Beta Chess");
            frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());
            frame.setSize(VIEW_WIDTH + 240, VIEW_HEIGHT + 22);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            pane = new JLayeredPane();
            frame.add(pane);
        }
        pane.removeAll();

        /* Initialize chess board and listeners on each slot.*/
        JLabel bgBoard = new JLabel(new ImageIcon("res/img/board.png"));
        bgBoard.setLocation(0, 0);
        bgBoard.setSize(VIEW_WIDTH, VIEW_HEIGHT);
        bgBoard.addMouseListener(new BoardClickListener());
        pane.add(bgBoard, 1);

        JLabel nameLabel = new JLabel("beta chess", 0);
        nameLabel.setLocation(VIEW_WIDTH, 10);
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        nameLabel.setSize(240, 80);
        nameLabel.addMouseListener(new BoardClickListener());
        pane.add(nameLabel, 1);

        try{
            consoleToTextArea = new ConsoleToTextArea();
        } catch (IOException e) {
            System.err.println("创建ConsoleToTextArea出错");
        }
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });

        jsp = new JScrollPane(consoleToTextArea);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
        jsp.setSize(240, 300);
        jsp.setLocation(VIEW_WIDTH, 230);
        jsp.setBorder(BorderFactory.createLineBorder(Color.black));
        pane.add(jsp,"Center");

        /* Initialize player image.*/
        lblPlayer = new JLabel(new ImageIcon("res/img/r.png"));
        lblPlayer.setLocation(10, 320);
        lblPlayer.setSize(PIECE_WIDTH, PIECE_HEIGHT);
        pane.add(lblPlayer, 0);

        /*选择output作为输入*/
        lblInputANNButton = new JButton("加载ANN");
        lblInputANNButton.setLocation(710, 100);
        lblInputANNButton.setSize(90, PIECE_HEIGHT - 20);
        lblInputANNButton.addActionListener(new InputANNClickListener());
        pane.add(lblInputANNButton, 0);

        //*************训练ai按钮
        lblTrainButton = new JButton("训练AI");
        lblTrainButton.setLocation(820, 100);
        lblTrainButton.setSize(90, PIECE_HEIGHT - 20);
        lblTrainButton.addActionListener(new ButtonClickListener());
        pane.add(lblTrainButton, 0);

        /*播放音乐*/
        lblMusicButton = new JButton("播放音乐");
        lblMusicButton.setLocation(710, 170);
        lblMusicButton.setSize(90, PIECE_HEIGHT - 20);
        lblMusicButton.addActionListener(new PlayMusicClickListener());
        pane.add(lblMusicButton, 0);
        //*************end

        //重新开始
        lblRestartButton = new JButton("重新开始");
        lblRestartButton.setLocation(820, 170);
        lblRestartButton.setSize(90, PIECE_HEIGHT - 20);
        lblRestartButton.addActionListener(new RestartClickListener());
        pane.add(lblRestartButton, 0);
        /////////////**end

        /* Initialize chess pieces and listeners on each piece.*/
        Map<String, Piece> pieces = board.pieces;
        for (Map.Entry<String, Piece> stringPieceEntry : pieces.entrySet()) {
            String key = stringPieceEntry.getKey();
            int[] pos = stringPieceEntry.getValue().position;
            int[] sPos = modelToViewConverter(pos);
            JLabel lblPiece = new JLabel(new ImageIcon("res/img/" + key.substring(0, 2) + ".png"));

            lblPiece.setLocation(sPos[0], sPos[1]);
            lblPiece.setSize(PIECE_WIDTH, PIECE_HEIGHT);
            lblPiece.addMouseListener(new PieceOnClickListener(key));
            pieceObjects.put(stringPieceEntry.getKey(), lblPiece);
            pane.add(lblPiece, 0);

        }

        frame.setVisible(true);
    }


    public void movePieceFromModel(String pieceKey, int[] to) {
        JLabel pieceObject = pieceObjects.get(pieceKey);
        JLabel pieceFrameObject = pieceObjects.get(pieceFrameKey);

        int[] from = board.pieces.get(pieceKey).position;
        int[] pPos = modelToViewConverter(from);
        int[] sPos = modelToViewConverter(to);

        if (prePieceFrameKey == null) {
            JLabel lblPrePieceFrame = new JLabel(new ImageIcon("res/img/selected.png"));
            lblPrePieceFrame.setLocation(pPos[0], pPos[1]);
            lblPrePieceFrame.setSize(PIECE_WIDTH, PIECE_HEIGHT);
            prePieceFrameKey = "PreFrame";
            pieceObjects.put(prePieceFrameKey, lblPrePieceFrame);
            pane.add(lblPrePieceFrame, 0);
        } else {
            JLabel prePieceFrameObject = pieceObjects.get(prePieceFrameKey);
            prePieceFrameObject.setLocation(pPos[0], pPos[1]);
        }

        pieceObject.setLocation(sPos[0], sPos[1]);
        pieceFrameObject.setLocation(sPos[0], sPos[1]);

        /* Clear 'from' and 'to' info on the board */
        selectedPieceKey = null;
    }

    public void movePieceFromAI(String pieceKey, int[] to) {
        Piece inNewPos = board.getPiece(to);
        if (inNewPos != null) {
            pane.remove(pieceObjects.get(inNewPos.key));
            pieceObjects.remove(inNewPos.key);
        }

        JLabel pieceObject = pieceObjects.get(pieceKey);
        JLabel pieceFrameObject = pieceObjects.get(pieceFrameKey);

        int[] from = board.pieces.get(pieceKey).position;
        int[] pPos = modelToViewConverter(from);
        int[] sPos = modelToViewConverter(to);

        if (prePieceFrameKey == null) {
            JLabel lblPrePieceFrame = new JLabel(new ImageIcon("res/img/selected.png"));
            lblPrePieceFrame.setLocation(pPos[0], pPos[1]);
            lblPrePieceFrame.setSize(PIECE_WIDTH, PIECE_HEIGHT);
            prePieceFrameKey = "PreFrame";
            pieceObjects.put(prePieceFrameKey, lblPrePieceFrame);
            pane.add(lblPrePieceFrame, 0);
        } else {
            JLabel prePieceFrameObject = pieceObjects.get(prePieceFrameKey);
            prePieceFrameObject.setLocation(pPos[0], pPos[1]);
        }

        pieceObject.setLocation(sPos[0], sPos[1]);
        pieceFrameObject.setLocation(sPos[0], sPos[1]);

        /* Clear 'from' and 'to' info on the board */
        selectedPieceKey = null;
    }

    private int[] modelToViewConverter(int pos[]) {
        int sx = pos[1] * SX_COE + SX_OFFSET, sy = pos[0] * SY_COE + SY_OFFSET;
        return new int[]{sx, sy};
    }

    private int[] viewToModelConverter(int sPos[]) {
        /* To make things right, I have to put an 'additional sy offset'. God knows why. */
        int ADDITIONAL_SY_OFFSET = 25;
        int y = (sPos[0] - SX_OFFSET) / SX_COE, x = (sPos[1] - SY_OFFSET - ADDITIONAL_SY_OFFSET) / SY_COE;
        return new int[]{x, y};
    }

    public void showPlayer(char player) {
        lblPlayer.setIcon(new ImageIcon("res/img/" + player + ".png"));
        frame.setVisible(true);
    }

    public void showWinner(char player) {
        JOptionPane.showMessageDialog(null, (player == 'r') ? "Red player has won!" : "Black player has won!", "Intelligent Chinese Chess", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    class PieceOnClickListener extends MouseAdapter {
        private String key;

        PieceOnClickListener(String key) {
            this.key = key;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectedPieceKey != null && key.charAt(0) != board.player) {
                int[] pos = board.pieces.get(key).position;
                int[] selectedPiecePos = board.pieces.get(selectedPieceKey).position;
                /* If an enemy piece already has been selected.*/
                for (int[] each : new Rules().getNextMove(selectedPieceKey, selectedPiecePos, board)) {
                    if (Arrays.equals(each, pos)) {
                        // Kill self and move that piece.

                        pane.remove(pieceObjects.get(key));

                        pieceObjects.remove(key);

                        controller.moveChess(selectedPieceKey, pos, board);
                        movePieceFromModel(selectedPieceKey, pos);
                        break;
                    }
                }
            } else if (key.charAt(0) == board.player) {
                /* Select the piece.*/
                selectedPieceKey = key;

                int[] pos = board.pieces.get(key).position;
                int[] sPos = modelToViewConverter(pos);

                // Create piece frame
                if (pieceFrameKey == null) {
                    JLabel lblPieceFrame = new JLabel(new ImageIcon("res/img/selected.png"));
                    lblPieceFrame.setLocation(sPos[0], sPos[1]);
                    lblPieceFrame.setSize(PIECE_WIDTH, PIECE_HEIGHT);
                    pieceFrameKey = "Frame";
                    pieceObjects.put(pieceFrameKey, lblPieceFrame);
                    pane.add(lblPieceFrame, 0);

                } else {
                    JLabel pieceFrameObject = pieceObjects.get(pieceFrameKey);
                    pieceFrameObject.setLocation(sPos[0], sPos[1]);
                }

            }
        }
    }

    class BoardClickListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (selectedPieceKey != null) {
                int[] sPos = new int[]{e.getXOnScreen() - frame.getX(), e.getYOnScreen() - frame.getY()};
                int[] pos = viewToModelConverter(sPos);
                int[] selectedPiecePos = board.pieces.get(selectedPieceKey).position;
                for (int[] each : new Rules().getNextMove(selectedPieceKey, selectedPiecePos, board)) {
                    if (Arrays.equals(each, pos)) {
                        controller.moveChess(selectedPieceKey, pos, board);
                        movePieceFromModel(selectedPieceKey, pos);
                        break;
                    }
                }
            }
        }
    }

    class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (isTrainingAI == false) {
                r = new Runnable1();
                t = new Thread(r);
                t.start();
                lblTrainButton.setText("停止训练");
            } else {
                t.interrupt();
                lblTrainButton.setText("训练AI");
            }
            isTrainingAI = !isTrainingAI;
        }
    }
    class InputANNClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            SetAi = 1;
            lblInputANNButton.setText("加载完成");
        }
    }

    class PlayMusicClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (isPlayingMusic == false) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedInputStream buffer = new BufferedInputStream(
                                    new FileInputStream(musicName));
                            player = new Player(buffer);
                            while(true){
                                player.play();
                            }

                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }).start();
                lblMusicButton.setText("停止播放");
            } else {
                player.close();
                lblMusicButton.setText("播放音乐");
            }
            isPlayingMusic = !isPlayingMusic;
        }
    }
    class RestartClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            isRestart = true;
        }
    }
    public static void startWriterTestThread(final String name, final PrintStream ps, final int dalay, final int count,String s) {
        new Thread(new Runnable(){
            public void run() {
                    ps.println(s);
                    try {
                        Thread.sleep(dalay);
                    } catch (InterruptedException e) {
                        // TODO: handle exception
                    }
            }
        }).start();
    }

}

class Runnable1 implements Runnable{
    public void run(){
        EvolutionaryProgramming evolutionaryProgramming = null;
        try {
            evolutionaryProgramming = new EvolutionaryProgramming(50, 20);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            evolutionaryProgramming.evolove();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
