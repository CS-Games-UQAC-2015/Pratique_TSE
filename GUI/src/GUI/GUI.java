package GUI;
import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;
	private static JTextField sender;
	private static JTextField receiver;

	public GUI() {
		super("Super awesome remote terminal simulator 2015");
		
		WindowListener listener = new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		};
		
		addWindowListener(listener);
		setSize(200,100);
		setVisible(true);
	}
	
	
	private void connect() {
		//Client.connect();
	} 
	private void sendText(String text){
		//Client.sendMessage(text);
		//addText(text);
	}
	
	public void addText(String text){
		receiver.setText(receiver.getText() + "\n" + text + "\n");
	}
	
	
	private void createGUI(){
		JFrame frame = new GUI();
		frame.setSize(700,500);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.black);
		
		//Sender
		sender = new JTextField(">", 60);
		sender.setBackground(Color.black);
		sender.setForeground(Color.white);
		//textField1.set
		sender.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	String text = sender.getText();
            	sendText(text);
            }});
		panel.add(sender);
		
		//Receiver
		receiver = new JTextField("", 60);
		receiver.setPreferredSize(new Dimension(200, 200));
		receiver.setBackground(Color.black);
		receiver.setForeground(Color.white);
		receiver.setEnabled(false);
		panel.add(receiver);
		
		//Connect
		JButton button = new JButton("Connect");
		button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		           connect();
		    }
		});
		panel.add(button);
		
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}
	
	public static void main(String [] args){
		GUI gui = new GUI();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui.createGUI();
			}
		});
	}
}
