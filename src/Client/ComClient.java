package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import protocol.ComData;

public class ComClient
{
	private Socket				comSocket;
	private ObjectOutputStream	oOut;
	private ObjectInputStream	oIn;
	private boolean				IsItOpen = false;
	private boolean				requesting = true;

	/**
	 * Main program to start the Client
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException
	{	
		ComClient cli = new ComClient();
		cli.openCom("localhost", 6666);
		if(cli.isItOpen()){
			ComData data = new ComData();
			cli.getServerData(data);
			if(data.comData.equals("Ready:")){
				cli.sendDataToServer(new ComData("GUI")); // GUI, CARS, LIGHTS (inne niedozwolone)
				cli.getServerData(data);
				
				// g³ówna pêtla klienta
				while(cli.requesting){
					
					cli.sendDataToServer(new ComData("zapytanie"));
					cli.getServerData(data);
					// odpowiedŸ w data.comData
					
					Thread.sleep(10000);
					
					break;
					
				}
				
				cli.sendDataToServer(new ComData("!EXIT.@"));
				cli.getServerData(data);
			}
			
		}
		cli.closeCom();
	}

	/**
	 * Open Socket
	 */
	public void openCom(String sServerName, int iPortNumber) throws UnknownHostException, IOException  
	{
		try {
			comSocket = new Socket(sServerName, iPortNumber);     
			oOut = new ObjectOutputStream(comSocket.getOutputStream());     
			oIn = new ObjectInputStream(comSocket.getInputStream());
			IsItOpen = true;
		} catch (java.net.UnknownHostException e){
			System.err.println("(openCom:) Nie rozpoznajê adresu: " + sServerName);
			IsItOpen = false;
			throw(e);
		} catch (java.io.IOException e){
			System.err.println("(openCom:) Nie mo¿na po³¹czyæ z: " + sServerName);
			IsItOpen = false;
			throw(e);
		}
	}

	/**
	 * Check if Socket is open
	 */
	public boolean isItOpen()
	{
		return IsItOpen;
	}

	/**
	 * Get data string from the Server
	 */
	public void getServerData(ComData tServData) throws IOException
	{
		tServData.comData = "";
		try {
			tServData.copy((ComData)oIn.readObject());
		} catch (ClassNotFoundException e){
			System.out.println("B³êdne dane...");
		} 
		
		System.out.println("OdpowiedŸ serwera: " + tServData.comData);
		if (tServData.comData.equals("EXIT.")){
			tServData.bExit = true;
		}
		return;
	}

	/**
	 * Send data to the Server
	 */
	public void sendDataToServer(ComData tServData) throws IOException
	{
		System.out.println("Wysy³am: " + tServData.comData);
		oOut.writeObject(tServData);
		return;
	}

	/**
	 * Close Socket
	 */
	public void closeCom() throws IOException
	{
		oOut.close();
		oIn.close();
		comSocket.close();     
		IsItOpen = false;
	}
}
