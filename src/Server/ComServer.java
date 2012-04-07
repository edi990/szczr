package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Main Server Class- Listening on a port for client. If there is a client,
 * starts a new Thread and goes back to listening for further clients.
 */
final public class ComServer 
{
	private static ComServer instance = null;
	private ComServerData data;
	static boolean GL_listening = true;
	
	/**
	 * Main program to start the Server
	 */
	public static void main(String[] args) throws IOException
	{
		ComServer.getInstance().listen(); 
	}

	/**
	 * Create server data object
	 */
	protected ComServer() {
		data = new ComServerData();
	}

	/**
	 * Singleton getInstance method
	 */
	public static ComServer getInstance() {
		if(instance == null) {
			instance = new ComServer();
		}
		return instance;
	}

	/**
	 * Server method; Listen for client
	 */
	public int listen() throws IOException
	{
		ServerSocket serverSocket = null;
		int iPortNumber = 6666;
		
		try {
			System.out.println( "*** Uruchomiono nas³uchiwanie na porcie: " + iPortNumber + " ***" );
			serverSocket = new ServerSocket(iPortNumber);
		} catch (IOException e) {
			System.err.println("B³ad nas³uchu na porcie: " + iPortNumber);
			System.exit(1);
		}
		
		while(GL_listening)
		{
			ComServerThread clientServ; 
			System.out.println("*** Czekam na klienta. Port: " + iPortNumber + " ***");
			clientServ = new ComServerThread(serverSocket.accept());
			System.out.println("*** Nadesz³o ¿¹danie... ***");
			clientServ.start();
		}
		System.out.println("*** Zamykam i zmykam... ***");
		serverSocket.close();
		return 0;
	}

	public ComServerData getData() {
		return data;
	}

}
