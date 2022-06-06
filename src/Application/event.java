package Application;

import javax.swing.*;
import javax.tools.Tool;
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
    static int port_attendance=5678,port_signIn=2345,port_signUp=3456;
    static String emailHost="localhost";
    static int emailPort=5555;


//sending emails to the reported students:-
    void sendReport(String to){
        try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost/emailSystem","root","root");
            Statement st=con.createStatement();
            String sql="INSERT INTO email(sender,receiver,msg) VALUES ( '@NCTU','"+ to+"', 'Warning you have exceeded the limitation of absent in university!Are you ok son?');";
            st.executeUpdate(sql);
            con.close();
        }catch (Exception er){
            er.printStackTrace();
        }
        System.out.println("method executed!");
    }

    //inserting email addresses into tables in DB:-
    void addMails() throws SQLException {
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost/emailSystem","root","root");
        Statement st=con.createStatement();
        String arr[]=
                {"AhmedAshraf",//1
                "AhmedKhaled",//2
                "AhmedSalama",//3
                "ayaAyman",//4
                "AyaAlaa",//5
                "PeterBassem",//6
                "JosephMagid",//7
                "HabibaNagi",//8
                "HassanMuhammed",//9
                "KhalidMuhammad",//10
                "AbdulRahmanOsama",//11
                "AbdulRahmanRajab",//12
                "AbdullahAsher",//13
                "KarimAhmed",//14
                "KarimTarek",//15
                "MazenTarek",//16
                "MuhammadAhmadMuhammad",//17
                "MohammedHossamFouad",//18
                "MuhammadHusamMuhammad",//19
                "MuhammadHisham",//20
                "MuhammadYusuf",//21
                "MahmoudMohamedFakir",//22
                "MustafaMorsi",//23
                "HebaGomaa",//24
                "HamsaSameh",//25
                "WesamAhmed",//26
                "YasminAhmed",//27
                "YasmineMagdy",//28
                "YoussefAhmed",//29
                "YoussefTariqHussein",//30
                "YoussefTariqAbdelMoneim",//31
                "YoussefAbdullah",//32
         };

        for(String a:arr){
            String sql="INSERT INTO auth(username,password) VALUES ('"+ a +"','123' );";
            st.executeUpdate(sql);
        }

        System.out.println("emails added successfully!");
    }




    String printPercentage(){
        String _final="";
        String sql="SELECT subject, (Select COUNT(*) from atend) as t , count(*) as v from atend where isAttended=1 group by subject;";
        try{
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance","root","root");
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                String subject=rs.getString(1);
                float t=rs.getInt(2);
                float v=rs.getInt(3);
                _final+="The Total Percentage Of Attendance for :"+subject+" is "+(v/t)*100+"%\n";
            }
        }catch (Exception er){
            er.printStackTrace();
        }
        return _final;
    }

    void receiveAttend(){

        try {
            String student= tools.receive(ss.accept());
            String subject=tools.receive(ss.accept());
            String isAttend=tools.receive(ss.accept());

            req.add("Client-Request : Adding new Attendance!\n\n\n\n\n");
            req_txt.setText(Arrays.toString(req.toArray()));
            if(!student.equals("") && !student.equals(null) && !subject.equals("") && !subject.equals(null) && !isAttend.equals("") && !isAttend.equals(null)){
                if(Integer.parseInt(isAttend)==0){
                    Thread.sleep(200);
                    sendReport(student);
                    System.out.println("entered condition!");
                }
                //1)-display the requests and responses into the textAreas:-
                res.add("Server-Response : Yes!\n\n\n\n\n");
                res_txt.setText(Arrays.toString(res.toArray()));
                //2)-Store the data into the sever database:-
                String query="insert into atend(student,isAttended,subject) values('"+student+"',"+Integer.parseInt(isAttend)+",'"+subject+"');";
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


    event() throws SQLException {
        long startTime=System.currentTimeMillis();

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
            JOptionPane.showMessageDialog(app, printPercentage(),
                    "Attendance-Percentage", JOptionPane.PLAIN_MESSAGE);
            Thread HandleData=new Thread(() -> {
                try {
                    if(ss.isClosed()){
                        ss=new ServerSocket(port_attendance);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                while (true){
                    receiveAttend(); //receiving each student attendance and store it in database.
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

        long endTime=System.currentTimeMillis();

        System.out.println("Benchmarks:"+(endTime-startTime));
    }
}
