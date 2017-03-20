import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.Timestamp;
import java.util.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import java.security.*;
import java.awt.Dialog;
import java.awt.Choice;
//import java.sql.Timestamp;

public class WelcomePage {

	private JFrame frmConvoMessaging;
	private ChatClient client;
	  private ChatServerInt server;
	  Date date = new Date();
	 List<String> selectedValuesList;
	 private String lastGroup;
	 long time = date.getTime();
	 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomePage window = new WelcomePage();
					window.frmConvoMessaging.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public boolean doConnect(JTextArea username,JTextArea ip) throws RemoteException{
		if(btnConnect.getText()=="Connect"){
        System.out.println("name: "+username.getText());
        if (username.getText().length()<2){JOptionPane.showMessageDialog(frmConvoMessaging, "You need to type a name."); }
	    	if (ip.getText().length()<2){JOptionPane.showMessageDialog(frmConvoMessaging, "You need to type an IP.");}	    	
	    	try{
				client=new ChatClient(username.getText());
				client.setGUI(this);
                                System.out.println("ip: "+ip.getText());
				server=(ChatServerInt)Naming.lookup("rmi://"+ip.getText()+"/myabc");
				server.login(client);
				updateUsers(server.getConnected());
				updateGroupList();
                  System.out.println("connected");
                  btnConnect.setText("Disconnect");
                               return true;
			    			    
	    	}catch(Exception e)
                {e.printStackTrace();
                JOptionPane.showMessageDialog(frmConvoMessaging, "ERROR, we wouldn't connect....");
                }
		}
		else{
			updateUsers(null);
			groupsList.removeAll();
			server.removeUser(username.getText());
			btnConnect.setText("Connect");
		}
	    	return false;
    }
	
	public void updateUsers(Vector v){
	      DefaultListModel listModel = new DefaultListModel();
	      if(v!=null) for (int i=0;i<v.size();i++){
	    	  try{  String tmp=((ChatClientInt)v.get(i)).getName();
	    	  		listModel.addElement(tmp);
	    	  		System.out.println(listModel.size());
	    	  }catch(Exception e){e.printStackTrace();}
	      }
	      
	      usersList.setModel(listModel);
	  }
	
