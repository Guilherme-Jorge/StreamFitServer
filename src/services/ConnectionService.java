package services;

import models.User;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionService extends Thread {
    private ServerSocket request;
    private ArrayList<User> users;

    public ConnectionService(String port, ArrayList<User> users) throws Exception {
        if (port == null) throw new Exception("Port is null");

        try {
            this.request = new ServerSocket(Integer.parseInt(port));
        } catch (Exception e) {
            throw new Exception("Invalid port");
        }

        this.users = users;
    }

    @Override
    public void run() {
        while (true) {
            Socket socket;

            try {
                socket = this.request.accept();
            } catch (Exception e) {
                continue;
            }

            ConnectionSupervisor connectionSupervisor = null;

            try {
                connectionSupervisor = new ConnectionSupervisor(socket, this.users);
            } catch (Exception e) {
            }

            connectionSupervisor.start();
        }
    }
}
