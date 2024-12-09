/* Interfaccia che definisce un socket "client" e i metodi collegati */
package gestionale_Uni;
import java.io.IOException;
import java.util.ArrayList;

public interface SocketW 
{
	public abstract void connect() throws Exception;
	public abstract void closeConn();
	public abstract Object getData() throws Exception;
	public abstract void sendData(Packet data) throws Exception;
	public abstract void setAddr(String addr);
	public abstract void setPort(int port);
	public abstract void getInfo();

}
