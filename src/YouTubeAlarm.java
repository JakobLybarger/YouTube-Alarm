import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.security.SecureRandom;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

/** Allows the user to enter a time for an alarm within the
 *  current day and when it is that time random YouTube video
 *  from a text file will be played.
 */
public class YouTubeAlarm {

    private static Scanner sc;
    private static SecureRandom random = new SecureRandom();
    private static int linenumber;

    public static void main(String[] args) throws Exception {
        // Open the file
        openFile();

        // the date formatter and scanner
        SimpleDateFormat sdfo = new SimpleDateFormat("h:mm a");

        // get the user input for the time of the alarm
        String alarmTimeString = JOptionPane.showInputDialog("Enter the time you want the alarm to go off. (ex: 5:25 PM)");
        Date alarm = sdfo.parse(alarmTimeString);


        boolean equal = false;
        // Keep looping while the current time does
        // not equal the selected time
        while(!equal){
            // current time
            Date realTime = new Date();
            Date actualTime = sdfo.parse(sdfo.format(realTime));

            // if the actual time is the same as the alarm time
            if(actualTime.equals(alarm)){
                Desktop d = Desktop.getDesktop();
                d.browse(new URI(getUrl()));
                equal = true;
            }
        } // end while

        // Close the file
        closeFile();
    }

    /**Opens up a file that has already been created.
     *  @throw IOException if there is an error opening file
     */
    private static void openFile(){
        try{
            sc = new Scanner(Paths.get("urls.txt"));
        }
        catch(IOException e){
            System.err.println("Error opening file. Terminating");
            System.exit(1);
        }
    }

    /** Based on a random number gets and returns the url
     *  on the randomly selected line of a text file.
     *  @throws NoSuchElementException if url does not exist
     *  @throws IllegalStateException if program not in correct state for certain action
     *  @returns The url from the random text file line*/
    private static String getUrl(){

        String url = null;
        try{
            int printQue = randomNumber() ;

            for(int i = 0; i < printQue - 1; i++){
                sc.nextLine();
            }

            url = sc.nextLine();
        }
        catch(NoSuchElementException e){
            System.err.println("File formed improperly. Terminating.");
        }
        catch(IllegalStateException e){
            System.err.println("Error reading from file. Terminating.");
        }



        return url;
    }

    /** Closes the file. */
    private static void closeFile(){
        if(sc != null){
            sc.close();
        }
    }

    /** Uses a LineNumberReader to find how many lines of urls
     *  are in the file
     *  @return the number of lines with urls
     *  @throws IOException if there is an error opening the file*/
    private static int fileSize(){
        try{
            File file = new File("urls.txt");

            if(file.exists()){
                LineNumberReader lnr = new LineNumberReader(new FileReader(file));

                linenumber = 0;

                while(lnr.readLine() != null){
                    linenumber++;
                }

                lnr.close();
            }
            else{
                System.out.println("File does not exist!");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return linenumber;
    }

    /** Generates a random number between 0 and the size of the file
     *  @return a random number <= fileSize()-1
     */
    private static int randomNumber(){
        int randomNum = random.nextInt(fileSize());

        while(randomNum <= 0){
            randomNum = random.nextInt(fileSize());
        }

        return randomNum;
    }
}
