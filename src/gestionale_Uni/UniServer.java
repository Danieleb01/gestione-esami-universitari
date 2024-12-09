/* Interfaccia che estende l'interfaccia Server e definisce le operazioni effettuate dal 
 * server universitario */
package gestionale_Uni;

import java.util.ArrayList;

public interface UniServer extends Server
{
	public int authenticate(String studentID);
	public int addExam(String courseCod, String courseDate, String examForm, String prof);
	public ArrayList<Integer> addReservation(String courseCod, String courseDate, String studentID);
	public ArrayList<Object> getExamInfo(String courseCod);
}
