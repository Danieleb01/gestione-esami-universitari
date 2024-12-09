// Classe che implementa l'interfaccia Student e i suoi metodi
package gestionale_Uni;

import java.io.IOException;
import java.util.ArrayList;

public class StudentImpl implements Student
{
	private String studentID;	// Matricola studente 
	
	// Metodo per ottenere la matricola dello studente
	public void setID(String studentID)
	{
		this.studentID = studentID;
	}
	
	// Metodo per autenticare lo studente. Restituisce il codice di esito
	public int getAccess()
	{
		// Socket impostato all'indirizzo e porta del server della segreteria
		SocketW socket = new SocketWImpl("127.0.0.1",1028);
		Packet sending, getting;
		int statusCode = 0;
		// Imposta il pacchetto dati (Codice = 0) inserendo la matricola
		sending = new PacketImpl();
		sending.setCode(0);
		sending.setData(studentID);
		try
		{
			// Connessione al server della segreteria e invia i dati
			socket.connect();
			socket.sendData(sending);
			// Riceve pacchetto risposta dal server, ricavando il codice di esito
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
	
	/* Metodo per ottenere le date di esame di un determinato corso, dato il codice corso. Restituisce
	 * un array contenente le informazioni richieste */
	public ArrayList getExamInfo(String courseCode)
	{
		SocketW socket = new SocketWImpl("127.0.0.1",1028);
		Packet sending, getting;
		// Array di output
		ArrayList result = new ArrayList();
		int statusCode = 0;
		// Imposta il pacchetto dati (Codice = 1) e inserendo il codice corso
		sending = new PacketImpl();
		sending.setCode(1);
		sending.setData(courseCode);
		try
		{
			// Connessione al server della segreteria e invio del pacchetto
			socket.connect();
			socket.sendData(sending);
			// Ottiene la risposta dal server sottoforma di pacchetto
			getting = (Packet) socket.getData();
			// Aggiunge l'array dei dati e il codice esito all'array di output
			result.add(getting.getData());
			statusCode = getting.getCode();
			result.add(statusCode);
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
			return result;	// Restituisce array di output
		}
	}
	
	/* Metodo per prenotare lo studente ad un esame, dato il codice corso e la data. Restituisce un array
	 * di interi, contenente il numero di prenotazione e il codice esito */
	public ArrayList<Integer> sendReservation(String courseCode, String examDate)
	{
		SocketW socket = new SocketWImpl("127.0.0.1",1028);
		Packet sending, getting;
		// Array di output
		ArrayList<Integer> result = new ArrayList<Integer>();
		// Array contenente i dati di output
		ArrayList<String> data = new ArrayList<String>();
		int statusCode = 0;
		// Imposta l'array con i dati dell'esame da prenotare e con la matricola
		data.add(courseCode);
		data.add(examDate);
		data.add(studentID);
		// Imposta pacchetto dati (Codice = 2) inserendo l'array di dati
		sending = new PacketImpl();
		sending.setCode(2);
		sending.setData(data);
		try
		{
			// Connessione al server della segreteria e invio dei dati
			socket.connect();
			socket.sendData(sending);
			// Ottiene risposta dal server sotto forma di pacchetto
			getting = (Packet) socket.getData();
			// Aggiunge all'array output il numero di prenotazione e il codice di esito
			result.add((int) getting.getData());
			statusCode = getting.getCode();
			result.add(statusCode);
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
			return result;	// Restituisce array di output
		}
	}

}
