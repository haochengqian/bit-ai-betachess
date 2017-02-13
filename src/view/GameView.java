package view;

import algorithm.EvolutionaryProgramming.EvolutionaryProgramming;
import chess.Board;
import chess.Piece;
import chess.Rules;
import control.GameController;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
    private JFrame frame;
    private JLayeredPane pane;
    private GameController controller;
    private JLabel lblPlayer;
    private JButton lblButton;
    private JButton lblPutAnn;
    private Player player;
    private String musicName = "高山流水-女子十二乐坊.mp3";
    public int SetAi = 0;
    public boolean isPlayingMusic = false;

    public GameView(GameController gameController) {
        this.controller = gameController;
    }

    public void init(final Board board) {
        this.board = board;
        frame = new JFrame("Beta Chess");
        frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());
        frame.setSize(VIEW_WIDTH + 180, VIEW_HEIGHT + 22);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pane = new JLayeredPane();
        frame.add(pane);

        /* Initialize chess board and listeners on each slot.*/
        JLabel bgBoard = new JLabel(new ImageIcon("res/img/board.png"));
        bgBoard.setLocation(0, 0);
        bgBoard.setSize(VIEW_WIDTH, VIEW_HEIGHT);
        bgBoard.addMouseListener(new BoardClickListener());
        pane.add(bgBoard, 1);

        /* Initialize player image.*/
        lblPlayer = new JLabel(new ImageIcon("res/img/r.png"));
        lblPlayer.setLocation(10, 320);
        lblPlayer.setSize(PIECE_WIDTH, PIECE_HEIGHT);
        pane.add(lblPlayer, 0);

        /*选择output作为输入*/
        lblPutAnn = new JButton("加载ANN");
        lblPutAnn.setLocation(710, 50);
        lblPutAnn.setSize(PIECE_WIDTH,PIECE_HEIGHT - 20);
        lblPutAnn.addActionListener(new PutAiClickListen());
        pane.add(lblPutAnn,0);

        //*************训练ai按钮
        lblButton = new JButton("训练AI");
        lblButton.setLocation(800, 50);
        lblButton.setSize(PIECE_WIDTH,PIECE_HEIGHT - 20);
        lblButton.addActionListener(new ButtonClickListener());
        pane.add(lblButton,0);

        /*播放音乐*/
        lblPutAnn = new JButton("播放音乐");
        lblPutAnn.setLocation(710, 120);
        lblPutAnn.setSize(PIECE_WIDTH,PIECE_HEIGHT - 20);
        lblPutAnn.addActionListener(new PlayMusicClickListener());
        pane.add(lblPutAnn,0);
        //*************end


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
    class PutAiClickListen implements ActionListener{
        public void actionPerformed(ActionEvent e){
            SetAi = 1;
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
                            player.play();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }).start();
            } else {
                player.close();
            }
            isPlayingMusic = !isPlayingMusic;
        }
    }

}
