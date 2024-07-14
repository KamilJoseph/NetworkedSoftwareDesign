package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(12345))
        {
            while (true)
            {
                Socket clientSocket = serverSocket.accept();

                new ServerThread(clientSocket).start();
            }
        } catch (IOException e)
        {
            System.out.println("Exception listening for connection:" + 12345 );

            System.out.println(e.getMessage());
        }

    }
}