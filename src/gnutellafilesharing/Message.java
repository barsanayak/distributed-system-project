package gnutellafilesharing;



import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private MessageID messageID;
	private int TTL;
	private String fileName;
	private String peerIP;
	private String command;
	private int port;
	private String broadcastMessage;
        private String messagebId;
        private String originator;
	
	// Query message 
	public Message(String command, MessageID messageID, int TTL, String fileName){
		this.command = command;
		this.messageID = messageID;
		this.TTL = TTL;
		this.fileName = fileName;
	}

	// HitQuery message
	public Message(String command, MessageID messageID, 
			       int TTL, String fileName, String peerIP, int port){
		this.command = command;
		this.messageID = messageID;
		this.TTL = TTL;
		this.fileName = fileName;
		this.peerIP = peerIP;
		this.port = port;
	}
	
	// Down load message
	public Message(String command, String fileName, String peerIP, int port){
		this.command = command;
		this.fileName = fileName;
		this.peerIP = peerIP;
		// Set down load port
		this.port = port;
	}
	
	 //Broadcast message
        public Message(String command, String messageId, String message, String originator){
		this.command = command;
		this.messagebId = messageId ;
                this.broadcastMessage = message;
                this.originator = originator;
		//this.peerIP = peerIP;
		// Set down load port
		this.port = port;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public void setMessageID(MessageID messageID){
		this.messageID = messageID;
	}
	
	public void setTTL(int TTL){
		this.TTL = TTL;
	}
	
	public void setfileName(String fileName){
		this.fileName = fileName;
	}
	
	public void setPeerIP(String peerIP){
		this.peerIP = peerIP;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public MessageID getMessageID(){
		return messageID;
	}
	
	public int getTTL(){
		return TTL;
	}
	
	public String getfileName(){
		return fileName;
	}
	
	public String getPeerIP(){
		return peerIP;
	}
	
	public int getPort(){
		return port;
	}
	
	public String getCommand(){
		return command;
	}
	
	public String getBrdMsg() {
            return broadcastMessage;
        }
        
        public String getBrdMsgId() {
            return messagebId;
        }

	public String getOriginator() {
		return originator;
	}


}
