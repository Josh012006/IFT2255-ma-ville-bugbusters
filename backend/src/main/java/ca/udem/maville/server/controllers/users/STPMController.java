package ca.udem.maville.server.controllers.users;

import org.slf4j.Logger;

public class STPMController {

    public String urlHead;
    public Logger logger;

    public STPMController(String urlHead, Logger logger) {
        this.urlHead = urlHead;
        this.logger = logger;
    }

    // Todo: Déplacer la logique d'affectation de priorité
    //  et d'acceptation ou de refus de proposition de projet ici
}
