package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
    static List<String> clientList = new ArrayList<>();
    static private boolean usernameError = false;

    public static void main(String[] args) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);


        while(true)
        {
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            System.out.println("RECEIVED: " + sentence);
                //Splits join msg into 2 Strings, one containing username and IPadress + port, and adds that to arraylist of active clients
                if (sentence.contains("JOIN")){
                    String parse = sentence;
                    String[] splitParse = parse.trim().split(",");
                    String parseUsername = splitParse[1].toLowerCase();
                    String[] parseFurther = parseUsername.trim().split("-");
                    String parsedFully = parseFurther[0];
                    //Loops through clientList to check if wanted username, already exists
                    for (int i = 0; i < clientList.size(); i++){
                        String tempName = clientList.get(i).toLowerCase();
                        if (tempName.contains(parsedFully)){
                            String capitalizedSentence = "ERROR: Username is already taken, Please try again!";
                            sendData = capitalizedSentence.getBytes();
                            DatagramPacket sendPacket =
                                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
                            serverSocket.send(sendPacket);
                            System.out.println("User tried to connect with username already in use: " + parsedFully);
                            usernameError = true;
                        }else{
                            usernameError = false;
                        }
                    }
                    //If username doesnt exist, add to list and reply with success msg
                    if (usernameError == false) {
                        clientList.add(parseUsername);
                        System.out.println("Current users: " + clientList);
                        String capitalizedSentence = "Connection accepted! " + sentence.toUpperCase();
                        sendData = capitalizedSentence.getBytes();
                        DatagramPacket sendPacket =
                                new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        serverSocket.send(sendPacket);
                    }
                }
                //When Exit msg is received, then client is removed from the arraylist of active clients
                else if (sentence.toLowerCase().contains("has left the server".toLowerCase())){
                    String parse = sentence;
                    String[] splitParse = parse.trim().split(":");
                    String parseUsername = splitParse[1].toLowerCase();
                    String[] parseFurther = parseUsername.trim().split(",");
                    String parsedFully = parseFurther[0];
                    System.out.println(Arrays.toString(splitParse));
                    for (int i = 0; i < clientList.size(); i++){
                        String tempName = clientList.get(i).toLowerCase();
                        if (tempName.contains(parsedFully)){
                            String removed = clientList.remove(i);
                            System.out.println("Client left: " + removed);
                        }
                    }
                    System.out.println("Current users: " + clientList);

                }
                //Used in every other case
                else{
                    String capitalizedSentence = sentence.toUpperCase();
                    sendData = capitalizedSentence.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    serverSocket.send(sendPacket);

                }

        }
    }

}