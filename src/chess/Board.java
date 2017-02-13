package chess;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tong on 12.03.
 * Chess > Board entity
 */


public class Board{
    public final int BOARD_WIDTH = 9, BOARD_HEIGHT = 10;
    public Map<String, Piece> pieces;
    public Map changeFEN;
    public char player = 'r';
    private Piece[][] cells = new Piece[BOARD_HEIGHT][BOARD_WIDTH];
    public int AiorCut = 0;
    public Board(Board b){
        this.pieces=b.pieces;
        this.player=b.player;
        this.cells=b.cells;
    }
    public Board(){

    }
    public boolean isInside(int[] position) {
        return isInside(position[0], position[1]);
    }

    public boolean isInside(int x, int y) {
        return !(x < 0 || x >= BOARD_HEIGHT
                || y < 0 || y >= BOARD_WIDTH);
    }

    public boolean isEmpty(int[] position) {
        return isEmpty(position[0], position[1]);
    }

    public boolean isEmpty(int x, int y) {
        return isInside(x, y) && cells[x][y] == null;
    }

    public boolean update(Piece piece) {
        int[] pos = piece.position;
        cells[pos[0]][pos[1]] = piece;
        return true;
    }

    public Piece updatePiece(String key, int[] newPos) {
        Piece orig = pieces.get(key);
        Piece inNewPos = getPiece(newPos);
        /* If the new slot has been taken by another piece, then it will be killed.*/
        if (inNewPos != null)
            pieces.remove(inNewPos.key);
        /* Clear original slot and updatePiece new slot.*/
        int[] origPos = orig.position;
        cells[origPos[0]][origPos[1]] = null;
        cells[newPos[0]][newPos[1]] = orig;
        orig.position = newPos;
        player = (player == 'r') ? 'b' : 'r';
        return inNewPos;
    }

    public boolean backPiece(String key) {
        int[] origPos = pieces.get(key).position;
        cells[origPos[0]][origPos[1]] = pieces.get(key);
        return true;
    }

    public String fetchFen(){
        changeFEN = new HashMap<String,String>();
        changeFEN.put("bj","r");changeFEN.put("bm","n");changeFEN.put("bx","b");changeFEN.put("bs","a");
        changeFEN.put("bb","k");changeFEN.put("bp","c");changeFEN.put("bz","p");
        changeFEN.put("rj","R");changeFEN.put("rm","N");changeFEN.put("rx","B");changeFEN.put("rs","A");
        changeFEN.put("rb","K");changeFEN.put("rp","C");changeFEN.put("rz","P");
        StringBuffer fen = new StringBuffer("");
        for(int i = 0;i<BOARD_HEIGHT;i++) {
            int nullstep = 0;
            for(int j=0;j<BOARD_WIDTH;j++){
                Piece temp = getPiece(i,j);
                if(temp == null){
                    nullstep++;
                }
                else {
                    if(nullstep!=0)
                        fen.append(nullstep);
                    String c = (String) changeFEN.get(String.valueOf(temp.color)+String.valueOf(temp.character));
                    fen.append(c);
                    nullstep = 0;
                }
            }
            if(nullstep!=0)
                fen.append(nullstep);
            if(i+1<BOARD_HEIGHT) fen.append("/");
        }
        fen.append("%20");
        fen.append(this.player=='r'?"w":"b");
        return fen.toString();
    }

    public Piece getPiece(int[] pos) {
        return getPiece(pos[0], pos[1]);
    }

    public void changeAIorCut(){
        AiorCut = (AiorCut + 1) % 2;
    }

    public Piece getPiece(int x, int y) {
        return cells[x][y];
    }
}