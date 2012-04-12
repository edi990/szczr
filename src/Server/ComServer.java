package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * G³ówna klasa serwera.<br/>
 * Nas³uchuje na podanym porcie, kiedy nadchodzi zapytanie od klienta tworzy nowy w¹tek do jego obs³ugi i wraca do nas³uchiwania.<br/>
 * Obiekt jest Singletonem.
 * @see ComServerThread
 */
final public class ComServer 
{
	/**
	 * referencja do jedynej instancji serwera
	 */
	private static ComServer instance = null;
	
	/**
	 * dane symulacji
	 * @see ComServerData
	 */
	private ComServerData data;

	/**
	 * czy nas³uchuje
	 */
	static boolean GL_listening = true;
	
	/**
	 * Inicjalizacja serwera.
	 * @param args argumenty wywo³ania (nieistotne)
	 */
	public static void main(String[] args)
	{
		try {
			ComServer.getInstance().listen();
		} catch (IOException e) {
			System.err.println("Wyst¹pi³ b³¹d...");
		} 
	}

	/**
	 * Tworzy obiekt danych symulacji.
	 * @see ComServerData
	 */
	private ComServer() {
		data = new ComServerData();
	}

	/**
	 * Tworzy/zwraca referencjê do jedynej instancji serwera.
	 * @return referencja do obiektu serwera
	 */
	public static ComServer getInstance() {
		if(instance == null) {
			instance = new ComServer();
		}
		return instance;
	}

	/**
	 * Nas³uchuje zapytañ na podanym porcie.
	 * @return kod b³êdu
	 * @throws IOException
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

	/**
	 * Dostêp do danych symulacji.
	 * @return referencja do obiektu z danymi symulacji.
	 * @see ComServerData
	 */
	public ComServerData getData() {
		return data;
	}

}
