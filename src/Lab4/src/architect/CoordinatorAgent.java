package architect;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class CoordinatorAgent extends GuiAgent {
	private static final long serialVersionUID = 1L;
	private CoordinatorGUI coordinatorGUI;

	private String messageType = "";
	private String serverName = "";
	private String serverPort = "";
	private String tickerDuration = "";
	private String noOfAgents = "";
	private String statusMessagesText = "";

	private String payload;

	public ArrayList<AID> remoteBrokers;

	public ArrayList<String> agentList;
	public static int agentCounterInitial = 0;
	public static int agentCounterFinal = 0;

	protected void setup() {

		System.out.println("Coordinator agent " + getAID().getName() + " is ready.");

		agentList = new ArrayList<String>();
		refreshActiveAgents();

		remoteBrokers = new ArrayList<AID>();

		coordinatorGUI = new CoordinatorGUI(this);
		coordinatorGUI.displayGUI();

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("coordinator-agent");
		sd.setName(getLocalName() + "-Coordinator agent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new ReceiveMessage());

	}

	protected void takeDown() {

		if (coordinatorGUI != null) {
			coordinatorGUI.dispose();
		}

		System.out.println("Coordinator Agent " + getAID().getName() + " is terminating.");

		try {
			DFService.deregister(this);
			System.out.println("Agent " + getAID().getName() + " has been signed off.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public class SendMessage extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;
		public void action() {

			payload = messageType + " " + serverName + " " + serverPort + " " + tickerDuration + " " + noOfAgents;

			ACLMessage messageToSend = new ACLMessage(ACLMessage.REQUEST);

			for (AID receiver : remoteBrokers) {
				messageToSend.addReceiver(receiver);
			}

			messageToSend.setLanguage("English");
			messageToSend.setContent(payload);
			send(messageToSend);

			statusMessagesText += "\nCoordinator Message: " + messageToSend.getContent();
			coordinatorGUI.setMessageTextArea(statusMessagesText);
		}
	}

	public class ReceiveMessage extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		private int messagePerformative;
		private String messageContent;
		private String senderName;

		@Override
		public void action() {
			ACLMessage messageReceived = receive();
			if (messageReceived != null) {

				messagePerformative = messageReceived.getPerformative();
				if (messagePerformative == ACLMessage.REQUEST) {
					remoteBrokers.add(messageReceived.getSender());
					if (payload != null) {
						ACLMessage attack = new ACLMessage(ACLMessage.INFORM);
						attack.addReceiver(messageReceived.getSender());
						attack.setContent(payload);
						send(attack);
					}
				}
				if (messagePerformative == ACLMessage.FAILURE) {
					remoteBrokers.remove(messageReceived.getSender());
				}

				messageContent = messageReceived.getContent();
				senderName = messageReceived.getSender().getLocalName();

				statusMessagesText += "\n" + senderName + ": " + messageContent;
				coordinatorGUI.setMessageTextArea(statusMessagesText);
			}
		}

	}

	public void getFromGui(final String type, final String name, final String port, final String duration,
			final String number) {
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			public void action() {
				messageType = type;
				serverName = name;
				serverPort = port;
				tickerDuration = duration;
				noOfAgents = number;
			}
		});
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		addBehaviour(new SendMessage());
	}

	public void refreshActiveAgents() {
		AMSAgentDescription[] agents = null;

		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = AMSService.search(this, new AMSAgentDescription(), c);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < agents.length; i++) {
			AID agentID = agents[i].getName();
			if (agentID.getLocalName().equals("ams") || agentID.getLocalName().equals("rma")
					|| agentID.getLocalName().equals("df"))
				continue;
			agentList.add(agentID.getLocalName());
		}
	}
}
