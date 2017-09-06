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
public class Server1 extends JFrame{
    JTextField userText;
    JTextArea chat;
   ObjectOutputStream output;
    ObjectInputStream input;
    ServerSocket server;
    Socket connection;
    public Server1()
    {
        super("Chat Room");
        userText = new JTextField();
        userText.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
              sendMessage(e.getActionCommand());
           userText.setText("");
          
           }       
        });
        add(userText,BorderLayout.NORTH);
        chat = new JTextArea();
        add(new JScrollPane(chat));
        setSize(300,150);
        setVisible(true);
    }
    public void startRunning()
    {
        try
        {
           server = new ServerSocket(6789,100);
           while(true){
               try{
                   waitForConnection();
                   setupStreams();
                   whileChatting();
                   
               }catch(Exception d){
                   d.printStackTrace();
               }finally{
                  closeCon();
               }
               
           }            
        }catch(Exception e){e.printStackTrace();
        }
       
    }
 private void waitForConnection()
        {
           try {
                showMessage("\n Waiting for someone...\n");
            connection = server.accept();
            showMessage("\n Now Connected to " + connection.getInetAddress().getHostName());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
            
        }  
    public void setupStreams()
    {
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            showMessage("\n Streams are now set\n");       
        }catch(Exception e){e.printStackTrace();}
    }
    public void whileChatting()
    {
        try{
        String message = "You are now connected ";
                sendMessage(message);
                do{
                message = (String)input.readObject();
                showMessage("\n" + message);
                
                }while(!message.equals("CLIENT - END"));
        }catch(Exception e){e.printStackTrace();}
        
    }
    public void closeCon()
    {
       showMessage("\n closing Connection\n");
       try
       {
           output.close();
           input.close();
           connection.close();       
       }catch(Exception e){e.printStackTrace();}
    
    }
    public void sendMessage(String message)
    {
       try
       {
           output.writeObject("SERVER - " + message);
           showMessage("\n server - " + message);
           
       }catch(Exception e){e.printStackTrace();}
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
     public static void main(String args[])
    {
        Server1 d = new Server1();
        d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        d.startRunning();     
    }
    /**
     * @param args the command line arguments
     */
  
    
}
