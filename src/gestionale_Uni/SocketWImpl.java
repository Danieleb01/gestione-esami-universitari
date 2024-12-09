/* Classe che implementa l'interfaccia SocketW */
package gestionale_Uni;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SocketWImpl implements SocketW 
{
	private Socket socket;	// Campo per contenere il socket nativo di Java
	private String addr;	// Indirizzo della macchina con cui deve comunicare il socket
	private int port;	// Porta del processo con cui deve comunicare il socket
	private ObjectInputStream in;	// Flusso di oggetti in input 
	private ObjectOutputStream out;	// Flusso di oggetti in output
	
	// Costruttore che prende in input l'indirizzo e la porta
	SocketWImpl(String addr, int port)
	{
		this.addr = addr;
		this.port = port;
	}
	
	// Costruttore che prende in input un socket nativo
	SocketWImpl(Socket socket)
	{
		this.socket = socket;
		try
		{
			setIO();	// Imposta i flussi di input e output
		}
		catch(IOException e)	// Gestione eccezioni
		{
			System.out.println("Socket passato al costruttore; errore di apertura comunicazioni IO: " + e.getMessage() + "\n");
		}
		catch(Exception e)
		{
			System.out.println("Socket passato al costruttore; errore di apertura comunicazioni IO generico: " + e.getMessage() + "\n");
		}
	}
	
	// Metodo per aprire la connessione del socket
	public void connect() throws Exception
	{
		// Crea un oggetto di tipo socket passando l'indirizzo e la porta e memorizzandolo nel campo socket
		socket = new Socket(addr,port);
		setIO();
	}
	
	// Metodo per chiudere la connessione
	public void closeConn()
	{
		try
		{
			// Chiusura della socket e dei flussi di IO
			socket.close();
			out.close();
			in.close();
		}
		catch(IOException e)	// Gestione eccezioni
		{
			System.out.println("Si e' verificato un errore nella chiusura della socket: " + e.getMessage() + "\n");
		}
		catch(Exception e)
		{
			System.out.println("Errore generico nella chiusura socket: " + e.getMessage() + "\n");
		}

	}
	
	// Metodo per ricevere dati dalla socket
	public Object getData() throws Exception
	{
		Object data = in.readObject();
		return data;
	}
	
	// Metodo per inviare dati tramite la socket
	public void sendData(Packet data) throws Exception
	{
		out.writeObject(data);
		out.flush();
	}
	
	// Metodo per impostare i flussi di IO
	private void setIO() throws IOException
	{
		// Crea un flusso di output per scrivere oggetti
		out = new ObjectOutputStream(socket.getOutputStream());
		// Crea un flusso di input per ricevere oggetti
	    in = new ObjectInputStream(socket.getInputStream());

	}
	
	// Metodo per impostare l'indirizzo
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	
	// Metodo per impostare la porta
	public void setPort(int port)
	{
		this.port = port;
	}
	
	// Metodo per stampare info sulla socket
	public void getInfo()
	{
		System.out.println("Indirizzo impostato per la socket: " + this.addr + "\n");
		System.out.println("Porta impostata per la socket: " + this.port + "\n");
	}
	
}
