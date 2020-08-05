import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseButton;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import java.util.*;

public class Minesweeper extends Application {
    private static Grid grid;
    private static Box[][] minefield;
    private ArrayList<Box> toReveal;
    private boolean lost;
    private boolean won;
    private int revealCount;
    private SimpleIntegerProperty bombsLeft;

    public void start(Stage stage) throws Exception {
        // set name of window
        stage.setTitle("Minesweeper");

        // initialize instance variables, including population of grid
        lost = false;
        won = false;
        revealCount = 0;
        bombsLeft = new SimpleIntegerProperty(99);
        grid = new Grid();
        grid.populate();
        toReveal = new ArrayList();

        // create group of labels that make up the interactive minesweeper game
        Group g = play(grid);

        // create the restart button
        Button restart = new Button("Play Again");
        restart.setPrefHeight(24);
        restart.setPrefWidth(121);
        restart.setLayoutX(349);
        restart.setLayoutY(25);
        restart.setOnMouseClicked(event ->
        {
            try {
                lost = false;
                start(stage);
            } catch(Exception e) {

            }
        });
        g.getChildren().add(restart);

        // create the flag counter in the top left
        Label bombCount = new Label();
        bombCount.setPrefHeight(56);
        bombCount.setPrefWidth(121);
        bombCount.setLayoutX(10);
        bombCount.setLayoutY(10);
        bombCount.setText(bombsLeft.asString().get());
        bombsLeft.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                bombCount.setText(bombsLeft.asString().get());
            }
        });
        g.getChildren().add(bombCount);

        // set the scene and show the stage to the user
        Scene scene = new Scene(g, 818, 540, Color.LIGHTGRAY);
        stage.setScene(scene);
        stage.show();
    }

    private Group play(Grid grid) {
        Group g = new Group();
        minefield = grid.getMinefield();
        // loop through the grid
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getLength(); j++) {
                Box box = minefield[i][j];
                // create a label for each box that represents the box graphically
                Label l = box.getLabel();
                l.setStyle("-fx-background-color: #BAB6B2");
                l.setLayoutX(j * 27 + 5);
                l.setLayoutY(i * 27 + 75);
                l.setPrefHeight(25);
                l.setPrefWidth(25);
                l.setOnMouseClicked(event ->
                {
                    // if left click and the game is not over
                    if (event.getButton() == MouseButton.PRIMARY && !lost && !won) {
                        toReveal.add(box);
                        box.setRevealed(true);
                        // if clicked box has no mines adjacent, reveal
                        if (box.getNumMines() == 0 && !box.isMine()) {
                            reveal(box.getRow(), box.getCol());
                        }

                        // iterate through toReveal list and reveal them
                        for (int k = toReveal.size() - 1; k >= 0; k--) {
                            Box curr = minefield[toReveal.get(k).getRow()][toReveal.get(k).getCol()];
                            toReveal.remove(k);
                            revealCount++;
                            System.out.println(revealCount);
                            curr.getLabel().setStyle("-fx-background-color: #A6A39E");
                            curr.getLabel().setAlignment(Pos.CENTER);
                            if (curr.isFlagged()) {
                                curr.setFlagged(false);
                                bombsLeft.setValue(bombsLeft.getValue() + 1);
                            }
                            if (curr.isMine()) {
                                // if revealed is a mine, lose
                                curr.getLabel().setStyle("-fx-background-color: black");
                                lost = true;
                                // iterate through the grid and show all bombs
                                for (int x = 0; x < grid.getWidth(); x++) {
                                    for (int y = 0; y < grid.getLength(); y++) {
                                        Box mine = minefield[x][y];
                                        if (mine.isMine()) {
                                            mine.getLabel().setStyle("-fx-background-color: black");
                                        }
                                    }
                                }
                                break;
                            } else if (curr.getNumMines() == 0) {
                                // if revealed has no adjacent mines, have a blank label
                                curr.getLabel().setText(" ");
                            } else {
                                // if revealed has a nonzero number of adjacent mines, adjust the color and number of
                                // the label accordingly
                                curr.getLabel().setText("" + curr.getNumMines());
                                if (curr.getNumMines() == 1) {
                                    curr.getLabel().setStyle("-fx-text-fill: blue; -fx-background-color: #A6A39E");
                                } else if(curr.getNumMines() == 2) {
                                    curr.getLabel().setStyle("-fx-text-fill: green; -fx-background-color: #A6A39E");
                                } else if(curr.getNumMines() == 3) {
                                    curr.getLabel().setStyle("-fx-text-fill: red; -fx-background-color: #A6A39E");
                                } else if (curr.getNumMines() == 4) {
                                    curr.getLabel().setStyle("-fx-text-fill: purple; -fx-background-color: #A6A39E");
                                } else if (curr.getNumMines() == 5) {
                                    curr.getLabel().setStyle("-fx-text-fill: maroon; -fx-background-color: #A6A39E");
                                } else if (curr.getNumMines() == 6) {
                                    curr.getLabel().setStyle("-fx-text-fill: turquoise; -fx-background-color: #A6A39E");
                                } else if (curr.getNumMines() == 8) {
                                    curr.getLabel().setStyle("-fx-text-fill: gray; -fx-background-color: #A6A39E");
                                }
                            }
                        }
                        if (revealCount == 381) {
                            won = true;
                            l.setStyle("-fx-background-color: green");
                        }

                    // if right click on a valid box, either add or remove a flag
                    } else if (event.getButton() == MouseButton.SECONDARY && !box.isRevealed() && !lost && !won) {
                        if (box.isFlagged()) {
                            box.setFlagged(false);
                            l.setStyle("-fx-background-color: #BAB6B2");
                            bombsLeft.setValue(bombsLeft.getValue() + 1);
                        } else {
                            box.setFlagged(true);
                            l.setStyle("-fx-background-color: red");
                            bombsLeft.setValue(bombsLeft.getValue() - 1);
                        }
                    }
                });
                g.getChildren().add(l);
            }
        }
        return g;
    }

    private void reveal(int i, int j) {
        // reveal box indicated by parameters
        if (!minefield[i][j].isRevealed()) {
            toReveal.add(minefield[i][j]);
            minefield[i][j].setRevealed(true);
        }
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                // if inbounds, recursively reveal if number of mines is 0
                if (i + x < grid.getWidth() && i + x >= 0 && j + y < grid.getLength() && j + y >= 0) {
                    Box adj = minefield[i + x][j + y];
                    if (adj != null && adj.getNumMines() == 0 && !adj.isMine() && !adj.isRevealed()) {
                        reveal(i + x, j + y);
                    }
                    if (!minefield[i + x][j + y].isRevealed()) {
                        System.out.println("this is not useless");
                        toReveal.add(minefield[i + x][j + y]);
                        minefield[i + x][j + y].setRevealed(true);
                    }
                }
            }
        }
    }
}
