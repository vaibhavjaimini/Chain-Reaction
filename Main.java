import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Main extends JFrame implements ActionListener{

	JLabel chainReactionTitle;
	JButton createServer, join;
	public Main() throws IOException {
		// TODO Auto-generated constructor stub
		setLayout(null);
		chainReactionTitle = new JLabel("Chain Reaction");
		createServer = new JButton("Create New Server");
		join = new JButton("Join");
		add(chainReactionTitle);
		chainReactionTitle.setBounds(150, 50, 200, 30);
		add(createServer);
		createServer.setBounds(100, 150, 200, 30);
		createServer.setFocusable(false);
		createServer.setBorder(BorderFactory.createRaisedBevelBorder());
		add(join);
		join.setBorder(BorderFactory.createRaisedBevelBorder());
		join.setBounds(100, 200, 200, 30);
		join.setFocusable(false);
		createServer.addActionListener(this);
		join.addActionListener(this);
		setVisible(true);
		setSize(400, 500);
		//setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Main();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == createServer)
		{
			this.dispose();
			MyServer myServer = new MyServer(); 
		}
		if(arg0.getSource() == join)
		{
			this.dispose();
			Client client = new Client();
			
		}
	}

}
