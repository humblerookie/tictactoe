package com.hr.tictactoe.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hr.tictactoe.R;
import com.hr.tictactoe.util.TicTacToe;

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
    @BindView(R.id.game_result)
    TextView gameResult;
    boolean isTurnX = true;
    public static final int SIZE = 3;
    int val[][] = new int[SIZE][SIZE];
    private boolean isGameFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        TicTacToe.setBoard(val);
        imageView = new AppCompatImageView[][]{{r1c1, r1c2, r1c3}, {r2c1, r2c2, r2c3}, {r3c1, r3c2, r3c3}};
    }

    public void reset() {
        gameResult.setText("");
        isTurnX = true;
        isGameFinished = false;
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
        if (val[i][j] == 0 && !isGameFinished) {
            if (isTurnX) {
                val[i][j] = 1;
            } else {
                val[i][j] = 2;
            }
            isTurnX = !isTurnX;
            renderScreen();
            if (!showGameResult() && switchCompat.isChecked()) {
                int x[] = TicTacToe.optimalMove(isTurnX ? 1 : 2);
                val[x[0]][x[1]] = isTurnX ? 1 : 2;
                renderScreen();
                showGameResult();
                isTurnX = !isTurnX;
            }

        }
    }


    private boolean showGameResult() {
        switch (TicTacToe.checkWinner()) {
            case 0:
                return false;
            case 1:
                gameResult.setText("X wins !!");
                isGameFinished = true;
                break;
            case 2:
                gameResult.setText("O wins !!");
                isGameFinished = true;
                break;
            case 3:
                gameResult.setText("Its a draw !!");
                isGameFinished = true;
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

}
