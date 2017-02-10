package algorithm;

import chess.Board;
import chess.Piece;
//import algorithm.neural;

import java.util.Map;

/**
 * Created by Tong on 12.08.
 * Eval Model.
 */
public class EvalModel {
    /*  [red, black] >> [PieceValue, PiecePosition, PieceControl, PieceFlexible, PieceProtect, PieceFeature]*/
    /* However, only PieceValue and PiecePosition are implemented, so the array size is set to 2. */
    /******************************************************修改***********************************/
    public int[][] values = new int[2][11];
    public neural neu = new neural();
    //public ANN ann = new ANN();
    /******************************************************修改结束*************************************/
    /**
     * @param player, eval the situation in player's perspective.
     */


    public int eval(Board board, char player, ANN ann,int flag_ann) {
        if(player == 'b') {
            for (Map.Entry<String, Piece> stringPieceEntry : board.pieces.entrySet()) {
                Piece piece = stringPieceEntry.getValue();
            /* The table in PiecePosition is for red player in default. To eval black player, needs to perform a mirror transformation. */
                int[] reversePosition = new int[]{board.BOARD_HEIGHT - 1 - piece.position[0], piece.position[1]};
                switch (piece.character) {
                    /*************************************修改****************************/
                    case 'b':
                        if (piece.color == 'r') values[0][0] += evalPieceValue(0);
                        else values[1][0] += evalPieceValue(0);
                        break;
                    case 's':
                        if (piece.color == 'r') values[0][1] += evalPieceValue(1);
                        else values[1][1] += evalPieceValue(1);
                        break;
                    case 'x':
                        if (piece.color == 'r') values[0][2] += evalPieceValue(2);
                        else values[1][2] += evalPieceValue(2);
                        break;
                    case 'm':
                        if (piece.color == 'r') {
                            values[0][3] += evalPieceValue(3);
                            values[0][4] += evalPiecePosition(-3, piece.position);
                        } else {
                            values[1][3] += evalPieceValue(3);
                            values[1][4] += evalPiecePosition(-3, reversePosition);
                        }
                        break;
                    case 'j':
                        if (piece.color == 'r') {
                            values[0][5] += evalPieceValue(4);
                            values[0][6] += evalPiecePosition(-4, piece.position);
                        } else {
                            values[1][5] += evalPieceValue(4);
                            values[1][6] += evalPiecePosition(-4, reversePosition);
                        }
                        break;
                    case 'p':
                        if (piece.color == 'r') {
                            values[0][7] += evalPieceValue(5);
                            values[0][8] += evalPiecePosition(-5, piece.position);
                        } else {
                            values[1][7] += evalPieceValue(5);
                            values[1][8] += evalPiecePosition(-5, reversePosition);
                        }
                        break;
                    case 'z':
                        if (piece.color == 'r') {
                            values[0][9] += evalPieceValue(6);
                            values[0][10] += evalPiecePosition(-6, piece.position);
                        } else {
                            values[1][9] += evalPieceValue(6);
                            values[1][10] += evalPiecePosition(-6, reversePosition);
                        }
                        break;
                }
            }

            //int sumRed = values[0][0] + values[0][1] * 8, sumBlack = values[1][0] + values[1][1] * 8;
            int sumRed = 0;
            int sumBlack = 0;
            for (int i = 0; i < 11; i++) {
                sumRed += values[0][i];
                sumBlack += values[1][i];
            }

            sumRed += 7 * (values[0][4] + values[0][6] + values[0][8] + values[0][10]);
            sumBlack += 7 * (values[1][4] + values[1][6] + values[1][8] + values[1][10]);

            double sum_red = ann.evaluate(values[0]);
            double sum_black = ann.evaluate(values[1]);

            switch (player) {
                case 'r':{
                    if(flag_ann==1){
                        return (int) (sum_red - sum_black);
                    }
                    else{
                        return (int) (sumRed - sumBlack);
                    }
                }

                case 'b':
                    return (int) (sum_black - sum_red);
                default:
                    return -1;
            }
        }
        else {
            for (Map.Entry<String, Piece> stringPieceEntry : board.pieces.entrySet()) {
                Piece piece = stringPieceEntry.getValue();
            /* The table in PiecePosition is for red player in default. To eval black player, needs to perform a mirror transformation. */
                int[] reversePosition = new int[]{board.BOARD_HEIGHT - 1 - piece.position[0], piece.position[1]};
                switch (piece.character) {
                    case 'b':
                        if (piece.color == 'r') values[0][0] += evalPieceValue(0);
                        else values[1][0] += evalPieceValue(0);
                        break;
                    case 's':
                        if (piece.color == 'r') values[0][0] += evalPieceValue(1);
                        else values[1][0] += evalPieceValue(1);
                        break;
                    case 'x':
                        if (piece.color == 'r') values[0][0] += evalPieceValue(2);
                        else values[1][0] += evalPieceValue(2);
                        break;
                    case 'm':
                        if (piece.color == 'r') {
                            values[0][0] += evalPieceValue(3);
                            values[0][1] += evalPiecePosition(3, piece.position);
                        } else {
                            values[1][0] += evalPieceValue(3);
                            values[1][1] += evalPiecePosition(3, reversePosition);
                        }
                        break;
                    case 'j':
                        if (piece.color == 'r') {
                            values[0][0] += evalPieceValue(4);
                            values[0][1] += evalPiecePosition(4, piece.position);
                        } else {
                            values[1][0] += evalPieceValue(4);
                            values[1][1] += evalPiecePosition(4, reversePosition);
                        }
                        break;
                    case 'p':
                        if (piece.color == 'r') {
                            values[0][0] += evalPieceValue(5);
                            values[0][1] += evalPiecePosition(5, piece.position);
                        } else {
                            values[1][0] += evalPieceValue(5);
                            values[1][1] += evalPiecePosition(5, reversePosition);
                        }
                        break;
                    case 'z':
                        if (piece.color == 'r') {
                            values[0][0] += evalPieceValue(6);
                            values[0][1] += evalPiecePosition(6, piece.position);
                        } else {
                            values[1][0] += evalPieceValue(6);
                            values[1][1] += evalPiecePosition(6, reversePosition);
                        }
                        break;
                }
            }
            int sumRed = values[0][0] + values[0][1] * 8, sumBlack = values[1][0] + values[1][1] * 8;
            //System.out.println("a-b\n");
            switch (player) {
                case 'r':
                    return sumRed - sumBlack;
                case 'b':
                    return sumBlack - sumRed;
                default:
                    return -1;
            }
        }
    }

