import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class ChainReaction extends JFrame implements MouseListener, Runnable{
	Socket socket;
	PrintStream ps;
	Scanner read;
	int turn, noOfPlayers, id, winnerId;
	Color player[] = new Color[8];
	JPanel grid;
	JLabel yourColor, values[][]= new JLabel[8][6], playerId;
	JPanel blocks[][] = new JPanel[8][6];
	boolean gameOver, winner, hasPlayedOnce, allHavePlayedOnce, hasSent;
	public ChainReaction(Socket socket, int noOfPlayers, int id) throws IOException {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		player[0] = new Color(255, 0, 0);
		player[1] = new Color(0, 255, 0);
		player[2] = new Color(0, 0, 255);
		player[3] = new Color(0, 255, 255);
		player[4] = new Color(255, 0, 255);
		player[5] = new Color(255, 255, 0);
		player[6] = new Color(141, 27, 27);
		player[7] = new Color(19, 27, 62);
		this.noOfPlayers = noOfPlayers;
		this.id = id;
		hasPlayedOnce = false;
		allHavePlayedOnce = false;
		ps = new PrintStream(socket.getOutputStream());
		read = new Scanner(socket.getInputStream());
		System.out.println("No Of Players:" + noOfPlayers);
		System.out.println("Id:" + id);
		draw();
		new Thread(this).start();
	}
	void draw()
	{
		int i, j;
		grid = new JPanel();
		grid.setLayout(new GridLayout(8, 6));
		setLayout(null);
		for(i=0; i<8; i++)
			for(j=0; j<6; j++)
			{
				values[i][j] = new JLabel("");
				values[i][j].setFont(new Font("font", Font.BOLD, 20));
				blocks[i][j] = new JPanel();
				blocks[i][j].add(values[i][j]);
				blocks[i][j].setSize(50, 50);
				blocks[i][j].setBackground(Color.BLACK);
				if(id == 0)
				blocks[i][j].addMouseListener(this);
				
				blocks[i][j].setBorder(BorderFactory.createLineBorder(player[0]));
				grid.add(blocks[i][j]);
			}
		yourColor = new JLabel("This Is Your Color");
		playerId = new JLabel("Player " + (id+1));
		playerId.setBounds(115, 0, 200, 30);
		add(playerId);
		yourColor.setForeground(player[id]);
		add(yourColor);
		yourColor.setBounds(75, 10, 200, 50);
		add(grid);
		grid.setBounds(0, 50, 300, 400);
		setVisible(true);
		setResizable(false);
		setSize(300, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int temp, i, j;
		for(i=0; i<8; i++)
		for(j=0; j<6; j++)
			if(blocks[i][j] == arg0.getSource()){
				if(values[i][j].getText().equals("") && !hasSent)
				{
					values[i][j].setText("0");
					values[i][j].setForeground(player[turn]);
				}
				if(values[i][j].getForeground() == player[turn] && !hasSent)
				{
					hasPlayedOnce = true;
					ps.println(i+" "+j);
					hasSent = true;
				}
			}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	boolean chkGameOver()
	{
		int i, j, nonZero=0, players=0;
		for(i=0; i<8; i++)
		for(j=0; j<6; j++)
		{
			if(!values[i][j].getText().equals(""))
			{
				nonZero++;
				if(values[i][j].getForeground() == player[id])
					players++;
			}
		}
		winner = false;
		if(players == 0)
			return true;
		if(players == nonZero)
			winner = true;
		return false;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i, j, nextTurn;
		while(true)
		{
			String str, temp[];
			if(read.hasNext())
			{
				str = read.nextLine();
				System.out.println(str);
				temp = str.split(" ");
				i=Integer.parseInt(temp[0]);
				j=Integer.parseInt(temp[1]);
				bfs(i, j);
				
				if(hasPlayedOnce){
					if(chkGameOver()){
						ps.println("Game Over");
						yourColor.setText("Game Over :(");
					}
					else
						ps.println("Game Not Over");
				}
				else
					ps.println("Game Not Over");
				if(id == turn)
					ps.println("Calculate Turn");
				str = read.nextLine();
				turn = Integer.parseInt(str);
				if(id == turn )
					hasSent = false;
				for(i=0; i<8; i++)
				for(j=0; j<6; j++)
				{
					blocks[i][j].setBorder(BorderFactory.createLineBorder(player[turn]));
					if(id != turn)
						blocks[i][j].removeMouseListener(this);
					else
						blocks[i][j].addMouseListener(this);
				}
			}
		}
	}
	
	
	void bfs(int i, int j)
	{
		class Node
		{
			int i, j;
			public Node(int i, int j) {
				// TODO Auto-generated constructor stub
				this.i = i;
				this.j = j;
			}
		}
		int temp;
		Vector<Node> queue = new Vector();
		queue.add(new Node(i, j));
		
		while(queue.size() != 0)
		{
			Node topNode = queue.get(0);
			if(topNode.i < 0 || topNode.i > 7 || topNode.j < 0 || topNode.j > 5)
			{
				queue.remove(0);
				continue;
			}
			values[topNode.i][topNode.j].setForeground(player[turn]);
			if(values[topNode.i][topNode.j].getText().equals(""))
				values[topNode.i][topNode.j].setText("0");
			temp = Integer.parseInt(values[topNode.i][topNode.j].getText());
			temp++;
			values[topNode.i][topNode.j].setText(""+temp);
			Thread t = new Thread(){
				public void run() {
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			};
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(allHavePlayedOnce){
				if(id == turn)
				{
					chkGameOver();
					if(winner)
						ps.println(id);
					else
						ps.println(-1);
				}
				String str = read.nextLine();
				winnerId = Integer.parseInt(str);
				if(winnerId != -1)
				{
					JOptionPane.showMessageDialog(this,"Player " + (winnerId+1) + " wins");
					this.dispose();
					ps = null;
					System.exit(0);
					return;
				}
			}
			else if(turn == noOfPlayers-1)
				allHavePlayedOnce = true;
			queue.remove(0);
			if(chkExplode(topNode.i, topNode.j))
			{
				values[topNode.i][topNode.j].setText("");
				queue.add(new Node(topNode.i-1, topNode.j));
				queue.add(new Node(topNode.i+1, topNode.j));
				queue.add(new Node(topNode.i, topNode.j-1));
				queue.add(new Node(topNode.i, topNode.j+1));
			}
		}
	}
	boolean chkExplode(int i, int j)
	{
		int deg=0, val = Integer.parseInt(values[i][j].getText());
		if(i == 0 || i == 7)
			deg++;
		if(j == 0 || j == 5)
			deg++;
		if(4-deg == val)
			return true;
		return false;
	}
}
