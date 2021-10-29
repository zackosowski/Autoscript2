package app;

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
import java.awt.BorderLayout;
import java.awt.Container;

public class StartWindow extends JFrame {

    JTextField anchor1, anchor2;
    JButton bStart;
    JLabel a1 = new JLabel("Anchor 1:");
    JLabel a2 = new JLabel("Anchor 2:");
    JLabel warning = new JLabel();
    JPanel pMain, pSub;
    Dimension inputTextSize = new Dimension(100,20);

    public StartWindow() {
        super("Autoscript 2");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(200, 150));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        Container mainContainer = this.getContentPane();
        ActionHandler actionHandler = new ActionHandler();
        pMain = new JPanel();
        pSub = new JPanel();
        bStart = new JButton("Start");
        bStart.addActionListener(actionHandler);
        anchor1 = new JTextField();
        anchor2 = new JTextField();

        anchor1.setPreferredSize(inputTextSize);
        anchor2.setPreferredSize(inputTextSize);

        warning.setForeground(Color.RED);
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        pMain.add(a1, gbc);
        pMain.add(anchor1, gbc);
        pMain.add(a2, gbc);
        pMain.add(anchor2, gbc);

        pMain.add(bStart, gbc);
        pMain.add(warning, gbc);
        getRootPane().setDefaultButton(bStart);

        mainContainer.add(pMain, BorderLayout.CENTER);

    }

    private class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == bStart) {
                if (anchor1.getText().equals("")) {
                    warning.setForeground(Color.RED);
                    warning.setText("Invalid input for Anchor 1");
                }

                else if (anchor2.getText().equals("")) {
                    warning.setForeground(Color.RED);
                    warning.setText("Invalid input for Anchor 2");

                } else {

                    try {
                        LoadWindow l = new LoadWindow();
                        l.setVisible(true);
                        Autoscript.generate(anchor1.getText(), anchor2.getText());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
           
        }
    }



    

}