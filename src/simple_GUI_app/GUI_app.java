package simple_GUI_app;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GUI_app {
    private JPanel panel_main;
    private JPanel panel_input;
    private JTextField input_field;
    private JLabel input_label;
    private JButton button_submit;
    private JButton button_loop;
    private JTextArea output_text;
    private JTextField loop_text;
    private JLabel output_field;
    private int count=0;


    public GUI_app() {
        button_submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                output_text.setText(input_field.getText());
                output_text.setEditable(false);

                exec_cmd();

            }
        });
        button_loop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loop_text.setEditable(false);
                exec_loop();
            }
        });
    }

    public void exec_loop()
    {
        String loop_input=input_field.getText();
        output_text.setText(input_field.getText());

        int loop=Integer.parseInt(loop_input);
        try
        {
            my_thread t1 = new my_thread(loop);
            t1.start();
        }catch(Exception e1)
        {
            System.out.println(e1.toString());
        }
    }

    public void exec_cmd()
    {
        String user_input=output_text.getText();
        String[] cmd={"/bin/sh", "-c", user_input};
        try
        {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader input=new BufferedReader(new InputStreamReader(process.getInputStream()));
            String read_input=null;
            BufferedReader error_input=new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String error=null;

            while((read_input=input.readLine())!=null)
            {
                output_text.append("\n" + read_input);
            }


            while((error=error_input.readLine())!=null)
            {
                output_text.append("\n Error message: " + error);
            }
        }catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Input output Exception");
        }catch(Exception e2)
        {
            e2.toString();
            System.out.println("Other type of Exception");
        }

        loop_text.setText("Number of command/s entered: " + ++count);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI App");
        frame.setPreferredSize(new Dimension(800,800));
        frame.setContentPane(new GUI_app().panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
