package ru.hitrerros.purse.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hitrerros.purse.businesslogic.MessageObserver;
import ru.hitrerros.purse.clientcommons.Application;
import ru.hitrerros.purse.message.InitMessage;
import ru.hitrerros.purse.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServerAgent {

    private static final Logger logger = Logger.getLogger(SocketServerAgent.class.getName());
    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5060;
    private static final int ECHO_DELAY = 100;
    private static final int CAPACITY = 256;
    private static final String MESSAGES_SEPARATOR = "\n\n";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ExecutorService executor;
    private MessageObserver observer;


    private final Map<String, ChannelMessages> channelMessages;
    private final Map<String,ChannelDescription> channelOwners;

    public SocketServerAgent() {
        channelMessages = new ConcurrentHashMap<>();
        channelOwners = new ConcurrentHashMap<>();
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
    }

    public void start(MessageObserver observer) throws Exception {

        this.observer = observer;
        executor.submit(this::messageManager);

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));

            serverSocketChannel.configureBlocking(false); //non blocking mode
            int ops = SelectionKey.OP_ACCEPT;
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, ops, null);

            System.out.println("Started on port: " + PORT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                   if (key.isAcceptable()) {
                        registerClient(selector, serverSocketChannel);
                    }
                   if (key.isReadable()) {
                        processMessage(key);
                 }
                    iter.remove();
                }
           }
        }
    }

//    private static ByteBuffer messageWrapper(Message message) throws Exception {
//        String json = MAPPER.writeValueAsString(message);
//        ByteBuffer buf = ByteBuffer.wrap(json.getBytes());
//        return buf;
//    }

    private void registerClient(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);

        String remoteAddress = client.getRemoteAddress().toString();
        System.out.println("Connection Accepted: " + remoteAddress);
        channelMessages.put(remoteAddress, new ChannelMessages(client));
    }

    private void processMessage(SelectionKey key)
            throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
        SocketChannel client = (SocketChannel) key.channel();
        int read = client.read(buffer);

        if (read != -1) {
            String result = new String(buffer.array()).trim();
            System.out.println("Message received: " + result + " from: " + client.getRemoteAddress());
//            channelMessages.get(client.getRemoteAddress().toString()).messages.add(result);
            Message message = MAPPER.readValue(result,Message.class);

            if (message instanceof InitMessage) {
                channelOwners.put(client.getRemoteAddress().toString(),
                                  new ChannelDescription(message.getOwnerId(),message.getSenderType()));

                observer.dummySender();

            }
            else {
                observer.onGetMessage(message);
            }

        } else {
            key.cancel();
            String remoteAddress = client.getRemoteAddress().toString();
            channelMessages.remove(remoteAddress);
            System.out.println("Connection closed, key canceled");
        }
    }



    public void sendMessage(Message message, Application to){
        for (Map.Entry<String,ChannelDescription> entry: channelOwners.entrySet()){
                if (entry.getValue().application.equals(to)) {
                    channelMessages.get(entry.getKey()).messages.add(message);
                }
        }
   }

    private Object messageManager() throws InterruptedException {

         while (true) {
            for (Map.Entry<String, ChannelMessages> entry : channelMessages.entrySet()) {
                ChannelMessages channelMessages = entry.getValue();
                if (channelMessages.channel.isConnected()) {
                    channelMessages.messages.forEach(message -> {
                        try {
                            System.out.println("Processing message to: " + entry.getKey());

                            String json = MAPPER.writeValueAsString(message);

                            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
                            buffer.put(json.getBytes());
                            buffer.put(MESSAGES_SEPARATOR.getBytes());
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                channelMessages.channel.write(buffer);
                            }

                        } catch (IOException e) {
                            logger.log(Level.SEVERE, e.getMessage());
                        }
                    });
                    channelMessages.messages.clear();
                }
            }
            Thread.sleep(ECHO_DELAY);
        }
    }

    private class ChannelDescription{
        String ownerId;
        Application application;

        ChannelDescription(String ownerId,Application application){
            this.ownerId = ownerId;
            this.application = application;
        }
    }

    private class ChannelMessages {
        private final SocketChannel channel;
        private final List<Message> messages = new ArrayList<>();
        private ChannelMessages(SocketChannel channel) {
            this.channel = channel;
        }
    }

}
