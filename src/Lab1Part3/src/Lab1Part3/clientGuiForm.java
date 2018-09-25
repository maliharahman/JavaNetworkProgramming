package Lab1Part3;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class clientGuiForm {

    private JPanel panel_main;
    private JTextField input_field;
    private JButton command_button;
    private JButton loop_button;
    private JPanel panel_input;
    private JLabel input_label;
    private JLabel output_label;
    private JTextField uri_field;
    private JLabel uri_label;
    private JTextArea output_text;
    public String eventType=null;

    public clientGuiForm()
    {
        command_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String user_input=input_field.getText();
                String user_uri=uri_field.getText();
                input_field.setText("salam");

                System.out.println("Client gui received cmd: " + output_text.getText());
                eventType="command";
                ClientSocket sendCmd=new ClientSocket(user_input,user_uri);
                sendCmd.start();
                try{
                    TimeUnit.SECONDS.sleep(1);

                }catch (Exception e1)
                {e1.printStackTrace();}
                output_text.setText(Data.sth);
                //output_text.setText(sendCmd.);
                System.out.println("Client gui received cmd: " + output_text.getText());
            }
        });

        loop_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                eventType="loop";
            }
        });
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Client GUI form");
        frame.setPreferredSize(new Dimension(800,800));
        frame.setContentPane(new clientGuiForm().panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}