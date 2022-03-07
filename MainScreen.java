package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainScreen {
    //declare attributes
    private final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private final MyJFrame menuFrame;
    private final JButton startSim,settings,quitSim,fullscreen,normalscreen,back;
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private final int MAX_HEIGHT = (int) SCREEN_SIZE.getHeight();
    private final int MAX_WIDTH = (int) SCREEN_SIZE.getWidth();
    public static boolean isFullScreen = false;

    //create listeners for all buttons in start and settings frames
    public class StartButtonPressed implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //get rid of the start frame and call the simulation frame
            menuFrame.setVisible(false);
            menuFrame.dispose();
            new SimulationScreen();
        }//end actionPerformed
    }//end startButtonPressed
    public class SettingsButtonPressed implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //go from start menu to settings menu
            if(isFullScreen) {
                fullscreen.setBounds(MAX_WIDTH/4, MAX_HEIGHT/8, MAX_WIDTH/2, MAX_HEIGHT/6);
                normalscreen.setBounds(MAX_WIDTH/4, (MAX_HEIGHT*5)/12, MAX_WIDTH/2, MAX_HEIGHT/6);
                back.setBounds(MAX_WIDTH/4, (MAX_HEIGHT*17)/24, MAX_WIDTH/2, MAX_HEIGHT/6);
            }//end if statement
            else {
                fullscreen.setBounds(MAX_WIDTH/8, MAX_HEIGHT/24, MAX_WIDTH/4, MAX_HEIGHT/12);
                normalscreen.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*4)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
                back.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*7)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
            }//end else statement
            //switch buttons displayed on the frame
            menuFrame.repaint();
            menuFrame.remove(startSim);
            menuFrame.remove(settings);
            menuFrame.remove(quitSim);
            menuFrame.add(fullscreen);
            menuFrame.add(normalscreen);
            menuFrame.add(back);
            menuFrame.repaint();
        }//end actionPerformed
    }//end settingsButtonPressed
    public class QuitButtonPressed implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //exit the program
            menuFrame.dispose();
            System.exit(0);
        }//end actionPerformed
    }//end quitButtonPressed
    public class FullscreenPressed implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //check if frame is already in fullscreen
            if(isFullScreen) return;
            isFullScreen = true;
            //set to full screen
            device.setFullScreenWindow(menuFrame);
            fullscreen.setBounds(MAX_WIDTH/4, MAX_HEIGHT/8, MAX_WIDTH/2, MAX_HEIGHT/6);
            normalscreen.setBounds(MAX_WIDTH/4, (MAX_HEIGHT*5)/12, MAX_WIDTH/2, MAX_HEIGHT/6);
            back.setBounds(MAX_WIDTH/4, (MAX_HEIGHT*17)/24, MAX_WIDTH/2, MAX_HEIGHT/6);
            menuFrame.repaint();
        }//end actionPerformed
    }//end fullscreenPressed
    public class NormalscreenPressed implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //check if frame is already in default resolution
            if(!isFullScreen) return;
            isFullScreen = false;
            //set to default resolution
            device.setFullScreenWindow(null);
            fullscreen.setBounds(MAX_WIDTH/8, MAX_HEIGHT/24, MAX_WIDTH/4, MAX_HEIGHT/12);
            normalscreen.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*4)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
            back.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*7)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
            menuFrame.repaint();
        }//end actionPerformed
    }//end normalScreenPressed
    public class BackButtonPressed implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //go from back menu to settings menu;
            menuFrame.remove(fullscreen);
            menuFrame.remove(normalscreen);
            menuFrame.remove(back);
            menuFrame.add(startSim);
            menuFrame.add(settings);
            menuFrame.add(quitSim);
            if(isFullScreen) {
                startSim.setBounds(MAX_WIDTH/4, MAX_HEIGHT/8, MAX_WIDTH/2, MAX_HEIGHT/6);
                settings.setBounds(MAX_WIDTH/4, (MAX_HEIGHT*5)/12, MAX_WIDTH/2, MAX_HEIGHT/6);
                quitSim.setBounds(MAX_WIDTH/4, (MAX_HEIGHT*17)/24, MAX_WIDTH/2, MAX_HEIGHT/6);
            }//end if statement
            else {
                startSim.setBounds(MAX_WIDTH/8, MAX_HEIGHT/24, MAX_WIDTH/4, MAX_HEIGHT/12);
                settings.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*4)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
                quitSim.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*7)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
            }//end else statement
            menuFrame.repaint();
        }//end actionPerformed
    }//end backButtonPressed

    //constructor
    public MainScreen(){
        menuFrame = new MyJFrame("Welcome to CSE Fractal Simulation");
        startSim = new JButton("Start Simulation");
        settings = new JButton("Settings");
        quitSim = new JButton("Quit");
        fullscreen = new JButton("Fullscreen");
        normalscreen = new JButton("Default Resolution");
        back = new JButton("Back");
        //call method to start thread
        kickoff();
    }//end constructor

    //create the menu
    public void createStartMenu(){
        //instantiate listeners for start menu
        StartButtonPressed startList = new StartButtonPressed();
        SettingsButtonPressed setList = new SettingsButtonPressed();
        QuitButtonPressed qList = new QuitButtonPressed();
        FullscreenPressed fList = new FullscreenPressed();
        NormalscreenPressed nList = new NormalscreenPressed();
        BackButtonPressed bList = new BackButtonPressed();
        //create frame
        menuFrame.setSize(MAX_WIDTH/2,MAX_HEIGHT/2);
        menuFrame.setLayout(null);
        //start button
        startSim.addActionListener(startList);
        menuFrame.add(startSim);
        //settings button
        settings.addActionListener(setList);
        menuFrame.add(settings);
        //quit button
        quitSim.addActionListener(qList);
        menuFrame.add(quitSim);
        //set bounds for buttons
        startSim.setBounds(MAX_WIDTH/8, MAX_HEIGHT/24, MAX_WIDTH/4, MAX_HEIGHT/12);
        settings.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*4)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
        quitSim.setBounds(MAX_WIDTH/8, (MAX_HEIGHT*7)/24, MAX_WIDTH/4, MAX_HEIGHT/12);
        menuFrame.repaint();
        menuFrame.setVisible(true);
        //create buttons for setting menu
        //fullscreen button
        fullscreen.addActionListener(fList);
        //normalscreen button
        normalscreen.addActionListener(nList);
        //back button
        back.addActionListener(bList);
    }//end createStartMenu

    //GUI thread
    public void kickoff(){SwingUtilities.invokeLater(this::createStartMenu);}
}//end FractalGUI