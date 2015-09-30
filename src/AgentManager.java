import jade.core.Agent;
import jade.wrapper.ContainerController;

/**
 * Eduardo Fernandes
 */

public class AgentManager extends Agent {
    ContainerController mainContainer;

    public AgentManager(ContainerController mainContainer) {
        this.mainContainer = mainContainer;
    }

    protected void takeDown() {

    }
}
