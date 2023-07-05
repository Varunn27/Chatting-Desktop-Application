import java.util.*;
import java.net.*;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;


public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("Sending requests to server");
            socket = new Socket("127.0.0.1", 7778);
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
           
            createGUI();
            handleEvents();
            startReading();
           
            //startWriting();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
              
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
               
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key Released" +e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("You have pressed enter");
                    String contentToSend = messageInput.getText(); 
                    messagArea.append("ME:" + contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
            
        });
    }

    private void createGUI() {

        this.setTitle("Client Messanger[End]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // coding for components
        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("download.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messagArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);


        this.setLayout(new BorderLayout());

        // adding the components to the frame

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jscrollPane = new JScrollPane(messagArea);
        this.add(jscrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);


        this.setVisible(true);

    }

    // Reading
    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader Started");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {

                        System.out.println("Server Terminated The Chat");
                        JOptionPane.showMessageDialog(this,"Server Terminated the Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server : " + msg);
                    messagArea.append("Server : " + msg+ "\n");
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is Closed");
            }

        };
        new Thread(r1).start();

    }
    // Reading End

    // Writing
    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer Started..");
            try {
                while (!socket.isClosed()) { // if error occurs

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }
                System.out.println("Connection is Closed");
            } catch (Exception e) {
                e.printStackTrace();

            }

        };
        new Thread(r2).start();

    }

    // Writing End
    public static void main(String[] args) {

        System.out.println("Client side");
        new Client();
    }
}
