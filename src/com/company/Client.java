package com.company;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static boolean forbundet = true;

    public static void main(String[] args) throws Exception {
        System.out.println("Please choose a username:");
    while (forbundet == true) {
        BufferedReader userInput =
                new BufferedReader(new InputStreamReader(System.in));
        String input = userInput.readLine();
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        byte[] sendLeave = new byte[1024];
        if (input.equalsIgnoreCase("Exit")){
            System.out.println("Leaving server");
            String leaveSentence = "Client: " + input + ", " + IPAddress + ":" + "9876" + " has left the server";
            sendLeave = leaveSentence.getBytes();
            DatagramPacket closePacket = new DatagramPacket(sendLeave, sendLeave.length, IPAddress, 9876);
            clientSocket.send(closePacket);
            forbundet = false;
            clientSocket.close();
        }
        else {
            String sentence = "JOIN " + input + ", " + IPAddress + ":" + "9876";
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER: " + modifiedSentence);
             }

        }

    }


}
