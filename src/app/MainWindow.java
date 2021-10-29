package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.awt.datatransfer.*;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.Sides;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.commons.io.FileUtils;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.ss.formula.functions.Today;

public class MainWindow extends JFrame {

    private JTextArea tSubmitter, tSubdate, tStart, tEnd, tWeekdays, tMedia, tLinks, tComment;
    private JTextArea tbCurrent, tbTitle, tbAnchor;
    private JTextArea title, anchor;
    private String anchor1 = "anchor 1";
    private String anchor2 = "anchor 2";
    private JTextArea script = new JTextArea();
    private JButton bDelete, bNext, bLast, bPrint, bCopy, bSave;

    JTextArea[] titleBarTexts = new JTextArea[] { tbCurrent, tbTitle, tbAnchor };

    JPanel titleBar = new JPanel();

    java.util.Date d = Calendar.getInstance().getTime();
    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String today = df.format(d);

    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private static float leftPanelWidth = 0.3f;
    private static float rightPanWidth = 0.95f - leftPanelWidth;

    int currentAnnouncement = 0;

    ArrayList<Announcement> announcements;

    static final int sizeH = Toolkit.getDefaultToolkit().getScreenSize().height;
    static final int sizeW = Toolkit.getDefaultToolkit().getScreenSize().width;

    public MainWindow(ArrayList<Announcement> a, String a1, String a2) {
        super("Autoscript 2");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setForeground(Color.red);
        announcements = a;

        anchor1 = a1;
        anchor2 = a2;

        // INSTANTIATE BUTTONS
        bDelete = new JButton("DELETE");
        bNext = new JButton("NEXT");
        bLast = new JButton("LAST");
        bPrint = new JButton("PRINT");
        bCopy = new JButton("COPY");
        bSave = new JButton("SAVE");

        // INSTANTIATE LEFT TEXT
        tSubmitter = new JTextArea("ERROR");
        tSubdate = new JTextArea("ERROR");
        tStart = new JTextArea("ERROR");
        tEnd = new JTextArea("ERROR");
        tWeekdays = new JTextArea("ERROR");
        tMedia = new JTextArea("ERROR");
        tLinks = new JTextArea("ERROR");
        tComment = new JTextArea("ERROR");

        // INSTANTIATE TITLE BAR TEXT
        tbCurrent = new JTextArea(Integer.toString(announcements.size()));
        tbTitle = new JTextArea(announcements.get(0).getTitle());
        tbAnchor = new JTextArea(anchor1);
        titleBarTexts[0] = tbCurrent;
        titleBarTexts[1] = tbTitle;
        titleBarTexts[2] = tbAnchor;

        // SET DEFAULT ANNOUNCMENT
        setActiveAnnouncement();

        // PUT LEFT TEXT IN ARRAY AND FORMAT
        JTextArea[] leftContent = new JTextArea[] { tSubmitter, tSubdate, tStart, tEnd, tWeekdays, tMedia, tComment };
        initLeftSide(leftContent);

        // CREATE MAIN CONTAINER
        Container mainContainer = this.getContentPane();
        mainContainer.setBackground(Color.BLACK);

        // CREATE MENU BAR
        /*JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem copy = new JMenuItem("Copy All");
        JMenuItem print = new JMenuItem("Print");
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem anchor = new JMenuItem("Anchor");
        JMenuItem url = new JMenuItem("XSLX URL");
        file.add(open);
        file.add(copy);
        file.add(print);
        edit.add(delete);
        edit.add(anchor);
        edit.add(url);
        menuBar.add(file);
        menuBar.add(edit);
        */

        // CREATE TOOL BAR (TO BE REPLACED BY MENU BAR)
        JPanel toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(5));
        toolBar.setBackground(Color.DARK_GRAY);
        mainContainer.add(toolBar, BorderLayout.SOUTH);
        ActionHandler actionHandler = new ActionHandler();
        addButton(bLast, toolBar, actionHandler);
        addButton(bNext, toolBar, actionHandler);
        addButton(bSave, toolBar, actionHandler);
        addButton(bDelete, toolBar, actionHandler);
        addButton(bCopy, toolBar, actionHandler);
        addButton(bPrint, toolBar, actionHandler);

