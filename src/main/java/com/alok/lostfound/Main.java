package com.alok.lostfound;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Main {
    static final Color NAVY=new Color(28,39,66), BLUE=new Color(55,105,190);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored){}
            new LoginFrame().setVisible(true);
        });
    }
    static void error(Component c,Exception e){JOptionPane.showMessageDialog(c,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    static class LoginFrame extends JFrame {
        JTextField email=new JTextField(20); JPasswordField pass=new JPasswordField(20);
        LoginFrame(){
            setTitle("Lost & Found | Login");setSize(430,330);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);
            JPanel p=new JPanel(new GridBagLayout());p.setBorder(BorderFactory.createEmptyBorder(25,30,25,30));p.setBackground(Color.WHITE);
            GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(8,6,8,6);g.fill=GridBagConstraints.HORIZONTAL;
            JLabel title=new JLabel("LOST & FOUND",SwingConstants.CENTER);title.setFont(new Font("Arial",Font.BOLD,25));title.setForeground(NAVY);
            g.gridx=0;g.gridy=0;g.gridwidth=2;p.add(title,g);g.gridwidth=1;
            g.gridy++;g.gridx=0;p.add(new JLabel("Email"),g);g.gridx=1;p.add(email,g);
            g.gridy++;g.gridx=0;p.add(new JLabel("Password"),g);g.gridx=1;p.add(pass,g);
            JButton login=new JButton("Login"),reg=new JButton("Create account");login.setBackground(BLUE);login.setForeground(Color.WHITE);
            g.gridy++;g.gridx=0;p.add(login,g);g.gridx=1;p.add(reg,g);add(p);
            login.addActionListener(e->doLogin());reg.addActionListener(e->doRegister());
        }
        void doLogin(){try{User u=Auth.login(email.getText().trim(),new String(pass.getPassword()));if(u==null)JOptionPane.showMessageDialog(this,"Invalid email or password.");else{dispose();new Dashboard(u).setVisible(true);}}catch(Exception e){error(this,e);}}
        void doRegister(){
            JTextField n=new JTextField(),em=new JTextField();JPasswordField pw=new JPasswordField();
            Object[] fields={"Full name",n,"Email",em,"Password",pw};
            if(JOptionPane.showConfirmDialog(this,fields,"Create account",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                try{if(n.getText().isBlank()||em.getText().isBlank()||pw.getPassword().length<6)throw new IllegalArgumentException("Enter all fields. Password must be at least 6 characters.");
                    Auth.register(n.getText().trim(),em.getText().trim(),new String(pw.getPassword()));JOptionPane.showMessageDialog(this,"Account created. Please log in.");
                }catch(Exception e){error(this,e);}
            }
        }
    }
    static class Dashboard extends JFrame {
        User current; JTable table; DefaultTableModel model; JComboBox<String> filter;
        Dashboard(User u){
            current=u;setTitle("Lost & Found Management System");setSize(1050,600);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);
            JPanel top=new JPanel(new BorderLayout());top.setBackground(NAVY);top.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
            JLabel heading=new JLabel("Lost & Found Management System");heading.setForeground(Color.WHITE);heading.setFont(new Font("Arial",Font.BOLD,22));
            JLabel who=new JLabel("Signed in: "+u.name()+" ("+u.role()+")");who.setForeground(Color.WHITE);top.add(heading,BorderLayout.WEST);top.add(who,BorderLayout.EAST);add(top,BorderLayout.NORTH);
            model=new DefaultTableModel(new String[]{"ID","Title","Description","Category","Location","Type","Date","Status","Reported By"},0){public boolean isCellEditable(int r,int c){return false;}};
            table=new JTable(model);table.setRowHeight(25);add(new JScrollPane(table),BorderLayout.CENTER);
            JPanel bar=new JPanel(new FlowLayout(FlowLayout.LEFT,10,12));filter=new JComboBox<>(new String[]{"ALL","LOST","FOUND"});
            JButton refresh=new JButton("Refresh"),add=new JButton("Report item"),claim=new JButton("Claim selected item");
            bar.add(new JLabel("Show:"));bar.add(filter);bar.add(refresh);bar.add(add);bar.add(claim);add(bar,BorderLayout.SOUTH);
            refresh.addActionListener(e->load());filter.addActionListener(e->load());add.addActionListener(e->addItem());claim.addActionListener(e->claimItem());
            load();
        }
        void load(){try{model.setRowCount(0);List<Item> items=ItemDAO.all((String)filter.getSelectedItem());for(Item i:items)model.addRow(new Object[]{i.id(),i.title(),i.description(),i.category(),i.location(),i.type(),i.date(),i.status(),i.reporter()});}catch(Exception e){error(this,e);}}
        void addItem(){
            JTextField title=new JTextField(),category=new JTextField(),location=new JTextField(),date=new JTextField();
            JTextArea desc=new JTextArea(3,20);JComboBox<String> type=new JComboBox<>(new String[]{"LOST","FOUND"});
            Object[] f={"Title",title,"Description",new JScrollPane(desc),"Category",category,"Location",location,"Type",type,"Date (YYYY-MM-DD; optional)",date};
            if(JOptionPane.showConfirmDialog(this,f,"Report an item",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                try{if(title.getText().isBlank()||location.getText().isBlank())throw new IllegalArgumentException("Title and location are required.");
                    ItemDAO.add(current,title.getText().trim(),desc.getText().trim(),category.getText().trim(),location.getText().trim(),(String)type.getSelectedItem(),date.getText().trim());
                    load();JOptionPane.showMessageDialog(this,"Item reported successfully.");
                }catch(Exception e){error(this,e);}
            }
        }
        void claimItem(){
            int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select an item first.");return;}
            int id=(int)model.getValueAt(row,0);String status=(String)model.getValueAt(row,7);
            if(status.equals("CLOSED")){JOptionPane.showMessageDialog(this,"This item is closed.");return;}
            String msg=JOptionPane.showInputDialog(this,"Describe why this item belongs to you:");
            if(msg!=null&&!msg.isBlank())try{ItemDAO.claim(current,id,msg.trim());JOptionPane.showMessageDialog(this,"Claim submitted. Contact the administrator for verification.");}catch(Exception e){error(this,e);}
        }
    }
}
