package guiPack;

import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class gui extends JFrame{
    JFrame frame = this;
    JPanel mainPanel,panel1,panel2,panel3;
    JLabel msgLabel,rcvLabel,cntLabel,sentMsgLabel,rcvMsgLabel,fullMsgLabel;
    JTextField rcvField, cntField;
    JTextArea sentMsgArea, rcvMsgArea, fullMsgArea;
    String msgType="";
    ArrayList<String>rcvList, msgList;
    Agent agentObj;
    JButton sendButton, cancelButton;
    JComboBox cb, receiver;

    public gui(Agent agent) {

        super(agent.getLocalName());
        rcvList=new ArrayList<>();
        msgList=new ArrayList<>();
        agentObj = agent;

        frame= new JFrame();
        //set Main panel
        mainPanel=new JPanel();
        mainPanel.setPreferredSize(new Dimension(800,800));
        mainPanel.setBackground(Color.BLACK);
        frame.getContentPane().add(mainPanel);

        //set sub panels:
        panel1=new JPanel(new GridLayout(1,2));
        panel1.setPreferredSize(new Dimension(780,700));
        //create two sub panels inside other sub panel
        panel2=new JPanel();
        panel2.setBackground(Color.PINK);
        panel1.add(panel2);
        //add msg
        msgLabel=new JLabel("Message");
        msgLabel.setPreferredSize(new Dimension(150,80));
        panel2.add(msgLabel);
        msgList.add("Agree");
        msgList.add("Confirm");
        msgList.add("Request");
        msgList.add("Ignore");
        cb = new JComboBox<String> ();
        cb.setPreferredSize(new Dimension(200,50));
        cb.setBorder(BorderFactory.createLineBorder(Color.green));
        for (int i = 0; i < msgList.size(); i++) {
            cb.addItem(msgList.get(i));
        }
        cb.setVisible(true);
        panel2.add(cb);

        /*
         String[] choices = { "Agree","Confirm", "Ignore","Request"};
        final JComboBox<String> cb = new JComboBox<String>(choices);
        cb.setVisible(true);
        panel2.add(cb);
         */

        //add receiver
        updateRcvr();
        rcvLabel=new JLabel("Receiver");
        rcvLabel.setPreferredSize(new Dimension(150,80));
        panel2.add(rcvLabel);
        receiver = new JComboBox<String>();
        receiver.setBorder(BorderFactory.createLineBorder(Color.green));
        receiver.setPreferredSize(new Dimension(200,50));
        for (int i = 0; i < rcvList.size(); i++) {
            receiver.addItem(rcvList.get(i));
        }
        receiver.setVisible(true);
        panel2.add(receiver);

        /*rcvField=new JTextField();
        rcvField.setEditable(true);
        rcvField.setPreferredSize(new Dimension(80,80));
        panel2.add(rcvField);*/

        //add content
        cntLabel=new JLabel("Content  ");
        cntLabel.setPreferredSize(new Dimension(150,80));
        panel2.add(cntLabel);
        cntField=new JTextField();
        cntField.setEditable(true);
        cntField.setBorder(BorderFactory.createLineBorder(Color.green));
        cntField.setPreferredSize(new Dimension(200,90));
        panel2.add(cntField);

        //add full conversation label and text
        fullMsgLabel=new JLabel("Full Conversation");
        fullMsgLabel.setPreferredSize(new Dimension(200,50));
        panel2.add(fullMsgLabel);
        fullMsgArea=new JTextArea();
        fullMsgArea.setEditable(true);
        fullMsgArea.setBorder(BorderFactory.createLineBorder(Color.green));
        fullMsgArea.setPreferredSize(new Dimension(300,390));
        panel2.add(fullMsgArea);

        panel3=new JPanel();
        panel3.setBackground(Color.lightGray);
        panel1.add(panel3);
        //add Sent MSG and RCV msg i panel3
        sentMsgLabel=new JLabel("Sent Message");
        sentMsgLabel.setPreferredSize(new Dimension(150,20));
        panel3.add(sentMsgLabel);
        sentMsgArea=new JTextArea();
        sentMsgArea.setEditable(true);
        sentMsgArea.setPreferredSize(new Dimension(300,300));
        sentMsgArea.setBorder(BorderFactory.createLineBorder(Color.red));
        panel3.add(sentMsgArea);

        //add rcv msg
        rcvMsgLabel=new JLabel("Received Message");
        rcvLabel.setPreferredSize(new Dimension(150,20));
        panel3.add(rcvMsgLabel);
        rcvMsgArea=new JTextArea();
        rcvMsgArea.setEditable(true);
        rcvMsgArea.setPreferredSize(new Dimension(300,300));
        rcvMsgArea.setBorder(BorderFactory.createLineBorder(Color.red));
        panel3.add(rcvMsgArea);

        //panel1.setPreferredSize(new Dimension(380,320));
        panel1.setBackground(Color.WHITE);
        panel1.setBorder(BorderFactory.createLineBorder(Color.green));
        mainPanel.add(panel1);

        sendButton=new JButton("Send");
        sendButton.setPreferredSize(new Dimension(240,60));
        sendButton.setBackground(Color.white);
        sendButton.setVisible(true);
        mainPanel.add(sendButton);

        cancelButton=new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(240,60));
        cancelButton.setBackground(Color.white);
        cancelButton.setVisible(true);
        mainPanel.add(cancelButton);

        fullMsgArea.setEditable(false);
        rcvMsgArea.setEditable(false);
        sentMsgArea.setEditable(false);

        //set the frame size
        frame.setSize(new Dimension(800,800));
        //set relative start position
        frame.setLocationRelativeTo(null);
        //set default closing option
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set a frame title

        //disable resize option
        frame.setResizable(false);
        frame.setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg=cntField.getText().trim();

                for (int i = 0; i < msgList.size(); i++) {
                   if(cb.getSelectedItem().equals(msgList.get(i)))
                       msgType=msgList.get(i);
                }

             //   msgType=((ComboBoxModel<String>) msgList).getSelectedItem().toString();

                agentObj.allMessage(msgType, receiver.getSelectedItem().toString(),msg);
                cntField.setText("");

                //default method of jade, capture the button click
                GuiEvent guiEvent=new GuiEvent(this,1);
                agentObj.postGuiEvent(guiEvent);
            }
        });
    }

    public void setTitle(Agent agent)
    {
        Date objDate = new Date(); // Current System Date and time is assigned to objDate
        String strDateFormat = "dd-MMM-yyyy"; //Date format is Specified
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        //Date format string is passed as an argument to the Date format object

        String title=agent.getLocalName();
        frame.setTitle(title + " " + objSDF.format(objDate));
        return;
    }

    //check if agent is already in the list, if not add in the list
    public void updateRcvr() {
        for(String name :agentObj.allAgents)
        {
            if(agentObj.getLocalName().equals(name) || rcvList.contains(name))
                continue;
            rcvList.add(name);
            receiver.addItem(name);
        }
    }
}