        // CREATE LEFT PANEL AND FORMAT
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(Math.round(sizeW * leftPanelWidth), leftPanel.getHeight()));
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setBackground(Color.DARK_GRAY);
        for (int i = 0; i < 7; i++) {
            gbc.gridy = i;
            leftPanel.add(leftContent[i], gbc);
        }
        mainContainer.add(leftPanel, BorderLayout.WEST);

        // CREATE TITLE BAR AND FORMAT
        FlowLayout fl = new FlowLayout(3);
        fl.setAlignment(FlowLayout.CENTER);
        titleBar.setLayout(fl);
        titleBar.setBackground(Color.DARK_GRAY);
        titleBarInit();
        mainContainer.add(titleBar, BorderLayout.NORTH);

        // CREATE SCRIPT AREA AND FORMAT
        JPanel scriptArea = new JPanel();
        scriptArea
                .setPreferredSize(new Dimension(Math.round(sizeW - (sizeW * leftPanelWidth)), scriptArea.getHeight()));
        scriptArea.setLayout(new GridBagLayout());
        scriptArea.setBackground(Color.DARK_GRAY);
        script.setLineWrap(true);
        script.setPreferredSize(new Dimension(Math.round(sizeW - (sizeW * leftPanelWidth)), scriptArea.getHeight()));
        script.setText(announcements.get(0).getScript());
        script.setFont(new Font("Arial", Font.PLAIN, 24));
        script.setForeground(Color.BLACK);
        script.setBackground(Color.WHITE);
        script.setEditable(true);
        scriptArea.add(script, gbc);
        mainContainer.add(scriptArea, BorderLayout.EAST);
    }

    // FORMAT BUTTON
    void addButton(JButton b, JPanel p, ActionHandler h) {
        b.setPreferredSize(new Dimension(Math.round(sizeW * .16f), Math.round(sizeH * .05f)));
        b.setForeground(Color.darkGray);
        b.setBackground(Color.green);
        b.setFont(new Font("Arial", Font.BOLD, 28));
        b.addActionListener(h);
        p.add(b);
    }

    // SET CURRENT ANNOUNCEMENT
    public void setActiveAnnouncement() {
        tSubmitter.setText("SUBMITTED BY:\n" + announcements.get(currentAnnouncement).getSubmitterName());
        tSubdate.setText("SUBMISSION DATE:\n" + announcements.get(currentAnnouncement).getSubmissionDate());
        tStart.setText("START DAY:\n" + announcements.get(currentAnnouncement).getStartDate());
        tEnd.setText("LAST DAY:\n" + announcements.get(currentAnnouncement).getEndDate());
        tWeekdays.setText("DAY OF WEEK:\n" + announcements.get(currentAnnouncement).getWeekDays());
        tMedia.setText("MEDIA:\n" + announcements.get(currentAnnouncement).linksToString());
        tComment.setText("COMMENTS:\n" + announcements.get(currentAnnouncement).getComment());

        tbCurrent.setText("[" + Integer.toString(currentAnnouncement + 1) + "/" + Integer.toString(announcements.size()) + "]");
        tbTitle.setText(announcements.get(currentAnnouncement).getTitle());
        script.setText(announcements.get(currentAnnouncement).getScript());
        if (currentAnnouncement % 2 == 0) {
            tbAnchor.setText("(" + anchor1 + ")");
        } else {
            tbAnchor.setText("(" + anchor2 + ")");
        }
    }

    // PLACE AND FORMAT LEFT TEXT
    public void initLeftSide(JTextArea[] jl) {
        for (int i = 0; i < jl.length; i++) {
            jl[i].setFont(new Font("Arial", Font.PLAIN, 24));
            jl[i].setBackground(Color.DARK_GRAY);
            jl[i].setLineWrap(true);
            jl[i].setForeground(Color.WHITE);
        }
    }

    // INIT TITLE BAR
    public void titleBarInit() {
        for (int i = 0; i < titleBarTexts.length; i++) {
            titleBarTexts[i].setFont(new Font("Arial", Font.BOLD, 30));
            titleBarTexts[i].setBackground(Color.DARK_GRAY);
            titleBarTexts[i].setForeground(Color.WHITE);
            titleBar.add(titleBarTexts[i]);
        }
    }

    // COPY FULL SCRIPT
    public String getFullScript() {
        Calendar w = new GregorianCalendar();
        String dayOfWeek = w.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String s = today + "\n\nMIDCAM:\nHello STMA, I'm " + anchor1 + " and I'm " + anchor2
                + " and we have news for your " + dayOfWeek;

        for (int i = 0; i < announcements.size(); i++) {
            if (i % 2 == 0) {
                s = s + "\n\n" + anchor1;
            } else {
                s = s + "\n\n" + anchor2;
            }
            s = s + ":\n" + announcements.get(i).getScript();
        }

        s = s + "\n\nMIDCAM:\nAnd that's it for us on your " + dayOfWeek;
        if (dayOfWeek.equals("Monday")) {
            s = s + "\nWe end the day with the pledge of allegiance";
        } else if (!dayOfWeek.equals("Friday")) {
            s = s + "\nHave a great day STMA.";
        } else {
            s = s + "\nHave a great weekend STMA.";
        }

        StringSelection output = new StringSelection(s);
        clipboard.setContents(output, null);
        return s;
    }

    public StringSelection copyFullScript(String s) {
        StringSelection output = new StringSelection(s);
        clipboard.setContents(output, null);
        return output;
    }

    public void deleteAnnouncement() {
        announcements.remove(currentAnnouncement);
        if (currentAnnouncement > 0) {
            currentAnnouncement--;
        }
        refresh();
    }

    public void refresh() {
        titleBarInit();
        setActiveAnnouncement();
    }

    public void printFullScript() throws IOException, PrintException, PrinterException {
        File f = new File("W:\\HS\\Osowski, Z\\Autoscript\\Scripts\\" + today + ".txt");
        if (f.exists()){
            f.delete();
        }
        f.createNewFile();
        FileUtils.writeStringToFile(f, getFullScript(), "UTF-8", true);
        PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(Sides.DUPLEX);
        pras.add(new Copies(3));
        JEditorPane text = new JEditorPane("file:///" + f.getPath());
        text.print(null, null, false, ps, pras, false);
    }

    private class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == bNext) {
                if (currentAnnouncement != announcements.size()-1) {
                    currentAnnouncement++;
                    setActiveAnnouncement();
                }
            }
            if (event.getSource() == bLast) {
                if (currentAnnouncement != 0) {
                    currentAnnouncement--;
                    setActiveAnnouncement();
                }

            }
            if (event.getSource() == bCopy) {
                copyFullScript(getFullScript());
            }
            if (event.getSource() == bPrint) {
                try {
                    printFullScript();
                } catch (IOException | PrintException e) {
                    System.out.println(("Failed Print\n" + e.toString()));
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
            if (event.getSource() == bDelete) {
                deleteAnnouncement();
            }
            if (event.getSource() == bSave){
                announcements.get(currentAnnouncement).script = script.getText();
            }
        }
    }
}