package com.company;
import javax.swing.*;
import java.awt.*;
public class MyJFrame extends JFrame{
    //custom JFrame
    public MyJFrame(String title){
        super(title);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxHeight = (int) screenSize.getHeight();
        int maxWidth = (int) screenSize.getWidth();
        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        this.setSize(maxWidth/2,maxHeight/2);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setFocusable(true);
    } //end Constructor
}//end MyJFrame