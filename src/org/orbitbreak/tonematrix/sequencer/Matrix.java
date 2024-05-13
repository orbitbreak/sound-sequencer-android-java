
package org.orbitbreak.tonematrix.sequencer;

import android.content.Context;

public class Matrix {

    private int rows;
    private int beats; 
    private int[] samples;
    private Context context;
    private Cell[][] data;
    private boolean enabled;

    public Matrix(Context ctx, int r, int cols) {
        context = ctx;
        rows = r;
        beats = cols;
        enabled = false;
        data = new Cell[r][cols];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] = new Cell();
    }

    public int getCellValue(int r, int c) {
        return (data[r][c]).getValue();
    }

    public void setCellValue(int r, int c, int v) {
        (data[r][c]).setValue(v);
    }

    public void grow(int n) {
        if (n <= 0)
            return;
    }

    public void shrink(int n) {
        if (n <= 0)
            return;
    }

    public void deleteColumn(int n) {
        if ((n <= 0) || (n >= this.beats))
            return;
    }
}
