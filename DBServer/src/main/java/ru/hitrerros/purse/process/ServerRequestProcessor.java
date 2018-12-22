package ru.hitrerros.purse.process;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.hitrerros.purse.clientcommons.Application;
import ru.hitrerros.purse.clientcommons.ReturnCode;
import ru.hitrerros.purse.message.CRUDMessage;
import ru.hitrerros.purse.message.DBReplyMessage;
import ru.hitrerros.purse.message.Document;
import ru.hitrerros.purse.message.Message;
import ru.hitrerros.purse.clientcommons.SocketClientAgent;
import ru.hitrerros.purse.model.PurseEntity;
import ru.hitrerros.purse.service.PurseService;
import ru.hitrerros.purse.service.PurseServiceImpl;

import ru.hitrerros.purse.utils.ReflectionHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ServerRequestProcessor {

    private static final String ownerId = "dbserver";
    private static final String HOST = "localhost";
    private static final int PORT = 5060;

    private static final String SPRING_PATH = "spring-app.xml";
    private SocketClientAgent clientAgent;

    private final ConfigurableApplicationContext appContext;
    private final ExecutorService executorService;


    private PurseService purseService;

    public ServerRequestProcessor() {
        appContext = new ClassPathXmlApplicationContext(SPRING_PATH);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void start() throws IOException {
        purseService = appContext.getBean(PurseServiceImpl.class);

        SocketChannel channel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        clientAgent = new SocketClientAgent(channel, ownerId, Application.DBSERVER);
        clientAgent.init();

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

    private Message doReply(CRUDMessage incomingMessage) {

        DBReplyMessage replyMessage = new DBReplyMessage(ownerId, Application.DBSERVER);

        switch (incomingMessage.getOperation()) {
            case READ:
                generateReadReply(incomingMessage, replyMessage);
                break;
            case CREATE:
                generateCreateReply(incomingMessage, replyMessage);
                break;
            case UPDATE:
                // TODO
            case DELETE:
                // TODO
        }

        return replyMessage;

    }

    private void  generateReadReply(CRUDMessage incomingMessage, DBReplyMessage replyMessage) {

        Document doc = incomingMessage.getDocument();

        if (doc.getId() != 0) {
            Optional<PurseEntity> entity = Optional.of(purseService.getRecord(doc.getId()));
            entity.ifPresentOrElse(s -> replyMessage
                            .setDocuments(List.of(ReflectionHelper.transferObject(entity.get(),Document.class))),
                    () -> replyMessage.setReturnCode(ReturnCode.NOT_FOUND));
        } else {
            List<PurseEntity> entities = purseService.getAll();
            if (entities.isEmpty()) {
                replyMessage.setReturnCode(ReturnCode.NOT_FOUND);
            } else {
                List<Document> docs = entities.stream()
                        .map(s-> ReflectionHelper.transferObject(s,Document.class))
                        .collect(Collectors.toList());

                replyMessage.setDocuments(docs);

            }
        }
    }

    private void  generateCreateReply(CRUDMessage incomingMessage, DBReplyMessage replyMessage) {

        PurseEntity entity = ReflectionHelper.transferObject(incomingMessage.getDocument(),PurseEntity.class);
        PurseEntity respondEntity = purseService.saveRecord(entity);

        if (respondEntity != null) {
            replyMessage.setDocuments(List.of(ReflectionHelper.transferObject(entity, Document.class)));
            replyMessage.setReturnCode(ReturnCode.OK_SAVE);
        }
        else {
            replyMessage.setReturnCode(ReturnCode.NOT_FOUND);
        }




    }

}