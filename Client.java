/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author sanket
 */
public class Client extends JFrame{
    JTextField userText;
    JTextArea chat;
     ObjectOutputStream output;
    ObjectInputStream input;
    String message = "";
    String serverIP;
    Socket connection;
    public Client(String host)
    {
       super("Client");
       serverIP = host;
       userText = new JTextField();
       userText.setEditable(false);
       userText.addActionListener(
          new ActionListener()
          {
             public void actionPerformed(ActionEvent e)
             {
                 sendMessage(e.getActionCommand());
                 userText.setText("");
                 
             }
          }
       );
       add(userText,BorderLayout.NORTH);
       chat = new JTextArea();
       add(new JScrollPane(chat));
       setSize(300,150);
       setVisible(true);
    } 
    public void startRunning()
    {
       try{
           connectToServer();
          setupStreams();
          whileChatting();
       }catch(Exception e){
           showMessage("\n Nott Connected\n");
       }
       finally{closeCon();}
    }
    private void connectToServer()
    {
        try{
            showMessage("\n Attempting to connect....\n");
            connection = new Socket(InetAddress.getByName(serverIP),6789);
            showMessage(" Connected to "+ connection.getInetAddress().getHostName());
            
        
        }catch(Exception e){e.printStackTrace();}
    }
    private void setupStreams()
    {
         try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            showMessage("\n Streams are now set\n");       
        }catch(Exception e){e.printStackTrace();}
    }
    private void whileChatting()
    {
        typeOn(true);
          try{
              do{
                message = (String)input.readObject();
                showMessage("\n" + message);
                
                }while(!message.equals("CLIENT - END"));
        }catch(Exception e){e.printStackTrace();}
    }
    private void closeCon()
    {
        showMessage("\n closing connection\n");
        typeOn(false);
        try{
        output.close();
        input.close();
        connection.close();
        }catch(Exception e){e.printStackTrace();}
    }
    private void sendMessage(String message)
    {
       try
       {
          output.writeObject("CLIENT -"+ message);
          output.flush();
          showMessage("\nCLIENT - " + message);
       }catch(Exception e){chat.append("\n error\n");}
    
    }
      public void showMessage(String text)
    {
       SwingUtilities.invokeLater(
               new Runnable(){
       public void run()
       {
           chat.append(text);
       }
               } );       
    }
    private void typeOn(boolean tof)
    {
        SwingUtilities.invokeLater(
               new Runnable(){
       public void run()
       {
          userText.setEditable(tof);
       }
               } );   
    }
	  public static void main(String args[])
    {
         Client client;
		 /* enter the IP Address here*/
         client = new Client("127.0.0.1");
         client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         client.startRunning();
    
    }
}
