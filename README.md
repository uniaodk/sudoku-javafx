# Sudoku - JavaFX

Sudoku game with generator, import and solver.

## Interface

![image](https://github.com/uniaodk/sudoku-javafx/assets/52884069/92397fea-92e4-4942-8df2-7f6cdeb6b8dd)

## Running

To run must has the JavaFX library and run the follow script.

    java --module-path "%JAVAFX_LIB%" --add-modules javafx.base,javafx.controls,javafx.fxml -jar sudoku-1.0.jar

## Import - Sudoku Fields

To import the sudoku fields, must edit the text area and paste/type the puzzle. Does'nt matter if has any letter or symbols, will only accept numbers.

"0' (Zero) or "_" (underscore) = empty field

    // Example
    int[][] puzzle = {{0,8,0,0,0,5,0,0,0},
                     {0,3,9,2,0,1,0,8,7},
                     {0,0,6,0,8,0,9,2,0},
                     {7,2,0,0,0,0,0,3,0},
                     {0,0,4,0,0,0,1,0,0},
                     {0,6,0,0,0,0,0,7,2},
                     {0,4,5,0,3,0,7,0,0},
                     {8,7,0,1,0,9,2,6,0},
                     {0,0,0,5,0,0,0,9,0}};
