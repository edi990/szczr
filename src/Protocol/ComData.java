package protocol;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Przechowuje dane wymieniane mi�dzy serwerem a klientem (paczka danych).
 *
 */
@SuppressWarnings("serial")
public class ComData implements Serializable
{
	/**
	 * wiadomo�� od nadawcy- zwykle okre�la zawarto�� danych
	 */
	public String 				message;
	
	/**
	 * dane
	 */
	public LinkedList<Integer>	comData;

	/**
	 * czy wiadomo�� ko�cowa
	 */
	public boolean				bExit;

	/**
	 * czy oczekuj� na odpowied�
	 */
	public int					iRet;

	/**
	 * sta�a okre�laj�ca warto�� iRet- czekam na odpowied�
	 */
	public static final int WAIT_FOR_RESPONSE	= 0;

	/**
	 * sta�a okre�laj�ca warto�� iRet- nie czekam na odpowied�
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
	 * Inicjalizacja paczki z wiadomo�ci� tekstow�.
	 * @param mes wiadomo��
	 */
	public ComData(String mes)
	{
		message		= mes;
		comData     = null;
		bExit       = false;
		iRet        = 0;
	}

	/**
	 * Konstruktor kopiuj�cy.
	 * @param tSrc �r�d�o
	 */
	public ComData(ComData tSrc)
	{
		this.message	= tSrc.message;
		this.comData	= tSrc.comData;
		this.bExit		= tSrc.bExit;
		this.iRet		= tSrc.iRet;
	}

	/**
	 * Kopiowanie innej paczki do istniej�cej.
	 * @param tSrc �r�d�o
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
