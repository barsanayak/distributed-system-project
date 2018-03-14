package gnutellafilesharing;



import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class PeerInformation {
	public static class local{
		public static int messageNum;
		public static int totalPeersConnected;
		public static int hitQueryRequest;
		public static int TTL = 3;	
		public static int cutoffTime = 3000;
		public static Node nodeInfo = new Node();
		public static String sharedPath = "./../share";
		public static String completeSharedPath = "./../share/";
		public static ArrayList<String> addedPeerNames = new ArrayList<String>();;
		public static ArrayList<String> allPeerNames = new ArrayList<String>();;
		public static String configFilePath = "./../config.txt";
		public static String logFilePath = "./../peerLog.txt";
		public static ArrayList<Node> neighbor = new ArrayList<Node>();
		public static ArrayList<String> fileList = new ArrayList<String>();
		public static ConcurrentHashMap<Integer,MessageID> messageTable = new ConcurrentHashMap<Integer,MessageID>();
	}
	
	public static void initialize(){
		PeerInformation.local.messageNum = 0;
		PeerInformation.local.totalPeersConnected = 0;
		PeerInformation.local.hitQueryRequest = 0;
		PeerInformation.local.neighbor = new ArrayList<Node>();
		PeerInformation.local.messageTable = new ConcurrentHashMap<Integer,MessageID>();
		PeerInformation.destination.destPeer = new ArrayList<Node>();
		PeerInformation.local.addedPeerNames = new ArrayList<String>();
		PeerInformation.local.allPeerNames = new ArrayList<String>();
	}
	
	public static class destination{
		public static ArrayList<Node> destPeer = new ArrayList<Node>();
	}
	
	
	
}
