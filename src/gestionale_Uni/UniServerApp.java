// Applicazione principale del server universitario
package gestionale_Uni;


public class UniServerApp 
{

	public static void main(String[] args) 
	{
		// Oggetto di classe UniServer
		UniServer server = new UniServerImpl();
		if(server.start() == 0)	// Controlla se l'avvio del server non dà errori
		{
			while(true)
			{
				SocketW socket = server.acceptConn();	// Accetta connessioni
				// Se il socket restituito non è null...
				if(socket != null)
				{
					// Avvia un thread, passando il socket appena ottenuto e l'oggetto originale UniServer
					new UniServerThread(socket,server);
				}
			}
		}
	}
}

