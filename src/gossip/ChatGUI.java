package gossip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class ChatGUI extends JFrame {
	public static final int HOST_MODE = 0;
	public static final int CLIENT_MODE = 1;
	JButton btn_send;
	JScrollPane jScrollPane1;
	JTextArea jTextArea1;
	JLabel lbl_ipNroomName;
	JTextField txt_mymsg;
	int mode;
	String Name;
	String roomname;
	InetAddress hostip;
	ChatGUI pt;
	DatagramSocket socket;
	int maxPort = 8891;
	int minPort = 8888;
	/*
	 * 
	 * ArrayList<Client> ClientList=new ArrayList<Client>();
	 * 
	 */

	HashSet<Integer> portSet = new HashSet<Integer>();

	ConcurrentHashMap<Integer, String> idMessageHaspMap = new ConcurrentHashMap<Integer, String>();
	ConcurrentHashMap<Integer, ConcurrentHashMap> portIdMessageHaspMap = new ConcurrentHashMap<Integer, ConcurrentHashMap>();
	int messageId = 0;
	int currPort = 0;
	int dropMessageId = 0;

	ConcurrentHashMap<Integer, Integer> statusPortMessageHashMap = new ConcurrentHashMap<Integer, Integer>();

	byte[] b;

	public ChatGUI(String myname, int mod, String ip, int port) {
		try {
			idMessageHaspMap.put(0, " ");
			// Client client1=new Client(InetAddress.getLocalHost(), 8888,
			// "abcd1");
			statusPortMessageHashMap.put(8888, 0);
			portSet.add(8888);

			// Client client2=new Client(InetAddress.getLocalHost(), 8889,
			// "abcd2");
			statusPortMessageHashMap.put(8889, 0);
			portSet.add(8889);

			// Client client3=new Client(InetAddress.getLocalHost(), 8890,
			// "abcd3");
			statusPortMessageHashMap.put(8890, 0);
			portSet.add(8890);

			// Client client4=new Client(InetAddress.getLocalHost(), 8891,
			// "abcd4");
			statusPortMessageHashMap.put(8891, 0);
			portSet.add(8891);

			portIdMessageHaspMap.put(8888, idMessageHaspMap);
			portIdMessageHaspMap.put(8889, idMessageHaspMap);
			portIdMessageHaspMap.put(8890, idMessageHaspMap);
			portIdMessageHaspMap.put(8891, idMessageHaspMap);

			Name = myname;
			mode = mod;
			hostip = InetAddress.getLocalHost();
			// roomname = room;
			setLayout(null);
			setSize(400, 460);
			lbl_ipNroomName = new JLabel("", SwingConstants.CENTER);
			txt_mymsg = new JTextField();
			btn_send = new JButton("Send");
			jScrollPane1 = new JScrollPane();
			jTextArea1 = new JTextArea(8, 15);
			// ClientList = new ArrayList<>();
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			add(lbl_ipNroomName);
			lbl_ipNroomName.setBounds(10, 10, getWidth() - 30, 40);
			add(txt_mymsg);
			pt = this;
			txt_mymsg.setBounds(10, lbl_ipNroomName.getY() + lbl_ipNroomName.getHeight(), getWidth() - 130, 30);
			add(btn_send);
			btn_send.setBounds(txt_mymsg.getWidth() + 20, txt_mymsg.getY(), 80, 30);
			jScrollPane1.setViewportView(jTextArea1);
			add(jScrollPane1);
			jScrollPane1.setBounds(10, btn_send.getY() + 40, lbl_ipNroomName.getWidth(),
					getHeight() - 20 - jScrollPane1.getY() - 110);
			btn_send.setEnabled(true);
			jTextArea1.setEditable(true);
			txt_mymsg.setEnabled(true);
			btn_send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s = txt_mymsg.getText();
					if (s.equals("") == false) {

						ChatInfoSend statusChatInfo = new ChatInfoSend("status", s, messageId, hostip, currPort,
								portIdMessageHaspMap, statusPortMessageHashMap);
						currentStatus(statusChatInfo);

						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						messageId++;
						idMessageHaspMap.put(messageId, s);

						portIdMessageHaspMap.put(currPort, idMessageHaspMap);

						statusPortMessageHashMap.put(currPort, messageId);

						ChatInfoSend newchatInfo = new ChatInfoSend("broadcast", s, messageId, hostip, currPort,
								portIdMessageHaspMap, statusPortMessageHashMap);
						jTextArea1.setText(jTextArea1.getText() + "\n" + currPort + ":" + newchatInfo.getTextMsg());
						broadcast(newchatInfo);

						txt_mymsg.setText("");
					}
				}
			});

			if (mode == HOST_MODE) {
				this.currPort = port;
				socket = new DatagramSocket(currPort);
				lbl_ipNroomName.setText("Port:" + currPort);
			} else {
				socket = new DatagramSocket();
				String reqresp = "!!^^" + Name + "^^!!";
				DatagramPacket pk = new DatagramPacket(reqresp.getBytes(), reqresp.length(), hostip, 37988);
				socket.send(pk);
				b = new byte[300];
				pk = new DatagramPacket(b, 300);
				socket.setSoTimeout(6000);
				socket.receive(pk);
				reqresp = new String(pk.getData());
				if (reqresp.contains("!!^^")) {
					roomname = reqresp.substring(4, reqresp.indexOf("^^!!"));
					lbl_ipNroomName.setText("ChatRoom: " + roomname);
					btn_send.setEnabled(true);
					txt_mymsg.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(pt, "No response from the server");
					System.exit(0);
				}
			}
			Messenger.start();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}
	}

	public static void main(String args[]) {
		try {
			String host = "", room = "";
			int port = 0;
			String name = JOptionPane.showInputDialog("Enter Your Name");
			port = Integer.valueOf(JOptionPane.showInputDialog("Enter port"));
			if (name == null || name.equals("")) {
				JOptionPane.showMessageDialog(null, "Name cannot be blank");
				return;
			}

			// room = JOptionPane.showInputDialog("Name your chat room");
			ChatGUI obj = new ChatGUI(name, 0, host, port);
			obj.setVisible(true);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}
	}

	public void currentStatus(ChatInfoSend chatInfo) {
		try {

			Thread.sleep(100);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(chatInfo);
			byte[] data = outputStream.toByteArray();
			DatagramPacket pack = new DatagramPacket(data, data.length);

			if (currPort != maxPort) {
				pack.setAddress(InetAddress.getLocalHost());
				pack.setPort(currPort + 1);

				socket.send(pack);
			}
			if (currPort == maxPort) {

				pack.setAddress(InetAddress.getLocalHost());
				pack.setPort(minPort);

				socket.send(pack);

			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(pt, ex);
		}
	}

	public void broadcast(ChatInfoSend chatInfo) {
		try {
			System.out.println("Gossip");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(chatInfo);
			byte[] data = outputStream.toByteArray();
			DatagramPacket pack = new DatagramPacket(data, data.length);
			Random r = new Random();

			if (currPort != maxPort) {
				pack.setAddress(InetAddress.getLocalHost());
				pack.setPort(currPort + 1);

				socket.send(pack);
			}
			if (currPort == maxPort) {

				pack.setAddress(InetAddress.getLocalHost());
				pack.setPort(minPort);

				socket.send(pack);

			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(pt, ex);
		}
	}

	public void sendToHost(String str) {
		DatagramPacket pack = new DatagramPacket(str.getBytes(), str.length(), hostip, 37988);
		try {
			socket.send(pack);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(pt, "Sending to server failed");
		}
	}

	Thread Messenger = new Thread() {
		public void run() {
			try {
				while (true) {
					b = new byte[4096];
					DatagramPacket pkt = new DatagramPacket(b, 4096);
					socket.receive(pkt);
					byte[] data = pkt.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					socket.setSoTimeout(0);
					ChatInfoSend chatInfo = null;
					try {

						chatInfo = (ChatInfoSend) is.readObject();
						if (chatInfo.getType().contains("broadcast")) {

							if (statusPortMessageHashMap.get(chatInfo.getPort()) + 1 == chatInfo.getMessageId()) {
								statusPortMessageHashMap.put(chatInfo.getPort(), chatInfo.getMessageId());
								portIdMessageHaspMap = chatInfo.getStatusPortIdMessageHashMap();
								if (currPort != chatInfo.getPort()) {
									jTextArea1.setText(jTextArea1.getText() + "\n" + chatInfo.getPort() + ":"
											+ chatInfo.getTextMsg());
									broadcast(chatInfo);
								}

							} else if (statusPortMessageHashMap.get(chatInfo.getPort()) == chatInfo.getMessageId()) {

								System.out.println("duplicate message detected and dropped");

							}

						} else {

							ConcurrentHashMap<Integer, ConcurrentHashMap> receviedPortIdMessHashMap = chatInfo
									.getStatusPortIdMessageHashMap();
							ConcurrentHashMap<Integer, String> receviedIdMessHashMap = null;
							ConcurrentHashMap<Integer, Integer> receviedportMaxMessageId = null;
							receviedportMaxMessageId = chatInfo.getportMaxMessegeIdHashMap();
							Iterator it = portSet.iterator();
							while (it.hasNext()) {
								int port = (int) it.next();

								receviedIdMessHashMap = new ConcurrentHashMap<>();

								receviedIdMessHashMap = receviedPortIdMessHashMap.get(port);

								while (statusPortMessageHashMap.get(port) < receviedportMaxMessageId.get(port)) {
									System.out.println("Status Commit");
									// System.out.println( " currentPort:"
									// +currPort + " ---- "+ port +" : " +
									// receviedIdMessHashMap.get(statusPortMessageHashMap.get(port)+1));
									jTextArea1.setText(jTextArea1.getText() + "\n" + port + ":"
											+ receviedIdMessHashMap.get(statusPortMessageHashMap.get(port) + 1));
									statusPortMessageHashMap.put(port, statusPortMessageHashMap.get(port) + 1);
								}
								portIdMessageHaspMap.put(port, receviedIdMessHashMap);

							}

							// System.out.println("drop messege function");
							ChatInfoSend statusChatInfo = new ChatInfoSend("status", "status", messageId, hostip,
									currPort, portIdMessageHaspMap, statusPortMessageHashMap);
							currentStatus(statusChatInfo);

						}

					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

					if (mode == HOST_MODE) {

						/*
						 * if (s.contains("!!^^")) { Client temp = new Client();
						 * temp.ip = pkt.getAddress().getHostAddress();
						 * temp.port = pkt.getPort(); broadcast(s.substring(4,
						 * s.indexOf("^^!!")) + " joined.");
						 * ClientList.add(temp); s = "!!^^" + roomname + "^^!!";
						 * pkt = new DatagramPacket(s.getBytes(), s.length(),
						 * InetAddress.getByName(temp.ip), temp.port);
						 * socket.send(pkt); btn_send.setEnabled(true);
						 * txt_mymsg.setEnabled(true); } else {
						 * 
						 */
						// broadcast(s);
						// jTextArea1.setText(jTextArea1.getText() + "\n" +
						// chatInfo.getTextMsg());
					}

				}
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(pt, ex);
				System.exit(0);
			}
		}
	};
}