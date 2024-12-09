// Interfaccia che estende Server e definisce le operazioni effettuate dalla segreteria 
package gestionale_Uni;
import java.util.ArrayList;

public interface StudentAdminOffice extends Server 
{
	public abstract int forwardAccessRequest(String studentID);
	public abstract ArrayList<ArrayList> getExamInfo(String courseCode);
	public abstract int insertExam(String courseCode, String examDate, String examForm, String prof);
	public abstract ArrayList<Integer> forwardReservationInsert(String courseCode, String examDate, String studentID);
}
