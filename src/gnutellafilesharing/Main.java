package gnutellafilesharing;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

	public static void main(String args[]) {
		Main peer = new Main();
		FileSharingImpl peerfunc = new FileSharingImpl();
		peer.fileMonitor(PeerInformation.local.sharedPath);
		new WThread(PeerInformation.local.sharedPath);
		try {
			System.out.println( "Gnutella file sharing is initiated");
			peer.communicate(peerfunc);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/*
	 * Read configure file: config.txt Set up IP address and port of local peer
	 */
	/**
	 * method used to read configuration files 
	 * @param config
	 */
	public static void readConfigurationFile(String config) {
		FileWriter writer = null;
		String s = new String();
		try {
			writer = new FileWriter(PeerInformation.local.logFilePath, true);

			File file = new File(config);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferReader = new BufferedReader(read);
				while ((s = bufferReader.readLine()) != null) {
					PeerInformation.local.totalPeersConnected++;
					
					String info[] = s.split(" ");
					if (PeerInformation.local.nodeInfo.peerName.equals(info[0])) {
						PeerInformation.local.nodeInfo.IP = info[1];
						PeerInformation.local.nodeInfo.port = Integer.parseInt(info[2]);
						if (info.length > 2)
							for (int i = 3; i < info.length; i++) {
								BufferedReader reader = new BufferedReader(new FileReader(file));
								while ((s = reader.readLine()) != null) {
									String temp[] = s.split(" ");
									if (info[i].equals(temp[0])) {
										Node node = new Node(temp[0], temp[1], Integer.parseInt(temp[2]));
										PeerInformation.local.neighbor.add(node);
										writer.write(PeerInformation.local.nodeInfo.peerName + " information about neighbours:");
										writer.write(node.peerName + " ");
									}
								}
							}
						writer.write("\t\n");
						System.out.println("My (local peer) information:");
						PeerInformation.local.nodeInfo.NodeInfo();
						PeerInformation.local.addedPeerNames.add(PeerInformation.local.nodeInfo.getPeerName());
						System.out.println("My neighbor peers are:");

						for (int i = 0; i < PeerInformation.local.neighbor.size(); i++) {
							PeerInformation.local.neighbor.get(i).NodeInfo();
						}
					}
				}
			} else {
				System.out.println("Configuration file does not exist!");
				writer.write("Configuration file does not exist!\r\n");
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("unable to read configuration file" + e.getMessage());
		}
	}

	/*
	 * File read function Scan the file folder and register all files in the
	 * local file list.
	 */
	public void fileMonitor(String path) {
		File file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
		String test[];
		test = file.list();
		if (test.length != 0) {
			for (int i = 0; i < test.length; i++) {
				// register(test[i]);
			}
		}
	}

	/*
	 * Register function Set up a socket connection to the index server Register
	 * the file to the index server
	 */
	public static void register(String fileName) {
		FileWriter writer = null;

		try {
			File file = new File(PeerInformation.local.completeSharedPath + fileName);
			System.out.println();
			if (!file.exists()) {
				System.out.println(fileName + " does not exist!");

			} else {
				writer = new FileWriter(PeerInformation.local.logFilePath, true);

				PeerInformation.local.fileList.add(fileName);
				System.out.println("File: " + fileName + " is registered!");

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = df.format(new Date());
				writer.write(time + "\t\tFile " + fileName + " is registered on the local peer!\r\n");
				writer.close();
			}

		} catch (Exception e) {
			System.out.println("unable to register file :" + e.getMessage());
		}
	}

	/*
	 * Unregister function Set up a socket connection to the index server
	 * Unregister the file to the index server
	 */
	public static void unregister(String fileName) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(PeerInformation.local.logFilePath, true);
			PeerInformation.local.fileList.remove(fileName);
			System.out.println("File " + fileName + " is unregistered!");

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			writer.write(time + "\t\tFile " + fileName + " is unregistered on the index server!\r\n");
			writer.close();

		} catch (Exception e) {
			System.out.println("unable to unregister file :" + e.getMessage());
		}
	}

	public void communicate(FileSharingImpl peerfunc) throws IOException {

		boolean exit = false;
		// Store file name
		String fileName = null;

		BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));

		// Usage Interface
		while (!exit) {
			
			System.out.println("\n1 Name the peer \n2 Register a file for sharing \n3 Search for shared file\n4 Exit\n5 Broadcast a message" );
			switch (Integer.parseInt(localReader.readLine())) {
			case 1: {
				
				//System.out.println(" The names of all peers:" + getNamesOfallPeers());	
				
				//System.out.println(" The names of the registered peers:"  + getRegisteredPeerNames());
				System.out.println("Enter the  name of the peer :");
				PeerInformation.local.nodeInfo.peerName = localReader.readLine();
				PeerInformation.initialize();
				readConfigurationFile(PeerInformation.local.configFilePath);
				ServerSocket server = null;
				try {
					server = new ServerSocket(PeerInformation.local.nodeInfo.port);
					System.out.println("\nServer started!");
					new PeerThread(server, peerfunc);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}

			case 2: {
				System.out.println( "Files present in the shared folder:");
				listFilesInTheDirectory();
				System.out.println("Enter the file name to be registered:");
				fileName = localReader.readLine();
				// Register file to the index server
				register(fileName);
				String broadcastMessage = fileName;
				String newMssgId = new MessageIdCreator().uniqueId();
				broadcaster brd = new broadcaster(newMssgId,broadcastMessage,PeerInformation.local.nodeInfo.peerName);
                            	brd.sendToPeer(newMssgId);
System.out.println("check");
				break;
			}
			case 3: {
				// peerInfo.local.messageTable = new
				// HashMap<Integer,MessageID>();
				PeerInformation.destination.destPeer = new ArrayList<Node>();
				PeerInformation.local.hitQueryRequest = 0;
				System.out.println("Enter the file name:");
				fileName = localReader.readLine();
				System.out.println("\nStart processing...\n");
				// Assemble messageID
				int num = PeerInformation.local.messageNum + 1;
				MessageID messageID = new MessageID(num, PeerInformation.local.nodeInfo);

				// Send query message to the neighbors
				peerfunc.query(messageID, PeerInformation.local.TTL - 1, fileName);

				long runtime = 0;
				long start = System.currentTimeMillis();
				// Set cutoff time = 3s
				while (runtime < PeerInformation.local.cutoffTime) {
					long end = System.currentTimeMillis();
					runtime = end - start;
				}

				if (PeerInformation.destination.destPeer.size() != 0) {
					int index = 0;
					int indexNum = 0;
					System.out.println(fileName + " was found on peers!");
					System.out.println("\n1 Download the file\n2 Cancel and back");
					switch (Integer.parseInt(localReader.readLine())) {
					case 1:
						System.out.println("The destination peer list is:");
						for (int i = 0; i < PeerInformation.destination.destPeer.size(); i++) {
							index = i + 1;
							System.out.println(index + ":" + PeerInformation.destination.destPeer.get(i).IP + " "
									+ PeerInformation.destination.destPeer.get(i).port);
						}
						System.out.println("Chose which peer to download the file:");
						indexNum = Integer.parseInt(localReader.readLine());

						new DThread(PeerInformation.local.nodeInfo.port + 1, fileName);
						peerfunc.downLoad(fileName, indexNum, PeerInformation.local.nodeInfo.IP, PeerInformation.local.nodeInfo.port + 1);
						break;
					case 2:
						break;
					default:
						break;
					}
				} else {
					System.out.println(fileName + " was not found on peers!");
				}
				break;
			}

			case 4: {
				exit = true;
				System.exit(0);
				break;
			}

			case 5: {
                            //BufferedReader messageReader = new BufferedReader(new InputStreamReader(System.in));
                            System.out.println("Enter the message to be broadcasted");
                            String broadcastMessage = localReader.readLine();
			    //String broadcastMessage = messageReader.readLine();
			    //Scanner sc = new Scanner(System.in);
			    //String broadcastMessage = sc.nextLine();
			    //System.out.println("message read");
                            //sc.close();
                            String newMssgId = new MessageIdCreator().uniqueId();
                            //broadcastMessage = newMssgId+":"+broadcastMessage;
                         broadcaster brd = new broadcaster(newMssgId,broadcastMessage,PeerInformation.local.nodeInfo.peerName);
                         brd.sendToPeer(newMssgId);
		    	    //System.out.println("check 1");
			    break;
                        }

			default:
				break;
			}

		}
	}

	private void listFilesInTheDirectory() {
		File folder = new File(PeerInformation.local.sharedPath);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File Name: " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		
	}

	private String getRegisteredPeerNames() {
		StringBuffer strbuf = new StringBuffer();
		if(PeerInformation.local.addedPeerNames.size() > 0 ) {
			for(int i= 0;i< PeerInformation.local.addedPeerNames.size() ;i++) {
				strbuf.append(PeerInformation.local.addedPeerNames.get(i) + " ");
			}
		}
		return strbuf.toString();
	}

	private String getNamesOfallPeers() {
		StringBuffer strbuf = new StringBuffer();
		if(PeerInformation.local.allPeerNames.size() ==0 ) {
			
			String configFilePath = PeerInformation.local.configFilePath;
			FileWriter writer = null;
			String s = new String();
			try {
				writer = new FileWriter(PeerInformation.local.logFilePath, true);

				File file = new File(configFilePath);
				if (file.isFile() && file.exists()) {
					InputStreamReader read = new InputStreamReader(new FileInputStream(file));
					BufferedReader bufferReader = new BufferedReader(read);
					while ((s = bufferReader.readLine()) != null) {
						String info[] = s.split(" ");
						strbuf.append(info[0] + " ");
					}
				} else {
					System.out.println("getNamesOfallPeers :Configuration file does not exist!");
					writer.write("getNamesOfallPeers: Configuration file does not exist!\r\n");
				}
				writer.close();
			} catch (Exception e) {
				System.out.println("getNamesOfallPeers :unable to read configuration file" + e.getMessage());
			}
		
			
		}
		else {
			for(int i= 0;i< PeerInformation.local.allPeerNames.size() ;i++) {
				strbuf.append(PeerInformation.local.allPeerNames.get(i) + " ");
			}
		}
		return strbuf.toString();
	}


}

