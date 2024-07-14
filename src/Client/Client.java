package Client;

import Server.*;
import Client.Request.*;
import Server.Response.*;
import org.json.simple.JSONValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp; // Correct import for Timestamp
import java.util.Scanner;

public class Client {
    private static String channelName = null;
    private static String firstName = null;
    public static String surname;
    public static String password;
    private static final Clock clock = new Clock();
    private static Timestamp lastSubscriptionTime = null;
    private static final long SUBSCRIPTION_LIMIT_MS = 120000; // 2 minutes in milliseconds

    private static void printMenu() {
        System.out.println(
                "\nHello " + firstName + ",\n" +
                        "What would you like to do today?\n\n" +
                        "1 : Open Request\n" +
                        "2 : Subscribe to a Channel\n" +
                        "3 : Unsubscribe from a Channel\n" +
                        "4 : Publish a Message\n" +
                        "5 : Get Messages\n" +
                        "9 : Log Out");
    }

    //Handles response
    private static void processServerResponse(BufferedReader in) throws IOException {
        String serverResponse;
        if ((serverResponse = in.readLine()) == null) return;

        Object json = JSONValue.parse(serverResponse);
        Response resp;

        if ((resp = SuccessResponse.fromJSON(json)) != null) {
            System.out.println("Success: " + ((SuccessResponse) resp).getResponseType());
            return;
        }

        if ((resp = MessageListResponse.fromJSON(json.toString())) != null) {
            for (Message m : ((MessageListResponse) resp).getMessages()) {
                System.out.println(m);
            }
            return;
        }

        if ((resp = ErrorResponse.fromJSON(json)) != null) {
            System.out.println("Error: " + ((ErrorResponse) resp).getErrorResponse());
            return;
        }

        System.out.println("Unknown response: " + serverResponse);
    }

    // Handles login requests
    private static void loginUser(PrintWriter writeServer, Scanner scanner) {
        System.out.println("Welcome to Sheffield Hallam Message application");
        System.out.println("_______________________________________________");
        System.out.println("Please create your account");
        System.out.println("_______________________________________________");

        while (true) {
            System.out.print("First name: ");
            firstName = scanner.nextLine();
            if (firstName.isEmpty()) {
                System.out.println("First name cannot be empty. Please try again.");
                continue;
            }

            System.out.print("Surname: ");
            surname = scanner.nextLine();
            if (surname.isEmpty()) {
                System.out.println("Surname cannot be empty. Please try again.");
                continue;
            }

            System.out.print("Password: ");
            password = scanner.nextLine();
            System.out.print("Validate password: ");
            String validatePassword = scanner.nextLine();

            if (!validatePassword.equals(password)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }

            break;
        }

        int studentId;
        while (true) {
            System.out.print("Student ID: ");
            try {
                studentId = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Student ID. Please enter a valid integer.");
            }
        }

        writeServer.println(new AccountRequests(firstName, surname, password, password, studentId).toJSON().toString());

        System.out.println("Please log in now.");
        while (true) {
            System.out.print("First name: ");
            String loginFirstName = scanner.nextLine();
            if (loginFirstName.isEmpty()) {
                System.out.println("First name cannot be empty. Please try again.");
                continue;
            }

            System.out.print("Password: ");
            String loginPassword = scanner.nextLine();

            System.out.print("Student ID: ");
            int loginStudentId;
            try {
                loginStudentId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Student ID. Please enter a valid integer.");
                continue;
            }

            if (!loginFirstName.equals(firstName) || !loginPassword.equals(password) || loginStudentId != studentId) {
                System.out.println("Login credentials do not match. Please try again.");
            } else {
                writeServer.println(new LoginRequest(loginFirstName, loginPassword, loginStudentId).toJSON().toString());
                System.out.println("Login successful!");
                break;
            }
        }
    }

    public static void main(String[] args) {
        try (Socket serverSocket = new Socket("localhost", 12345);
             PrintWriter writeServer = new PrintWriter(serverSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            loginUser(writeServer, scanner);

            while (firstName != null) {
                printMenu();
                System.out.print("Please choose an option: ");

                try {
                    int option = Integer.parseInt(scanner.nextLine());

                    switch (option) {
                        case 1:
                            handleOpenRequest(writeServer);
                            continue;

                        case 2:
                            handleSubscription(writeServer, scanner);
                            continue;

                        case 3:
                            handleUnsubscription(writeServer);
                            continue;

                        case 4:
                            handlePublishRequest(writeServer, scanner);
                            continue;

                        case 5:
                            handleGetRequest(writeServer);
                            continue;

                        case 9:
                            handleLogout(writeServer);
                            continue;

                        default:
                            System.out.println("Invalid option. Please select from the menu.");
                    }

                    processServerResponse(in);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: localhost");
        } catch (IOException e) {
            System.err.println("Couldn't connect to: localhost");
        }
    }

    // Handles requests
    private static void handleOpenRequest(PrintWriter writeServer) {
        System.out.println("Open Request");
        writeServer.println(new OpenRequest(firstName).toJSON().toString());
    }

    private static void handleSubscription(PrintWriter writeServer, Scanner scanner) {
        System.out.print("Enter the channel name: ");
        channelName = scanner.nextLine();
        if (channelName == null || channelName.isEmpty()) {
            System.out.println("Channel name cannot be empty.");
            return;
        }

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (lastSubscriptionTime == null || currentTime.getTime() - lastSubscriptionTime.getTime() > SUBSCRIPTION_LIMIT_MS) {
            lastSubscriptionTime = currentTime;
            writeServer.println(new SubscribeRequests(firstName, channelName).toJSON().toString());
            System.out.println("Subscription successful.");
        } else {
            System.out.println("You cannot subscribe to a new channel within 2 minutes of your last subscription.");
        }
    }

    private static void handleUnsubscription(PrintWriter writeServer) {
        System.out.println("Unsubscribe Request");
        if (channelName == null || channelName.isEmpty()) {
            System.out.println("No channel to unsubscribe from.");
            return;
        }

        writeServer.println(new UnsubscribeRequests(firstName, channelName).toJSON().toString());
    }

    private static void handlePublishRequest(PrintWriter writeServer, Scanner scanner) {
        System.out.print("Please enter message: ");
        String messageBody = scanner.nextLine();
        writeServer.println(new PublishRequest(channelName, new Message(firstName, clock.tick(), messageBody)).toJSON().toString());
    }

    private static void handleGetRequest(PrintWriter writeServer) {
        System.out.println("Get messages");
        int timestamp = clock.tick(); // Assuming current time for simplicity
        writeServer.println(new GetRequests(timestamp).toJSON().toString());
    }

    private static void handleLogout(PrintWriter writeServer) {
        System.out.println("Quit Application");
        System.out.println("See you soon :)");
        firstName = null; // Properly log out the user
        writeServer.println(new QuitRequest().toJSON().toString());
    }
}
