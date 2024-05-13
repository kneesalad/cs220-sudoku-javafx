package sudoku;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Board
{
    
public BoardSnapshot createSnapshot() {
    // ボードの状態のスナップショットを作成するコードを実装します
}

public List<Move> diff(BoardSnapshot otherSnapshot) {
    // 2 つのスナップショットを比較し、追加された値を見つけるコードを実装します
}

     public boolean isValidBoard() {
        // ボードが有効かどうかを確認するコードを追加します
        // 有効な場合は true を返し、無効な場合は false を返します
    }
}
    private int[][] board;

    public Board()
    {
        board = new int[9][9];
    }

    public static Board loadBoard(InputStream in)
    {
        Board board = new Board();
        Scanner scanner = new Scanner(in);
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                board.setCell(row, col, scanner.nextInt());
            }
        }
        scanner.close();
        return board;
    }

    public boolean isLegal(int row, int col, int value)
    {
        return value >= 1 && value <= 9 && getPossibleValues(row, col).contains(value);
    }

    public void setCell(int row, int col, int value)
    {
        if (value < 0 || value > 9)
        {
            throw new IllegalArgumentException("Value must be between 1 and 9 (or 0 to reset a value)");
        }
        if (value != 0 && !getPossibleValues(row, col).contains(value))
        {
            throw new IllegalArgumentException("Value " + value + " is not possible for this cell");
        }
        // based on other values in the sudoku grid
        board[row][col] = value;
    }

    public int getCell(int row, int col)
    {
        return board[row][col];
    }

    public boolean hasValue(int row, int col)
    {
        return getCell(row, col) > 0;
    }

    public Set<Integer> getPossibleValues(int row, int col)
    {
        Set<Integer> possibleValues = new HashSet<>();
        for (int i = 1; i <= 9; i++)
        {
            possibleValues.add(i);
        }
        // check the row
        for (int c = 0; c < 9; c++)
        {
            possibleValues.remove(getCell(row, c));
        }
        // check the column
        for (int r = 0; r < 9; r++)
        {
            possibleValues.remove(getCell(r, col));
        }
        // check the 3x3 square
        int startRow = row / 3 * 3;
        int startCol = col / 3 * 3;
        for (int r = startRow; r < startRow + 3; r++)
        {
            for (int c = startCol; c < startCol + 3; c++)
            {
                possibleValues.remove(getCell(r, c));
            }
        }
        return possibleValues;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                sb.append(getCell(row, col));
                if (col < 8)
                {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean isValidBoard() {
    // 各行を検証
    for (int row = 0; row < 9; row++) {
        Set<Integer> rowSet = new HashSet<>();
        for (int col = 0; col < 9; col++) {
            int cellValue = getCell(row, col);
            if (cellValue != 0 && !rowSet.add(cellValue)) {
                return false; // 重複が見つかった場合は無効なボード
            }
        }
    }
    
    // 各列を検証
    for (int col = 0; col < 9; col++) {
        Set<Integer> colSet = new HashSet<>();
        for (int row = 0; row < 9; row++) {
            int cellValue = getCell(row, col);
            if (cellValue != 0 && !colSet.add(cellValue)) {
                return false; // 重複が見つかった場合は無効なボード
            }
        }
    }
    
    // 各3x3のセルのグループを検証
    for (int startRow = 0; startRow < 9; startRow += 3) {
        for (int startCol = 0; startCol < 9; startCol += 3) {
            Set<Integer> groupSet = new HashSet<>();
            for (int row = startRow; row < startRow + 3; row++) {
                for (int col = startCol; col < startCol + 3; col++) {
                    int cellValue = getCell(row, col);
                    if (cellValue != 0 && !groupSet.add(cellValue)) {
                        return false; // 重複が見つかった場合は無効なボード
                    }
                }
            }
        }
    }
    
    // すべての検証を通過した場合は有効なボード
    return true;
}


}
