import java.rmi.*;
import java.util.*;

public interface ChatServerInt extends Remote{	
	public boolean login (ChatClientInt a)throws RemoteException ;
	public void publish (String s, String string)throws RemoteException ;
	public Vector getConnected() throws RemoteException ;
	public boolean createGroup(String groupName) throws RemoteException;
	public ArrayList sendGroupList() throws RemoteException;
	public void updategroupMembers(String userName, String groupName, String lastGroups)throws RemoteException;
	public Map<String, ArrayList<String>> sendGroupMembers() throws RemoteException;
	public void publishUserList(ArrayList<String> removeList) throws RemoteException;
	public void removeUser(String userName) throws RemoteException;
}