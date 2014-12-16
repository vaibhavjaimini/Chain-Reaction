import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class MyServer extends JFrame implements ActionListener, Runnable{
	
	static Vector<Socket> connectedSocks;
	static Vector<String> feeds;
	JLabel serverLabel;
	JLabel players;
	JLabel serverAddress;
	JTextField text;
	JButton submit;
	JScrollPane scrollPane;
	JList<String> list;
	static JFrame serverFrame;
	ServerSocket server;
	static int noOfPlayers, port = 2303, turn;
	static boolean start=false, gameOver[], close, allHaveSentStatus, hasSentStatus[];
	public MyServer() {
		setLayout(null);
		turn = 0;
		connectedSocks = new Vector();
		feeds = new Vector();
		close = false;
		list = new JList(feeds);
		scrollPane = new JScrollPane(list);
		serverLabel = new JLabel("Server");
		players = new JLabel("Enter Number Of Players (2-8):");
		text = new JTextField(2);
		submit = new JButton("Submit");
		add(serverLabel);
		serverLabel.setBounds(175, 50, 50, 20);
		add(players);
		players.setBounds(20, 100, 250, 20);
		add(text);
		text.setBounds(300, 100, 50, 20);
		add(submit);
		submit.setBounds(150, 150, 100, 30);
		submit.addActionListener(this);
		setVisible(true);
	//	setResizable(false);
		setSize(400, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == submit)
		{
			noOfPlayers = Integer.parseInt(text.getText());
			if(noOfPlayers < 2 || noOfPlayers > 8)
				JOptionPane.showMessageDialog(this,"Number of Players Must Be Between 2 and 8");
			else
				try {
					gameOver = new boolean[noOfPlayers];
					hasSentStatus = new boolean[noOfPlayers];
					allHaveSentStatus = false;
					for(int i=0; i<hasSentStatus.length; i++)
						hasSentStatus[i] = false;
					startServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	void startServer() throws IOException
	{
		server = new ServerSocket(MyServer.port);
		this.dispose();
		serverGUI();
	//	System.out.println("Server Started...");
		MyServer.addFeed("Server Started...");
		new Thread(this).start();
	}
	void serverGUI() throws UnknownHostException
	{		
		serverFrame = new JFrame();
		serverFrame.setLayout(null);
		serverFrame.add(serverLabel);
		serverAddress = new JLabel(server.getInetAddress().getHostAddress());
		serverFrame.add(serverAddress);
		serverAddress.setBounds(175, 100, 200, 30);
		scrollPane.setBounds(100, 150, 200, 250);
		serverFrame.add(scrollPane);
		serverFrame.setVisible(true);
		//serverFrame.setResizable(false);
		serverFrame.setSize(400, 500);
		serverFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	static void addFeed(String feed)
	{
		feeds.add(feed);
		SwingUtilities.updateComponentTreeUI(serverFrame);
	}
	void waitForClients() throws IOException
	{
		int id=0;
		while(true)
		{
			MyServer.addFeed("Waiting For " + (noOfPlayers - id) + " More Player(s)");
			Socket socket = server.accept();
			if(id < noOfPlayers){
				gameOver[id] = false;
				connectedSocks.add(socket);
				PrintStream out = new PrintStream(socket.getOutputStream());
				out.println(noOfPlayers+" "+id);
				ConnectClient client = new ConnectClient(socket, id);
				MyServer.addFeed("Client " + (id+1) + " Has Joined");
				//System.out.println(id + " Client Has Joined");
				id++;
			}
			else
				System.out.println("No More Players...");
			if(id == noOfPlayers)
			{
				start=true;
				MyServer.addFeed("Game Started...");
				while(!close)
				{
					int temp=0;
				}
				MyServer.addFeed("Server Shutting Down...");
			//	System.out.println("Server Shutting Down...");
				server.close();
				System.exit(0);
			}
			System.out.println("Running Infinitely");
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			waitForClients();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
