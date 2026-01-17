package groupe1.il3.app.domain.authentication;

import groupe1.il3.app.domain.agent.Agent;

/**
 * Singleton class to manage the current user session.
 */
public class SessionManager {

    private static volatile SessionManager instance;
    private Agent currentAgent;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Sets the current logged-in agent.
     *
     * @param agent the agent to set as current
     */
    public void setCurrentAgent(Agent agent) {
        this.currentAgent = agent;
    }

    /**
     * Gets the current logged-in agent.
     *
     * @return the current agent, or null if no agent is logged in
     */
    public Agent getCurrentAgent() {
        return currentAgent;
    }

    /**
     * Checks if an agent is currently logged in.
     *
     * @return true if an agent is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentAgent != null;
    }

    /**
     * Logs out the current agent.
     */
    public void logout() {
        this.currentAgent = null;
    }
}
