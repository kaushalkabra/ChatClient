import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatClient  extends UnicastRemoteObject implements ChatClientInt{
	private WelcomePage wp;
	private String name;
	public ChatClient (String n) throws RemoteException {
		name=n;
		}
	
	public void tell(String st) throws RemoteException{
		System.out.println(st);
		wp.writeMsg(st);
	}
	
	public void tellUsers(String st) throws RemoteException{
		System.out.println(st);
		wp.updateUsersList(st);
	}
	public String getName() throws RemoteException{
		return name;
	} 	
	
	public void setGUI(WelcomePage t){ 
		wp=t ; 
	}
	
	public void tellGroups(String st) throws RemoteException{
		System.out.println(st);
		wp.updateGroupList();
	}

	public void tellUsersList(ArrayList<String> removeList) throws RemoteException {
		wp.updateUsersAfterRemoval(removeList);
		
	}
}
