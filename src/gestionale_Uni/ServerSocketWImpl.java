/* Classe che implementa il socket di tipo "server" */
package gestionale_Uni;
import java.io.IOException;
import java.net.*;

public class ServerSocketWImpl implements ServerSocketW 
{
	private int port;	// Porta sulla quale è in ascolto
	private ServerSocket socket;	// Campo per memorizzare un Server Socket nativo
	
	
	ServerSocketWImpl(int port)
	{
		this.port = port;
	}
	
	// Metodo per impostare la porta di ascolto
	public void setPort(int port)
	{
		this.port = port;
	}
	
	// Metodo per avviare il server
	public void start() throws Exception
	{
		// Crea un oggetto di tipo ServerSocket passando la porta di ascolto e lo memorizza nella var. socket
		this.socket = new ServerSocket(port);
		System.out.println("Server in ascolto...\n");
	}
	
	// Metodo per interrompere il server
	public void stop() throws Exception
	{
		this.socket.close();
		System.out.println("Server chiuso\n");
	}
	
	// Metodo per accettare connessioni
	public SocketW acceptConn()
	{
		// Var di tipo SocketW
		SocketW commSocket = null;
		try
		{
			/* Accetta la connessione tramite il metodo accept di socket, che restituisce un oggetto di tipo
			 * Socket. Tale viene passato al costruttore della classe SocketW, creando un oggetto di tale tipo
			 * che viene poi memorizzato in commSocket */
			commSocket = new SocketWImpl(this.socket.accept());
		}
		catch(IOException e) // Gestione errori
		{
			System.out.println("Errore nell'accettazione della connessione: " + e.getMessage() + "\n");
		}
		catch(Exception e)
		{
			System.out.println("Errore generico nell'accettazione della connessione: " + e.getMessage() + "\n");
		}
		finally
		{
			return commSocket; // Restituisci il socket client
		}
	}
	
	// Metodo per stampare le informazioni sul socket
	public void getInfo()
	{
		System.out.println("Socket con porta di ascolto impostata su: " + this.port + "\n");
	}
}
