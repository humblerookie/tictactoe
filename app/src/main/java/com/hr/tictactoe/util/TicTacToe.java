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

    /**
     * This function calculates possibility of victory at a given board state
     * This is calculated by evaluating rows and olcums and two diagonals for
     * a situation where there are two spots occupied by the player and the
     * third spot empty.
     */
    private static int calculatePossibilityScore(int board[][]) {
        int boost = 0;
        // X And O Row/Column Diagonal counts
        int xrd = 0, ord = 0, xcd = 0, ocd = 0;

        for (int i = 0, k = 2; i < 3; i++, k--) {
            // Column/Row Counts
            int xc = 0, xr = 0;
            int oc = 0, or = 0;
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1) {
                    xc++;
                } else if (board[i][j] == 2) {
                    oc++;
                }

                if (board[j][i] == 1) {
                    xr++;
                } else if (board[j][i] == 2) {
                    or++;
                }
            }
            if (board[i][i] == 1) {
                xrd++;
            } else if (board[i][i] == 2) {
                ord++;
            }

            if (board[i][k] == 1) {
                xcd++;
            } else if (board[i][k] == 2) {
                ocd++;
            }

            boost += oc == 2 && xc == 0 ? -1 : (xc == 2 && oc == 0 ? 1 : 0);
            boost += or == 2 && xr == 0 ? -1 : (xr == 2 && or == 0 ? 1 : 0);

        }
        // Diagonal elements
        boost += ord == 2 && xrd == 0 ? -1 : (xrd == 2 && ord == 0 ? 1 : 0);
        boost += ocd == 2 && xcd == 0 ? -1 : (xcd == 2 && ocd == 0 ? 1 : 0);

        return boost;
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
        List<Integer> possibilityScore = new ArrayList<>();
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
                        possibilityScore.clear();
                        possibilityScore.add(calculatePossibilityScore(board));
                        moves.add(ret);
                    } else if ((player == 1 && moveVal == optimalValue) || (player == 2 && moveVal == optimalValue)) {
                        moves.add(new int[]{i, j});
                        possibilityScore.add(calculatePossibilityScore(board));
                    }
                    board[i][j] = 0;
                }
            }
        }
        if (moves.size() > 1) {
            boolean haveDifferentPossibilityOfWinning = false;
            int current = possibilityScore.get(0);
            int index = 0, maxScore = current;
            for (int i = 1; i < possibilityScore.size(); i++) {
                if (current != possibilityScore.get(i)) {
                    haveDifferentPossibilityOfWinning = true;

                }

                if ((player == 1 && possibilityScore.get(i) > maxScore) || (player == 2 && possibilityScore.get(i) < maxScore)) {
                    maxScore = possibilityScore.get(i);
                    index = i;
                }
            }

            if (!haveDifferentPossibilityOfWinning) {
                return moves.get(randomGenerator.nextInt(moves.size()));
            } else {
                return moves.get(index);
            }
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
