package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.swing.JFrame;

//delete this comment

public class Autoscript{
    public static void main(String[] args) throws Exception {
        trustAllHosts();
        StartWindow startWindow = new StartWindow();
        startWindow.setVisible(true);

    }
    

    public static void generate(String a1, String a2) throws IOException {
        
        class Go implements Runnable{
            String a1, a2;
            Go(String one, String two){
                a1 = one;
                a2 = two;
            }
             public void run(){
                java.util.Date d = Calendar.getInstance().getTime();                                                                         // Get date
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");                                                                     //Set date format
                String today = df.format(d);                                                                                         //Format date
                Calendar w = new GregorianCalendar();
                String dayOfWeek = w.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault());                                                                                                              //Create variable that stores total announcements found                                   
        
                URL url;

                try {
                    url = new URL(
                            "https://docs.google.com/spreadsheets/d/1zUA59HuroWaKO-qtnl8JPNnusquvJKLzh0_XX26nm0Y/export?format=xlsx");
                } catch (MalformedURLException e) {
                    url = null;
                    System.out.println("Invalid URL");
                    e.printStackTrace();
                }

                String destination = "W:\\HS\\Osowski, Z\\Autoscript\\STAFF ONLY\\" + today + ".xlsx";                                          //Define hard drive location for file download
                File file = new File(destination);   
        
                try {
                    FileUtils.copyURLToFile(url, file, 2000, 2000);
                } catch (IOException e) {
                    System.out.println("Copy url to file error.");
                    e.printStackTrace();
                }
        
                InputStream excelFileToRead;
                try {
                    excelFileToRead = new FileInputStream(destination);
                } catch (FileNotFoundException e) {
                    System.out.println("File input stream error");
                    excelFileToRead = null;
                    e.printStackTrace();
                }
                XSSFWorkbook wb;
                try {
                    wb = new XSSFWorkbook(excelFileToRead);
                } catch (IOException e) {
                    System.out.println("wb creation error");
                    wb = null;
                    e.printStackTrace();
                }
                XSSFSheet sheet = wb.getSheetAt(0);     
                
                int numFound = sheet.getPhysicalNumberOfRows() - 1;
                
                ArrayList<Announcement> a = new ArrayList<Announcement>();  
        
                for (int i = 1; i <= numFound; i++){  
                    System.out.println("checking announcement row..." + i);                              
                    java.util.Date sd = sheet.getRow(i).getCell(3).getDateCellValue();
                    java.util.Date ed = sheet.getRow(i).getCell(4).getDateCellValue();
                    String days = sheet.getRow(i).getCell(5).getStringCellValue();
        
                    if (d.compareTo(sd) >= 0 && d.compareTo(ed) <= 0 && days.contains(dayOfWeek)) {
                        try {
                            a.add(new Announcement(sheet, i));
                        } catch (MalformedURLException e) {
                            System.out.println("add announcement error");
                            a.add(null);
                            e.printStackTrace();
                        }
                    }
                }
        
                for (int i = 0; i < a.size(); i++){
                    System.out.println(a.get(i).toString()); 
                }
                
                MainWindow mainWindow = new MainWindow(a, a1, a2);
                mainWindow.setVisible(true);
        
                try {
                    wb.close();
                } catch (IOException e) {
                    System.out.println("wb close error");
                    e.printStackTrace();
                }
            }
        }

        Thread t = new Thread(new Go(a1, a2));
        t.start();
        
    }

    public static void trustAllHosts()
    {
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509ExtendedTrustManager()
                    {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                        {
                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
                        {

                        }

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {

                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
                        {

                        }

                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new  HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {
            System.out.println("Error occurred");
        }
    }

}