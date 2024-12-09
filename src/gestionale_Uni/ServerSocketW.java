/* Interfaccia che definisce un socket "server" con i propri metodi */
package gestionale_Uni;

public interface ServerSocketW 
{
	public abstract void start() throws Exception;
	public abstract void stop() throws Exception;
	public abstract SocketW acceptConn();
	public abstract void setPort(int port);
	public abstract void getInfo();

}
