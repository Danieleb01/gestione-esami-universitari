/* Classe astratta Packet che definisce un generico pacchetto dati usato per inviare e ricevere informazioni
 * da una socket */
package gestionale_Uni;

import java.io.Serializable;

public abstract class Packet implements Serializable {
	public abstract int getCode();
	public abstract void setCode(int code);
	public abstract Object getData();
	public abstract void setData(Object data);
}
