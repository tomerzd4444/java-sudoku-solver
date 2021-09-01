package sample;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Main extends Application{

    TextArea[][] textAreas = new TextArea[9][9];
    private Group root;
    private Scene scene;

    private boolean checkIfValid(int posX, int posY, String number) {
        // same function as checkIfValid but with hypothetical number


        // checks if column is valid
        for (int i = 0; i < 9; i ++) {
            if (textAreas[posX][i].getText().equals(number)) {
                if (i == posY) continue;
                System.out.println("NOT VALID COLUMN");
                return false;

            }
        }

        // checks if row is valid
        for (int i = 0; i < 9; i ++ ) {
            if (textAreas[i][posY].getText().equals(number)) {
                if (i == posX) continue;
                System.out.println("NOT VALID ROW");
                return false;
            }
        }

        // check is box is valid

        System.out.println(posX / 3 + " " + posY / 3);
        int boxPositionX = posX / 3 * 3;
        int boxPositionY = posY / 3 * 3;
        for (int y = boxPositionY; y < boxPositionY + 3; y ++ ) {
            for (int x = boxPositionX; x < boxPositionX + 3; x ++) {
                if (textAreas[x][y].getText().equals(number)){
                    if (x == posX && y == posY) continue;
                    System.out.println("NOT VALID BOX");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkIfValid(int posX, int posY) {
        System.out.println("Testing x: " + posX + " y: " + posY);
        String number = textAreas[posX][posY].getText();
        if (number.equals("")) return true;

        // checks if column is valid
        for (int i = 0; i < 9; i ++) {
            if (textAreas[posX][i].getText().equals(number)) {
                if (i == posY) continue;
                System.out.println(posX + " " +  posY + " " + i + " " + posY);
                System.out.println("NOT VALID COLUMN");
                return false;

            }
        }

        // checks if row is valid
        for (int i = 0; i < 9; i ++ ) {
            if (textAreas[i][posY].getText().equals(number)) {
                if (i == posX) continue;
                System.out.println(posX + " " + posY + " " + i + " " + posX);
                System.out.println("NOT VALID ROW");
                return false;
            }
        }

        // check is box is valid

        // gets the box position
        int boxPositionX = posX / 3 * 3;
        int boxPositionY = posY / 3 * 3;
        for (int y = boxPositionY; y < boxPositionY + 3; y ++ ) {
            for (int x = boxPositionX; x < boxPositionX + 3; x ++) { // loop over each cell in box
                if (textAreas[x][y].getText().equals(number)){
                    if (x == posX && y == posY) continue;
                    System.out.println(posX + " " + posY + " " + x + " " + y + " " + boxPositionX + " " + boxPositionY);
                    System.out.println(textAreas[posX][posY].getText());
                    System.out.println("NOT VALID BOX");
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkIfBoardIsValid() {
        for (int y = 0; y < 9; y ++) {
            for (int x= 0; x < 9; x ++) {
                if (!checkIfValid(x, y)) { // loops over each cell to check if its valid
                    return false;
                }
            }
        }
        return true;
    }

    private boolean backtracking() {
        for (int y = 0; y < 9; y ++) {
            for (int x = 0; x < 9;  x ++) { // Loops over each cell
                if (textAreas[x][y].getText().equals("")) { // Checks if cell is empty
                    for (int n = 1; n < 10; n ++) { // loops over the digits 1 to 9
                        if (checkIfValid(x, y, String.valueOf(n))) { // check if the digit is valid
                            textAreas[x][y].setText(String.valueOf(n)); // if it is then sets the cell to that digit
                            if (backtracking()) { // and recursively check if all cells work with that digit
                                return true;
                            }
                            textAreas[x][y].clear();
                        }
                    }
                    return false;
                }

            }
        }
        return true;
    }

    private void solve() {
        if (!checkIfBoardIsValid()) { // Checks if board is valid
            System.out.println("BOARD IS NOT VALID");
        }
        System.out.println("Starting");
        System.out.println(backtracking()); // Starts backtracking
    }

    private void createCells() {
        TextArea textArea;
        for (int x = 0; x < 9; x ++ ){
            for (int y = 0; y < 9; y ++){
                // Creates TextArea and adds some parameters
                textArea = new TextArea();
                textArea.setLayoutX(x * scene.getWidth() / 9);
                textArea.setLayoutY(y * scene.getHeight() / 9);
                textArea.setPrefHeight(100);
                textArea.setPrefWidth(100);
                textArea.setStyle("-fx-font-size: 40");

                textArea.textProperty().addListener(new ChangeListener<String>() {
                    // Checks if user write in the TextArea
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String valueOfCell) {
                        StringProperty textProperty = (StringProperty) observableValue;
                        TextArea cell = (TextArea) textProperty.getBean();

                        if (valueOfCell.length() == 0) {
                            return;
                        }
                        if (valueOfCell.length() > 1) {
                            cell.setText(String.valueOf(valueOfCell.charAt(0)));
                        }
                        else if (!Character.isDigit(valueOfCell.charAt(0)) || valueOfCell.charAt(0) == '0') {
                            cell.clear();
                        }

                    }
                });

                root.getChildren().add(textArea);
                textAreas[x][y] = textArea;
            }
        }
    }

    private void drawBorders() {
        Line line, line2;

        for (int i = 1; i < 3; i ++) {

            line = new Line(i * scene.getWidth() / 3, 0, i * scene.getWidth() / 3, scene.getHeight()); // Create the border lines
            line2 = new Line(0, i * scene.getHeight() / 3, scene.getWidth(), i * scene.getHeight() / 3);

            root.getChildren().add(line); // Adds the border line to scene
            root.getChildren().add(line2);
        }
    }

    private void keyboardEventListener() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getText().equals("s")) { // Check if user pressed s
                    solve();
                }

            }
        });
    }


    @Override
    public void start(Stage primaryStage) {

        root = new Group();
        scene = new Scene(root, 900, 900, Color.WHITE); // Creates the scene
        System.out.println("PRESS S TO SOLVE");

        // Draws to scene and adds event listener
        createCells();
        drawBorders();
        keyboardEventListener();

        // Shows the scene
        primaryStage.setTitle("Sudoku Solver, press s to solve");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