	public void UpdateGroups(String text){
		// TODO Auto-generated method stub
		try{
		server=(ChatServerInt)Naming.lookup("rmi://172.20.121.159/myabc");
		server.createGroup(text);
		switchGroup(text,lastGroup);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateGroupList(){
		ArrayList groupList = new ArrayList();
		DefaultListModel groupModel = new DefaultListModel();
		try {
			groupList = server.sendGroupList();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(groupList!=null) for (int i=0;i<groupList.size();i++){
	    	  try{  String tmp= (String) groupList.get(i);
	    	  groupModel.addElement(tmp);
	    	  		//System.out.println(groupModel.size());
	    	  }catch(Exception e){e.printStackTrace();}
	      }
		groupsList.setModel(groupModel);
	}

	public void sendText(){
	    /*if (connect.getText().equals("Connect")){
	    	JOptionPane.showMessageDialog(frame, "You need to connect first."); return;	
	    }*/
	      String st=textToBeSent.getText();
	      java.sql.Timestamp ts = new java.sql.Timestamp(time);
	      st= ts + ":"+"\n"+"["+username.getText()+"] "+st;
	      textToBeSent.setText("");
	      //Remove if you are going to implement for remote invocation
	      try{
	    	 	server.publish(st,selectedValuesList.get(0));
	  	  	}catch(Exception e){e.printStackTrace();}
	  }
	
	public void writeMsg(String txt){
		textArea.setText(textArea.getText()+"\n"+txt);
	}
	
	public void updateUsersList(String username) throws RemoteException{
		Vector v = server.getConnected();
		DefaultListModel listModel = new DefaultListModel();
	      if(v!=null) for (int i=0;i<v.size();i++){
	    	  try{  String tmp=((ChatClientInt)v.get(i)).getName();
	    	  		listModel.addElement(tmp);
	    	  		System.out.println(listModel.size());
	    	  }catch(Exception e){e.printStackTrace();}
	      }
	      
	      usersList.setModel(listModel);
	}
	
	private void switchGroup(String string, String lastGroups) throws RemoteException {
		// TODO Auto-generated method stub
		DefaultListModel listModel = new DefaultListModel();
		//System.out.println(lastGroups);
		server.updategroupMembers(username.getText(), string,lastGroups);
		Map<String, ArrayList<String>> groupName_users =  server.sendGroupMembers();
		ArrayList<String> usersList = groupName_users.get(string);
		for(String name:usersList){
			listModel.addElement(name);
		}
		
		textArea.setText("");
		this.usersList.setModel(listModel);
	}
	
	public void updateUsersAfterRemoval(ArrayList<String> removeList) {
		DefaultListModel listModel = new DefaultListModel();
		for(String name:removeList){
			listModel.addElement(name);
		}
		this.usersList.setModel(listModel);
	}
	
	/**
	 * Create the application.
	 */

	/**
	 * Initialize the contents of the frame.
	 */
	public WelcomePage() {
		frmConvoMessaging = new JFrame();
		frmConvoMessaging.setTitle("Convo - Messaging Application");
		frmConvoMessaging.setBounds(100, 100, 488, 513);
		frmConvoMessaging.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConvoMessaging.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome to Chat Application");
		lblNewLabel.setBounds(6, 0, 218, 16);
		frmConvoMessaging.getContentPane().add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("IP Address");
		lblUsername.setBounds(16, 18, 66, 16);
		frmConvoMessaging.getContentPane().add(lblUsername);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setBounds(202, 18, 62, 16);
		frmConvoMessaging.getContentPane().add(lblNewLabel_1);
		
		JTextArea ip = new JTextArea();
		ip.setBounds(94, 18, 96, 16);
		frmConvoMessaging.getContentPane().add(ip);
		
		username = new JTextArea();
		username.setBounds(276, 18, 96, 16);
		frmConvoMessaging.getContentPane().add(username);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(doConnect(username,ip)== true){
						//System.out.println("Here");
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				};
			}
		});
		btnConnect.setBounds(379, 13, 82, 29);
		frmConvoMessaging.getContentPane().add(btnConnect);
		
		JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");
		internalFrame.setBounds(255, 207, 150, 65);
		frmConvoMessaging.getContentPane().add(internalFrame);
		
		textArea = new JTextArea();
		textArea.setBounds(6, 46, 258, 252);
		frmConvoMessaging.getContentPane().add(textArea);
		
		usersList = new JList();
		usersList.setBounds(384, 66, 93, 224);
		frmConvoMessaging.getContentPane().add(usersList);
		
		JInternalFrame internalFrame_1 = new JInternalFrame("Switch Group");
		internalFrame_1.setClosable(true);
		internalFrame_1.setBounds(245, 346, 253, 127);
		frmConvoMessaging.getContentPane().add(internalFrame_1);
		
		groupsList = new JList();
		groupsList.setBounds(286, 66, 86, 224);
		frmConvoMessaging.getContentPane().add(groupsList);
		groupsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedValuesList = groupsList.getSelectedValuesList();
					//lastGroup = selectedValuesList.get(0);
					//System.out.println(selectedValuesList);
					 internalFrame_1.setVisible(true);
				}

			}
		});
		//groupsList.addListSelectionListener(selectedList);
		
		JLabel lblGroups = new JLabel("Groups");
		lblGroups.setBounds(286, 46, 61, 16);
		frmConvoMessaging.getContentPane().add(lblGroups);
		
		JLabel lblUsersList = new JLabel("Users List");
		lblUsersList.setBounds(389, 46, 88, 16);
		frmConvoMessaging.getContentPane().add(lblUsersList);
		
		JInternalFrame createGroupFrame = new JInternalFrame("Create Group");
		createGroupFrame.setMaximizable(true);
		createGroupFrame.setClosable(true);
		createGroupFrame.setBounds(-4, 346, 263, 127);
		frmConvoMessaging.getContentPane().add(createGroupFrame);
		
		JLabel lblEnterGroupName = new JLabel("Enter Group Name");
		createGroupFrame.getContentPane().add(lblEnterGroupName, BorderLayout.NORTH);
		
		JTextArea groupName = new JTextArea();
		createGroupFrame.getContentPane().add(groupName, BorderLayout.CENTER);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGroupFrame.setVisible(false);
				UpdateGroups(groupName.getText());
				lastGroup = groupName.getText();
				groupName.setText("");
			}});

			
		createGroupFrame.getContentPane().add(btnSubmit, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Create Group");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createGroupFrame.setVisible(true);
			}
		});
		btnNewButton.setBounds(276, 302, 117, 29);
		frmConvoMessaging.getContentPane().add(btnNewButton);
		
		textToBeSent = new JTextArea();
		textToBeSent.setBounds(6, 310, 203, 16);
		frmConvoMessaging.getContentPane().add(textToBeSent);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendText();
			}
		});
		btnSend.setBounds(213, 302, 66, 29);
		frmConvoMessaging.getContentPane().add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(6, 46, 263, 252);
		frmConvoMessaging.getContentPane().add(scrollPane);
		
		
		
		JLabel lblDoYouWant = new JLabel("Do you want to switch group?");
		internalFrame_1.getContentPane().add(lblDoYouWant, BorderLayout.NORTH);
		
		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					lastGroup = selectedValuesList.get(0);
					switchGroup(selectedValuesList.get(0),lastGroup);
					internalFrame_1.setVisible(false);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			
		});
		internalFrame_1.getContentPane().add(btnYes, BorderLayout.CENTER);
		
		
	}
	JList usersList,groupsList;
	JTextArea textToBeSent;
	JTextArea textArea;
	JTextArea username;
	JDialog mydialog;
	JButton btnConnect;


	
}
