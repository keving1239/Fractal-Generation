package com.company;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SimulationScreen implements Runnable{
    //declare attributes
    private final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private final MyJFrame fractalFrame;
    private PaintedPanel fractalPanel;
    private final JComboBox<String> generationType,type,iterations,set;
    private final JButton simulateButton;
    private static JLabel timeDisplay;
    //resolution options
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private final int MAX_HEIGHT = (int) SCREEN_SIZE.getHeight();
    private final int MAX_WIDTH = (int) SCREEN_SIZE.getWidth();
    //ComboBox choices
    private final String[] algorithms = {"Mandelbrot","Koch","Sierpinski"};
    private final String[] kochTypes = {"Snowflake","Anti-snowflake"};
    private final String[] sierpTypes = {"Triangle","Square"};
    private final String[] mandItr = {"1","10","20","50","100","5000"};
    private final String[] kochItr = {"1","2","3","4","5","10"};
    private final String[] sierpItr = {"1","2","3","4","5","10"};
    private final String[] formulas = {"z^2+c","z^3+c","z^4+c","z^8+c"};

    //create listeners
    public class SimulateListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){updatePanel();}
    }//end simulateListener
    public class GenerationTypeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){updateChoices();}
    }//end generationTypeListener
    public class EscapeListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {/*do nothing*/}
        @Override
        public void keyPressed(KeyEvent e) {/*do nothing*/}
        @Override
        public void keyReleased(KeyEvent e) {
            //close when escape is released
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                fractalFrame.dispose();
                System.exit(0);
            }//end if statement
            if(e.getKeyCode() == KeyEvent.VK_ENTER) updatePanel();
        }//end keyReleased
    }//end escapeListener

    public SimulationScreen(){
        //create frame, comboBoxes, and button
        fractalFrame = new MyJFrame("Simulation");
        generationType = new JComboBox<>(algorithms);
        iterations = new JComboBox<>(mandItr);
        type = new JComboBox<>(kochTypes);
        set = new JComboBox<>(formulas);
        simulateButton = new JButton("Simulate");
        //start thread
        Thread simulation = new Thread(this,"SimulationThread");
        simulation.start();
    }//end constructor

    @Override
    public void run() {createSimulation();}//end run

    public void createSimulation(){
        //create listeners
        GenerationTypeListener gList = new GenerationTypeListener();
        EscapeListener eList = new EscapeListener();
        SimulateListener bList = new SimulateListener();
        //create frame
        if (MainScreen.isFullScreen) device.setFullScreenWindow(fractalFrame);
        else fractalFrame.setSize((MAX_WIDTH/2), (MAX_HEIGHT/2));
        fractalFrame.setLayout(null);
        fractalFrame.addKeyListener(eList);
        fractalFrame.setFocusable(true);
        //create panel for comboBoxes
        JPanel comboPanel = new JPanel();
        comboPanel.setBackground(Color.LIGHT_GRAY);
        comboPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        comboPanel.setLayout(new GridLayout(6,2));
        //time JLabel
        JLabel timeDescription = new JLabel("Run-Time: ",JLabel.CENTER);
        timeDisplay = new JLabel("0 ms",JLabel.CENTER);
        timeDescription.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        timeDescription.setToolTipText("Running total of the time taken to create the fractal image.");
        comboPanel.add(timeDescription);
        timeDisplay.setOpaque(true);
        timeDisplay.setBackground(Color.WHITE);
        timeDisplay.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        comboPanel.add(timeDisplay);
        //generation ComboBox/JLabel
        JLabel genDescription = new JLabel("Generation Type:",JLabel.CENTER);
        genDescription.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        genDescription.setToolTipText("Recursive/iterative method used to create the fractal image.");
        comboPanel.add(genDescription);
        generationType.addActionListener(gList);
        generationType.addKeyListener(eList);
        generationType.setSelectedIndex(0);
        comboPanel.add(generationType);
        //type ComboBox/JLabel
        JLabel typeDescription = new JLabel("Fractal Type:",JLabel.CENTER);
        typeDescription.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        typeDescription.setToolTipText("Specific algorithm subtype used to create the fractal image.");
        comboPanel.add(typeDescription);
        type.addKeyListener(eList);
        type.setSelectedIndex(0);
        comboPanel.add(type);
        //iterations ComboBox/JLabel
        JLabel itrDescription = new JLabel("Iterations:",JLabel.CENTER);
        itrDescription.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        itrDescription.setToolTipText("Number of iterations used to generate fractal image." +
                "\nNote: Typically, more iterations use more computing power.");
        comboPanel.add(itrDescription);
        iterations.addKeyListener(eList);
        iterations.setSelectedIndex(0);
        comboPanel.add(iterations);
        //setComboBox/JLabel
        JLabel setDescription = new JLabel("Formula:",JLabel.CENTER);
        setDescription.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        setDescription.setToolTipText("Subtype for fractal image generation.");
        comboPanel.add(setDescription);
        set.addKeyListener(eList);
        set.setSelectedIndex(0);
        comboPanel.add(set);
        //button/JLabel
        JLabel buttonDescription = new JLabel("Press to start simulation:",JLabel.CENTER);
        buttonDescription.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        comboPanel.add(buttonDescription);
        simulateButton.addActionListener(bList);
        simulateButton.addKeyListener(eList);
        comboPanel.add(simulateButton);
        //add comboBox panel to frame
        //height adjusted to account for title at top
        int hAdjust = MAX_HEIGHT/24;
        if(MainScreen.isFullScreen)comboPanel.setBounds((MAX_HEIGHT*23)/24,0,(MAX_WIDTH-(MAX_HEIGHT*23)/24)-10,MAX_HEIGHT-hAdjust);
        else comboPanel.setBounds(((MAX_HEIGHT*11)/24),0,((MAX_WIDTH/2)-((MAX_HEIGHT*11)/24))-10,(MAX_HEIGHT/2)-hAdjust);
        fractalFrame.add(comboPanel);
        fractalFrame.repaint();
        fractalFrame.setVisible(true);
    }//end createSimulation

    public void updateChoices(){
        //change options for mandelbrot
        if(generationType.getSelectedIndex() == 0){
            iterations.removeAllItems();
            for(String i : mandItr){
                iterations.addItem(i);
            }//end for loop
            iterations.setSelectedIndex(0);
            type.removeAllItems();
            type.addItem("N/A");
            set.removeAllItems();
            for(String f : formulas){
                set.addItem(f);
            }//end for loop
        }//end if statement

        //change options for koch
        if(generationType.getSelectedIndex() == 1){
            iterations.removeAllItems();
            for(String s : kochItr){
                iterations.addItem(s);
            }//end for loop
            iterations.setSelectedIndex(0);
            type.removeAllItems();
            for(String t : kochTypes){
                type.addItem(t);
            }//end for loop
            set.removeAllItems();
            set.addItem("N/A");
        }//end if statement

        //change options for sierpinski
        if(generationType.getSelectedIndex() == 2){
            iterations.removeAllItems();
            for(String s : sierpItr){
                iterations.addItem(s);
            }//end for loop
            type.removeAllItems();
            for(String t : sierpTypes){
                type.addItem(t);
            }//end for loop
            set.removeAllItems();
            set.addItem("N/A");
        }//end if statement
    }//end update Choices

    public void updatePanel(){
        //determine items selected in comboBoxes
        int pos1,pos2;
        pos1 = generationType.getSelectedIndex();
        String selectedGeneration = algorithms[pos1];
        pos2 = type.getSelectedIndex();
        String selectedType;
        int selectedItr;
        pos1 = set.getSelectedIndex();
        String selectedFormula = formulas[pos1];
        int power = Integer.parseInt(selectedFormula.substring(2,3));
        pos1 = iterations.getSelectedIndex();
        if(fractalPanel != null) fractalFrame.remove(fractalPanel);
        switch(selectedGeneration){
            case "Mandelbrot":
                //run mandelbrot panel thread
                selectedItr = Integer.parseInt(mandItr[pos1]);
                if(power == 8) fractalPanel = new MandelbrotCurve(selectedItr,power,4,0,0,2.25);
                else if(power == 4) fractalPanel = new MandelbrotCurve(selectedItr,power,4,-0.25,0,2.25);
                else if(power == 3) fractalPanel = new MandelbrotCurve(selectedItr,power,4,0,0,2.5);
                else fractalPanel = new MandelbrotCurve(selectedItr,power,4,-0.5,0,2);
                fractalPanel.setToolTipText("Left click image to zoom in, right click to zoom out");
                break;
            case "Koch":
                //run koch panel thread
                selectedItr = Integer.parseInt(kochItr[pos1]);
                selectedType = kochTypes[pos2];
                fractalPanel = new KochCurve(selectedItr,selectedType);
                break;
            case "Sierpinski":
                ///run Sierpenski panel thread
                selectedItr = Integer.parseInt(sierpItr[pos1]);
                selectedType = sierpTypes[pos2];
                fractalPanel = new Sierpinski(selectedItr,selectedType);
                break;
            default:    //do nothing
                break;
        }//end switch statement
        //make updated frame visible
        fractalFrame.add(fractalPanel);
        fractalFrame.repaint();
        fractalFrame.setVisible(true);
    }//end updatePanel

    public static void updateTimer(String newTime){
        timeDisplay.setText(newTime);
    }//end updateTimer
}//end SimulateScreen