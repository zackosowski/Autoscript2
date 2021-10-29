package app;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hpsf.Date;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Announcement {
    String script;
    String title;
    String weekDays;
    String subName;
    String comment;
    java.util.Date startDate, endDate, subDate;
    Boolean hasMedia;
    String[] links;
    Boolean days[] = new Boolean[5];

    // CREATE THE ANNOUNCEMENT
    Announcement(XSSFSheet s, int row) throws MalformedURLException {
        subDate = s.getRow(row).getCell(getIndex(index.SUB_DATE)).getDateCellValue();
        subName = s.getRow(row).getCell(getIndex(index.NAME)).getStringCellValue();
        title = s.getRow(row).getCell(getIndex(index.TITLE)).getStringCellValue();
        startDate = s.getRow(row).getCell(getIndex(index.START)).getDateCellValue();
        endDate = s.getRow(row).getCell(getIndex(index.END)).getDateCellValue();
        weekDays = s.getRow(row).getCell(getIndex(index.WEEK_DAYS)).getStringCellValue();
        script = s.getRow(row).getCell(getIndex(index.SCRIPT)).getStringCellValue();

        try {
            comment = s.getRow(row).getCell(getIndex(index.COMMENT)).getStringCellValue();
        } catch (Exception e) {
            comment = "n/a";
        }

        try {
            hasMedia = (s.getRow(row).getCell(getIndex(index.UPLOAD)).getCellType() != CellType.BLANK);
        } catch (Exception e) {
            hasMedia = false;
        }
        

        if (hasMedia){
            String allLinks = s.getRow(row).getCell(getIndex(index.UPLOAD)).getStringCellValue();
            String[] l = allLinks.split(", ");
            links = new String[l.length];
            for (int i = 0; i < l.length; i++){
                links[i] = l[i];
            }

        }
    }

    // SUBMISSION DATE
    public java.util.Date getSubmissionDate(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.SUB_DATE)).getDateCellValue();
    }

    public String getSubmissionDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(subDate);
    }

    // SUB NAME
    public String getSubmitterName(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.NAME)).getStringCellValue();
    }

    public String getSubmitterName() {
        return subName;
    }

    // TITLE
    public String getTitle(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.TITLE)).getStringCellValue();
    }

    public String getTitle() {
        return title;
    }

    // START DATE
    public java.util.Date getStartDate(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.START)).getDateCellValue();
    }

    public String getStartDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(startDate);
    }

    // END DATE
    public java.util.Date getEndDate(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.END)).getDateCellValue();
    }

    public String getEndDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(endDate);
    }

    // WEEK DAYS
    public String gettWeekDays(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.WEEK_DAYS)).getStringCellValue();
    }

    public String getWeekDays() {
        return weekDays;
    }

    // SCRIPT
    public String getScript(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.SCRIPT)).getStringCellValue();
    }

    public String getScript() {
        return script;
    }

    // COMMENT
    public String getComment(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.COMMENT)).getStringCellValue();
    }

    public String getComment() {
        return comment;
    }

     // HAS MEDIA
     public Boolean getHasMedia(XSSFSheet s, int row) {
        return s.getRow(row).getCell(getIndex(index.UPLOAD)).getCellType() != CellType.BLANK;
    }

    public Boolean getHasMedia() {
        return hasMedia;
    }

    // MEDIA LINKS
    public String[] getLinks(){
        return links;
    }

    public String linksToString(){
        String l = "";
        for (int i = 0; i < links.length; i ++){
            l += links[i];
            l += "\n";
        }
        return l;
    }

    // GET COLUMN NUMBER FOR DATA
    public static int getIndex(index x) {
        switch (x) {
        case SUB_DATE:
            return 0;
        case NAME:
            return 1;
        case TITLE:
            return 2;
        case START:
            return 3;
        case END:
            return 4;
        case WEEK_DAYS:
            return 5;
        case SCRIPT:
            return 6;
        case COMMENT:
            return 7;
        case UPLOAD:
            return 8;
        default:
            System.out.println("getIndex Error");
            return 9;
        }
    }

    @Override
    public String toString() {
        return "TITLE: " + title + 
                "\nSUBMISSION DATE: " + subDate +
                "\nSUBMITTER NAME: " + subName +
                "\nSTART DATE: " + startDate +
                "\nEND DATE: " + endDate +
                "\nWEEK DAYS: " + weekDays +
                "\nHAS MEDIA " + hasMedia +
                "\nSCRIPT: " + script + 
                "\nCOMMENT: " + comment +
                "\n----------------------";
    }

    public enum index {
        SUB_DATE, NAME, TITLE, START, END, WEEK_DAYS, SCRIPT, COMMENT, UPLOAD;

    }

}