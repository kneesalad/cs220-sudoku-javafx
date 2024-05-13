package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Sudoku extends Application
{
    private Board board = new Board();
    public static final int SIZE = 9;
    private VBox root;
    private TextField[][] textFields = new TextField[SIZE][SIZE];
    private int width = 800;
    private int height = 800;
    private boolean updatingBoard = false;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        root = new VBox();

        //System.out.println(new File(".").getAbsolutePath());

        root.getChildren().add(createMenuBar(primaryStage));

        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
        gridPane.getStyleClass().add("grid-pane");

        // create a 9x9 grid of text fields
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                textFields[row][col] = new TextField();
                TextField textField = textFields[row][col];
                
                // setting ID so that we can look up the text field by row and col
                // IDs are #3-4 for the 4th row and 5th column (start index at 0)
                textField.setId(row + "-" + col);
                gridPane.add(textField, col, row);
                // using CSS to get the darker borders correct
                if (row % 3 == 2 && col % 3 == 2)
                {
                    // we need a special border to highlight the borrom right
                    textField.getStyleClass().add("bottom-right-border");
                }
                else if (col % 3 == 2) { 
                    // Thick right border
                    textField.getStyleClass().add("right-border");
                }
                else if (row % 3 == 2) { 
                    // Thick bottom border
                    textField.getStyleClass().add("bottom-border");
                }

                // add a handler for when we select a textfield
                textField.setOnMouseClicked(event -> {
                    // toggle highlighting
                    if (textField.getStyleClass().contains("text-field-selected"))
                    {
                        // remove the highlight if we click on a selected cell
                        textField.getStyleClass().remove("text-field-selected");
                    }
                    else
                    {
                        // otherwise 
                        textField.getStyleClass().add("text-field-selected");
                    }
                });

                // add a handler for when we lose focus on a textfield
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue)
                    {
                        // remove the highlight when we lose focus
                        textField.getStyleClass().remove("text-field-selected");
                    }
                });

                // RIGHT-CLICK handler
                // add handler for when we RIGHT-CLICK a textfield
                // to bring up a selection of possible values
                textField.setOnContextMenuRequested(event -> {
                    // change the textfield background to red while keeping the rest of the css the same
                    textField.getStyleClass().add("text-field-highlight");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Possible values");
                    // TODO: show a list of possible values that can go in this square
                    alert.setContentText("1 2 3 4 5 6 7 8 9");
                    alert.showAndWait();
                    textField.getStyleClass().remove("text-field-highlight");
                });

                // using a listener instead of a KEY_TYPED event handler
                // KEY_TYPED requires the user to hit ENTER to trigger the event
                textField.textProperty().addListener((observable, oldValue, newValue) -> {

                    if (updatingBoard)
                    {
                        return;
                    }

                    if (!newValue.matches("[1-9]?")) {
                        // restrict textField to only accept single digit numbers from 1 to 9
                        textField.setText(oldValue);
                    }
                    String id = textField.getId();
                    String[] parts = id.split("-");
                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    
                    if (newValue.length() > 0)
                    {
                        try
                        {
                            System.out.printf("Setting cell %d, %d to %s\n", r, c, newValue);
                            int value = Integer.parseInt(newValue);
                            board.setCell(r, c, value);
                            // remove the highlight when we set a value
                            textField.getStyleClass().remove("text-field-selected");
                        }
                        catch (NumberFormatException e)
                        {
                            // ignore; should never happen
                        }
                        catch (Exception e)
                        {
                            // TODO: if the value is not a possible value, catch the exception and show an alert
                            System.out.println("Invalid Value: " + newValue);
                        }
                    }
                    else
                    {
                        board.setCell(r, c, 0);
                    }
                });
            }
        }

        // add key listener to the root node to grab ESC keys
        root.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode());
            switch (event.getCode())
            {
                // check for the ESC key
                case ESCAPE:
                    // clear all the selected text fields
                    for (int row = 0; row < SIZE; row++)
                    {
                        for (int col = 0; col < SIZE; col++)
                        {
                            TextField textField = textFields[row][col];
                            textField.getStyleClass().remove("text-field-selected");
                        }
                    }
                    break;
                default:
                    System.out.println("you typed key: " + event.getCode());
                    break;
                
            }
        });

        Scene scene = new Scene(root, width, height);

        URL styleURL = getClass().getResource("/style.css");
		String stylesheet = styleURL.toExternalForm();
		scene.getStylesheets().add(stylesheet);
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
        	System.out.println("oncloserequest");
        });
    }
    // Undo メニューアイテムが選択されたときに呼び出されるメソッド
    private void undoMove() {
        if (!moveStack.isEmpty()) {
        // スタックから最後の移動を取り出す
        Move lastMove = moveStack.pop();

        // その移動を元に戻す（セルの値を元の値に戻すなど）
        // 例：board.setCell(lastMove.getRow(), lastMove.getCol(), 0);

        // その他の処理
    }
    }

        // Show values entered メニューアイテムが選択されたときに呼び出されるメソッド
    private void showEnteredValues() {
        StringBuilder enteredValues = new StringBuilder();
            for (Move move : moveStack) {
        enteredValues.append(move.getValue()).append(" ");
    }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Values entered since loading the board");
        alert.setHeaderText(null);
        alert.setContentText("Entered values: " + enteredValues.toString());
        alert.showAndWait();
}

