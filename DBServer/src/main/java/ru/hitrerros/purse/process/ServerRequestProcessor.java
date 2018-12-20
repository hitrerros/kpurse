package ru.hitrerros.purse.process;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.hitrerros.purse.clientcommons.Application;
import ru.hitrerros.purse.message.CRUDMessage;
import ru.hitrerros.purse.message.DBReplyMessage;
import ru.hitrerros.purse.message.Message;
import ru.hitrerros.purse.clientcommons.SocketClientAgent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerRequestProcessor {

    private static final String ownerId = "dbserver";
    private static final String HOST = "localhost";
    private static final int PORT = 5060;

    private static final String SPRING_PATH = "spring-app.xml";
    private SocketClientAgent clientAgent;

    final ConfigurableApplicationContext appContext;

    public ServerRequestProcessor() {
        appContext = new ClassPathXmlApplicationContext(SPRING_PATH);
    }

    public void start() throws IOException {

        SocketChannel channel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        clientAgent = new SocketClientAgent(channel, ownerId, Application.DBSERVER);
        clientAgent.init();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try {
                while (true) {
                    final Message inMessage = clientAgent.take();

                    System.out.println("Message received on db client: " + inMessage.toString());

                    final Message outMessage = doReply((CRUDMessage) inMessage);
                    if (outMessage != null) {
                        System.out.println("outMessage from db: " + inMessage.toString());
                        clientAgent.send(outMessage);
                    }

                }
            } catch (Exception e) {
                //       logger.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }
        });

    }

    Message doReply(CRUDMessage incomingMessage) {

        DBReplyMessage replyMessage = new DBReplyMessage(ownerId, Application.DBSERVER);

        switch (incomingMessage.getOperation()) {

            case READ:

            case CREATE:

            case UPDATE:

            case DELETE:

        }

        return replyMessage;

    }
}