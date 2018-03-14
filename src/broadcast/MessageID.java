

import java.io.Serializable;

public class MessageID implements Serializable{
	private static final long serialVersionUID = 1L;
	private int sequenceNumber;
	private NodeMain peerID;
	
	public MessageID(int sequenceNumber, NodeMain peerID){
		this.sequenceNumber = sequenceNumber;
		this.peerID = peerID;
	}
	
	public void setSequenceNumber(int sequenceNumber){
		this.sequenceNumber = sequenceNumber;
	}

	public void setPeerID(NodeMain peerID){
		this.peerID = peerID;
	}
	
	public int getSequenceNumber(){
		return sequenceNumber;
	}
	
	public NodeMain getPeerID(){
		return peerID;
	}
}
