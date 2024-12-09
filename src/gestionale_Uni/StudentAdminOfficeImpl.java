// Classe che implementa StudentAdminOffice e i suoi metodi
package gestionale_Uni;
import java.io.IOException;
import java.util.ArrayList;

public class StudentAdminOfficeImpl implements StudentAdminOffice 
{
	private ServerSocketW serverSocket;
	
	// Costruttore che prende in input un carattere che definisce la modalità della classe
	StudentAdminOfficeImpl(char mode)
	{
		if(mode == 'S')	// 'S' corrisponde alla modalità server
		{
			this.serverSocket = new ServerSocketWImpl(1028); 
		}
		else
		{
			this.serverSocket = null; // 'C' corrisponde alla modalità client
		}
	}
	
	// Metodo per far partire il server
	public int start()
	{
		int statusCode = 0;
		if(serverSocket == null)	// Se il serverSocket è null (modalità client)
		{
			// Visualizza messaggio e restituisce il codice esito
			System.out.println("L'oggetto è impostato in modalita' client: non può effettuare operazioni server");
			return -3;
		}
		else	// Altrimenti fa partire il server e gestisce eccezionu
		{
			try
			{
				serverSocket.start();
			}
			catch(IOException exc)
			{
				System.out.println("Errore di IO o del socket server : " + exc.getMessage()+ "\n");
				statusCode = -1;
			}
			catch(Exception exc)
			{
				System.out.println("Errore generico: " + exc.getMessage() + "\n");
				statusCode = -2;
			}
			finally
			{
				return statusCode;
			}
		}
	}
	
	// Metodo per interrompere il server
	public void stop()
	{
		if(serverSocket == null)
		{
			System.out.println("L'oggetto è impostato in modalita' client: non può effettuare operazioni server");
		}
		else
		{
			try
			{
				serverSocket.stop();
			}
			catch(Exception e)
			{
				System.out.println("Errore chiusura server: " + e.getMessage() + "\n");
			}

		}
	}
	
	// Metodo per accettare la connessione. Restituisce un oggetto di tipo SocketW
	public SocketW acceptConn()
	{
		// Controlla se l'oggetto è in modalità client
		if(serverSocket == null)
		{
			System.out.println("L'oggetto è impostato in modalita' client: non può effettuare operazioni server");
			return null;
		}
		else
		{
			return serverSocket.acceptConn();
		}
	}
	
	/* Metodo per inoltrare al server universitario la richiesta di accesso, data la matricola dello studente.
	 * Restituisce l'esito dell'operazione */
	public int forwardAccessRequest(String studentID)
	{
		Packet sending, getting;	// Pacchetti dati in entrata e in uscita
		int statusCode = 0;
		// Socket impostato con l'indirizzo e la porta del server universitario
		SocketW socket = new SocketWImpl("127.0.0.1",1027);
		// Imposta il pacchetto in uscita con campo codice uguale a 0 e inserendo la matricola
		sending = new PacketImpl();
		sending.setCode(0);
		sending.setData(studentID);
		try
		{
			// Connessione al server e invio del pacchetto
			socket.connect();
			socket.sendData(sending);
			// Ottiene una risposta dal server (sottoforma di pacchetto) e lo memorizza in getting
			getting = (Packet) socket.getData();
			statusCode = getting.getCode();	// Ricava dal pacchetto l'esito dell'operazione del server
		}
		catch(IOException e)	// Gestione eccezioni
		{
			System.out.println("Si e' verificato un errore riguardante la connessione: " + e.getMessage() + "\n");
			statusCode = -1;
		}
		catch(NullPointerException e)
		{
			System.out.println("Errore di comunicazione e invio dati: " + e.getMessage() + "\n");
			statusCode = -2;
		}
		catch(Exception e)
		{
			System.out.println("Errore generico nella connessione: " + e.getMessage() + "\n");
			statusCode = -3;
		}
		finally
		{
			return statusCode;	// Restituisce esito
		}
	}
	
