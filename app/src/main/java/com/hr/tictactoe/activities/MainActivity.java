package com.hr.tictactoe.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hr.tictactoe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    AppCompatImageView imageView[][];
    @BindView(R.id.r1c1)
    AppCompatImageView r1c1;
    @BindView(R.id.r1c2)
    AppCompatImageView r1c2;
    @BindView(R.id.r1c3)
    AppCompatImageView r1c3;
    @BindView(R.id.r2c1)
    AppCompatImageView r2c1;
    @BindView(R.id.r2c2)
    AppCompatImageView r2c2;
    @BindView(R.id.r2c3)
    AppCompatImageView r2c3;
    @BindView(R.id.r3c1)
    AppCompatImageView r3c1;
    @BindView(R.id.r3c2)
    AppCompatImageView r3c2;
    @BindView(R.id.r3c3)
    AppCompatImageView r3c3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.player_type)
    SwitchCompat switchCompat;

    boolean isTurnX = true;

    public static final int SIZE = 3;
    int val[][] = new int[SIZE][SIZE];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        imageView = new AppCompatImageView[][]{{r1c1, r1c2, r1c3}, {r2c1, r2c2, r2c3}, {r3c1, r3c2, r3c3}};
    }

    public void reset() {

        isTurnX = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                val[i][j] = 0;
            }
        }
        renderScreen();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                reset();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @OnClick({R.id.r1c1, R.id.r1c2, R.id.r1c3, R.id.r2c1, R.id.r2c2, R.id.r2c3, R.id.r3c1, R.id.r3c2, R.id.r3c3})
    public void onClick(View view) {
        int i = 0, j = 0;
        switch (view.getId()) {
            case R.id.r1c1:
                break;
            case R.id.r1c2:
                j = 1;
                break;
            case R.id.r1c3:
                j = 2;
                break;
            case R.id.r2c1:
                i = 1;
                break;
            case R.id.r2c2:
                i = 1;
                j = 1;
                break;
            case R.id.r2c3:
                i = 1;
                j = 2;
                break;
            case R.id.r3c1:
                i = 2;
                break;
            case R.id.r3c2:
                i = 2;
                j = 1;
                break;
            case R.id.r3c3:
                i = 2;
                j = 2;
                break;
        }
        if (val[i][j] == 0) {
            if (isTurnX) {
                val[i][j] = 1;
            } else {
                val[i][j] = 2;
            }
            isTurnX = !isTurnX;
            renderScreen();
            if (!showGameResult() && switchCompat.isChecked()) {
                int x[] = evaluatePositionalValues(isTurnX);
                val[x[0]][x[1]] = isTurnX ? 1 : 2;
                renderScreen();
                showGameResult();
                isTurnX = !isTurnX;
            }

        }
    }


    private boolean showGameResult() {
        switch (evaluateGameState()) {
            case 0:
                return false;
            case 1:
                Toast.makeText(this, "X wins !!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "O wins !!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "Its a draw !!", Toast.LENGTH_SHORT).show();
                break;


        }
        return true;
    }

    private void renderScreen() {
        for (int i = 0; i < val.length; i++) {
            for (int j = 0; j < val[i].length; j++) {
                if (val[i][j] > 0) {
                    imageView[i][j].setImageResource(val[i][j] == 1 ? R.drawable.ic_x : R.drawable.ic_o);
                } else {
                    imageView[i][j].setImageDrawable(null);
                }
            }
        }
    }

    private int[] evaluatePositionalValues(boolean isTurnX) {
        int lastMax = Integer.MIN_VALUE;
        int score[][] = new int[SIZE][SIZE];
        int x[] = new int[2];
        String[][] print = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (val[i][j] != 0) {
                    score[i][j] = -100;
                    print[i][j] = "-100";
                } else {

                    score[i][j] = (int) (getHueristicForPosition(i, j, isTurnX) + 2 * getHueristicForPosition(i, j, !isTurnX));
                    print[i][j] = score[i][j] + "";
                }
                if (score[i][j] > lastMax) {
                    x[0] = i;
                    x[1] = j;
                    lastMax = score[i][j];
                }
            }
        }

        printValues(print);
        return x;

    }

    private void printValues(String[][] print) {
        for (String[] strings : print) {
            String s = "";
            for (String string : strings) {
                s += string + " | ";
            }
            Log.d("Guru", s);
        }
        Log.d("Guru", "--------------------------------------------");
    }

    private int getHueristicForPosition(int i, int j, boolean isTurnX) {

        int rowScore = 0, colScore = 0, diagLeftScore = 0, diagRightScore = 0;
        int key = isTurnX ? 1 : 2;
        int oppKey = isTurnX ? 2 : 1;
        boolean shouldCheckLeftScore = false, shouldCheckRightScore = false;
        if (i == j) {
            shouldCheckLeftScore = true;
        }

        if (SIZE - j - 1 == i) {
            shouldCheckRightScore = true;
        }
        for (int k = 0; k < SIZE; k++) {

            if (val[i][k] == oppKey) {
                rowScore += -2;
            } else {
                rowScore += val[i][k] == key ? 2 : 1;
            }

            if (val[k][j] == oppKey) {
                colScore += -2;
            } else {
                colScore += val[k][j] == key ? 2 : 1;
            }

            if (shouldCheckLeftScore) {
                if (val[k][k] == oppKey) {
                    diagLeftScore += -2;
                } else {
                    diagLeftScore += val[k][k] == key ? 2 : 1;
                }
            }

            if (shouldCheckRightScore) {
                if (val[k][SIZE - 1 - k] == oppKey) {
                    diagRightScore += -2;
                } else {
                    diagRightScore += val[k][SIZE - 1 - k] == key ? 2 : 1;
                }
            }

        }

        return rowScore + colScore + (shouldCheckLeftScore ? diagLeftScore : 0) + (shouldCheckRightScore ? diagRightScore : 0);
    }


    // getPossibleOfOppWin() + getPossibilityOfSelfWin()

    // 0 continue
    // 1 x wins
    // 2 o wins
    // 3 draw
    private int evaluateGameState() {
        for (int i = 0; i < SIZE; i++) {
            boolean areSame = true;
            int prev = val[i][0];
            if (prev > 0) {
                for (int j = 1; j < SIZE; j++) {
                    if (prev != val[i][j]) {
                        areSame = false;
                        break;
                    }
                }
                if (areSame) {
                    return prev;
                }
            }
        }
        for (int i = 0; i < SIZE; i++) {
            boolean areSame = true;
            int prev = val[0][i];
            if (prev > 0) {
                for (int j = 1; j < SIZE; j++) {
                    if (prev != val[j][i]) {
                        areSame = false;
                        break;
                    }
                }
                if (areSame) {
                    return prev;
                }
            }
        }
        boolean areSameDiagonalLeft = true;
        boolean areSameDiagonalRight = true;
        int prevLeft = val[0][0];
        int prevRight = val[0][SIZE - 1];
        for (int i = 0; i < SIZE; i++) {
            if (prevLeft == 0 || prevLeft != val[i][i]) {
                areSameDiagonalLeft = false;
            }
            if (prevRight == 0 || prevRight != val[SIZE - 1 - i][i]) {
                areSameDiagonalRight = false;
            }
        }

        if (areSameDiagonalLeft) {
            return prevLeft;
        }

        if (areSameDiagonalRight) {
            return prevRight;
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (val[i][j] == 0) {
                    return 0;
                }
            }
        }


        return 3;
    }
}
