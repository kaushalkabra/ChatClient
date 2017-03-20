import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ChatClientInt extends Remote{	
	public void tell (String name)throws RemoteException ;
	public String getName()throws RemoteException ;
	public void tellGroups(String name) throws RemoteException; 
	public void tellUsers(String st) throws RemoteException;
	public void tellUsersList(ArrayList<String> removeList)throws RemoteException;
}