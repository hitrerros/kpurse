package ru.hitrerros.purse.clientcommons;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hitrerros.purse.message.InitMessage;
import ru.hitrerros.purse.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientAgent {

    private SocketChannel client;
    private final String ownerId;
    private final Application senderType;
    private final ExecutorService executor;
    private final List<Runnable> shutdownRegistrations;


    private static final Logger logger = Logger.getLogger(SocketClientAgent.class.getName());
    private static final int WORKERS_COUNT = 2;
    private static final int QUEUE_CAPACITY = 10;
    private static final int BUFFER_CAPACITY = 256;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    public SocketClientAgent (SocketChannel client, String ownerId, Application senderType) {
        this.ownerId = ownerId;
        this.senderType = senderType;
        this.client = client;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
        this.shutdownRegistrations = new ArrayList<>();
    }


    public void send(Message message) {
        output.add(message);
    }
    public Message poll() {
        return input.poll();
    }
    public Message take() throws InterruptedException {
        return input.take();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
        send(new InitMessage(ownerId,senderType));
    }

    public void addShutdownRegistration(Runnable runnable) {
        this.shutdownRegistrations.add(runnable);
    }

    private void sendMessage() {
        try  {
            while (client.isConnected()) {
                final Message msg = output.take(); //blocks
                final String json = MAPPER.writeValueAsString(msg);
                System.out.println("Sending message: " + json);
//                writer.println(json);
//                writer.println();//line with json + an empty line
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
                buffer = ByteBuffer.wrap(json.getBytes());
                client.write(buffer);

            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private void receiveMessage() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(client.socket().getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) { //blocks
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty()) { //empty line is the end of the message
                    final String json = stringBuilder.toString();
                    System.out.println("Receiving message: " + json);
                    final Message msg = MAPPER.readValue(json, Message.class);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
    }


    public void close() {
        System.out.println(ownerId + " closed");
        shutdownRegistrations.forEach(Runnable::run);
        shutdownRegistrations.clear();

        executor.shutdown();
    }




}
