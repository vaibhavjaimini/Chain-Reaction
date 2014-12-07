import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Client extends JFrame implements Runnable, ActionListener{

	Socket socket;
	JTextField text;
	JButton submit;
	JLabel clientLabel, address;
	Scanner read;
	int noOfPlayers, id;
	public Client()  {
		draw();	
	}
	void draw() 
	{
		setLayout(null);
		clientLabel =  new JLabel("Client");
		address = new JLabel("Enter Address:");
		text = new JTextField(200);
		submit = new JButton("Start Server");
		clientLabel.setBounds(175, 50, 50, 30);
		address.setBounds(150, 100, 200, 30);
		text.setBounds(100, 150, 200, 30);
		submit.setBounds(100, 200, 200, 30);
		submit.addActionListener(this);
		add(clientLabel);
		add(address);
		add(text);
		add(submit);
		setVisible(true);
		setSize(400, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean start ;
		JLabel waiting = new JLabel("Waiting For More Players...");
		start = read.nextBoolean();
		submit.removeActionListener(this);
		if(start == false)
		{
			add(waiting);
		}
		waiting.setBounds(100, 250, 200, 30);
		
		SwingUtilities.updateComponentTreeUI(this);
		while(start!=true)
			start=read.nextBoolean();
		this.dispose();
		try {
			new ChainReaction(socket, noOfPlayers, id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == submit)
		{
			try {
				socket = new Socket(text.getText(), MyServer.port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				read = new Scanner(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = read.nextLine(), str[];
			str = temp.split(" ");
			noOfPlayers = Integer.parseInt(str[0]);
			id = Integer.parseInt(str[1]);
			new Thread(this).start();	
		}
	}
}
