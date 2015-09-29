import jade.core.Agent;
import jade.wrapper.ContainerController;

/**
 * Created by eluis on 29/09/2015.
 */
public class AgentManager extends Agent {
    ContainerController mainContainer;

    public AgentManager(ContainerController mainContainer) {
        this.mainContainer = mainContainer;
    }

    protected void takeDown() {

    }
}
