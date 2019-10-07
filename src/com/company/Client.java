package com.company;

import java.io.*;
import java.net.*;

public class Client {

    private boolean forbundet = true;
    private boolean username_Chosen = false;
    private String username;

    public static void main(String[] args) throws Exception {
        Client client = new Client(9876);
        client.executeClient();
    }

    public Client(int port){

    }

    private void executeClient() {
        while (forbundet == true) {
            try{

            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            byte[] sendLeave = new byte[1024];
                //If no username is chosen, or already taken
                while(username_Chosen == false){
                    System.out.println("Please choose a username:");
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                    username = userInput.readLine();
                    String sentence = "JOIN, " + username + " - " + IPAddress + ":" + "9876";
                    sendData = sentence.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                    clientSocket.send(sendPacket);
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    String modifiedSentence = new String(receivePacket.getData());
                    if (modifiedSentence.contains("ERROR: Username is already taken")){
                        System.out.println(modifiedSentence);
                        username_Chosen = false;
                    }//If you wrote a username that isn't taken
                    else{
                        System.out.println("FROM SERVER: " + modifiedSentence);
                        username_Chosen = true;
                    }

                }
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             String input = userInput.readLine();
             //Used to leave server and close socket
            if (input.equalsIgnoreCase("Exit")){
                System.out.println("Leaving server");
                String leaveSentence = "Client: " + username + ", " + IPAddress + ":" + "9876" + " has left the server";
                sendLeave = leaveSentence.getBytes();
                DatagramPacket closePacket = new DatagramPacket(sendLeave, sendLeave.length, IPAddress, 9876);
                clientSocket.send(closePacket);
                forbundet = false;
                clientSocket.close();
            }
            //Used in every other case
            else{
                String sentence = username +  ": Message: " + input + " " ;
                sendData = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                System.out.println("FROM SERVER: " + modifiedSentence);
            }

        }catch (UnknownHostException e){
                System.out.println("UnknownHostException: " + e);
            }catch (IOException e){
                System.out.println("IOException: " + e);
            }

        }

    }


}
