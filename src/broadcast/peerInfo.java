import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class peerInfo {
    
                public static class local{
		public static int messageNum;
		public static int peerNum;
		public static int hitQueryRequest;
		public static int TTL = 3;	
		public static int cutoffTime = 3000;
		public static NodeMain nick = new NodeMain();
		public static String sharedPath = "./../share";
		public static String completeSharedPath = "./../share/";
		public static String configFilePath = "./../config.txt";
		public static String logFilePath = "./../peerLog.txt";
		public static ArrayList<NodeMain> neighbor = new ArrayList<NodeMain>();
		public static ArrayList<String> fileList = new ArrayList<String>();
		public static ConcurrentHashMap<Integer,MessageID> messageTable = new ConcurrentHashMap<Integer,MessageID>();
	}
	
	public static class dest{
		public static ArrayList<NodeMain> destPeer = new ArrayList<NodeMain>();
	}
	
	public static void initial(){
		peerInfo.local.messageNum = 0;
		peerInfo.local.peerNum = 0;
		peerInfo.local.hitQueryRequest = 0;
		peerInfo.local.neighbor = new ArrayList<NodeMain>();
		peerInfo.local.messageTable = new ConcurrentHashMap<Integer,MessageID>();
		peerInfo.dest.destPeer = new ArrayList<NodeMain>();
	}
	
}
