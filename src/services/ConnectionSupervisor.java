package services;

import models.User;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ConnectionSupervisor extends Thread {
    private User user;
    private Socket socket;
    private ArrayList<User> users;
    private final CipherBase64 cipherBase64 = new CipherBase64();

    public ConnectionSupervisor(Socket socket, ArrayList<User> users) throws Exception {
        if (socket == null) throw new Exception("Port is null");
        if (users == null) throw new Exception("Users are null");

        this.socket = socket;
        this.users = users;
    }

    @Override
    public void run() {
        OutputStreamWriter outputStreamWriter;
        try {
            outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream());
        } catch (Exception e) {
            return;
        }

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(this.socket.getInputStream());
        } catch (Exception e) {
            try {
                outputStreamWriter.close();
            } catch (Exception e2) {
            }
            return;
        }

        try {
            this.user = new User(this.socket, inputStreamReader, outputStreamWriter);
        } catch (Exception e) {
        }

        try {
            synchronized (this.users) {
                this.users.add(this.user);
            }

            while (true) {
                String message = this.user.send();

                if (message == null) return;
                else {
                    StringTokenizer stringTokenizer = new StringTokenizer(message, ":");
                    String option = stringTokenizer.nextToken();
                    if (option.equalsIgnoreCase("ENCRYPT")) {
                        this.user.receive(cipherBase64.encrypt(stringTokenizer.nextToken()));
                    } else if (option.equalsIgnoreCase("DECRYPT")) {
                        this.user.receive(cipherBase64.decrypt(stringTokenizer.nextToken()));
                    } else if (option.equalsIgnoreCase("EXIT")) {
                        synchronized (this.users) {
                            this.users.remove(this.user);
                        }

                        this.user.goodbye();
                    }
                }
            }
        } catch (Exception e) {
            try {
                outputStreamWriter.close();
                inputStreamReader.close();
            } catch (Exception e2) {
            }
        }

        return;
    }
}
