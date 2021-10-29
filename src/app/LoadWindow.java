package app;

import java.awt.Dimension;

import javax.swing.JFrame;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;

import java.awt.Color;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Container;

public class LoadWindow extends JFrame {
    String[] messages = new String[] { "Closing the door", "Contacting the Smurph", "Buying the rights",
            "Sitting normal", "Living deliciously", "Rendering in 8k", "Frothing at the mouth", "Mixing Soylent",
            "Calculating net positives", "Uninstalling Final Cut Pro", "Quitting Fleet Farm", "Quenching thirst",
            "Chugging Sprite Cranberry", "Flagging the play", "Calling the Splaash", "Snitching", "Hosting the Superbowl",
            "Unsubbing" };
            
    JPanel loadPanel;
    JLabel msg = new JLabel();
    Boolean loadMsg = true;
    public int i = 0;

    public LoadWindow() throws InterruptedException {
        super("Autoscript 2");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(400, 100));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        Container mainContainer = this.getContentPane();
        loadPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        loadPanel.add(msg, gbc);
        mainContainer.add(loadPanel, BorderLayout.CENTER);
        new javax.swing.Timer(1500, taskPerformer).start();
        loadMessages();

    }

    public void loadMessages() throws InterruptedException {
        System.out.println("Loading Messages i = " + i);
        if (i < 5) {
            String currentMessage = messages[new Random().nextInt(messages.length)] + "...";
            msg.setText(currentMessage);
            System.out.println("generating message");
            i++;
        } else if (i > 4) {
            //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }

    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            try {
                loadMessages();
            } catch (InterruptedException e) {
                System.out.println("Could not load messages");
                e.printStackTrace();
            }
            System.out.println("loadMsg = true;");
        }
    };

    

}