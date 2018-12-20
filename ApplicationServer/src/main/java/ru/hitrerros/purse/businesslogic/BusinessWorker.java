package ru.hitrerros.purse.businesslogic;

import ru.hitrerros.purse.clientcommons.Application;
import ru.hitrerros.purse.message.CRUDMessage;
import ru.hitrerros.purse.message.FrontendMessage;
import ru.hitrerros.purse.message.Message;
import ru.hitrerros.purse.server.SocketServerAgent;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusinessWorker implements  MessageObserver{

    private SocketServerAgent serverAgent;
    private final ExecutorService executor;
    private static final int THREADS_NUMBER = 1;

    public  BusinessWorker() {
        serverAgent = new SocketServerAgent();
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
    }

    public void start() {
        executor.submit(()-> {
            try {
                serverAgent.start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("Application server started....");
    }

    @Override
    public void onGetMessage(Message message) {

         if (message instanceof FrontendMessage) {   // from Frontend
            ProcessFrontendMessage((FrontendMessage)message);
        }
        else {

        }

    }

    @Override
    public void dummySender() {

        System.out.println("dummy");
        CRUDMessage crudMessage = new CRUDMessage();
        crudMessage.setOperation(CRUDOperation.READ);
        crudMessage.setOwnerId("MessageServer");

        serverAgent.sendMessage(crudMessage, Application.DBSERVER);
    }

    private void ProcessFrontendMessage(FrontendMessage frontendMessage) {

        CRUDMessage crudMessage = new CRUDMessage();

        if (FrontendOperation.BALANCE.equals(frontendMessage.getOperation())) {
            crudMessage.setOperation(CRUDOperation.READ);
            crudMessage.setOwnerId(frontendMessage.getOwnerId());
            serverAgent.sendMessage(crudMessage, Application.DBSERVER);


        }





    }


}
