package Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import Protocol.*;

/**
 * A class extended from a Thread; Responsible to service one client
 */
class ComServerThread extends Thread
{
	private Socket clientSocket = null;
	ComData tDataFromClient;
	ComData tDataToClient;
	ObjectInputStream oIn;
	ObjectOutputStream oOut;
	
	/**
	 * Constructor
	 */
	public ComServerThread( Socket socket )
	{
		super("ComServerThread");
		this.clientSocket = socket;
	}
	
	/**
	 * Overrun from the Thread (super) class
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
	 * Get data from Client
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
		System.out.println("Otrzymano: " + tDataFromClient.comData);
		return tDataFromClient;
	}

	/**
	 * Send data to Client
	 */
	private static void sendDataToClient(ComData tDataToClient, ObjectOutputStream oOut) throws IOException
	{         
		System.out.println("Wys³ano: " + tDataToClient.comData);
		oOut.writeObject(tDataToClient);
		return;
	}

}
