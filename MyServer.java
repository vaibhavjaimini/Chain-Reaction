import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class MyServer extends JFrame implements ActionListener{
	
	static Vector<Socket> connectedSocks;
	JLabel serverLabel;
	JLabel players;
	JTextField text;
	JButton submit;
	ServerSocket server;
	static int noOfPlayers, port = 2303, turn;
	static boolean start=false, gameOver[], close;
	public MyServer() {
		setLayout(null);
		turn = 0;
		connectedSocks = new Vector();
		close = false;
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
		setVisible(false);
		System.out.println("Server Started...");
		waitForClients();
	}
	void waitForClients() throws IOException
	{
		int id=0;
		while(true)
		{
			Socket socket = server.accept();
			if(id < noOfPlayers){
				gameOver[id] = false;
				connectedSocks.add(socket);
				PrintStream out = new PrintStream(socket.getOutputStream());
				out.println(noOfPlayers+" "+id);
				ConnectClient client = new ConnectClient(socket, id);
				System.out.println(id + " Client Has Joined");
				id++;
			}
			else
				System.out.println("No More Players...");
			if(id == noOfPlayers)
			{
				start=true;
				while(!close)
				{
					int temp=0;
				}
				System.out.println("Server Shutting Down...");
				server.close();
				System.exit(0);
			}
			System.out.println("Running Infinitely");
		}
	}
}