	/* Metodo per inoltrare la richiesta di visione esami, dato il codice esame. Restituisce un array
	 * contenente le date dell'esame richiesto*/
	public ArrayList getExamInfo(String courseCode)
	{
		// Pacchetti dati in entrata e uscita
		Packet sending, getting;
		// Array che conterrà l'output
		ArrayList result = new ArrayList<ArrayList>();
		int statusCode = 0;
		SocketW socket = new SocketWImpl("127.0.0.1",1027);
		// Imposta pacchetto da mandare (codice = 1) e inserisce codice esame
		sending = new PacketImpl();
		sending.setCode(1);
		sending.setData(courseCode);
		try
		{
			// Connessione e invio del pacchetto dati
			socket.connect();
			socket.sendData(sending);
			// Risposta del server
			getting = (Packet) socket.getData();
			if(getting.getCode() == 0)	// Se l'operazione effettuata dal server è andata a buon fine...
			{
				// Estrae dal pacchetto dati l'array con le informazioni e lo inserisce in quello di output
				result.add(getting.getData());
			}
			else
			{
				// Altrimenti estrae il codice e lo assegna a statusCode
				statusCode = getting.getCode();
			}
		}
		catch(IOException e)	// Gestione eccezioni
		{
			System.out.println("Si e' verificato un errore riguardante la connessione: " + e.getMessage() + "\n");
			statusCode = -1;
		}
		catch(NullPointerException e)
		{
			System.out.println("Errore di comunicazione e invio dati: " + e.getMessage() + "\n");
			statusCode = -2;
		}
		catch(Exception e)
		{
			System.out.println("Errore generico nella connessione: " + e.getMessage() + "\n");
			statusCode = -3;
		}
		finally
		{
			// Aggiunge il codice di esito nell'array di output e lo restituisce
			result.add(statusCode);
			return result;
		}
		
	}
	
	/* Metodo per inserire un esame, dato il codice corso, la data, la modalità e il prof. Restituisce
	 * il codice di esito dell'operazione */
	public int insertExam(String courseCode, String examDate, String examForm, String prof)
	{
		Packet sending, getting;
		int statusCode = 0;
		// Array che conterrà i dati dell'esame
		ArrayList data;
		SocketW socket = new SocketWImpl("127.0.0.1",1027);
		// Inserimento dei dati all'interno dell'array
		data= new ArrayList<String>();
		data.add(courseCode);
		data.add(examDate);
		data.add(examForm);
		data.add(prof);
		// Imposta pacchetto dati in uscita con codice = 2 e inserendo l'array
		sending = new PacketImpl();
		sending.setCode(2);
		sending.setData(data);
		try
		{
			// Connessione, invio dati e ottenimento codice esito
			socket.connect();
			socket.sendData(sending);
			getting = (Packet) socket.getData();
			statusCode = getting.getCode();
		}
		catch(IOException e)	// Gestione eccezioni
		{
			System.out.println("Si e' verificato un errore riguardante la connessione: " + e.getMessage() + "\n");
			statusCode = -1;
		}
		catch(NullPointerException e)
		{
			System.out.println("Errore di comunicazione e invio dati: " + e.getMessage() + "\n");
			statusCode = -2;
		}
		catch(Exception e)
		{
			System.out.println("Errore generico nella connessione: " + e.getMessage() + "\n");
			statusCode = -3;
		}
		finally
		{
			return statusCode;
		}
	}

	/* Metodo per inoltrare la richiesta di prenotazione ad un esame, dati il codice corso, la data e
	 * la matricola dello studente. Restituisce il codice di esito e il n. di prenotazione */
	public ArrayList<Integer> forwardReservationInsert(String courseCod, String courseDate, String studentID)
	{
		Packet sending, getting;
		int statusCode = 0, reservationNumb;
		// Array che conterrà i dati per la prenotazione
		ArrayList data;
		// Array di output
		ArrayList <Integer> result = new ArrayList<Integer>();
		SocketW socket = new SocketWImpl("127.0.0.1",1027);
		// Inserimento dei dati all'interno dell'array
		data= new ArrayList<String>();
		data.add(courseCod);
		data.add(courseDate);
		data.add(studentID);
		// Imposta pacchetto dati (codice = 3) e inserisce i dati
		sending = new PacketImpl();
		sending.setCode(3);
		sending.setData(data);
		try
		{
			// Connessione e invio del pacchetto dati
			socket.connect();
			socket.sendData(sending);
			// Riceve risposta dal server ed estrae il codice esito
			getting = (Packet) socket.getData();
			statusCode = getting.getCode();
			if(statusCode == 0)	// Se tutto è andato a buon fine...
			{
				// Estrae il n. di prenotazione e lo inserisce nell'array di output
				reservationNumb = (int) getting.getData(); 
				result.add(reservationNumb);
			}
		}
		catch(IOException e)	// Gestione eccezioni
		{
			System.out.println("Si e' verificato un errore riguardante la connessione: " + e.getMessage() + "\n");
			statusCode = -1;
		}
		catch(NullPointerException e)
		{
			System.out.println("Errore di comunicazione e invio dati: " + e.getMessage() + "\n");
			statusCode = -2;
		}
		catch(Exception e)
		{
			System.out.println("Errore generico nella connessione: " + e.getMessage() + "\n");
			statusCode = -3;
		}
		finally
		{
			// Aggiunge il codice di esito e lo restituisce
			result.add(statusCode);
			return result;
		}
	}

}
