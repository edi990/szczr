package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * G��wna klasa serwera.<br/>
 * Nas�uchuje na podanym porcie, kiedy nadchodzi zapytanie od klienta tworzy nowy w�tek do jego obs�ugi i wraca do nas�uchiwania.<br/>
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
	 * czy nas�uchuje
	 */
	static boolean GL_listening = true;
	
	/**
	 * Inicjalizacja serwera.
	 * @param args argumenty wywo�ania (nieistotne)
	 */
	public static void main(String[] args)
	{
		try {
			ComServer.getInstance().listen();
		} catch (IOException e) {
			System.err.println("Wyst�pi� b��d...");
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
	 * Tworzy/zwraca referencj� do jedynej instancji serwera.
	 * @return referencja do obiektu serwera
	 */
	public static ComServer getInstance() {
		if(instance == null) {
			instance = new ComServer();
		}
		return instance;
	}

	/**
	 * Nas�uchuje zapyta� na podanym porcie.
	 * @return kod b��du
	 * @throws IOException
	 */
	public int listen() throws IOException
	{
		ServerSocket serverSocket = null;
		int iPortNumber = 6666;
		
		try {
			System.out.println( "*** Uruchomiono nas�uchiwanie na porcie: " + iPortNumber + " ***" );
			serverSocket = new ServerSocket(iPortNumber);
		} catch (IOException e) {
			System.err.println("B�ad nas�uchu na porcie: " + iPortNumber);
			System.exit(1);
		}
		
		while(GL_listening)
		{
			ComServerThread clientServ; 
			System.out.println("*** Czekam na klienta. Port: " + iPortNumber + " ***");
			clientServ = new ComServerThread(serverSocket.accept());
			System.out.println("*** Nadesz�o ��danie... ***");
			clientServ.start();
		}
		System.out.println("*** Zamykam i zmykam... ***");
		serverSocket.close();
		return 0;
	}

	/**
	 * Dost�p do danych symulacji.
	 * @return referencja do obiektu z danymi symulacji.
	 * @see ComServerData
	 */
	public ComServerData getData() {
		return data;
	}

}
