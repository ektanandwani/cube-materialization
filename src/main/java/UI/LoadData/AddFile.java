package UI.LoadData;

import Pojo.Schema.Attribute;
import Pojo.Schema.Dimension;
import Pojo.Schema.Fact;
import Pojo.Schema.StarSchema;
import Processing.DatabaseSetup;
import Services.LoadDataService;
import UI.SchemaCreation.FirstPage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddFile extends JFrame implements ActionListener {

    static JLabel l;
    StarSchema globalSchema = new StarSchema();
    Dimension d = new Dimension();
    String sname;
    ArrayList<Dimension> dim;
    String str;
    String filepath;
    ArrayList<Fact> facts;
    DatabaseSetup dbsetup = new DatabaseSetup();
    int typeOfLoad;
    // Components of the Form
    private Container c;
    private JLabel title;

    public AddFile(StarSchema s, int type) {

        globalSchema = s;
        typeOfLoad = type;
        sname = globalSchema.getName();
        dim = new ArrayList<Dimension>();
        dim = globalSchema.getDimension();
        facts = new ArrayList<Fact>();
        facts = globalSchema.getFact();


        setTitle("Data-Cube Management");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Upload the Excel File with following specifications: ");
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        title.setSize(800, 30);
        title.setLocation(170, 50);
        c.add(title);

        // button to open open dialog
        JButton button2 = new JButton("Open");
        button2.setFont(new Font("Arial", Font.PLAIN, 15));
        button2.setSize(100, 20);
        button2.setLocation(600, 380);
        button2.addActionListener(this);
        c.add(button2);

        JButton button3 = new JButton("Upload");
        button3.setFont(new Font("Arial", Font.PLAIN, 15));
        button3.setSize(100, 20);
        button3.setLocation(720, 380);
        button3.addActionListener(this);
        c.add(button3);

        l = new JLabel("No files selected");
        l.setFont(new Font("Arial", Font.PLAIN, 15));
        l.setSize(290, 20);
        l.setLocation(600, 350);
        c.add(l);

        String str = "";
        String str1 = "";
        String str2 = "";
        String fc = "";
        int y = 140;
        List<String> firstAttrNameOfAllDim = new ArrayList<>();
        str1 = "File name: " + s.getName() + "\n";
        System.out.println(str1);
        int t = 1;
        for (Dimension dimension : dim) {
            str = "Sheet" +t+ " name: " + dimension.getName() + "   " + " Columns in sequence : ";
            //System.out.println(str + "\n" + "Attribute in sequence: ");
            java.util.List<Attribute> attributeList = dimension.getAttributes();
            JLabel l2 = new JLabel(str);
            l2.setFont(new Font("Arial", Font.PLAIN, 15));
            l2.setSize(890, 20);
            l2.setLocation(20, y);
            c.add(l2);
            int b = y + 20;
            int a = 250;
            int g = 1;
            for (Attribute attribute : attributeList) {

                str2 = attribute.getName() + "";
                System.out.print(str2);
                JLabel l3 = new JLabel(str2);
                l3.setFont(new Font("Arial", Font.PLAIN, 15));
                l3.setSize(100, 20);
                l3.setLocation(a, b);
                c.add(l3);
                a = a + 100;
                if(g == 1){
                    firstAttrNameOfAllDim.add(attribute.getName());
                }
                g++;
            }
            y = y + 50;
            t++;
            System.out.println(t);
        }
        int q = y + 10;
        String factsnames = "";
        for (Fact fact : facts) {
            factsnames = factsnames + fact.getName() + "     ";
            System.out.println("--------------");
            System.out.println(factsnames);
        }
        String rr="";
        for(int z=0;z<firstAttrNameOfAllDim.size();z++){
                rr = rr + "    " + firstAttrNameOfAllDim.get(z);
        }
        String strr = "Sheet" + t + " name: facts " + "  Columns in sequence: ";
        JLabel newfactsnames = new JLabel( strr);
        newfactsnames.setFont(new Font("Arial", Font.PLAIN, 15));
        newfactsnames.setSize(890, 20);
        newfactsnames.setLocation(20, q);
        c.add(newfactsnames);

        q = q+21;
        String strr1 = factsnames;
        JLabel ll = new JLabel(rr + "    " + strr1);
        ll.setFont(new Font("Arial", Font.PLAIN, 15));
        ll.setSize(890, 20);
        ll.setLocation(231, q);
        c.add(ll);


        JLabel l1 = new JLabel(str1);
        l1.setFont(new Font("Arial", Font.PLAIN, 15));
        l1.setSize(850, 20);
        l1.setLocation(20, 100);
        c.add(l1);


        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Arial", Font.PLAIN, 15));
        exit.setSize(80, 20);
        exit.setLocation(810, 500);
        exit.addActionListener(this);
        c.add(exit);

        JButton home = new JButton("Go to Homepage");
        home.setFont(new Font("Arial", Font.PLAIN, 15));
        home.setSize(200, 20);
        home.setLocation(600, 500);
        home.addActionListener(this);
        c.add(home);



        //this.setVisible(true);


    }

    public void actionPerformed(ActionEvent evt) {

        String com = evt.getActionCommand();
        if (com.equals("Go to Homepage")){
            this.setVisible(false);
            FirstPage f = new FirstPage();
            f.setVisible(true);

        }
        if (com.equals("Open")) {
            // create an object of JFileChooser class
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            j.setFileSelectionMode(JFileChooser.FILES_ONLY);
            j.addChoosableFileFilter(new FileNameExtensionFilter("Excel File", "xlsx", "xls"));
            j.setAcceptAllFileFilterUsed(false);
            // invoke the showsOpenDialog function to show the save dialog
            int r = j.showOpenDialog(null);
            l.setText("Click on upload.");

            if (r == JFileChooser.APPROVE_OPTION) {
                filepath = j.getSelectedFile().getAbsolutePath();
                System.out.println(filepath);
            }
            // if the user cancelled the operation
            else
                l.setText("The user cancelled the operation");
        }
        if (com.equals("Exit")) {
            System.exit(0);
        }
        if (com.equals("Upload")) {

            if (typeOfLoad == 1){
                LoadDataService lds = new LoadDataService();
                boolean ans = lds.addDataService(globalSchema, filepath);
                if(ans) {
                    JOptionPane.showMessageDialog(this, "Successfully created new database!");
                    this.setVisible(false);
                    FirstPage f = new FirstPage();
                    f.setVisible(true);
                }
                else
                    JOptionPane.showMessageDialog(this, "Error in creating new database");
            }
            else{
                LoadDataService lds = new LoadDataService();
                boolean ans = lds.appendDataService (globalSchema, filepath);
                if(ans) {
                    JOptionPane.showMessageDialog(this, "Successfully updated database!");
                    this.setVisible(false);
                    FirstPage f = new FirstPage();
                    f.setVisible(true);
                }
                else
                    JOptionPane.showMessageDialog(this, "Error in updating database");
            }
            l.setText("");
        }

    }
}

//class AddFile {
//
//    public static void main(String[] args) throws Exception
//    {
//        A my = new A();
//    }
//}
