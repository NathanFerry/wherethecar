package groupe1.il3.app.domain.authentication;

import groupe1.il3.app.domain.agent.Agent;

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

    public void setCurrentAgent(Agent agent) {
        this.currentAgent = agent;
    }

    public Agent getCurrentAgent() {
        return currentAgent;
    }

    public boolean isLoggedIn() {
        return currentAgent != null;
    }

    public void logout() {
        this.currentAgent = null;
    }
}
