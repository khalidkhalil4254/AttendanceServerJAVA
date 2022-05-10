package Application;
import javax.swing.*;
import java.awt.*;

public class gui {
    JFrame app;
    JLabel server_lbl,req_lbl,res_lbl,status_lbl;
    JTextArea req_txt,res_txt;
    JButton ext_btn,open_btn;
    JScrollPane scroll,scroll0;

    gui(){

        //creating the main application frame:-
        app=new JFrame("Attendance-Server-Application");
        app.setResizable(false);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(1125,800);
        app.setLayout(null);


        //creating components:-
        server_lbl=new JLabel("Attendance-Server");
        server_lbl.setBounds(420,30,500,30);
        server_lbl.setFont(new Font("Verdana", Font.BOLD, 24));
        req_lbl=new JLabel("Requests:-");
        req_lbl.setBounds(30,80,500,30);
        req_lbl.setFont(new Font("Verdana", Font.PLAIN, 20));
        res_lbl=new JLabel("Responses:-");
        res_lbl.setBounds(580,80,500,30);
        res_lbl.setFont(new Font("Verdana", Font.PLAIN, 20));
        status_lbl=new JLabel("Server\nStatus");
        status_lbl.setBounds(480,680,500,30);
        status_lbl.setFont(new Font("Verdana", Font.PLAIN, 16));
        req_txt=new JTextArea();
        req_txt.setEditable(false);
         scroll = new JScrollPane (req_txt,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(30,130,500,500);
        scroll.setFont(new Font("Verdana", Font.BOLD, 24));
        res_txt=new JTextArea();
        res_txt.setEditable(false);
         scroll0 = new JScrollPane (res_txt,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll0.setBounds(580,130,500,500);
        scroll0.setFont(new Font("Verdana", Font.BOLD, 24));
        ext_btn=new JButton("Exit");
        ext_btn.setBounds(30,650,80,50);
        ext_btn.setFont(new Font("Verdana", Font.PLAIN, 16));
        open_btn=new JButton("OnOff");
        open_btn.setBounds(1000,650,80,50);
        open_btn.setFont(new Font("Verdana", Font.PLAIN, 15));


        //adding created components into the main app frame:-
        app.add(server_lbl);
        app.add(req_lbl);
        app.add(res_lbl);
        app.add(status_lbl);
        app.add(scroll);
        app.add(scroll0);
        app.add(ext_btn);
        app.add(open_btn);


        //showing the created app to the user:-
        app.setVisible(true);

    }
}
