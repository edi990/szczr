package server;

import protocol.ComData;

/**
 * Protokó³ po³¹czenia.<br/>
 * Obiekt tej klasy zajmuje siê g³ównie interpretacj¹, przetwarzaniem zapytania oraz konstruowaniem odpowiedzi do klienta.
 */
class ComProtocol
{
	/**
	 * typy klientów
	 */
	private enum ClientType {
	    GUI, CARS, LIGHTS
	}

	/**
	 * status po³¹czenia- oczekuje
	 */
	private static final int COM_STATUS_WAITING    = 0;

	/**
	 * status po³¹czenia- gotowy
	 */
	private static final int COM_STATUS_READY_SENT = 1;

	/**
	 * status po³¹czenia- czeka na identyfikacjê
	 */
	private static final int COM_STATUS_WAITING_FOR_TERMINALID = 2;

	/**
	 * obecny status po³¹czenia-
	 */
	private int state = COM_STATUS_WAITING;

	/**
	 * identyfikator klienta
	 */
	private ClientType whoAmI;

	/**
	 * Metoda przetwarzaj¹ca zapytanie oraz tworz¹ca odpowiedŸ do klienta.<br/>
	 * Na podstawie otrzymanych wiadomoœci zmienia stan po³¹czenia, a po uzyskaniu pe³nej gotowoœci<br/>
	 * na ka¿de zapytanie tworzy dla odpowiedniego klienta wiadomoœæ z danymi w okreœlony sposób:<br/>
	 * <table>
	 * <tr><th>id klienta</th>	<th>wiadomosc</th>		<th>akcja</th>						<th>odpowiedŸ</th></tr>
	 * <tr><td>ka¿dy</td>		<td>STATE</td>			<td>-</td>							<td>STARTED/STOPPED</td></tr>
	 * <tr><td>ka¿dy</td>		<td>SETTINGS</td>		<td>-</td>							<td>ustawienia symulacji</td></tr>
	 * <tr><td>ka¿dy</td>		<td>ka¿da</td>			<td>wykryto stop</td>				<td>STOPPED</td></tr>
	 * <tr><td>CARS</td>		<td>CARS</td>			<td>zapisuje samochody</td>			<td>SAVED</td></tr>
	 * <tr><td>CARS</td>		<td>CROSSROADSCARS</td>	<td>zapisuje obi¹¿enia skrzy¿owañ</td><td>SAVED</td></tr>
	 * <tr><td>CARS</td>		<td>LIGHTS</td>			<td>-</td>							<td>wysy³a stan sygnalizatorów</td></tr>
	 * <tr><td>CARS</td>		<td>sLIGHTS</td>		<td>zapisuje stan sygnalizatorów (awaryjnie)</td><td>SAVED</td></tr>
	 * <tr><td>LIGHTS</td>		<td>CROSSROADSCARS</td>	<td>-</td>							<td>obi¹¿enia skrzy¿owañ</td></tr>
	 * <tr><td>LIGHTS</td>		<td>LIGHTS</td>			<td>zapisuje stan sygnalizatorów</td><td>SAVED</td></tr>
	 * <tr><td>GUI</td>			<td>CARS</td>			<td>dodaje nowe samochody</td>		<td>aktualne po³o¿enia samochodów</td></tr>
	 * <tr><td>GUI</td>			<td>LIGHTS</td>			<td>-</td>							<td>stan sygnalizatorów</td></tr>
	 * <tr><td>GUI</td>			<td>STOP</td>			<td>zatrzymuje symulacjê</td>		<td>STOPPED</td></tr>
	 * <tr><td>GUI</td>			<td>START</td>			<td>start, zapisuje ustawienia</td>	<td>SETTINGS SAVED</td></tr>
	 * </table>
	 * @param theInput otrzymane zapytanie
	 * @return paczka z utworzon¹ odpowiedzi¹
	 * @throws UnknownClientException 
	 */
	public ComData processInput(ComData theInput) throws UnknownClientException
	{
		ComData theOutput;
		theOutput = new ComData();
		// --- Check if the Clients want to disconnect ---
		if(theInput != null)
		{
			if ( theInput.message.equals("!EXIT.@") )
			{
				theOutput.message = "EXIT.";
				theOutput.bExit = true;
				state = COM_STATUS_WAITING;
				return theOutput;
			}
			if ( theInput.message.equals("!SHUTDOWN.@") )
			{
				theOutput.message = "BYE.";
				theOutput.bExit = true;
				ComServer.GL_listening = false;
				state = COM_STATUS_WAITING;
				return theOutput;
			}
		}

		if (state == COM_STATUS_WAITING)
		{
			theOutput.message = "Ready:";
			state = COM_STATUS_WAITING_FOR_TERMINALID;
		}
		else if (state == COM_STATUS_WAITING_FOR_TERMINALID)
		{
			if(theInput.message.equals("GUI"))
				whoAmI = ClientType.GUI;
			else if(theInput.message.equals("CARS"))
				whoAmI = ClientType.CARS;
			else if(theInput.message.equals("LIGHTS"))
				whoAmI = ClientType.LIGHTS;
			else throw new UnknownClientException(theInput.message);

			markAsConnected();
			
			theOutput.message = "Ready;Server Version 1.0:";
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
				theOutput.message = "";
			} else{

				/*
				 * prosi o stan symulacji (start, stop)- wysylam
				 */
				if(theInput.message.equals("STATE")){
					theOutput.message = (ComServer.getInstance().getData().simulationRunning) ? "STARTED" : "STOPPED";
				}
				else
				switch(whoAmI){
				case CARS:
					/*
					 * symulacja zatrzymana
					 */
					if(ComServer.getInstance().getData().simulationRunning == false){
						theOutput.message = "STOPPED";
						break;
					}
					
					/*
					 * przysyla samochody- zapisuje, wysylam nowe
					 */
					if(theInput.message.equals("CARS")){
						ComServer.getInstance().getData().setCars(theInput.comData);
						theOutput.message = "SAVED";
						theOutput.comData = ComServer.getInstance().getData().getNewCars();
					}
					/*
					 * przysyla obciazenie skrzyzowan- zapisuje
					 */
					else if(theInput.message.equals("CROSSROADSCARS")){
						ComServer.getInstance().getData().setCrossroadsCars(theInput.comData);
						theOutput.message = "SAVED";
					}
					/*
					 * prosi o swiatla- wysylam swiatla (null jesli cos nie styka)
					 */
					else if(theInput.message.equals("LIGHTS")){
						theOutput.message = "LIGHTS";
						theOutput.comData = (ComServer.getInstance().getData().isLightConnected) ? ComServer.getInstance().getData().getLights() : null;
					}
					/*
					 * prosi o ustawienia- wysylam ustawienia
					 */
					else if(theInput.message.equals("SETTINGS")){
						theOutput.message = "SETTINGS";
						theOutput.comData = ComServer.getInstance().getData().getSettings();
					}
					/*
					 * przysyla swiatla- w razie awarii
					 */
					else if(theInput.message.equals("sLIGHTS")){
						ComServer.getInstance().getData().setLights(theInput.comData);
						theOutput.message = "SAVED";
					}
					
				break;
				case LIGHTS:
					/*
					 * symulacja zatrzymana
					 */
					if(ComServer.getInstance().getData().simulationRunning == false){
						theOutput.message = "STOPPED";
						break;
					}
					
					/*
					 * prosi o obciazenie- wysylam
					 */
					if(theInput.message.equals("CROSSROADSCARS")){
						theOutput.message = "CROSSROADSCARS";
						theOutput.comData = ComServer.getInstance().getData().getCrossroadsCars();
					}
					/*
					 * przysyla swiatla- zapisuje
					 */
					else if(theInput.message.equals("LIGHTS")){
						ComServer.getInstance().getData().setLights(theInput.comData);
						theOutput.message = "SAVED";
					}
					/*
					 * prosi o ustawienia- wysylam ustawienia
					 */
					else if(theInput.message.equals("SETTINGS")){
						theOutput.message = "SETTINGS";
						theOutput.comData = ComServer.getInstance().getData().getSettings();
					}
				break;
				case GUI:
					/*
					 * prosi o samochody, przysyla nowe- wysylam samochody
					 */
					if(theInput.message.equals("CARS")){
						ComServer.getInstance().getData().addNewCars(theInput.comData);
						theOutput.message = "CARS";
						theOutput.comData = (ComServer.getInstance().getData().isCarConnected) ? ComServer.getInstance().getData().getCars() : null;
					}
					/*
					 * prosi o swiatla- wysylam swiatla
					 */
					else if(theInput.message.equals("LIGHTS")){
						theOutput.message = "LIGHTS";
						theOutput.comData = ComServer.getInstance().getData().getLights();
					}
					/*
					 * zatrzymuje symulacje- stop
					 */
					else if(theInput.message.equals("STOP")){
						ComServer.getInstance().getData().simulationRunning = false;
						theOutput.message = "STOPPED";
					}
					/*
					 * startuje symulacje- dostaje ustawienia, wysylam samochody
					 */
					else if(theInput.message.equals("START")){
						ComServer.getInstance().getData().simulationRunning = true;
						ComServer.getInstance().getData().setSettings(theInput.comData);
						theOutput.message = "SETTINGS SAVED";
						//theOutput.comData = (ComServer.getInstance().getData().isCarConnected) ? ComServer.getInstance().getData().getCars() : null;
					}
					/*
					 * prosi o ustawienia- wysylam ustawienia
					 */
					else if(theInput.message.equals("SETTINGS")){
						theOutput.message = "SETTINGS";
						theOutput.comData = ComServer.getInstance().getData().getSettings();
					}
				break;
				}

				theOutput.iRet = iRet;
			}
		}

		return theOutput;
	}

	/**
	 * Oznacza klienta jako po³¹czonego.
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
	 * Oznacza klienta jako roz³¹czonego.
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
