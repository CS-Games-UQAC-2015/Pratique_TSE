import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;

public class Client 
{
	AsynchronousSocketChannel socket;
	String ip = "127.0.0.1";
	
	public Client()
	{
		socket = null;
		connect();
	}
	
	public void connect()
	{
		Thread demarrer = new Thread(new Demarrer());
		demarrer.run();
	}
	
	public class Demarrer extends Thread
	{
		public void run()
		{
			try
			{
				socket = AsynchronousSocketChannel.open();
			}
			catch(IOException ex)
			{
				addText("Impossible de creer le socket");
			}
			
			if(socket != null)
			{
				socket.connect(new InetSocketAddress(ip,1234),null,new CompletionHandler<Void,Void>()
						{
	
							@Override
							public void completed(Void arg0, Void arg1) {
								// TODO Auto-generated method stub
								addText("Connection etablie");
								
								read(socket);
								
								sendMessage("cd allo/bonjour");
							}
	
							@Override
							public void failed(Throwable arg0, Void arg1) {
								// TODO Auto-generated method stub
								addText("Impossible de se connecter au serveur");
							}
					
						});
			}
		}
	}
	
	public void read(AsynchronousSocketChannel socket)
	{
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		socket.read(buffer, buffer, new CompletionHandler<Integer,ByteBuffer>()
				{

					@Override
					public void completed(Integer result, ByteBuffer attachment) {
						// TODO Auto-generated method stub
						attachment.flip();
						String message = StandardCharsets.US_ASCII.decode(attachment).toString();
						handleMessage(message);
					}

					@Override
					public void failed(Throwable exc, ByteBuffer attachment) {
						// TODO Auto-generated method stub
						addText("Erreur de reception");
					}
			
				});
	}
	
	public void handleMessage(String message)
	{
		addText("Serveur: " + message);
		
		read(socket);
	}
	
	public void sendMessage(String message)
	{
		ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
		Object[] packet = new Object[] {socket, buffer};
		
		socket.write(buffer, packet, new CompletionHandler<Integer,Object[]>(){

			@Override
			public void completed(Integer result, Object[] attachment) {
				// TODO Auto-generated method stub
				addText(message);
			}

			@Override
			public void failed(Throwable exc, Object[] attachment) {
				// TODO Auto-generated method stub
				addText("Echec de l'envoi de la commande");
			}	
		});
	}
	
	public void addText(String text)
	{
		System.out.print(text += "\n");
	}
}
