import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

import javax.net.ssl.SSLSocket;

public class GameThread extends Thread {

	private BufferedReader firstPlayerInput;
	private PrintWriter firstPlayerOutput;
	private BufferedReader secondPlayerInput;
	private PrintWriter secondPlayerOutput;
	private SSLSocket firstPlayerSocket;
	private SSLSocket secondPlayerSocket;

	public GameThread(SSLSocket firstPlayer, SSLSocket secondPlayer)
			throws IOException, OutOfGameException {

		if (!isFirstPlayerOnMove()) {
			SSLSocket tempPlayer = firstPlayer;
			firstPlayer = secondPlayer;
			secondPlayer = tempPlayer;
		}

		this.firstPlayerSocket = firstPlayer;
		this.secondPlayerSocket = secondPlayer;
		this.firstPlayerInput = new BufferedReader(new InputStreamReader(
				firstPlayer.getInputStream()));
		this.firstPlayerOutput = new PrintWriter(new OutputStreamWriter(
				firstPlayer.getOutputStream()));
		this.secondPlayerInput = new BufferedReader(new InputStreamReader(
				secondPlayer.getInputStream()));
		this.secondPlayerOutput = new PrintWriter(new OutputStreamWriter(
				secondPlayer.getOutputStream()));

		firstPlayerOutput.println("first");
		firstPlayerOutput.flush();
		secondPlayerOutput.println("second");
		secondPlayerOutput.flush();

	}

	@Override
	public void run() {

		while (!isInterrupted()) {
			try {
				while (true) {
					Point firstPlayerMove = readMove(firstPlayerInput);
					writeMove(secondPlayerOutput, firstPlayerMove);
					Point secondPlayerMove = readMove(secondPlayerInput);
					writeMove(firstPlayerOutput, secondPlayerMove);
				}
			} catch (OutOfGameException e) {
				System.out.println("The game was ended");
				try {
					firstPlayerSocket.close();
				} catch (IOException e2) {
				}
				try {
					secondPlayerSocket.close();
				} catch (IOException e1) {
				}
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private Point readMove(BufferedReader reader) throws IOException,
			OutOfGameException {
		String data = reader.readLine();
		if (data == null) {
			throw new OutOfGameException();
		}
		String[] point = data.split(" ");
		int x = Integer.parseInt(point[0]);
		int y = Integer.parseInt(point[1]);
		return new Point(x, y);
	}

	private void writeMove(PrintWriter writer, Point point) {
		int x = (int) point.getX();
		int y = (int) point.getY();
		writer.println(String.format("%d %d", x, y));
		writer.flush();
	}

	private boolean isFirstPlayerOnMove() {
		return new Random().nextBoolean();
	}

}
