package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    public static void main(String[] args) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        List<ArrayList> client = new ArrayList<>();

        while(true)
        {
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String parse = sentence;
            String[] splitParse = parse.trim().split("\\s+");
            System.out.println(Arrays.toString(splitParse));
            
            client.add(splitParse[1]);
            String capitalizedSentence = sentence.toUpperCase();
            System.out.println("RECEIVED: " + sentence);
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }

    public void checkUsername(){

    }

}
