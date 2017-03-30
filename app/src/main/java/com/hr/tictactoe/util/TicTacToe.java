package com.hr.tictactoe.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe {

    private static int[][] board = null;
    private static Random randomGenerator = new Random();

    private static int calculateScore(int board[][], int depth) {
        int score = -1;

        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                score = board[i][0];
                break;
            }
            if (board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                score = board[0][i];
                break;
            }

        }
        if (score == -1) {
            if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
                score = board[1][1];
            } else if (board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
                score = board[1][1];
            }
        }
        return score == 1 ? 10 - depth : (score == 2 ? -10 + depth : 0);
    }

    private static boolean isAnyPositionEmpty(int board[][]) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int boardValue(boolean isX, int depth, int board[][]) {
        int score = calculateScore(board, depth);
        if (score == 10 - depth) {
            return score;
        }

        if (score == -10 + depth) {
            return score;
        }

        if (!isAnyPositionEmpty(board)) {
            return 0;
        }

        if (isX) {

            int bestval = Integer.MIN_VALUE;

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = 1;
                        bestval = Math.max(bestval, boardValue(false, depth + 1, board));
                        board[i][j] = 0;
                    }
                }
            }
            return bestval;
        } else {
            int bestval = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        board[i][j] = 2;
                        bestval = Math.min(bestval, boardValue(true, depth + 1, board));
                        board[i][j] = 0;
                    }
                }
            }
            return bestval;
        }
    }

    public static int[] optimalMove(int player) {
        int optimalValue = player == 1 ? -Integer.MIN_VALUE : Integer.MAX_VALUE;
        List<int[]> moves = new ArrayList<>();
        int ret[] = {0, 0};
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = player;
                    int moveVal = boardValue(player != 1, 0, board);
                    if ((player == 1 && moveVal > optimalValue) || (player == 2 && moveVal < optimalValue)) {
                        optimalValue = moveVal;
                        ret[0] = i;
                        ret[1] = j;
                        moves.clear();
                        moves.add(ret);
                    } else if ((player == 1 && moveVal == optimalValue) || (player == 2 && moveVal == optimalValue)) {
                        moves.add(new int[]{i, j});
                    }
                    board[i][j] = 0;
                }
            }
        }
        if (moves.size() > 1) {
            return moves.get(randomGenerator.nextInt(moves.size()));
        } else {
            return ret;
        }
    }

    public static int checkWinner() {
        int winner = 0;

        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                winner = board[i][0];
                break;
            }
            if (board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                winner = board[0][i];
                break;
            }

        }
        if (winner == 0) {
            if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
                winner = board[1][1];
            } else if (board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
                winner = board[1][1];
            }
        }
        if (winner == 0 && !isAnyPositionEmpty(board)) {
            return 3;
        }

        return winner;
    }

    public static void setBoard(int[][] data) {
        board = data;
    }


}
