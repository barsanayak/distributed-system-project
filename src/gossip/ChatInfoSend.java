package gossip;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class ChatInfoSend implements Serializable {
	
	private String type;
	private String textMsg;
	private int messageId;
	private InetAddress host;
	private int port;
	private ConcurrentHashMap<Integer, ConcurrentHashMap> statusPortIdMessageHashMap; 
    private ConcurrentHashMap<Integer,Integer> portMaxMessegeIdHashMap;
    





	






	public ChatInfoSend(
			String type,
			String textMsg,
			int messageId,
			InetAddress host,
			int port,
			ConcurrentHashMap<Integer, ConcurrentHashMap> statusPortIdMessageHashMap,
			ConcurrentHashMap<Integer, Integer> portMaxMessegeIdHashMap) {
		super();
		this.type = type;
		this.textMsg = textMsg;
		this.messageId = messageId;
		this.host = host;
		this.port = port;
		this.statusPortIdMessageHashMap = statusPortIdMessageHashMap;
		this.portMaxMessegeIdHashMap = portMaxMessegeIdHashMap;
	}



	public ConcurrentHashMap<Integer, Integer> getportMaxMessegeIdHashMap() {
		return portMaxMessegeIdHashMap;
	}



	public void setportMaxMessegeIdHashMap(
			ConcurrentHashMap<Integer, Integer> portMaxMessegeId) {
		this.portMaxMessegeIdHashMap = portMaxMessegeId;
	}



	public ConcurrentHashMap<Integer, ConcurrentHashMap> getStatusPortIdMessageHashMap() {
		return statusPortIdMessageHashMap;
	}



	public void setStatusPortIdMessageHashMap(
			ConcurrentHashMap<Integer, ConcurrentHashMap> statusPortIdMessageHashMap) {
		this.statusPortIdMessageHashMap = statusPortIdMessageHashMap;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public InetAddress getHost() {
		return host;
	}



	public void setHost(InetAddress host) {
		this.host = host;
	}



	public String getTextMsg() {
		return textMsg;
	}



	public void setTextMsg(String textMsg) {
		this.textMsg = textMsg;
	}



	public int getMessageId() {
		return messageId;
	}



	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}



	

	public String toString() {
		return "messageId = " + getMessageId() + " Name = " + getTextMsg() ;
		}
		

	

}
