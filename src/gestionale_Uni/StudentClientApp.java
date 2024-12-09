package gestionale_Uni;

import java.util.ArrayList;

public class StudentClientApp 
{

	public static void main(String[] args) 
	{
		boolean authenticated = false;	// Segnala se l'utente è autenticato o meno
		Student user = new StudentImpl();	// Oggetto di classe StudentImpl
		ConsoleReader console = new ConsoleReader(System.in);	// Per lettura input
		String studentID, courseCode, examDate;
		int status, done = 1, choice, reservationNumb;
		ArrayList result;
		// Se l'utente non è ancora autenticato...
		while(!authenticated)
		{
			// Inserimento matricola
			System.out.println("Inserisci la matricola:");
			studentID = console.readLine();
			// Imposta la matricola nel campo studentID dell'oggetto
			user.setID(studentID);
			// Effettua l'accesso e ottiene il codice di esito
			status = user.getAccess();
			// Controllo del codice di esito
			switch(status)
			{
				case 0:	// Accesso consentito
					System.out.println("Accesso consentito\n");
					authenticated = true;
					break;
				case 1:	// Accesso negato -> Matricola non trovata
					System.out.println("Matricola non trovata. Inseriscila di nuovo \n");
					break;
				default:	// Errore interno
					System.out.println("Errore interno\n");
					break;
			}
		}
		
		// Ciclo che viene eseguito fintanto che l'utente ha varie operazioni da effettuare
		while(done != 0)
		{
			// Stampa il menu
			printMenu();
			// Inserimento scelta (in base a quello riportato nel menu)
			choice = console.readInt();
			switch(choice)
			{
			case 1:	// Visione esami
				System.out.println("Inserisci il codice corso: \n");	// Inserimento codice corso
				courseCode = console.readLine();
				/* Ottiene un array di oggetti (contenente le info dell'esame richiesto e il codice di esito)
				 *  dall'invocazione del metodo getExamInfo */
				result = user.getExamInfo(courseCode);
				status = (int) result.get(result.size() - 1);	// Ricava il codice di esito dall'array
				// Controllo codice esito
				switch(status)
				{
				case 0:	// Successo
					// Ricava le date dell'esame e le stampa a schermo
					ArrayList<ArrayList<String>> examInfo = (ArrayList<ArrayList<String>>) result.get(0);
					printExamData(examInfo);
					break;
				case 2:	// Corso non esistente
					System.out.println("Nessuna corrispondenza trovata. Controlla che il codice sia corretto o rivolgiti alla segreteria");
					break;
				default:	// Errore interno
					System.out.println("Errore interno. Riprova tra qualche istante");
					break;
				}
				break;
				
			case 2:	// Prenotazione per un esame
				// Inserimento codice del corso e data dell'esame per cui l'utente si vuole prenotare
				System.out.println("Inserisci il codice del corso di cui vuoi prenotare l'esame: \n");
				courseCode = console.readLine();
				System.out.println("Inserisci la data dell'esame: \n");
				examDate = console.readLine();
				ArrayList<Integer> codes = user.sendReservation(courseCode, examDate);
				status = codes.get(codes.size() - 1);
				switch(status)
				{
				case 0:
					reservationNumb = codes.get(0);
					System.out.println("Prenotazione effettuata! Il tuo numero di prenotazione e' " + reservationNumb + "\n");
					break;
				case 2:
					System.out.println("Appello non trovato! Controlla di aver inserito bene il codice esame o la data\n");
					break;
				default:
					System.out.println("Errore interno. Riprova tra qualche istante\n");
					break;
				}
				break;
				
				default:
					break;
			}
			System.out.println("Vuoi effettuare un'altra operazione? (1:Sì; 0: No)");
			done = console.readInt();
		}

	}

	
	public static void printExamData(ArrayList<ArrayList<String>> info)
	{
		System.out.println("** Date esami del corso richiesto **");
	    String string = info.toString();
	    string = string.replace("[[", "") // Rimuove l'apertura iniziale
	                   .replace("]]", "") // Rimuove la chiusura finale
	                   .replace("], [", "\n") // Nuova riga tra righe
	                   .replace(", ", " | ")
	                   .replace("[", "")
	                   .replace("]", ""); // Aggiunge separatori tra colonne
	    System.out.println(string);
	}
	
	public static void printMenu()
	{
		System.out.println("**OPERAZIONI**\n");
		System.out.println("1. Visualizza date esame\n2. Prenota esame\n");
		System.out.println("Cosa vuoi fare?\n");
	}
}
