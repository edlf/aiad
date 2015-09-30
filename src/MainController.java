import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

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

    @FXML
    MenuItem menuSimStart;
    @FXML
    MenuItem menuSimStep;
    @FXML
    MenuItem menuSimStop;

    @FXML
    Button buttonSimStart;
    @FXML
    Button buttonSimStep;
    @FXML
    Button buttonSimStop;

    public MainController(){

    }

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

        menuSimStart.setDisable(false);
        // Stop all agents

        // Stop Agent Manager
        agentManager.doDelete();

        isJadeRunning = false;
    }

    private void lostCommunicationJADE(){

    }

    /* GUI */
    @FXML
    private void handleSimStart() {
        menuSimStart.setDisable(true);
        buttonSimStart.setDisable(true);
        initJade();
        menuSimStop.setDisable(false);
        buttonSimStop.setDisable(false);
    }

    @FXML
    private void handleSimStop() {
        menuSimStart.setDisable(false);
        buttonSimStart.setDisable(false);
        stopJade();
        menuSimStop.setDisable(true);
        buttonSimStop.setDisable(true);
    }

    public void close(){
        stopJade();
        System.exit(0);
    }

    public void start(){
        menuSimStop.setDisable(true);
        menuSimStep.setDisable(true);
        buttonSimStop.setDisable(true);
        buttonSimStep.setDisable(true);
    }
}