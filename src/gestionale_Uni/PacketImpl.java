/*  Classe che implementa la classe astratta Packet e i suoi metodi */
package gestionale_Uni;

public class PacketImpl extends Packet 
{
	private int code;	// Codice di stato
	private Object data;	// Dati
	
	// Costruttore
	PacketImpl(int code, String data)
	{
		setCode(code);
		setData(data);
	}
	
	// Costruttore senza parametri
	PacketImpl()
	{
		setCode(0);
		setData(null);
	}
	
	// Metodo che restituisce il codice di stato
	public int getCode()
	{
		return code;
	}
	
	// Metodo che imposta il codice di stato
	public void setCode(int code)
	{
		this.code = code;
	}
	
	// Metodo che restituisce i dati inclusi nel pacchetto
	public Object getData()
	{
		return data;
	}
	
	// Metodo che imposta i dati nel pacchetto
	public void setData(Object data)
	{
		this.data = data;
	}
	
	
	
	
}
