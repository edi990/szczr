package Server;

import Protocol.ComData;

class ComProtocol
{
	private enum ClientType {
	    GUI, CARS, LIGHTS
	}

	private static final int COM_STATUS_WAITING    = 0; 
	private static final int COM_STATUS_READY_SENT = 1;
	private static final int COM_STATUS_WAITING_FOR_TERMINALID = 2;
	private int state = COM_STATUS_WAITING;
	private ClientType whoAmI;

	/**
	 * Create a protocol object;
	 */
	public ComProtocol()
	{
		
	}
	/**
	 * Process the Input and Create the output to the Client
	 * @throws UnknownClientException 
	 */
	public ComData processInput(ComData theInput) throws UnknownClientException
	{
		ComData theOutput;
		theOutput = new ComData();
		// --- Check if the Clients want to disconnect ---
		if(theInput != null)
		{
			if ( theInput.comData.equals("!EXIT.@") )
			{
				theOutput.comData = "EXIT.";
				theOutput.bExit = true;
				state = COM_STATUS_WAITING;
				return theOutput;
			}
			if ( theInput.comData.equals("!SHUTDOWN.@") )
			{
				theOutput.comData = "BYE.";
				theOutput.bExit = true;
				ComServer.GL_listening = false;
				state = COM_STATUS_WAITING;
				return theOutput;
			}
		}

		if (state == COM_STATUS_WAITING)
		{
			theOutput.comData = "Ready:";
			state = COM_STATUS_WAITING_FOR_TERMINALID;
		}
		else if (state == COM_STATUS_WAITING_FOR_TERMINALID)
		{
			if(theInput.comData.equals("GUI"))
				whoAmI = ClientType.GUI;
			else if(theInput.comData.equals("CARS"))
				whoAmI = ClientType.CARS;
			else if(theInput.comData.equals("LIGHTS"))
				whoAmI = ClientType.CARS;
			else throw new UnknownClientException(theInput.comData);

			markAsConnected();
			
			theOutput.comData = "Ready;Server Version 1.0:";
			state = COM_STATUS_READY_SENT;
		}
		else if (state == COM_STATUS_READY_SENT)
		{
			int iRet = 0;
			
			/*
			 * Check if we should get Response data
			 */
			if (theInput.iRet == ComData.NOWAIT_FOR_RESPONSE){
				theOutput.iRet = iRet;
				theOutput.comData = "";
			} else{
				// TODO budowa odpowiedzi (nie wiem jeszcze jak bêd¹ przesy³ane dane).
				theOutput.comData = "re:"+theInput.comData;//mqTe.sResponseBuffer; 
				theOutput.iRet = iRet;
			}
		}

		return theOutput;
	}

	/**
	 * Mark as connected
	 */
	public void markAsConnected(){
		switch(whoAmI){
		case GUI:
			ComServer.getInstance().getData().isGuiConnected = true;
		break;
		case CARS:
			ComServer.getInstance().getData().isCarConnected = true;
		break;
		case LIGHTS:
			ComServer.getInstance().getData().isLightConnected = true;
		break;
		}
	}

	/**
	 * Mark as disconnected
	 */
	public void markAsDisconnected(){
		switch(whoAmI){
		case GUI:
			ComServer.getInstance().getData().isGuiConnected = false;
		break;
		case CARS:
			ComServer.getInstance().getData().isCarConnected = false;
		break;
		case LIGHTS:
			ComServer.getInstance().getData().isLightConnected = false;
		break;
		}
	}

}