    private int evalPieceValue(int p) {
        /* b | s | x | m | j | p | z*/
        int[] pieceValue = new int[]{1000000, 110, 110, 300, 600, 300, 70};
        return pieceValue[p];
    }

    private int evalPiecePosition(int p, int[] pos) {
        int[][] pPosition = new int[][]{
                {6, 4, 0, -10, -12, -10, 0, 4, 6},
                {2, 2, 0, -4, -14, -4, 0, 2, 2},
                {2, 2, 0, -10, -8, -10, 0, 2, 2},
                {0, 0, -2, 4, 10, 4, -2, 0, 0},
                {0, 0, 0, 2, 8, 2, 0, 0, 0},
                {-2, 0, 4, 2, 6, 2, 4, 0, -2},
                {0, 0, 0, 2, 4, 2, 0, 0, 0},
                {4, 0, 8, 6, 10, 6, 8, 0, 4},
                {0, 2, 4, 6, 6, 6, 4, 2, 0},
                {0, 0, 2, 6, 6, 6, 2, 0, 0}
        };
        int[][] mPosition = new int[][]{
                {4, 8, 16, 12, 4, 12, 16, 8, 4},
                {4, 10, 28, 16, 8, 16, 28, 10, 4},
                {12, 14, 16, 20, 18, 20, 16, 14, 12},
                {8, 24, 18, 24, 20, 24, 18, 24, 8},
                {6, 16, 14, 18, 16, 18, 14, 16, 6},
                {4, 12, 16, 14, 12, 14, 16, 12, 4},
                {2, 6, 8, 6, 10, 6, 8, 6, 2},
                {4, 2, 8, 8, 4, 8, 8, 2, 4},
                {0, 2, 4, 4, -2, 4, 4, 2, 0},
                {0, -4, 0, 0, 0, 0, 0, -4, 0}
        };
        int[][] jPosition = new int[][]{
                {14, 14, 12, 18, 16, 18, 12, 14, 14},
                {16, 20, 18, 24, 26, 24, 18, 20, 16},
                {12, 12, 12, 18, 18, 18, 12, 12, 12},
                {12, 18, 16, 22, 22, 22, 16, 18, 12},
                {12, 14, 12, 18, 18, 18, 12, 14, 12},
                {12, 16, 14, 20, 20, 20, 14, 16, 12},
                {6, 10, 8, 14, 14, 14, 8, 10, 6},
                {4, 8, 6, 14, 12, 14, 6, 8, 4},
                {8, 4, 8, 16, 8, 16, 8, 4, 8},
                {-2, 10, 6, 14, 12, 14, 6, 10, -2}
        };
        int[][] zPosition = new int[][]{
                {0, 3, 6, 9, 12, 9, 6, 3, 0},
                {18, 36, 56, 80, 120, 80, 56, 36, 18},
                {14, 26, 42, 60, 80, 60, 42, 26, 14},
                {10, 20, 30, 34, 40, 34, 30, 20, 10},
                {6, 12, 18, 18, 20, 18, 18, 12, 6},
                {2, 0, 8, 0, 8, 0, 8, 0, 2},
                {0, 0, -2, 0, 4, 0, -2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        if (p == 3) return mPosition[pos[0]][pos[1]];
        if (p == 4) return jPosition[pos[0]][pos[1]];
        if (p == 5) return pPosition[pos[0]][pos[1]];
        if (p == 6) return zPosition[pos[0]][pos[1]];
        if (p == -3) return mPosition[9 - pos[0]][pos[1]];
        if (p == -4) return jPosition[9 - pos[0]][pos[1]];
        if (p == -5) return pPosition[9 - pos[0]][pos[1]];
        if (p == -6) return zPosition[9 - pos[0]][pos[1]];
        return -1;
    }

    private int evalPieceControl() {
        return 0;
    }

    private int evalPieceFlexible(int p) {
        // b | s | x | m | j | p | z
        int[] pieceFlexible = new int[]{0, 1, 1, 13, 7, 7, 15};
        return 0;
    }

    private int evalPieceProtect() {
        return 0;
    }

    private int evalPieceFeature() {
        return 0;
    }
}