package models;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class User {
    private Socket socket;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private OutputStreamWriter outputStreamWriter;
    private BufferedWriter bufferedWriter;
    private String nextMessage = null;
    private Semaphore mutEx = new Semaphore(1, true);

    public User(Socket socket, InputStreamReader inputStreamReader, OutputStreamWriter outputStreamWriter) throws Exception {
        if (socket == null) throw new Exception("Socket is null");
        if (inputStreamReader == null) throw new Exception("InputStreamReader is null");
        if (outputStreamWriter == null) throw new Exception("OutputStreamWriter is null");

        this.socket = socket;
        this.inputStreamReader = inputStreamReader;
        this.bufferedReader = new BufferedReader(inputStreamReader);
        this.outputStreamWriter = outputStreamWriter;
        this.bufferedWriter = new BufferedWriter(outputStreamWriter);
    }

    public void receive(String m) throws Exception {
        try {
            this.bufferedWriter.write(m);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            throw new Exception("Error from BufferedWriter");
        }
    }

    public String spy() throws Exception {
        try {
            this.mutEx.acquireUninterruptibly();
            if (this.nextMessage == null) this.nextMessage = this.bufferedReader.readLine();
            this.mutEx.release();
            return this.nextMessage;
        } catch (Exception e) {
            throw new Exception("Error from BufferedReader");
        }
    }

    public String send() throws Exception {
        try {
            if (this.nextMessage == null) this.nextMessage = this.bufferedReader.readLine();
            String message = this.nextMessage;
            this.nextMessage = null;
            return message;
        } catch (Exception e) {
            throw new Exception("Error from BufferedReader");
        }
    }

    public void goodbye() throws Exception {
        try {
            this.inputStreamReader.close();
            this.outputStreamWriter.close();
            this.bufferedWriter.close();
            this.bufferedReader.close();
            this.socket.close();
        } catch (Exception e) {
            throw new Exception("Error when disconnecting");
        }
    }
}
