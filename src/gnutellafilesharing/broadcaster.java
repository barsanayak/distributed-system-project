package gnutellafilesharing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aksha
 */
public class broadcaster {

    public String msgID = "";
    public String msg = "";
    public static ArrayList<String> msgStore = new ArrayList<>();
    public static String sender = null;

    broadcaster(String msgId, String msg, String sender) {
        this.msg = msg;
        this.msgID = msgId;
        //this.msgStore.add(msgID);        
	this.sender = sender;
	//System.out.println("broadcaster");
    }
    
    broadcaster (String msgId, String msg) {
        this.msg = msg;
        this.msgID = msgId;
        
        //this.msgStore.add(msgID);
    }

    /*
    public String create_message() {
        String message = "";
        message = "broadcast:"+message + msgID + ":" + msg;
        return message;
    }*/
    public boolean checkIfDuplicate(String msgID) {

        if (msgStore.contains(msgID)) {
//System.out.println("msg duplicate");
//System.out.println("msg store" + msgStore);
            return true;
        } else {
//System.out.println("msg not duplicate");
            return false;
        }
    }

    public void sendToPeer(String msgId) {
//System.out.println("check 8");
//System.out.println(!checkIfDuplicate(msgId));
        if (!checkIfDuplicate(msgId)) {
//System.out.println("inside if -- ");
	if(msgStore.size()>3)
            msgStore.remove(0);	
	msgStore.add(msgId);
//System.out.println("check 2");
            for (int i = 0; i < PeerInformation.local.neighbor.size(); i++) {
		int flag = 0;
                //if (!(PeerInformation.local.neighbor.get(i).peerName.equals(sender))) {
                    Socket servSocket = null;
//System.out.println("check 3");
                    try {
                        servSocket = new Socket(PeerInformation.local.neighbor.get(i).IP, PeerInformation.local.neighbor.get(i).port);
			
                    } catch (IOException ex) {
			System.out.println("no connection");
			flag =1;
                        //Logger.getLogger(broadcaster.class.getName()).log(Level.SEVERE, null, ex);
                    }
		    
		    if (flag ==0) {
                    ObjectOutputStream os = null;
                    Message broadcastMessage = new Message("broadcast", msgId, msg,sender);

                    try {
                        os = new ObjectOutputStream(servSocket.getOutputStream());
                        os.writeObject(broadcastMessage);
                        os.flush();
            
                    } catch (Exception e) {
                    	System.out.println(".");
                        //e.printStackTrace();
                    }
		    }
                //}
            }
        }
//System.out.println("check 9");

    }

}
