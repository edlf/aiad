import jade.BootProfileImpl;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.ProfileImpl;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;

/**
 * Eduardo Fernandes
 */

public class MainController {
    private static final boolean JADE_GUI = true;
    private static boolean isJadeRunning = false;
    private static ProfileImpl profile;
    private static ContainerController containerController;
    private static jade.core.Runtime jadeRuntime;
    AgentManager agentManager;

    void initJade() {
        if (isJadeRunning) {
            return;
        }

        isJadeRunning = true;

        // Init JADE platform w/ or w/out GUI
        if (JADE_GUI) {
            List<String> params = new ArrayList<String>();
            params.add("-gui");
            profile = new BootProfileImpl(params.toArray(new String[params.size()]));

        } else {
            profile = new ProfileImpl();
        }

        jadeRuntime = jade.core.Runtime.instance();

        // containerController
        containerController = jadeRuntime.createMainContainer(profile);

        // Manager Agents
        agentManager = new AgentManager(containerController);

        // Add agents to main container
        try {
            containerController.acceptNewAgent("Agent Manager", agentManager).start();
        } catch (StaleProxyException e) {
            lostCommunicationJADE();
        } catch (NullPointerException e) {
            lostCommunicationJADE();
        }
    }

    void stopJade(){
        if (!isJadeRunning) {
            return;
        }

        // Stop all agents

        // Stop Agent Manager
        agentManager.doDelete();

        // TODO: Shutdown Jade
        /*
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
        */
    }

    private void lostCommunicationJADE(){

    }

    /* GUI */
    @FXML
    private void handleSimStart() {
        initJade();
    }

    @FXML
    private void handleSimStop() {
        stopJade();
    }
}