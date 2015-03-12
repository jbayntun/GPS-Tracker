/*---------------------------------------------------------------------------------------
--  Source File:        Server.java - a simple Java UDP (multi-threaded) echo server
--
--  Classes:        Server        - public class
--              DatagramSocket  - java.net
--              DatagramPacket  - java.net
--              
--  Methods:
--              getAddress      (DatagramPacket Class)
--              getPort         (DatagramPacket Class)
--              getLength       (DatagramPacket Class)
--              getLocalPort        (DatagramSocket Class)
--              setSoTimeout        (DatagramSocket Class)
--              send            (DatagramSocket Class)
--              receive         (DatagramSocket Class)
--              
--
--  Date:           February 8, 2014
--
--  Revisions:      (Date and Description)
--                  
--  Designer:       Aman Abdulla
--              
--  Programmer:     Aman Abdulla
--
--  Notes:
--  The program illustrates the use of the java.net package to implement a basic
--  echo server.The server is multi-threaded so every new client connection is 
--  handled by a separate thread.
--  
--  The application receives a string from an echo client and simply sends back after 
--  displaying it. 
--
--  Generate the class file and run it as follows:
--          javac Server
--          java Server <server port>
---------------------------------------------------------------------------------------*/

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.String;
import java.util.Arrays;

public class Server extends Thread
{
    private String ClientString;
    private int ClientPort;
    private DatagramSocket ListeningSocket;
    private DatagramPacket dgram;
    private static final int DgramSize = 1024;
    private byte[] PacketData;
    private InetAddress Addr;

    public Server (int port) throws IOException
    {
        ListeningSocket = new DatagramSocket (port);
    }

    public void run()
    {
        while(true)
        {
            PacketData = new byte[DgramSize];
            dgram = new DatagramPacket (PacketData, DgramSize);
            try
            {
                // Listen for datagrams
                System.out.println("Port: " + ListeningSocket.getLocalPort());
                ListeningSocket.receive(dgram);
                Addr = dgram.getAddress();
                ClientPort = dgram.getPort();
                System.out.println ("Datagram from: "+ Addr +":"+ ClientPort);
                
                // Get the client data
                ClientString = new String (PacketData, 0, dgram.getLength());
                System.out.println ("Message: "+ ClientString.trim());
                addPoint(ClientString);
            }
    
            catch (SocketTimeoutException s)
            {
                System.out.println ("Socket timed out!");
                ListeningSocket.close();
                break;
            }
  
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

    public void addPoint(String message)
    {
        FileInputStream in;
        FileOutputStream out;
        int keepLength = 0;
        String point;

        try
        {
            in = new FileInputStream("data.xml");
            keepLength = in.available() - 8;
            byte[] keep = new byte[keepLength];
            in.read(keep);
            in.close();

            out = new FileOutputStream("data.xml");
            point = getPoint(message);
            byte bPoint[] = point.getBytes();

            byte[] result = new byte[keep.length + bPoint.length];
            System.arraycopy(keep, 0, result, 0, keep.length);
            System.arraycopy(bPoint, 0, result, keep.length, bPoint.length);

            out.write(result);
        }
        catch(Exception e)
        {
            System.out.println(e);
            die("addPoint failure");
        }
    }

    // Requires the format of the input be name|ip|time|lat|lon|
    public String getPoint(String message)
    {
        String err = "File missing fields";

        String name;
        String ip;
        String time;
        String lat;
        String lon;

        int index = 0;
        int nextIndex = 0;
        // plan B
        //char[] msg = message.toCharArray();
        //Arrays.copyOfRange(msg, index, nextIndex - 1);

        nextIndex = getNextDivider(message, index);
        if (index < 0) die(err);
        name = message.substring(index, nextIndex - 1);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (index < 0) die(err);
        ip = message.substring(index, nextIndex - 1);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (index < 0) die(err);
        time = message.substring(index, nextIndex - 1);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (index < 0) die(err);
        lat = message.substring(index, nextIndex - 1);
        index = nextIndex + 1;

        nextIndex = getNextDivider(message, index);
        if (index < 0) die(err);
        lon = message.substring(index, nextIndex - 1);

        return "    <user>\n" +
               "        <name>"      + name + "</name>\n" +
               "        <ip>"        + ip   + "</ip>\n" +
               "        <time>"      + time + "</time>\n" +
               "        <latitude>"  + lat  + "</latitude>\n" +
               "        <longitude>" + lon  + "</longitude>\n" +
               "    </user>" +
               "</users>";
    }

    public int getNextDivider(String message, int start)
    {
        for (int i = start; i < message.length(); i++)
        {
            if (message.charAt(i) == '|') return i;
        }

        return -1;
    }

    public void die(String message)
    {
        System.out.println(message);
        System.err.println(message);
        System.exit(-1);
    }

    public static void main (String [] args)
    {
        int port = 0;
        if(args.length != 1)
            port = 7000;
        else
            port = Integer.parseInt(args[0]);
       
        try
        {
            Thread t = new Server (port);
            t.start();
        }
        
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}