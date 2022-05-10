package Application;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class event extends gui{
    static int count=0;
    TCP_NET tools;
    ServerSocket ss;
    Connection con;
    ArrayList req,res;

    event(){
        tools=new TCP_NET();
        req=new ArrayList<String>();
        res=new ArrayList<String>();

        try {
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance","root","root");
            ss=new ServerSocket(5555);
        } catch (Exception e) {e.printStackTrace();}

        //creating event handlers:-
        ext_btn.addActionListener(e -> System.exit(1));

        open_btn.addActionListener(e -> {
            count+=1;
            Thread HandleData=new Thread(() -> {
                try {
                    if(ss.isClosed()){
                        ss=new ServerSocket(5555);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                while (true){
                    try {
                       Socket s=ss.accept();
                       String msg= tools.receive(s);
                       req.add("Client-Request : Adding new Attendance!\n\n");
                        req_txt.setText(Arrays.toString(req.toArray()));
                       if(!msg.equals("") && !msg.equals(null)){
                           //1)-display the requests and responses into the textAreas:-
                            res.add("Server-Response : Yes!\n\n");
                            res_txt.setText(Arrays.toString(res.toArray()));
                           //2)-Store the data into the sever database:-
                            String query="insert into atend(atendance) values('"+msg+"')";
                            Statement statement=con.createStatement();
                            statement.executeUpdate(query);
                       }else {
                           res.add("Server-Response : No!\n\n");
                           res_txt.setText(Arrays.toString(res.toArray()));
                       }
                    } catch (Exception ex) {ex.printStackTrace();}
                }
            });

            if(count%2!=0){
                status_lbl.setText("Server-Started");
                HandleData.start();
            }else {
                status_lbl.setText("Server-Stopped");
                HandleData.stop();
            }
        });

    }

}
