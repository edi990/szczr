package protocol;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Przechowuje dane wymieniane miêdzy serwerem a klientem (paczka danych).
 *
 */
@SuppressWarnings("serial")
public class ComData implements Serializable
{
	/**
	 * wiadomoœæ od nadawcy- zwykle okreœla zawartoœæ danych
	 */
	public String 				message;
	
	/**
	 * dane
	 */
	public LinkedList<Integer>	comData;

	/**
	 * czy wiadomoœæ koñcowa
	 */
	public boolean				bExit;

	/**
	 * czy oczekujê na odpowiedŸ
	 */
	public int					iRet;

	/**
	 * sta³a okreœlaj¹ca wartoœæ iRet- czekam na odpowiedŸ
	 */
	public static final int WAIT_FOR_RESPONSE	= 0;

	/**
	 * sta³a okreœlaj¹ca wartoœæ iRet- nie czekam na odpowiedŸ
	 */
	public static final int NOWAIT_FOR_RESPONSE	= 1;

	/**
	 * Inicjalizacja pustej paczki.
	 */
	public ComData()
	{
		message		= "";
		comData     = null;
		bExit       = false;
		iRet        = 0;
	}

	/**
	 * Inicjalizacja paczki z wiadomoœci¹ tekstow¹.
	 * @param mes wiadomoœæ
	 */
	public ComData(String mes)
	{
		message		= mes;
		comData     = null;
		bExit       = false;
		iRet        = 0;
	}

	/**
	 * Konstruktor kopiuj¹cy.
	 * @param tSrc Ÿród³o
	 */
	public ComData(ComData tSrc)
	{
		this.message	= tSrc.message;
		this.comData	= tSrc.comData;
		this.bExit		= tSrc.bExit;
		this.iRet		= tSrc.iRet;
	}

	/**
	 * Kopiowanie innej paczki do istniej¹cej.
	 * @param tSrc Ÿród³o
	 */
	public void copy(ComData tSrc)
	{
		this.message	= tSrc.message;
		this.comData	= tSrc.comData;
		this.bExit		= tSrc.bExit;
		this.iRet		= tSrc.iRet;
		return;
	}

}
