import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

/**
 * Eduardo Fernandes
 */

public class AgentManager extends Agent {
    ContainerController mainContainer;

    ArrayList<TaxiAgent> taxiAgents;
    ArrayList<PassengerAgent> passengerAgents;
    int numberOfTaxiAgents = 10;
    int numberOfPassengers = 50;


    public AgentManager(ContainerController mainContainer) {
        this.mainContainer = mainContainer;

        taxiAgents = new ArrayList<>();
        passengerAgents = new ArrayList<>();
    }

    protected void setup(){
        System.out.println("AgentManager::setup()");

        /* Create passenger agents */
        for (int i = 0; i < numberOfPassengers; i++){
            passengerAgents.add(i, new PassengerAgent(mainContainer));
        }

        /* Create taxi agents */
        for (int i = 0; i < numberOfTaxiAgents; i++){
            taxiAgents.add(i, new TaxiAgent(mainContainer));
        }

        addAgents();
    }

    protected void addAgents(){
        try {
            /* Add passenger agents to system */
            for (int i=0; i < passengerAgents.size(); i++) {
                mainContainer.acceptNewAgent("Passenger Agent: " + String.valueOf(i), passengerAgents.get(i)).start();
            }

            /* Add taxi agents to system */
            for (int i=0; i < taxiAgents.size(); i++) {
                mainContainer.acceptNewAgent("Taxi Agent: " + String.valueOf(i), taxiAgents.get(i)).start();
            }


        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    protected void takeDown() {
        System.out.println("AgentManager::takeDown()");

        /* Shutdown agents */
        /* Create passenger agents */
        for (int i = 0; i < passengerAgents.size(); i++){
            passengerAgents.get(i).takeDown();
        }

        /* Create taxi agents */
        for (int i = 0; i < taxiAgents.size(); i++){
            taxiAgents.get(i).takeDown();
        }

        // Shutdown Jade
        Codec codec = new SLCodec();
        Ontology jmo = JADEManagementOntology.getInstance();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(jmo);
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(getAMS());
        msg.setLanguage(codec.getName());
        msg.setOntology(jmo.getName());
        try {
            getContentManager().fillContent(msg, new Action(getAID(), new ShutdownPlatform()));
            send(msg);
        }
        catch (Exception e) {}
    }
}
