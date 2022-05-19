package Application;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class event extends gui{
    static int count=0;
    TCP_NET tools;
    ServerSocket ss,subSS,serverSocketSignIn,serverSocketSignUp;
    Socket msgS,subS;
    Connection con;
    ArrayList req,res;
    static int port_attendance=5555,port_signIn=2222,port_signUp=3333;

    event(){
        tools=new TCP_NET();
        req=new ArrayList<String>();
        res=new ArrayList<String>();

        try {
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance","root","root");
            ss=new ServerSocket(port_attendance);
        } catch (Exception e) {e.printStackTrace();}

        //creating event handlers:-
        ext_btn.addActionListener(e -> System.exit(1));

        open_btn.addActionListener(e -> {
            count+=1;

            Thread HandleData=new Thread(() -> {
                try {
                    if(ss.isClosed()){
                        ss=new ServerSocket(port_attendance);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                while (true){
                    try {
                       String msg= tools.receive(ss.accept());
                       String subject=tools.receive(ss.accept());

                       req.add("Client-Request : Adding new Attendance!\n\n\n\n\n");
                        req_txt.setText(Arrays.toString(req.toArray()));
                       if(!msg.equals("") && !msg.equals(null) && !subject.equals("") && !subject.equals(null)){
                           //1)-display the requests and responses into the textAreas:-
                            res.add("Server-Response : Yes!\n\n\n\n\n");
                            res_txt.setText(Arrays.toString(res.toArray()));
                           //2)-Store the data into the sever database:-
                            String query="insert into atend(atendance,subject) values('"+msg+"','"+subject+"');";
                            Statement statement=con.createStatement();
                            statement.executeUpdate(query);
                       }else {
                           res.add("Server-Response : No!\n\n\n\n\n");
                           res_txt.setText(Arrays.toString(res.toArray()));
                       }
                    } catch (Exception ex) {
                        System.out.println("Error attend:"+ex);
                    }
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




        Thread signIn=new Thread(()->{
            try {
                serverSocketSignIn=new ServerSocket(port_signIn);
            }catch (Exception er){er.printStackTrace();}
            while(true){
                try{
                    String username=tools.receive(serverSocketSignIn.accept());
                    String password=tools.receive(serverSocketSignIn.accept());
                    req.add("Client requests : signIn \n with username :"+username+" password:"+password+"\n\n\n");
                    req_txt.setText(req.toString());
                    String sql="select * from auth where username='"+username+"' and password='"+password+"';";
                    Statement statement=con.createStatement();
                    ResultSet rs=statement.executeQuery(sql);

                    if (rs.next()){
                        if(username.equals(rs.getString(1)) && password.equals(rs.getString(2))){
                            tools.send(serverSocketSignIn.accept(),"yes");
                            res.add("Server-Response : Yes!\n\n\n\n\n");
                            res_txt.setText(res.toString());
                        }
                    }else {
                        tools.send(serverSocketSignIn.accept(),"no");
                        res.add("Server-Response : No!\n\n\n\n\n");
                        res_txt.setText(res.toString());
                    }

                    System.out.println("user:"+username+" pass:"+password);
                }catch (Exception ex){
                    System.out.println("signUp error:"+ex);
                    try {
                        tools.send(serverSocketSignIn.accept(),"no");
                        res.add("Server-Response : No!\n\n\n\n\n");
                        res_txt.setText(res.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        signIn.start();





        Thread signUp=new Thread(()->{
            try {
                serverSocketSignUp=new ServerSocket(port_signUp);
            }catch (Exception er){er.printStackTrace();}
            while(true){
                try{
                    String username=tools.receive(serverSocketSignUp.accept());
                    String password=tools.receive(serverSocketSignUp.accept());
                    req.add("Client requests : signUp \n with username :"+username+" password:"+password+"\n\n\n");
                    req_txt.setText(req.toString());
                    String sql="insert into auth(username,password) values('"+username+"','"+password+"');";
                    Statement Stmnt=con.createStatement();
                    Stmnt.executeUpdate(sql);
                    tools.send(serverSocketSignUp.accept(),"yes");
                    res.add("Server-Response : Yes!\n\n\n\n\n");
                    res_txt.setText(res.toString());
                    System.out.println("user:"+username+" pass:"+password);
                }catch (Exception ex){
                    System.out.println("signUp error:"+ex);
                    try {
                        tools.send(serverSocketSignUp.accept(),"no");
                        res.add("Server-Response : No!\n\n\n\n\n");
                        res_txt.setText(res.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        signUp.start();



    }
}
