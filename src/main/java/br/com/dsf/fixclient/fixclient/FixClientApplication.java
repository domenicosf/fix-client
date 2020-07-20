package br.com.dsf.fixclient.fixclient;

import io.allune.quickfixj.spring.boot.starter.EnableQuickFixJClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import quickfix.*;

@Log4j2
@EnableQuickFixJClient
@SpringBootApplication
public class FixClientApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(FixClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();
    }

    @Bean
    public Application clientApplication(MessageCracker messageCracker) {
        return new ClientApplication(messageCracker);
    }

    @Bean
    public MessageCracker messageCracker() {
        return new ApplicationMessageCracker();
    }

    @Bean
    public Initiator clientInitiator(quickfix.Application clientApplication, MessageStoreFactory clientMessageStoreFactory,
                                     SessionSettings clientSessionSettings, LogFactory clientLogFactory,
                                     MessageFactory clientMessageFactory) throws ConfigError {

        return new ThreadedSocketInitiator(clientApplication, clientMessageStoreFactory, clientSessionSettings,
                clientLogFactory, clientMessageFactory);
    }

    @Bean
    public LogFactory clientLogFactory(SessionSettings clientSessionSettings) {
        return new FileLogFactory(clientSessionSettings);
    }

}
