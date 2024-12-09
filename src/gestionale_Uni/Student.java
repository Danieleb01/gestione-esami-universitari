// Interfaccia che definisce le operazioni effettuate dall'utente
package gestionale_Uni;
import java.util.ArrayList;

public interface Student 
{
	public abstract void setID(String studentID);
	public abstract int getAccess();
	public abstract ArrayList getExamInfo(String courseCode);
	public abstract ArrayList<Integer> sendReservation(String courseCode, String examDate);
}
