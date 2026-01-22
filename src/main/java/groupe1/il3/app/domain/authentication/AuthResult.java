package groupe1.il3.app.domain.authentication;

import groupe1.il3.app.domain.agent.Agent;

public record AuthResult(Agent agent, String errorMessage) {}
