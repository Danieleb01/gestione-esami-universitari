// Classe che definisce un thread con tutte le operazioni effettuate dal server della segreteria
package gestionale_Uni;

import java.util.ArrayList;

public class SAOThread implements Runnable 
{
	// Variabili necessarie per l'esecuzione
	private StudentAdminOffice server;
	private SocketW commSocket;
	private int reqCode, statusCode, resultCode;
	private Packet p, s;
	private ArrayList examInfo;
	private ArrayList<String> data;
	private ArrayList<Integer> result;
	private String courseID, courseCode, examDate, studentID;
	
	// Costruttore
	SAOThread(SocketW socket, StudentAdminOffice originalObj)
	{
		// Imposta commSocket, server e avvia il thread
		this.commSocket = socket;
		this.server = originalObj;
		new Thread(this).start();
	}
	
	@Override
	public void run() 
	{
		try
		{
			// Ricava il pacchetto dalla comunicazione con il client
			p = (Packet) commSocket.getData();
			// Estrae codice richiesta
			reqCode = p.getCode();
			System.out.println("Codice richiesta: " + reqCode + "\n");
			// Effettua le operazioni in base al codice richiesta contenuto nel pacchetto
			switch(reqCode) 
			{
			case 0: // Codice 0 -> Autenticazione studente
				// Estrae matricola dal pacchetto
				studentID = (String) p.getData();
				System.out.println("Richiesta di accesso ottenuta per " + studentID + ". Inoltro in corso\n");
				// Inoltra la richiesta al server universitario e ottiene l'esito
				statusCode = server.forwardAccessRequest(studentID);
				// Imposta il pacchetto risposta e lo invia
				s= new PacketImpl();
				s.setCode(statusCode);
				commSocket.sendData(s);
				break;
			case 1: // Codice 1 -> Richiesta visione esame
				// Ricava dal pacchetto il codice dell'esame
				courseID = (String) p.getData();
				System.out.println("Richiesta di visione esame ottenuta. Inoltro in corso\n");
				// Ottiene l'array contenente le date dell'esame scelto
				examInfo = server.getExamInfo(courseID);
				// Estrae il codice di esito dall'array
				statusCode = (int) examInfo.get(examInfo.size() - 1);
				examInfo.remove(examInfo.size() - 1);
				// Imposta il pacchetto di risposta e lo invia al client
				s = new PacketImpl();
				s.setCode(statusCode);
				s.setData((ArrayList<ArrayList<String>>) examInfo);
				commSocket.sendData(s);
				break;
			case 2: // Codice 2 -> Richiesta prenotazione esame
				// Estrae dall'array i dati per la prenotazione
				data = (ArrayList<String>) p.getData();
				courseCode = data.get(0);
				examDate = data.get(1);
				studentID = data.get(2);
				System.out.println("Richiesta di prenotazione ottenuta. Inoltro in corso\n");
				// Invoca il metodo apposito, ottenendo il codice di esito e il n. di prenotazione
				result = server.forwardReservationInsert(courseCode, examDate, studentID);
				// Ricava il codice di esito dall'array ottenuto
				statusCode = result.get(result.size() - 1);
				// Impostazione pacchetto di risposta e invio al client
				s = new PacketImpl();
				s.setCode(statusCode);
				s.setData(result.get(0));
				commSocket.sendData(s);
				break;
				default:
					System.out.println("Codice richiesta non valido\n");
					break;
			}
		}
		catch(Exception e)	// Gestione eccezioni
		{
			System.out.println("Errore interno di comunicazione... " + e.getMessage() + "\n");
		}
		finally
		{
			// Chiusura connessione
			commSocket.closeConn();
		}
	}
}

