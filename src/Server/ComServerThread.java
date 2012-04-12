package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import protocol.*;


/**
 * Klasa w¹tku obs³uguj¹cego zapytania od jednego klienta.
 */
class ComServerThread extends Thread
{
	/**
	 * uchwyt po³¹czenia
	 */
	private Socket clientSocket = null;

	/**
	 * paczka danych od klienta
	 */
	ComData tDataFromClient;

	/**
	 * paczka do wys³ania
	 */
	ComData tDataToClient;

	/**
	 * strumieñ wejœciowy
	 */
	ObjectInputStream oIn;

	/**
	 * strumieñ wyjœciowy
	 */
	ObjectOutputStream oOut;
	
	/**
	 * Inicjalizacja w¹tku.
	 * @param socket uchwyt po³¹czenia
	 */
	public ComServerThread(Socket socket)
	{
		super("ComServerThread");
		this.clientSocket = socket;
	}
	
	/**
	 * Nas³ychiwanie zapytañ, przetwarzanie i odpowiedŸ do klienta.<br/>
	 * Interpretacja zapytania i konstrukcja odpowiedzi:
	 * @see ComProtocol
	 */
	public void run()
	{
		try {
			oOut = new ObjectOutputStream(clientSocket.getOutputStream());
			oIn  = new ObjectInputStream(clientSocket.getInputStream());
			ComProtocol comp = new ComProtocol();
			// Send something to client to indicate that server is ready
			tDataToClient = comp.processInput(null);
			sendDataToClient(tDataToClient, oOut);
			// --- Get the data from the client ---
			while (true)
			{
				try {
					tDataFromClient = getDataFromClient(oIn);
					// --- Parse the request and get the reply ---
					tDataToClient = comp.processInput(tDataFromClient);
					// --- Send data to the Client ---
					sendDataToClient(tDataToClient, oOut);
				} catch (EOFException e){
					comp.markAsDisconnected();
					System.out.println("Klient roz³¹czony...");
					break;
				} catch (SocketException e){
					comp.markAsDisconnected();
					System.out.println("Klient roz³¹czony...");
					break;
				}
				// --- See if the Client wanted to terminate the connection ---
				if(tDataToClient.bExit)
				{
					comp.markAsDisconnected();
					System.out.println("Klient roz³¹cza siê...");
					break;
				}
			}
			// Close resources;  This client is gone
			//comp.Final();
			oOut.close();
			oIn.close();
			clientSocket.close();
		} catch (IOException e){
			e.printStackTrace();
		} catch (UnknownClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pobranie paczki danych ze strumienia wejœciowego.
	 * @param oIn strumieñ wejœciowy.
	 * @return paczka danych
	 * @throws IOException
	 */
	private static ComData getDataFromClient(ObjectInputStream oIn) throws IOException                                                                         
	{
		ComData tDataFromClient = null;
		while (tDataFromClient == null)
		{
			try {
				// --- Read Line Number first --
				tDataFromClient = (ComData)oIn.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("B³êdne dane od klienta...");
			}
		}
		System.out.println("Otrzymano: " + tDataFromClient.message);
		return tDataFromClient;
	}

	/**
	 * Wys³anie paczki danych do strumienia wyjœciowego.
	 * @param tDataToClient paczka danych dla klienta
	 * @param oOut strumieñ wyjœciowy
	 * @throws IOException
	 */
	private static void sendDataToClient(ComData tDataToClient, ObjectOutputStream oOut) throws IOException
	{         
		System.out.println("Wys³ano: " + tDataToClient.message);
		oOut.writeObject(tDataToClient);
		return;
	}

}
