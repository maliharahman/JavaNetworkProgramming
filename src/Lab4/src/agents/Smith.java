package agents;

import java.io.IOException;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;



public class Smith extends Agent {

	private static final long serialVersionUID = 1L;
	private String serverIP = "";
	private int serverPort = 0;
	private long timeForTickerBehaviour = 0;
	
	final String fibRange = "20";
	
	protected void setup() {
		
		serverIP = getProperty("serverIP","localhost");
		serverPort = Integer.parseInt(getProperty("serverPort","5678"));
		timeForTickerBehaviour = Long.parseLong(getProperty("timeForTicker","5000"));
		
		System.out.println("Messenger agent "+getAID().getName()+" is ready.");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Attacker agent");
		sd.setName(getLocalName()+"-Attacker agent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new ConnectToTCPServer(this,timeForTickerBehaviour));
	}
	
	protected void takeDown() {

		System.out.println("Agent: "+getAID().getName()+"terminating.");

		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	public class ConnectToTCPServer extends TickerBehaviour{
		private static final long serialVersionUID = 1L;

		public ConnectToTCPServer(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick(){
			ClientSocket soc = new ClientSocket(serverIP,serverPort);
			try {
				soc.connect();
				String serverResponse = soc.send(fibRange); 
				System.out.println("Connection Established with ip: "+serverIP+" at port: "+serverPort+" at Ticker value: "+timeForTickerBehaviour);
				System.out.println("Fibo series for n("+ fibRange +") ="+serverResponse);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}