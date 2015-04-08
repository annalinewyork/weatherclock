package nyc.c4q.ac21.weatherclock;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

public class Main {

    /**
     * From sample code, returns sunset + sunrise time for the current day.
     */
    public static Calendar getSunset() {
        URL url = HTTP.stringToURL("http://api.openweathermap.org/data/2.5/weather?q=New%20York,NY");
        String doc = HTTP.get(url);
        if (doc == null)
            return null;
        JSONObject obj = (JSONObject) JSONValue.parse(doc);
        if (obj == null)
            return null;
        JSONObject sys = (JSONObject) obj.get("sys");
        if (sys == null)
            return null;
        Long sunsetTimestamp = (Long) sys.get("sunset");
        if (sunsetTimestamp == null)
            return null;
        return DateTime.fromTimestamp(sunsetTimestamp);
    }

    public static Calendar getSunrise() {
        URL url = HTTP.stringToURL("http://api.openweathermap.org/data/2.5/weather?q=New%20York,NY");
        String doc = HTTP.get(url);
        if (doc == null)
            return null;
        JSONObject obj = (JSONObject) JSONValue.parse(doc);
        if (obj == null)
            return null;
        JSONObject sys = (JSONObject) obj.get("sys");
        if (sys == null)
            return null;
        Long sunriseTimestamp = (Long) sys.get("sunrise");
        if (sunriseTimestamp == null)
            return null;
        return DateTime.fromTimestamp(sunriseTimestamp);
    }


    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();

        final int numCols = TerminalSize.getNumColumns();
        final int numRows = TerminalSize.getNumLines();


        final AnsiTerminal terminal = new AnsiTerminal();

        // When the program shuts down, reset the terminal to its original state.
        // This code makes sure the terminal is reset even if you kill your
        // program by pressing Control-C.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                terminal.showCursor();
                terminal.reset();
                terminal.scroll(1);
                terminal.moveTo(numRows, 0);
            }
        });

        // Clear the screen to black.
        terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
        terminal.clear();
        // Don't show the cursor.
        terminal.hideCursor();

        while (true)
        {

            // graphical representation of day/night
            if (daytime(cal)) {
                terminal.setTextColor(AnsiTerminal.Color.YELLOW);
            } else {
                terminal.setTextColor(AnsiTerminal.Color.CYAN);
            }

            // draws digital clock display
            terminal.moveTo(numRows - 12, 0);
            terminal.write(buildClock(cal));
            terminal.moveTo(numRows - 1, 57);
            terminal.write(amPm(cal));
            DateTime.pause(1.0);
        }

    }

    /*
    * Takes the current time from a calendar instance and builds a 'digital clock' display
    * using ascii strings from DigitalClock.java.
     */

    public static String buildClock(Calendar cal) {

        String[] digits = {DigitalClock.zero, DigitalClock.uno, DigitalClock.two, DigitalClock.three, DigitalClock.four, DigitalClock.five, DigitalClock.six, DigitalClock.seven, DigitalClock.eight, DigitalClock.nine};
        String clock;

        int hour = cal.get(cal.HOUR);
        int minute = cal.get(cal.MINUTE);

        if (hour == 0) {
            clock = concatenate(digits[1], digits[2], DigitalClock.colon, digits[minute/10], digits[minute%10]);
        } else if (hour == 11) {
            clock = concatenate(digits[1], digits[1], DigitalClock.colon, digits[minute/10], digits[minute%10]);
        } else {
            clock = concatenate(DigitalClock.spacer, digits[hour], DigitalClock.colon, digits[minute/10], digits[minute%10]);
        }

        return clock;
    }

    /*
    * Scans each ascii digit into individual lines which are spaced out and concatenated together
    * into a single block
     */

    public static String concatenate(String digit1, String digit2, String colon, String digit3, String digit4) {
        String clockDisplay = "";

        Scanner scanner1 = new Scanner(digit1);
        scanner1.useDelimiter("\n");
        Scanner scanner2 = new Scanner(digit2);
        scanner2.useDelimiter("\n");
        Scanner scanner3 = new Scanner(colon);
        scanner3.useDelimiter("\n");
        Scanner scanner4 = new Scanner(digit3);
        scanner4.useDelimiter("\n");
        Scanner scanner5 = new Scanner(digit4);
        scanner5.useDelimiter("\n");

        while(scanner1.hasNext()) {
            String nextLine = scanner1.next() + "   " + scanner2.next() + "   " + scanner3.next() + "   " + scanner4.next() + "   " + scanner5.next();
            clockDisplay += nextLine + "\n";
        }

        return clockDisplay;
    }

    /*
    * Uses sunrise and sunset times to determine whether it's daytime
     */

    public static boolean daytime(Calendar cal) {
        Calendar sunrise = getSunrise();
        Calendar sunset = getSunset();
        if (sunrise == null || sunset == null) {
            return false;
        }
        boolean isDaytime;

        if(sunrise.get(Calendar.HOUR_OF_DAY) < cal.get(Calendar.HOUR_OF_DAY) && sunset.get(Calendar.HOUR_OF_DAY) > cal.get(Calendar.HOUR_OF_DAY)) {
            isDaytime = true;
        } else if (sunrise.get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY) && sunrise.get(Calendar.MINUTE) < cal.get(Calendar.MINUTE)) {
            isDaytime = true;
        } else if (sunset.get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY) && sunset.get(Calendar.MINUTE) > cal.get(Calendar.MINUTE)) {
            isDaytime = true;
        } else {
            isDaytime = false;
        }
        return isDaytime;
    }

    public static String amPm(Calendar cal){
        String amPm = "PM";
        if (cal.get(Calendar.AM_PM) == 0) {
            amPm = "AM";
        }
        return amPm;
    }
}
