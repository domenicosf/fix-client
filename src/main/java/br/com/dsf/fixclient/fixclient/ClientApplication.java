package br.com.dsf.fixclient.fixclient;

import lombok.extern.log4j.Log4j2;
import quickfix.*;
import quickfix.examples.banzai.BanzaiApplication;
import quickfix.field.MsgType;
import quickfix.field.Password;
import quickfix.field.Username;

import java.io.IOException;

@Log4j2
public class ClientApplication implements Application {
    private static final String userID = "domenico";
    private static final String password = "domenico";
    private final MessageCracker messageCracker;


    public ClientApplication(MessageCracker messageCracker) {
        this.messageCracker = messageCracker;
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) {
        log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
        MsgType msgType;
        try {
            msgType = (MsgType) message.getHeader( ).getField( new MsgType( ) );
            Session.lookupSession(sessionId).disconnect("Logon custom disconnection", false);
        }catch(FieldNotFound | IOException e ) {
            log.error(e.getMessage());
            return;
        }
        if( msgType.valueEquals( MsgType.HEARTBEAT ) ) {
            log.info( ">>> Heartbeat <<<" );
        }
    }

    @Override
    public void fromApp(Message message, SessionID sessionId) {
        log.info("fromApp: Message={}, SessionId={}", message, sessionId);

        try {
            messageCracker.crack(message, sessionId);
        } catch (UnsupportedMessageType | FieldNotFound | IncorrectTagValue e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("onCreate: SessionId={}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("onLogon: SessionId={}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        Session.lookupSession(sessionId).logout();
        try {
            Session.lookupSession(sessionId).disconnect("Logon custom disconnection", false);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {
        if (isMessageOfType(message, MsgType.LOGON)) {
            addLogonField(message, sessionId);
        }
    }

    @Override
    public void toApp(Message message, SessionID sessionId) {
        log.info("toApp: Message={}, SessionId={}", message, sessionId);
    }

    private boolean isMessageOfType(Message message, String type) {
        try {
            return type.equals(message.getHeader().getField(new MsgType()).getValue());
        } catch (FieldNotFound e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private void addLogonField(Message message, SessionID sessionID) {
        String str = sessionID.getSessionQualifier().replaceAll("\\D+", "");
        Integer scenario = Integer.valueOf(str);
        if(scenario % 2 != 0) {
            message.setField(new Username("domenico"));
            message.setField(new Password("domenico"));
            //		message.getHeader().setField(new SenderSubID(userID));
            //		message.getHeader().setField(new RawDataLength(password.length()));
            //		message.getHeader().setField(new RawData(password));
        }else{
            message.setField(new Username("domenico"));
            message.setField(new Password("domenico2"));
        }
    }
}
