public class Grid {
    private Box[][] minefield;
    private int length;
    private int width;
    private int bombs;

    public Grid() {
        this(30, 16, 99);
    }

    public Grid(int length, int width, int bombs) {
        this.length = length;
        this.width = width;
        this.bombs = bombs;
        minefield = new Box[width][length];
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getBombs() {
        return bombs;
    }

    public void populate() {
        // insert all the bombs
        for (int i = 0; i < bombs; i++) {
            int w = (int) (Math.random() * width);
            int l = (int) (Math.random() * length);
            Box box = minefield[w][l];
            if (box == null || !box.isMine()) {
                minefield[w][l] = new Box(true, w, l);
            } else {
                i--;
            }
        }

        // populate the rest of the minefield
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                Box box = minefield[i][j];
                if (box == null) {
                    minefield[i][j] = new Box(false, i, j);

                    // find out how many mines adjacent to this box
                    int count = 0;
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            // if inbounds increase count of the current box
                            if (i + x < width && i + x >= 0 && j + y < length && j + y >= 0) {
                                Box adj = minefield[i + x][j + y];
                                if (adj != null && adj.isMine()) {
                                    count++;
                                }
                            }
                        }
                    }
                    minefield[i][j].setNumMines(count);
                }
            }
        }
    }

    public Box[][] getMinefield() {
        return minefield;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[0].length; j++) {
                result += minefield[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }

    public static void main(String[] args) {
        Grid g = new Grid();
        g.populate();
        System.out.println(g);
    }
}
