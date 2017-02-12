package view;

import algorithm.EvolutionaryProgramming.EvolutionaryProgramming;
import chess.Board;
import chess.Piece;
import chess.Rules;
import control.GameController;

import javax.swing.*;
import java.awt.event.*;
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
    private String pieceFrameKey;
    private JFrame frame;
    private JLayeredPane pane;
    private GameController controller;
    private JLabel lblPlayer;
    private JButton lblButton;
    private JButton lblPutAnn;
    public int SetAi = 0;

    public GameView(GameController gameController) {
        this.controller = gameController;
    }

    public void init(final Board board) {
        this.board = board;
        frame = new JFrame("Beta Chess");
        frame.setIconImage(new ImageIcon("res/img/icon.png").getImage());
        frame.setSize(VIEW_WIDTH, VIEW_HEIGHT + 40);
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

        //*************训练ai按钮
        lblButton = new JButton("训练ai.");
        lblButton.setLocation(10,150);
        lblButton.setSize(PIECE_WIDTH,PIECE_HEIGHT - 20);
        lblButton.addActionListener(new ButtonClickListener());
        pane.add(lblButton,0);
        //*************end

        /*选择output作为输入*/

        lblPutAnn = new JButton("加载out.");
        lblPutAnn.setLocation(10,490);
        lblPutAnn.setSize(PIECE_WIDTH,PIECE_HEIGHT - 20);
        lblPutAnn.addActionListener(new PutAiClickListen());
        pane.add(lblPutAnn,0);
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
        int[] sPos = modelToViewConverter(to);
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
        int[] sPos = modelToViewConverter(to);
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

                        System.out.println("Here~~~");
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
                    System.out.println(pieceFrameKey);

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
                        System.out.println("Here");
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
                evolutionaryProgramming = new EvolutionaryProgramming(10, 20);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                evolutionaryProgramming.evolove();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
//            for(int i = 1; i <= 1; i++){
//                GameController tempGameController = new GameController();
//                trainBoard = tempGameController.playChess();
//                RobotTrain robottrain = new RobotTrain(new ANN());
//                int times = 0;
//                while(true) {
//                    System.out.println(++times);
//                    System.out.println(robottrain.ann.getWeights());
//                    robottrain.RobotMoveChess(trainBoard);
//                }
//            }
        }
    }
    class PutAiClickListen implements ActionListener{
        public void actionPerformed(ActionEvent e){
            SetAi = 1;
        }
    }
}
