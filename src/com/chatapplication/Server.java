/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chatapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PANDIT
 */
public class Server {
    
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Server(){
        try{
            server = new ServerSocket(7777);
            System.out.println("server ready...!");
            System.out.println("waiting...!");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            
            startReading();
            startWriting();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Server();
    }

    private void startReading() {
        try{
            Runnable r1 = () -> {
                System.out.println("reader start...!");
                while(!socket.isClosed()){
                    try {
                        String msg = br.readLine();
                        System.out.println("Client : "+msg);
                        if(msg.equalsIgnoreCase("exit")){
                            System.out.println("client terminated...!");
                            socket.close();
                            break;
                        }
                    } catch (IOException ex) {
                        System.out.println("connection closed...!");
                    }
                }
            };
            
            new Thread(r1).start();
        }catch(Exception ex){
            System.out.println("connection closed...!");
        }
    }

    private void startWriting() {
        try{
            Runnable r2 = () -> {
                System.out.println("writer start...!");
                while(!socket.isClosed()){
                    try{
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                        String readLine = br1.readLine();
                        out.println(readLine);
                        out.flush();
                        if(readLine.equalsIgnoreCase("exit")){
                            socket.close();
                            break;
                        }
                    }catch(Exception ex){
                        System.out.println("connection closed...!");
                    }
                }
            };
            
            new Thread(r2).start();
        }catch(Exception ex){
            System.out.println("connection closed...!");
        }
    }
}
