package br.com.dsf.fixclient.fixclient;

import lombok.extern.log4j.Log4j2;
import org.slf4j.LoggerFactory;
import quickfix.*;

@Log4j2
public class ApplicationMessageCracker extends MessageCracker {

    @Override
    protected void onMessage(Message message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        // Handle the message here
        log.info("*****************");
        log.info("Mensagem recebida para sessionID={}: {}", sessionID, message);
        log.info("*****************");

        super.onMessage(message, sessionID);
    }
}
