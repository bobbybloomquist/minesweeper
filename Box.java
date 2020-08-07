import javafx.scene.control.Label;

public class Box {
    private boolean isMine;
    private boolean isFlagged;
    private boolean isRevealed;
    private int numMines;
    private int row;
    private int col;
    private Label label;

    public Box(boolean isMine, int r, int c) {
        this.isMine = isMine;    
        numMines = 0;
        isFlagged = false;
        isRevealed = false;
        row = r;
        col = c;
        label = new Label(" ");
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public int getNumMines() {
        return numMines;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Label getLabel() {
        return label;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setFlagged(boolean flag) {
        isFlagged = flag;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public void setNumMines(int mines) {
        numMines = mines;
    }

    public String toString() {
        if (isMine) {
            return "B";
        }
        return "" + numMines;
    }
}
