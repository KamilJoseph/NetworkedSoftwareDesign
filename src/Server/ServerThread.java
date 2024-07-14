package Server;

import Client.Request.*;
import Server.Response.ErrorResponse;
import Server.Response.MessageListResponse;
import Server.Response.SuccessResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {

    private String userName;
    private final PrintWriter serverWriter;
    private final BufferedReader clientReader;
    public final List<Message> board = new ArrayList<>();
    private static final Clock clock = new Clock();
    private static final String FILE_PATH= "C:/Users/Babita Joseph/Desktop/Sheffield Hallam University/Networked Software Design/src/Server/Text.txt";

    public ServerThread(Socket socket)
    {
        PrintWriter tempWriter = null;
        BufferedReader tempReader = null;
        try {
            tempWriter = new PrintWriter(socket.getOutputStream(), true);
            tempReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userName = null;
        } catch (IOException e) {
            System.out.println("Error");
        }
        serverWriter = tempWriter;
        clientReader = tempReader;
        loadMessagesFromFile();
    }

    // Method to load persisted messages from file
    private void loadMessagesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject jsonMessage = (JSONObject) JSONValue.parse(line);
                if (jsonMessage != null) {
                    Message message = Message.fromJSON(jsonMessage);
                    if (message != null) {
                        board.add(message);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading messages from file: " + e.getMessage());
        }
    }

    public void run() {
        try {
            String input;
            while ((input = clientReader.readLine()) != null) {

                if (userName != null)
                    System.out.println(userName + ": " + input);
                else
                    System.out.println(input);

                // Parse request, then try to deserialize JSON
                Object json = JSONValue.parse(input);
                Request req;

                //Account request
                if (userName == null && (req = AccountRequests.fromJSON(json))!=null)
                {
                    userName = ((AccountRequests) req).getFirstname();
                }

                // Login request
                if (userName == null && (req = LoginRequest.fromJSON(json)) != null) {
                    userName = ((LoginRequest) req).getFirstname();
                }

                // Open request
                if (userName != null && (req = OpenRequest.fromJSON(json)) != null) {
                    userName = ((OpenRequest) req).getChannelUser();
                }

                // Subscribe requests
                if (userName != null && (req = SubscribeRequests.fromJSON(json)) != null) {
                    String channel = ((SubscribeRequests) req).getChannel();
                    if (channel != null) {
                        serverWriter.println(new SuccessResponse("User has joined channel " + channel));
                    } else
                        serverWriter.println(new SuccessResponse("User has left the channel"));
                }

                // Unsubscribe requests
                if ((userName != null && (req = UnsubscribeRequests.fromJSON(json)) != null)) {
                    String channel = ((UnsubscribeRequests) req).getChannel();
                    if (channel != null)
                        serverWriter.println(new SuccessResponse("User has left the channel"));
                }

                // Publish request
                if (userName != null && (req = PublishRequest.fromJSON(json)) != null) {
                    PublishRequest publishRequest = (PublishRequest) req;
                    Message message = publishRequest.getMessage();

                    // Validate message size
                    if (message.getBody().length() > 200) { // 200 characters as the limit
                        serverWriter.println(new ErrorResponse("Message too big"));
                        continue;
                    }
                    synchronized (board) {
                        board.add(new Message(message.getFrom(), clock.tick(), message.getBody()));
                    }
                    // Response acknowledging the post request
                    serverWriter.println(new SuccessResponse("SuccessResponse"));
                    // Save message to text file using PublishRequest's saveToTextFile method
                    publishRequest.saveToTextFile(FILE_PATH);
                    continue;
                }

                // Inside the Get Requests Handling block
                if (userName != null && GetRequests.fromJSON(json) != null) {
                    List<Message> msgs;
                    synchronized (ServerThread.class) {
                        msgs = new ArrayList<>(board);
                    }
                    serverWriter.println(new MessageListResponse(msgs).toJSON().toString());
                    continue;
                }

                // Quit request
                if (userName != null && QuitRequest.fromJSON(json) != null) {
                    serverWriter.close();
                    clientReader.close();
                    return;
                }
            }
        } catch (IOException e)
        {
            System.out.println("Exception while connected");
            System.out.println(e.getMessage());
        }
    }
}
