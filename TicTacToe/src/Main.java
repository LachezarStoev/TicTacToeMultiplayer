import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Main {

	public static final String SERVER_HOSTNAME = "localhost";
	public static final int SERVER_PORT = 3333;
	private static BufferedReader socketReader;
	private static PrintWriter socketWriter;

	public static void main(String[] args) {

		TicTacToe game = null;
		boolean gameIsRunning = true;
		try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault()
				.createSocket(SERVER_HOSTNAME, SERVER_PORT)) {
			socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
			socketReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			socketWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			System.out.println("Waiting for opponent..");
			socketReader.readLine(); // the server start the game
			socketWriter.println("ready"); // still waiting
			socketWriter.flush();
			String firstOnMove = socketReader.readLine();
			if (firstOnMove.equals("first")) {
				game = new TicTacToe("X", "O");
			} else {
				game = new TicTacToe("O", "X");
				readMove(socketReader, game);
			}
			while (gameIsRunning) {
				while (game.isOnTurn()) {

				}
				writeMove(socketWriter, game);
				if (game.hasWinner(game.getMyMark())) {
					game.displayWinMessage();
					gameIsRunning = false;
				}
				readMove(socketReader, game);
				if (game.hasWinner(game.getOpponentMark())) {
					game.displayLoseMessage();
					game.setOnTurn(false);
					gameIsRunning = false;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfGameException e) {
			game.displayOutOfGameMessage();
		}
	}

	private static void readMove(BufferedReader reader, TicTacToe game)
			throws IOException, OutOfGameException {
		String input = reader.readLine();
		if (input == null) {
			throw new OutOfGameException();
		}
		String[] point = input.split(" ");
		int x = Integer.parseInt(point[0]);
		int y = Integer.parseInt(point[1]);
		game.opponentMove(x, y);
	}

	private static void writeMove(PrintWriter writer, TicTacToe game) {
		Point pos = game.getMyLastMove();
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		socketWriter.println(String.format("%d %d", x, y));
		socketWriter.flush();

	}

}
