package game;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;

public class Server {
    private static Socket player1;
    private static Socket player2;
    private static PrintWriter out1;
    private static PrintWriter out2;
    private static BufferedReader in1;
    private static BufferedReader in2;
    private static String[] board = new String[9];
    private static boolean isPlayer1Turn = true;

    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);

            player1 = serverSocket.accept();
            out1 = new PrintWriter(player1.getOutputStream(), true);
            in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            out1.println("START X");

            player2 = serverSocket.accept();
            out2 = new PrintWriter(player2.getOutputStream(), true);
            in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            out2.println("START O");

            for (int i = 0; i < 9; i++) {
                board[i] = "";
            }

            var executor = Executors.newFixedThreadPool(2);
            executor.execute(() -> handlePlayer(in1, out1, out2, "X"));
            executor.execute(() -> handlePlayer(in2, out2, out1, "O"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handlePlayer(BufferedReader in, PrintWriter out, PrintWriter opponentOut, String mark) {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("MOVE")) {
                    String[] parts = message.split(" ");
                    int index = Integer.parseInt(parts[1]);
                    if (board[index].isEmpty() && ((isPlayer1Turn && mark.equals("X")) || (!isPlayer1Turn && mark.equals("O")))) {
                        board[index] = mark;
                        out.println("MOVE " + index + " " + mark);
                        opponentOut.println("MOVE " + index + " " + mark);
                        isPlayer1Turn = !isPlayer1Turn;

                        if (checkWin(mark)) {
                            out.println("WIN " + mark);
                            opponentOut.println("WIN " + mark);
                            break;
                        } else if (isBoardFull()) {
                            out.println("DRAW");
                            opponentOut.println("DRAW");
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkWin(String mark) {
        int[][] winPositions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] pos : winPositions) {
            if (mark.equals(board[pos[0]]) && mark.equals(board[pos[1]]) && mark.equals(board[pos[2]])) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBoardFull() {
        for (String cell : board) {
            if (cell.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
