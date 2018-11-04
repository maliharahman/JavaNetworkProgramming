package guiPack;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Agent extends GuiAgent {

    //string for message,type,content and receiver;
    String convo = "", receiver = "", content = "", type = "", sender = "";
    //for storing all agents
    ArrayList<String> allAgents;
    //object for gui class
    public gui callGui;

    public void setup() {

        //create arraylist for having all the agents in a list
        allAgents = new ArrayList();
        //check if new agent is added
        Behaviour loop;

        loop = new TickerBehaviour( this, 1000 )
        {
            protected void onTick() {
                updateAgent();
            }
        };

        addBehaviour(loop);

        callGui = new gui(this);
        callGui.setTitle(this);

        //register with DF, agents wish to advertise their services register with DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Agent");
        sd.setName(getLocalName() + "Agent Local Name");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new messageRcv());
    }

    private void updateAgent() {
        //sent request to AMS to have all the agent names
        //agentDesc has all the names of agents
        AMSAgentDescription[] agentDesc = null;
        try {
            SearchConstraints search = new SearchConstraints();
            search.setMaxResults(new Long(-1));
            agentDesc = AMSService.search(this, new AMSAgentDescription(), search);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        //showing the result of the request sent to AMS
        for (int i = 0; i < agentDesc.length; ++i) {
            AID agent = agentDesc[i].getName();

            //check if it is any new agent other than regular ams,rms and df
            if (!agent.getLocalName().equals("ams") && !agent.getLocalName().equals("rma") && !agent.getLocalName().equals("df"))
                allAgents.add(agent.getLocalName());
        }
        callGui.updateRcvr();
    }

    private class messageRcv extends CyclicBehaviour {
        String msg="", sendMsgTo="";
        public void action() {
            ACLMessage aclMsg = receive();
            //msg can be null
            if (aclMsg != null) {
                type = aclMsg.getPerformative(aclMsg.getPerformative());
                msg = aclMsg.getContent();
                sendMsgTo = aclMsg.getSender().getLocalName();
                // add date and time
                Date objDate = new Date(); // Current System Date and time is assigned to objDate
                String strDateFormat = "hh:mm:ss"; //Date format is Specified
                SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
                convo=objSDF.format(objDate)+"--TYPE:" + type + "--FROM:" +  sendMsgTo + "\n"+msg + "\n-- ---- -- -- -- -- ----\n";
                callGui.rcvMsgArea.append(convo);
                callGui.fullMsgArea.append(convo);
            }
        }
    }

    @Override
    protected void onGuiEvent(GuiEvent arg0) {
        addBehaviour(new messageSend());
    }

    private class messageSend extends OneShotBehaviour {
        public void action()
        {
            ACLMessage aclMsg = null;
            if (type.equalsIgnoreCase("AGREE")) {
                aclMsg = new ACLMessage(ACLMessage.AGREE);
            } else if (type.equalsIgnoreCase("CONFIRM")) {
                aclMsg = new ACLMessage(ACLMessage.CONFIRM);
            } else if (type.equalsIgnoreCase("INFORM")) {
                aclMsg = new ACLMessage(ACLMessage.INFORM);
            } else if (type.equalsIgnoreCase("REQUEST")) {
                aclMsg = new ACLMessage(ACLMessage.REQUEST);
            }
            aclMsg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
            aclMsg.setLanguage("English");
            aclMsg.setContent(content);
            send(aclMsg);
            Date objDate = new Date(); // Current System Date and time is assigned to objDate
            String strDateFormat = "hh:mm:ss"; //Date format is Specified
            SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
            convo=objSDF.format(objDate)+ "--TYPE:" + type + "--TO:" + receiver + "\n"+aclMsg.getContent() + "\n-- -- --- - --- -- ---\n";
            callGui.sentMsgArea.append(convo);
            callGui.fullMsgArea.append(convo);
        }
    }

    public void allMessage(String msgType,String sendTo, String msgContent)
    {
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                type=msgType;
                receiver=sendTo;
                content=msgContent;
            }
        });
    }
}