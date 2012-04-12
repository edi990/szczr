package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import protocol.*;


/**
 * Klasa w�tku obs�uguj�cego zapytania od jednego klienta.
 */
class ComServerThread extends Thread
{
	/**
	 * uchwyt po��czenia
	 */
	private Socket clientSocket = null;

	/**
	 * paczka danych od klienta
	 */
	ComData tDataFromClient;

	/**
	 * paczka do wys�ania
	 */
	ComData tDataToClient;

	/**
	 * strumie� wej�ciowy
	 */
	ObjectInputStream oIn;

	/**
	 * strumie� wyj�ciowy
	 */
	ObjectOutputStream oOut;
	
	/**
	 * Inicjalizacja w�tku.
	 * @param socket uchwyt po��czenia
	 */
	public ComServerThread(Socket socket)
	{
		super("ComServerThread");
		this.clientSocket = socket;
	}
	
	/**
	 * Nas�ychiwanie zapyta�, przetwarzanie i odpowied� do klienta.<br/>
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
					System.out.println("Klient roz��czony...");
					break;
				} catch (SocketException e){
					comp.markAsDisconnected();
					System.out.println("Klient roz��czony...");
					break;
				}
				// --- See if the Client wanted to terminate the connection ---
				if(tDataToClient.bExit)
				{
					comp.markAsDisconnected();
					System.out.println("Klient roz��cza si�...");
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
	 * Pobranie paczki danych ze strumienia wej�ciowego.
	 * @param oIn strumie� wej�ciowy.
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
				System.out.println("B��dne dane od klienta...");
			}
		}
		System.out.println("Otrzymano: " + tDataFromClient.message);
		return tDataFromClient;
	}

	/**
	 * Wys�anie paczki danych do strumienia wyj�ciowego.
	 * @param tDataToClient paczka danych dla klienta
	 * @param oOut strumie� wyj�ciowy
	 * @throws IOException
	 */
	private static void sendDataToClient(ComData tDataToClient, ObjectOutputStream oOut) throws IOException
	{         
		System.out.println("Wys�ano: " + tDataToClient.message);
		oOut.writeObject(tDataToClient);
		return;
	}

}
