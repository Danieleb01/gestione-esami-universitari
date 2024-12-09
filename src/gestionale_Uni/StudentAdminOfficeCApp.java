// Applicazione client della segreteria per inserire gli esami
package gestionale_Uni;

public class StudentAdminOfficeCApp 
{

	public static void main(String[] args) 
	{
		ConsoleReader console = new ConsoleReader(System.in);	// Oggetto per leggere l'input da tastiera
		// Oggetto di tipo StudentAdminOffice per effettuare le operazioni (in modalità client)
		StudentAdminOffice SAOclient = new StudentAdminOfficeImpl('C');	
		String courseCode, examDate, examForm, prof;
		int statusCode;
		// Variabile per controllare se l'utente vuole effettuare ulteriori operazioni
		int done = 0;
		System.out.println("***SISTEMA DI AGGIUNTA ESAMI***\n");
		// Ciclo che viene eseguito fintanto che l'utente ha ancora operazioni da effettuare
		while(done != 1)
		{
			// Inserimento informazioni riguardante l'esame
			System.out.println("Inserisci il codice del corso:\n");
			courseCode = console.readLine();
			System.out.println("Inserisci la data dell'esame:\n");
			examDate = console.readLine();
			System.out.println("Inserisci la modalita' di svolgimento dell'esame:\n");
			examForm = console.readLine();
			System.out.println("Inserisci il docente:\n");
			prof = console.readLine();
			// Invocazione del metodo apposito che restituisce il codice di esito
			statusCode = SAOclient.insertExam(courseCode, examDate, examForm, prof);
			if(statusCode == 0)	// Controllo codice esito
			{
				System.out.println("Esame inserito con successo!\n");
			}
			else
			{
				System.out.println("Errore interno\n");
			}
			System.out.println("Vuoi effettuare un'altra operazione? (0: Sì; 1: No)");
			done = console.readInt();
		}
		System.out.println("Arrivederci");

	}

}
