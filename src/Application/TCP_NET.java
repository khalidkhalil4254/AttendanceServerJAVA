package Application;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;


public class TCP_NET {
    private Socket s;
    private ServerSocket ss;
    private PrintWriter out;
    private Scanner in;



    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    public ServerSocket getSs() {
        return ss;
    }

    public void setSs(ServerSocket ss) {
        this.ss = ss;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public Scanner getIn() {
        return in;
    }

    public void setIn(Scanner in) {
        this.in = in;
    }


    Socket createSocket(String address,int port){
        Socket s=null;
        try {
            s=new Socket(address,port);
        }catch (Exception er){}
        return s;
    }

    ServerSocket createServerSocket(int port){
        ServerSocket ss=null;
        try{
            ss=new ServerSocket(port);
        }catch (Exception er){

        }
        return ss;
    }


    void send(Socket socket,String msg) throws IOException {
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            out.writeUTF(msg);
            out.flush();
            out.close();
    }


    String receive(Socket socket) throws IOException {
        String res;
            DataInputStream in=new DataInputStream(socket.getInputStream());
            res=in.readUTF();
        return res;
    }

    void closeEveryThing(ServerSocket ss,Socket s,DataInputStream dIN,DataOutputStream dOUT){
        try {
            s.close();
            ss.close();
            dIN.close();
            dOUT.close();
        }catch (Exception er){}
    }



}
