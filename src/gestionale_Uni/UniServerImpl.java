// Classe che implementa l'interfaccia UniServer e i suoi metodi
package gestionale_Uni;

import java.util.ArrayList;
import java.io.IOException;
import java.sql.*;

public class UniServerImpl implements UniServer 
{
	private ServerSocketW serverSocket;	// Socket di tipo server
	
	// Costruttore
	UniServerImpl()
	{
		// Crea un oggetto di tipo ServerSocketWImpl, passando la porta e lo memorizza in serverSocket
		this.serverSocket = new ServerSocketWImpl(1027);
	}
	
	// Metodo per far partire il server
	public int start()
	{
		int statusCode = 0;	// Codice di stato
		try
		{
			serverSocket.start();
		}
		catch(IOException exc)	// Gestione eccezioni
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
			// Restituisce il codice di stato
			return statusCode;
		}
	}
	
	// Metodo per interrompere il server
	public void stop()
	{
		try
		{
			serverSocket.stop();
		}
		catch(IOException exc)
		{
			System.out.println("Errore di chiusura del socket server : " + exc.getMessage()+ "\n");
		}
		catch(Exception exc)
		{
			System.out.println("Errore generico di chiusura: " + exc.getMessage() + "\n");
		}
	}
	
	// Metodo per accettare connessioni. Restituisce un oggetto di tipo SocketW
	public SocketW acceptConn()
	{
		return serverSocket.acceptConn();
	}
	
	// Metodo per autenticare uno studente data la sua matricola. Restituisce un codice di esito
	public int authenticate(String studentID)
	{
		int statusCode = 0; // Codice di stato
		// Query per ottenere la matricola di un dato studente dal db
		String query = "SELECT Matricola FROM Studente WHERE Matricola = ?";
		// Oggetto per la connessione al database
		SQLDatabase db = UniversityDB.getIstance("daniele", "admin");
		// Variabili di tipo PreparedStatement e ResultSet per impostare la query e ottenere il risultato
		PreparedStatement stm = null;
		ResultSet result = null;
		try
		{
			db.connect();	// Connessione al database
			// Ottiene un oggetto di tipo PreparedStatement passando la query
			stm = db.getStatement(query);
			// Imposta i parametri della query
			stm.setString(1, studentID);
			// Esegue la query e restituisce il risultato
			result = db.executeStatement(stm);
			// Se la query non restituisce risultato...
			if(!result.isBeforeFirst())
			{
				// Lancia eccezione riguardante la matricola non trovata
				throw new StudentNotFound();
			}
		}
        // Eccezione per gli errori relativi al database
        catch(SQLException e) 
        {
            System.out.println("Si e' verificato un errore: " + e.getErrorCode() + " - " + e.getMessage());
            statusCode= -1;
        }
		catch(StudentNotFound e)	// Matricola non trovata
		{
			System.out.println("Si e' verificato un errore: " + e.getMessage() + "\n");
			statusCode= 1;
		}
        // Eccezioni generiche
        catch (Exception ex) 
        {
            System.out.println("Si e' verificato un errore: " + ex.getMessage());
            statusCode= -2;
        }
		finally
		{
			// Chiusura della connessione al db e degli elementi collegati
			db.closeEverything(stm, result);
			return statusCode;	// Restituisce codice di stato
		}
		
	}
	
	/* Metodo che inserisce un esame all'interno del database, passando il codice esame, la data,
	 * la modalità e il professore. Restituisce un codice di esito */
	public int addExam(String courseCod, String courseDate, String examForm, String prof)
	{
		int statusCode = 0, result;
		// Query per inserire l'esame all'interno del database
		String query = "INSERT INTO Esame VALUES(?,STR_TO_DATE(?,'%d/%m/%Y'),?,?)";
		PreparedStatement stm = null;
		SQLDatabase db = UniversityDB.getIstance("daniele", "admin");
		try
		{
			db.connect();	// Connessione al database
			// Ottiene oggetto di tipo PreparedStatement passando la query e imposta i parametri
			stm = db.getStatement(query);	
			stm.setString(1, courseCod);
			stm.setString(2, courseDate);
			stm.setString(3, examForm);
			stm.setString(4, prof);
			// Esegue l'inserimento
			result = db.insertOrUpdate(stm);
		}
        catch(SQLException e) 
        {
            System.out.println("Si e' verificato un errore: " + e.getErrorCode() + " - " + e.getMessage());
            statusCode= -1;
        }
        // Eccezioni generiche
        catch (Exception ex) 
        {
            System.out.println("Si e' verificato un errore: " + ex.getMessage());
            statusCode= -2;
        }
		finally
		{
			db.closeEverything(stm); // Chiusura connessione
			return statusCode; // Restituisce codice di stato
		}
	}
	
	/* Metodo per ottenere le date di esame dato un codice di corso. Restituisce un array contenente le
	 * informazioni e un codice di esito */
	public ArrayList<Object> getExamInfo(String courseCod)
	{
		int statusCode = 0;
		// Query per ottenere le informazioni riguardo un corso e le sue date di esame
		String query = "SELECT Cod_corso, nome_corso, DATE_FORMAT(data_esame, '%d/%m/%y') AS data_esame, " +
                "modalita, docente " +
                "FROM Esame E " +
                "JOIN Corso C ON E.corso = C.cod_corso " +
                "WHERE Cod_corso = ?";
		SQLDatabase db = UniversityDB.getIstance("daniele","admin");
		PreparedStatement stm = null;
		ResultSet result = null;
		// Array che conterrà le informazioni e il codice di stato
		ArrayList examInfo = new ArrayList<ArrayList<Object>>();
		try
		{
			db.connect();	// Connessione al db
			// Ottiene il PreparedStatement, lo imposta e lo esegue
			stm = db.getStatement(query);
			stm.setString(1,courseCod);
			result = db.executeStatement(stm);
			if(!result.isBeforeFirst()) // Se non ci sono risultati...
			{
				// Lancia eccezione riguardante l'esame non trovato
				throw new ExamNotFoundExc();
			}
			else if(result.next())	// Altrimenti...
			{
				while(result.next())
				{
					// Estrae le righe da result e le memorizza nell'array examInfo
					ArrayList info = new ArrayList<String>();
					info.add(result.getString("Cod_corso"));
					info.add(result.getString("nome_corso"));
					info.add(result.getString("data_esame"));
					info.add(result.getString("modalita"));
					info.add(result.getString("docente"));
					examInfo.add(info);
				}
			}
		}
        // Eccezione per gli errori relativi al database
        catch(SQLException e) 
        {
            System.out.println("Si e' verificato un errore: " + e.getErrorCode() + " - " + e.getMessage());
            statusCode= -1;
        }
		// Eccezione per esame non trovato
		catch(ExamNotFoundExc e)
		{
			System.out.println("Si e' verificato un errore: " + e.getMessage() + "\n");
			statusCode = 2;
		}
        // Eccezioni generiche
        catch (Exception ex) 
        {
            System.out.println("Si e' verificato un errore: " + ex.getMessage());
            statusCode= -2;
        }
		finally
		{
			db.closeEverything(stm, result);	// Chiusura connessione al database
			examInfo.add(statusCode);	// Aggiunge il codice di stato all'array
			return examInfo;	// Restituisce l'array
		}
	}
	
	/* Metodo per aggiungere la prenotazione di uno studente ad un esame, dato il codice di corso, la data
	 * e la matricola dello studente in questione. Restituisce un array contenente il codice di stato e il
	 * numero di prenotazione */
	public ArrayList<Integer> addReservation(String courseCod, String courseDate, String studentID)
	{
		// Query per verificare che un dato esame sia presente nel database
		String query1 = "SELECT Corso, Data_esame FROM Esame WHERE Corso = ? AND Data_esame = STR_TO_DATE(?, '%d/%m/%Y')";
		// Query per ottenere il max numero di prenotazione
		String query2 = "SELECT MAX(Numero_prenot) AS ultimo_n_prenot FROM Prenotazione";
		// Query per inserire la prenotazione
		String query3 = "INSERT INTO Prenotazione VALUES (?,STR_TO_DATE(?,'%d/%m/%Y'),?,?)";
							// Numero di prenotazione
		int statusCode = 0, reservingNumb = 0, r;
		PreparedStatement stm = null;
		ResultSet result = null;
		SQLDatabase db = UniversityDB.getIstance("daniele", "admin");
		// Array risultato
		ArrayList output = new ArrayList<Integer>();
		try
		{
			db.connect();	// Connessione al database
		// Ottiene il PreparedStatement passando la prima query. Segue impostazione parametri ed esecuzione
			stm = db.getStatement(query1);
			stm.setString(1, courseCod);
			stm.setString(2, courseDate);
			result = db.executeStatement(stm);
			// Se la query non produce risultati...
			if(!result.isBeforeFirst())
				throw new ExamNotFoundExc();	// Lancia eccezione riguardo l'esame non trovato
			// Ottiene un nuovo oggetto PreparedStatement passando la seconda query e la esegue
			stm = db.getStatement(query2);
			result = db.executeStatement(stm);
			// Se non ci sono risultati...
			if(!result.isBeforeFirst())
			{
				// Imposta il n. di prenotazione a 1
				reservingNumb = 1;
			}
			else if(result.next()) // Altrimenti...
			{
				// Incrementa numero di prenotazione
				int latestReservingNumb = result.getInt("ultimo_n_prenot");
				reservingNumb = latestReservingNumb + 1;
			}
// Ricava un altro oggetto di tipo PreparedStatement passando la seconda query. Imposta i parametri e la esegue
			stm = db.getStatement(query3);
			stm.setString(1, courseCod);
			stm.setString(2, courseDate);
			stm.setString(3, studentID);
			stm.setInt(4, reservingNumb);
			r = db.insertOrUpdate(stm);
			System.out.println("Prenotazione effettuata");
			// Aggiunge all'array il numero di prenotazione
			output.add(reservingNumb);	
		}
		catch(SQLException e)	// Gestione eccezioni
		{
			System.out.println("Errore nell'inserimento della prenotazione: " + e.getMessage() + "\n");
			statusCode = -1;
		}
		catch(ExamNotFoundExc e)
		{
			System.out.println("Errore nell'inserimento della prenotazione: " + e.getMessage() + "\n");
			statusCode = 2;
		}
		catch(Exception e)
		{
			System.out.println("Errore generico: " + e.getMessage() + "\n");
			statusCode = -2;
		}
		finally
		{
			db.closeEverything(stm, result);	// Chiusura connessione al db
			// Aggiunge all'array il codice di stato e restituisce il tutto
			output.add(statusCode);
			return output;
		}
	}
	
	
	
}
