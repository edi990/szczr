package protocol;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ComData implements Serializable
{
	public String	comData;
	public boolean	bExit;
	public int		iRet;

	/**
	 * Constants values can be passed in iRet to the Server
	 */
	public static final int WAIT_FOR_RESPONSE	= 0;
	public static final int NOWAIT_FOR_RESPONSE	= 1;

	/**
	 * Initialize the data structure
	 */
	public ComData()
	{
		comData     = "";
		bExit       = false;
		iRet        = 0;
	}
	public ComData(String data)
	{
		comData     = data;
		bExit       = false;
		iRet        = 0;
	}

	/**
	 * Copy over it contents
	 */
	public void copy(ComData tSrc)
	{
		this.comData	= tSrc.comData;
		this.bExit		= tSrc.bExit;
		this.iRet		= tSrc.iRet;
		return;
	}

}
