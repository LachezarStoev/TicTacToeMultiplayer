import java.io.IOException;


public class Main {

	public static void main(String[] args) {
		MyServer server;
		try {
			server = new MyServer(MyServer.PORT);
			server.startListening();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
