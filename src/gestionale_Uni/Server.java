// Interfaccia che definisce le operazioni di un server generico
package gestionale_Uni;

public interface Server 
{
	public abstract int start();
	public abstract void stop();
	public abstract SocketW acceptConn();
}