public static void main(String[] args) {
    launch(args);
}


    private void updateBoard()
    {
        updatingBoard = true;
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                TextField textField = textFields[row][col];
                int value = board.getCell(row, col);
                if (value > 0)
                {
                    textField.setText(Integer.toString(value));
                }
                else
                {
                    textField.setText("");
                }
            }
        }
        updatingBoard = false;
    }

    private MenuBar createMenuBar(Stage primaryStage)
    {
        MenuBar menuBar = new MenuBar();
    	menuBar.getStyleClass().add("menubar");

        //
        // File Menu
        //
    	Menu fileMenu = new Menu("File");

        // "Load from file" メニューアイテムの作成
    addMenuItem(fileMenu, "Load from file", () -> {
    System.out.println("Load from file");
    FileChooser fileChooser = new FileChooser();
    // XXX: this is a hack to get the file chooser to open in the right directory
    // we should probably have a better way to find this folder than a hard coded path
    fileChooser.setInitialDirectory(new File("../puzzles"));
    File sudokuFile = fileChooser.showOpenDialog(primaryStage);
    if (sudokuFile != null)
    {
        System.out.println("Selected file: " + sudokuFile.getName());
        
        try {
            // Board.loadBoard() メソッドを呼び出す部分を try ブロック内に移動する
            board = Board.loadBoard(new FileInputStream(sudokuFile));
            updateBoard();
        } catch (Exception e) {
            // pop up and error window
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Unable to load sudoku board from file "+ sudokuFile.getName());
            alert.setHeaderText(e.getMessage());
            alert.setContentText(e.getMessage());
            e.printStackTrace();
            if (e.getCause() != null) e.getCause().printStackTrace();
            
            alert.showAndWait();    
        }

        import java.util.Stack;

        public class Sudoku extends Application
        {
        // Move スタックを保存するための変数
         private Stack<Move> moveStack = new Stack<>();
         // ...

             // ユーザーが新しい移動を行ったときに呼び出されるメソッド
         private void makeMove(int row, int col, int value)
     {
        // 現在のボード状態をスタックに保存する
        moveStack.push(new Move(row, col, value));
        
        // その他の処理（ボードの更新など）
    }

    // Undo メニューアイテムが選択されたときに呼び出されるメソッド
    private void undoMove()
    {
        if (!moveStack.isEmpty())
        {
            // スタックから最後の移動を取り出す
            Move lastMove = moveStack.pop();
            
            // その移動を元に戻す（セルの値を元の値に戻すなど）
            // 例：board.setCell(lastMove.getRow(), lastMove.getCol(), 0);
            
            // その他の処理
        }
    }
    }



    }
    }};


        // save to text
        addMenuItem(fileMenu, "Save to text", () -> {
            System.out.println("Save puzzle to text");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("../puzzles"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null)
            {
                System.out.println("Selected file: " + file.getName());
                try {
                    //TODO: check if the file already exists, and ask the user if they want to overwrite
                    writeToFile(file, board.toString());
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Unable to save to file");
                    alert.setHeaderText("Unsaved changes detected!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
        
        addMenuItem(fileMenu, "Print Board", () -> {
            // Debugging method that just prints the board
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Board");
            alert.setHeaderText(null);
            alert.setContentText(board.toString());
            alert.showAndWait();
        });
        // add a separator to the fileMenu
        fileMenu.getItems().add(new SeparatorMenuItem());

        addMenuItem(fileMenu, "Exit", () -> {
            System.out.println("Exit");
            primaryStage.close();
        });

        menuBar.getMenus().add(fileMenu);

        //
        // Edit
        //
        Menu editMenu = new Menu("Edit");

        addMenuItem(editMenu, "Undo", () -> {
            System.out.println("Undo");
            //TODO: Undo the last move
        });

        addMenuItem(editMenu, "Show values entered", () -> {
            System.out.println("Show all the values we've entered since we loaded the board");
            //TODO: pop up a window showing all of the values we've entered
        });

        menuBar.getMenus().add(editMenu);

        //
        // Hint Menu
        //
        Menu hintMenu = new Menu("Hints");

        addMenuItem(hintMenu, "Show hint", () -> {
            System.out.println("Show hint");
            //TODO: highlight cell where only one legal value is possible
        });

        menuBar.getMenus().add(hintMenu);

        return menuBar;
    }

    private static void writeToFile(File file, String content) throws IOException
    {
        Files.write(file.toPath(), content.getBytes());
    }

    private void addMenuItem(Menu menu, String name, Runnable action)
    {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction(event -> action.run());
        menu.getItems().add(menuItem);
    }
        
    public static void main(String[] args) 
    {
        launch(args);
    }
}
