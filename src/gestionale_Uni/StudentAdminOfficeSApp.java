// Applicazione principale del server della segreteria
package gestionale_Uni;

public class StudentAdminOfficeSApp 
{

	public static void main(String[] args) 
	{
		StudentAdminOffice server = new StudentAdminOfficeImpl('S');
		if(server.start() == 0)
		{
			while(true)
			{
				SocketW socket = server.acceptConn();
				if(socket != null)
				{
					new SAOThread(socket, server);
				}
			}
		}	
	}
}
