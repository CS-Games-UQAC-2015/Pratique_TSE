import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;

public class Serveur 
{
	AsynchronousServerSocketChannel socketServeur;
	AsynchronousSocketChannel[] clients = new AsynchronousSocketChannel[5];
	
	public Serveur()
	{
		socketServeur = null;
		for(int i=0;i<5;i++)
		{
			clients[i] = null;
		}
		
		start();
	}
	
	public void start()
	{
		try
		{
			socketServeur = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(1234));
			//addText("Serveur demarrer");
			socketServeur.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>()
					{

						@Override
						public void completed(AsynchronousSocketChannel arg0,
								Void arg1) {
							// TODO Auto-generated method stub
							socketServeur.accept(null, this);
							
							addClient(arg0);
						}

						@Override
						public void failed(Throwable arg0, Void arg1) {
							// TODO Auto-generated method stub
							//addText("Echec d'ajout du client");
						}
				
					});
		}
		catch(IOException ex)
		{
			//addText("Echec du demarrage");
		}
	}
	
	public void readClient(AsynchronousSocketChannel socket)
	{
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		Object[] packet = new Object[] {socket, buffer};
		socket.read(buffer, packet, new CompletionHandler<Integer,Object[]>()
				{

					@Override
					public void completed(Integer result, Object[] attachment) {
						// TODO Auto-generated method stub
						((ByteBuffer)attachment[1]).flip();
						String message = StandardCharsets.US_ASCII.decode(((ByteBuffer)attachment[1])).toString();
						handleMessage((AsynchronousSocketChannel)attachment[0], message);
					}

					@Override
					public void failed(Throwable exc, Object[] attachment) {
						// TODO Auto-generated method stub
						//addText("Erreur de reception");
					}
			
				});
	}
	
	public void addClient(AsynchronousSocketChannel socket)
	{
		boolean added = false;
		for(int i=0;i<5;i++)
		{
			if(!added && clients[i] != null)
			{
				clients[i] = socket;
			}
		}
		
		readClient(socket);
	}
	
	public void handleMessage(AsynchronousSocketChannel socket, String message)
	{
		addText(message);
		String[] commands = message.split(" ");
		String commande = commands[0];
		String result = "";
		
		if(commande.equals("cd"))
		{
			result = cd(commands[1]);
		}
		if(commande.equals("ls"))
		{
			result = ls(commands[1]);
		}
		if(commande.equals("mkdir"))
		{
			result = mkdir(commands[1]);
		}
		if(commande.equals("rm"))
		{
			result = rm(commands[1]);
		}
		if(commande.equals("cat"))
		{
			result = cat(commands[1]);
		}
		if(commande.equals("pwd"))
		{
			result = pwd();
		}
		if(commande.equals("cp"))
		{
			result = cp(commands[1], commands[2]);
		}
		if(commande.equals("mv"))
		{
			result = mv(commands[1], commands[2]);
		}
		if(commande.equals("touch"))
		{
			result = touch(commands[1]);
		}
		
		sendMessageToClient(socket, result);
		
		readClient(socket);
	}
	
	public void sendMessageToClient(AsynchronousSocketChannel socket, String message)
	{
		ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
		
		socket.write(buffer,buffer,new CompletionHandler<Integer,ByteBuffer>(){

			@Override
			public void completed(Integer arg0, ByteBuffer arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void failed(Throwable arg0, ByteBuffer arg1) {
				// TODO Auto-generated method stub
				//addText("Echec de l'envoi au client");
			}
			
		});
	}
	
	public void addText(String text)
	{
		System.out.println(text);
	}
	
	public String cd(String dossier)
	{
		return "cd";
	}
	
	public String ls(String dossier)
	{
		return "ls";
	}
	
	public String mkdir(String dossier)
	{
		return "mkdir";
	}
	
	public String rm(String dossier)
	{
		return "rm";
	}
	
	public String cat(String dossier)
	{
		return "cat";
	}
	
	public String pwd()
	{
		return "pwd";
	}
	
	public String cp(String source, String destination)
	{
		return "cp";
	}
	
	public String mv(String source, String destination)
	{
		return "mv";
	}
	
	public String touch(String dossier)
	{
		return "touch";
	}
}
