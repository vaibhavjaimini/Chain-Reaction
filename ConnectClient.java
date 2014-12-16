import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class ConnectClient extends Thread{

	Socket socket;
	Scanner read;
	PrintStream ps;
	int id;
	public ConnectClient(Socket socket, int id) throws IOException {
		// TODO Auto-generated constructor stub
		this.socket=socket;
		read = new Scanner(socket.getInputStream());
		ps = new PrintStream(socket.getOutputStream());
		this.id = id;
		
		start();
	}
	
	@Override
	public void run() {
		while(!MyServer.start){
			try {
				ps = new PrintStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ps.println(false);
		}
		ps.println(true);
		while(true)
		{
			String str;
			PrintStream out;
			if(read.hasNext()){
				str=read.nextLine();
				if(str.equals("Game Over")){
					MyServer.gameOver[id] = true;
					MyServer.hasSentStatus[id] = true;
					System.out.println(id + " Game Over");
					continue;
				}
				else if( str.equals("Game Not Over")){
					System.out.println(id + " Game Not Over");
					MyServer.hasSentStatus[id] = true;
					continue;
				}
				else if(str.equals("Calculate Turn"))
				{
					while(!MyServer.allHaveSentStatus){
						MyServer.allHaveSentStatus = true;
							for(int i=0; i<MyServer.hasSentStatus.length; i++)
							if(!MyServer.hasSentStatus[i]){
								MyServer.allHaveSentStatus = false;
								break;
							}
					}
					MyServer.allHaveSentStatus = false;
					for(int i=0; i<MyServer.hasSentStatus.length; i++)
						MyServer.hasSentStatus[i] = false;
					calcTurn();
					continue;
				}
				for(int i=0; i<MyServer.connectedSocks.size(); i++)
				{
					try {
						out = new PrintStream(MyServer.connectedSocks.get(i).getOutputStream());
						out.println(str);
						out.flush();
					} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
			else
			{
				for(int i=0; i<MyServer.connectedSocks.size(); i++)
					if(MyServer.connectedSocks.get(i) == socket)
					{
						MyServer.connectedSocks.remove(i);
					}
//				System.out.println(MyServer.connectedSocks.size());
				if(MyServer.connectedSocks.size() == 0)
						MyServer.close = true;
				MyServer.addFeed("Client " + (id+1) + " Has Left");
			//	System.out.println(MyServer.close);
				break;
			}
		}
	}
	
	void calcTurn()
	{
		MyServer.turn = (MyServer.turn + 1) % MyServer.noOfPlayers;
		while(MyServer.gameOver[MyServer.turn])
			MyServer.turn = (MyServer.turn + 1) % MyServer.noOfPlayers;
		
		System.out.println(MyServer.turn);
		PrintStream out;
		for(int i=0; i<MyServer.connectedSocks.size(); i++)
		{
			try {
				out = new PrintStream(MyServer.connectedSocks.get(i).getOutputStream());
				out.println(MyServer.turn);
				out.flush();
			} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
