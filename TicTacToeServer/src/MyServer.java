import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class MyServer {

	public static final int PORT = 3333;
	private SSLServerSocket serverSocket;
	private SSLSocket waitingPlayer;

	public MyServer(int port) throws IOException {

		serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault()
				.createServerSocket(PORT);
		serverSocket.setEnabledCipherSuites(serverSocket
				.getSupportedCipherSuites());
		System.out
				.println("The server was started on " + new Date().toString());
		waitingPlayer = null;
	}

	public boolean isStillWaiting(SSLSocket socket) throws IOException {
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		writer.println("ready"); // game is ready to start..
		writer.flush();
		try {
			String input = reader.readLine();
			return input != null;
		} catch (IOException e) {
			return false;
		}
	}

	public void startListening() throws IOException {

		SSLSocket currentPlayer = null;
		while (true) {
			try {
				currentPlayer = (SSLSocket) serverSocket.accept();
				if (waitingPlayer == null) {
					waitingPlayer = currentPlayer;

				} else if (isStillWaiting(waitingPlayer)
						&& isStillWaiting(currentPlayer)) {
					GameThread game = new GameThread(waitingPlayer,
							currentPlayer);
					game.start();
					System.out.println("A new game was started on"
							+ new Date().toString());
					waitingPlayer = null;
				} else {
					waitingPlayer = currentPlayer;
				}
			} catch (IOException | OutOfGameException e) {
				System.out.println("The game was ended.");
			}
		}
	}

}