/*
 * Watch file Listening to the local file folder When there is a change,
 * register or unregister file in the local list.
 */
class WThread extends Thread {
	String path = null;

	// peerInfo.local.fileList
	public WThread(String path) {
		this.path = path;
		// Record the original file list in the folder
		start();
	}

	public void run() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (PeerInformation.local.fileList.size() != 0) {
					for (int i = 0; i < PeerInformation.local.fileList.size(); i++) {
						File file = new File(path + File.separator + PeerInformation.local.fileList.get(i));
						if (!file.exists()) {
							System.out.println(PeerInformation.local.fileList.get(i) + " was removed!");
							Main.unregister(PeerInformation.local.fileList.get(i));

						}
					}
				}
			}

		}, 1000, 100);

	}
}

/*
 * Used to receive file from file client Step 1. Set up a server socket Step 2.
 * Waiting for input data
 */
class DThread extends Thread {
	int port;
	String fileName;

	public DThread(int port, String fileName) {
		this.port = port;
		this.fileName = fileName;
		start();
	}

	public void run() {
		try {
			ServerSocket server = new ServerSocket(port);
			// while(true){
			Socket socket = server.accept();
			receiveFile(socket, fileName);
			socket.close();
			server.close();
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Receive function used to receive file and save the file in the local
	// machine
	public static void receiveFile(Socket socket, String fileName) throws IOException {
		byte[] inputByte = null;
		int length = 0;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		String filePath = PeerInformation.local.completeSharedPath + fileName;
		try {
			try {
				dis = new DataInputStream(socket.getInputStream());
				System.out.println();

				File f = new File(PeerInformation.local.completeSharedPath);
				System.out.println(" file " + f.getPath());
				if (!f.exists()) {
					f.mkdir();
				}

				fos = new FileOutputStream(new File(filePath));
				inputByte = new byte[1024];
				System.out.println("\nStart receiving...");
				System.out.println("display file " + fileName);
				while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
					fos.write(inputByte, 0, length);
					fos.flush();
				}
				System.out.println("Finish receive:" + filePath);
			} finally {
				if (fos != null)
					fos.close();
				if (dis != null)
					dis.close();
				if (socket != null)
					socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
