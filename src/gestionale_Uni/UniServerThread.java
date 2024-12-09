// Classe che definisce un thread con tutte le operazioni effettuate dal server universitario
package gestionale_Uni;

import java.util.ArrayList;

public class UniServerThread implements Runnable
{
	// Variabili necessarie per l'esecuzione
	private UniServer server;
	private SocketW socket;
	private int reqCode, statusCode, resultCode, reservationNumb;
	private String studentID, courseID,examDate, examForm, prof;
	private ArrayList examInfo;
	private ArrayList<String> data;
	private ArrayList<Integer> result;
	private Packet p, s;

	/* Costruttore che ha come parametri il socket restituito dalla SocketServer e l'oggetto originale
	 * 'server' di UniServer */
	UniServerThread(SocketW socket, UniServer originalObj)
	{
		// Imposta i campi socket e server e fa partire il thread
		this.socket = socket;
		this.server = originalObj;
		new Thread(this).start();
	}
	
	// Metodo che contiene le operazioni che il server deve effettuare
	@Override
	public void run() 
	{
		try
		{
			// Ottiene un pacchetto dati dalla comunicazione e ricava il codice
			p= (Packet) socket.getData();
			reqCode = p.getCode();
			System.out.println("Codice richiesta: " + reqCode + "\n");
			// Effettua le operazioni in base al codice richiesta contenuto nel pacchetto
			switch(reqCode)
			{
			case 0:		// Codice 0 -> Autenticazione studente
				// Estrae dal pacchetto la matricola
				studentID = (String) p.getData();
				System.out.println("Pronto ad autenticare lo studente di matricola " + studentID + "\n");
				// Verifica che la matricola sia corretta e restituisce il codice di esito dell'operazione
				resultCode = server.authenticate(studentID);
				System.out.println("Richiesta di autenticazione servita\n");
				// Imposta il pacchetto risposta con il codice esito e lo invia al server segreteria
				s = new PacketImpl();
				s.setCode(resultCode);
				socket.sendData(s);
				break;
			case 1:	// Codice 1 -> Richiesta visione esami
				// Estrae dal pachetto il codice del corso
				courseID = (String) p.getData();
				// Invoca il metodo apposito che restituisce le info sottoforma di array
				examInfo = server.getExamInfo(courseID);
				s = new PacketImpl();
				// Estrae dall'array restiutito il codice di esito e lo inserisce nel pacchetto risposta
				statusCode= (int) examInfo.get(examInfo.size() - 1);
				examInfo.remove(examInfo.size() - 1);
				s.setCode(statusCode);
				// Inserisce nel pacchetto l'array delle informazioni e lo invia al server della segreteria
				s.setData((ArrayList<ArrayList<String>>) examInfo);
				socket.sendData(s);
				System.out.println("Richiesta visione esami servita\n");
				break;
			case 2:	// Codice 2 -> Inserimento esame
				System.out.println("Richiesta di inserimento esame ricevuta");
				// Estrae dal pacchetto le informazioni da inserire nel database
				data = (ArrayList<String>) p.getData();
				courseID = data.get(0);
				examDate = data.get(1);
				examForm = data.get(2);
				prof = data.get(3);
				// Effettua l'aggiunta dell'esame, ottenendo il codice di esito della operazione
				resultCode = server.addExam(courseID, examDate, examForm, prof);
				// Imposta il pacchetto risposta inserendo il codice di esito e lo invia
				s = new PacketImpl();
				s.setCode(resultCode);
				socket.sendData(s);
				System.out.println("Operazione conclusa");
				break;
			case 3: // Codice 3 -> Inserimento prenotazione
				System.out.println("Richiesta di prenotazione per esame ricevuta\n");
				// Estrae i dati per la prenotazione
				data = (ArrayList<String>) p.getData();
				courseID = data.get(0);
				examDate = data.get(1);
				studentID = data.get(2);
				// Invoca il metodo apposito, che restituisce il codice di esito e il n. di prenotazione
				result = server.addReservation(courseID, examDate, studentID);
				// Imposta il pacchetto risposta col codice esito e con i dati, inviandolo
				s = new PacketImpl();
				statusCode = (int) result.get(result.size() - 1);
				reservationNumb = (int) result.get(0);
				s.setCode(statusCode);
				s.setData(reservationNumb);
				socket.sendData(s);
				break;
			default:
				System.out.println("Codice richiesta non valido\n");
				break;
			}
		}
		catch(Exception e)	// Gestione eccezioni
		{
			System.out.println("Errore generico interno..." + e.getMessage() + "\n");
		}
		finally
		{
			// Chiude la connessione
			socket.closeConn();
		}
	}

}
