#Fix Client Sample Project

This project will present a customized sample client developed to use the TCP/IP connections to send dix messages to a server (or adapter) application.

The client were developed to send for now only logon messages to a FIX Server.

In this project we have 4 test scenarios: two with successful authentication and the other two with failure during the authentication.

A server project should be used to receive the messages send my this client [Fix Server](https://github.com/domenicosf/artio-fix-server)

When the client project doesn't need stay connected it will send a disconnection message to the server.

This project should be run after server initialization. As this project is a Spring Boot application and shoulb be build and run with the following:

- mvn install
- mvn spring-boot run 

###Sample Client Customized Authentication Method

```
 @Override
    public void toAdmin(Message message, SessionID sessionId) {
        if (isMessageOfType(message, MsgType.LOGON)) {
            addLogonField(message, sessionId);
        }
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
            message.setField(new Username("username"));
            message.setField(new Password("password"));
        }else{
            message.setField(new Username("wrong_username"));
            message.setField(new Password("wrong_password"));
        }
    }
```