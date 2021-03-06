package nyc.c4q.ac21.weatherclock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class Main
{
    public  static  String info;

    /**
     * displayWind method gets wind speed and displays it
     * Written by Anthony Fermin
     */

    public static void displayWind(int x, int y, JSONObject weatherData, AnsiTerminal terminal, int secs)
    {

        String windDirection = "No Speed Data";
        ArrayList<String[]> wind = new ArrayList<String[]>();
        wind.add(WindASCII.letterW);
        wind.add(WindASCII.letterI);
        wind.add(WindASCII.letterN);
        wind.add(WindASCII.letterD);

        terminal.setTextColor(AnsiTerminal.Color.GREEN);
        terminal.setBackgroundColor(AnsiTerminal.Color.BLUE);

        JSONObject windData = (JSONObject) weatherData.get("wind");

        // windData returns speed in meters per second, converting to Miles per hour
        Double speed = (Double) windData.get("speed");
        // 1 Meter per Second = 2.2369362920544 Miles per Hour
        speed = speed * 2.2369362920544;
        DecimalFormat formatSpeed = new DecimalFormat("##.#");

        Double degrees = (Double) windData.get("deg");

        if(speed == null || degrees == null || windData == null)
        {
            terminal.write(windDirection);
            return;
        }

        // clears any previous wind animation
        for(int i = 0; i < WindASCII.letterW.length + 2; i++)
        {
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            terminal.moveTo(y + i, x);
            terminal.write("                         ");
        }
        terminal.setBackgroundColor(AnsiTerminal.Color.BLUE);

        int offset;

        if(secs % 2 == 0){
            offset = 1;
        }else{
            offset = -1;
        }

        int letterWidth = 6;

                   for(int i = 0; i < wind.size(); i++){
                String[] letter = wind.get(i);
                if(offset == 1)
                {
                    offset = 0;
                }else{
                    offset = 1;
                }


                for(int j = 0; j < letter.length; j++){

                    terminal.moveTo(y+1+j + offset, x + (i * letterWidth));
                    terminal.write(letter[j]);

                }

        }

        //         Cardinal Direction
        //    	   Degree Direction

        //         N
        //         348.75 - 11.25
        if((degrees > 348.75 && degrees <= 360) || (degrees >= 0 && degrees < 11.25)){
            windDirection = "N";
        }

        //         NNW
        //         326.25 - 348.75
        if((degrees > 326.25 && degrees <= 348.75)){
            windDirection = "NNW";
        }

        //         NW
        //         303.75 - 326.25
        if((degrees > 303.75 && degrees <= 326.25)){
            windDirection = "NW";
        }

        //         WNW
        //         281.25 - 303.75
        if((degrees > 281.25 && degrees <= 303.75)){
            windDirection = "WNW";
        }

        //         W
        //         258.75 - 281.25
        if((degrees > 258.75 && degrees <= 281.25)){
            windDirection = "W";
        }

        //         WSW
        //         236.25 - 258.75
        if((degrees > 236.25 && degrees <= 258.75)){
            windDirection = "WSW";
        }

        //         SW
        //         213.75 - 236.25
        if((degrees > 213.75 && degrees <= 236.75)){
            windDirection = "SW";
        }

        //         SSW
        //         191.25 - 213.75
        if((degrees > 191.25 && degrees <= 213.75)){
            windDirection = "SSW";
        }

        //         S
        //         168.75 - 191.25
        if((degrees > 168.75 && degrees <= 191.25)){
            windDirection = "S";
        }

        //         ENE
        //         56.25 - 78.75
        if((degrees > 56.25 && degrees <= 78.75)){
            windDirection = "ENE";
        }

        //         SSE
        //         146.25 - 168.75
        if((degrees > 146.25 && degrees <= 168.75)){
            windDirection = "SSE";
        }

        //         SE
        //         123.75 - 146.25
        if((degrees > 123.75 && degrees <= 146.25)){
            windDirection = "SE";
        }

        //         ESE
        //         101.25 - 123.75
        if((degrees > 101.25 && degrees <= 123.75)){
            windDirection = "ESE";
        }

        //         E
        //         78.75 - 101.25
        if((degrees > 78.75 && degrees <= 101.25)){
            windDirection = "E";
        }

        //         NE
        //         33.75 - 56.25
        if((degrees > 33.75 && degrees <= 56.25)){
            windDirection = "NE";
        }

        //         NNE
        //         11.25 - 33.75
        if((degrees > 11.25 && degrees <= 33.75)){
            windDirection = "NE";
        }

        terminal.moveTo(y+6, x);
        terminal.write("Wind Direction: " + windDirection);

        terminal.moveTo(y+7, x);
        terminal.write("Wind Speed: " + formatSpeed.format(speed) + " MPH");

    }


    /**
     * SAMPLE CODE: Returns sunset time for the current day.
     */
    public static Calendar getSunset()
    {

        if(Main.info == null)
        {
            return null;
        }
        JSONObject obj = (JSONObject) JSONValue.parse(Main.info);
        if(obj == null)
        {
            return null;
        }
        JSONObject sys = (JSONObject) obj.get("sys");
        if(sys == null)
        {
            return null;
        }
        Long sunsetTimestamp = (Long) sys.get("sunset");
        if(sunsetTimestamp == null)
        {
            return null;
        }
        return DateTime.fromTimestamp(sunsetTimestamp);
    }

    public static Calendar getSunrise()
    {

        if(Main.info == null)
        {
            return null;
        }
        JSONObject obj = (JSONObject) JSONValue.parse(Main.info);
        if(obj == null)
        {
            return null;
        }
        JSONObject sys = (JSONObject) obj.get("sys");
        if(sys == null)
        {
            return null;
        }
        Long sunriseTimestamp = (Long) sys.get("sunrise");
        if(sunriseTimestamp == null)
        {
            return null;
        }
        return DateTime.fromTimestamp(sunriseTimestamp);
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException
    {


        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=New%20York");

        Main.info = HTTP.get(url);

        JSONObject jsonweatherobj = (JSONObject) JSONValue.parse(info);

        //Gets the city name
        String cityName = (String) jsonweatherobj.get("name");

        //calls the map main in the API to get values within it.
        JSONObject tempJSONObj = (JSONObject) jsonweatherobj.get("main");
        //calls temp,pressure, and humidity from "main".
        Double temp = (Double) tempJSONObj.get("temp");
        Double pressure = (Double) tempJSONObj.get("pressure");
        Long humid = (Long) tempJSONObj.get("humidity");
        //calls the map sys in the API to get values within it.
        JSONObject sunrise = (JSONObject) jsonweatherobj.get("sys");
        //calls sunrise time from "sys".
        Long sunrsTime = (Long) sunrise.get("sunrise");
        //calls the array "weather, to gain values within it.
        JSONArray weathr = (JSONArray) jsonweatherobj.get("weather");
        //calls object at index 0.
        JSONObject weatherJsonObj = (JSONObject) weathr.get(0);
        //calls "description" within that object, which the api tells us is the
        //current weather.

        String currWeather = (String)weatherJsonObj.get("description");


        //sets up alarm.
        System.out.println("Set your alarm: HH:MM");
        Scanner scanner = new Scanner(System.in);

        String alarm = scanner.next();

        int alarmHour = Integer.valueOf(alarm.substring(0, 2));
        int alarmMinute = Integer.valueOf(alarm.substring(3, 5));


        // Find out the size of the terminal currently.
        final int numCols = TerminalSize.getNumColumns();
        final int numRows = TerminalSize.getNumLines();

        // Create the terminal.
        final AnsiTerminal terminal = new AnsiTerminal();

        // When the program shuts down, reset the terminal to its original state.
        // This code makes sure the terminal is reset even if you kill your
        // program by pressing Control-C.
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
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

        // Get sunset time for the current day.
        Calendar sunset = getSunset();
        Calendar sunrise1 = getSunrise();

        int xPosition = 1 + numCols / 2 - 5;


        while(true)
        {

            //Set the City name.
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(19, xPosition);
            terminal.write("City: " + cityName);

            // Get the current date and time.
            Calendar cal = Calendar.getInstance();


            //Sets off alarm if user input equal cal output.
            if(cal.get(Calendar.HOUR) == alarmHour && cal.get(Calendar.MINUTE) == alarmMinute)
            {
                alarm(terminal, 3);
            }

            // Write the day of the week in green on a blue background.
            String dayOfWeek = DateTime.getDayOfWeekNames().get(cal.get(Calendar.DAY_OF_WEEK));
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            terminal.moveTo(21, xPosition);
            terminal.write("Day : " + dayOfWeek + "  ");


            // Write the time, including seconds, in white.
            String time = DateTime.formatTime(cal, true);
            if(cal.get(Calendar.HOUR_OF_DAY) >= 12)
            {
                time += " PM";
            }
            else
            {
                time += " AM";
            }
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            terminal.moveTo(20, xPosition);
            terminal.write("Time: " + time);


            // Write the date in gray.
            String date = DateTime.formatDate(cal);
            terminal.setTextColor(AnsiTerminal.Color.WHITE, false);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            terminal.moveTo(4, xPosition);

            terminal.write("Date: " + date);

            // Set the background color back to black.
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);


            //sets the position of the current weather
            terminal.setTextColor(AnsiTerminal.Color.GREEN);
            terminal.moveTo(6, xPosition);
            terminal.write("Weather: " + currWeather);


            //sets position of temperature in terminal
            terminal.setTextColor(AnsiTerminal.Color.MAGENTA);
            terminal.moveTo(8, xPosition);
            //temp returns kelvin, convert to farenheit using F = 1.8 x (K - 273) + 32.
            double F = 1.8 * (temp - 273) + 32;
            // Following code trims the double.
            DecimalFormat df = new DecimalFormat("#.##");
            terminal.write("Temp: " + df.format(F) + "F");

            //sets position of pressure in terminal
            terminal.setTextColor(AnsiTerminal.Color.MAGENTA);
            terminal.moveTo(9, xPosition);
            //Pressure returns hectopascal(hPa), convert to inch of mercury (inHg) using,
            //0.02952998751*hPa = inHg
            Double inHgPressure = 0.02952998751 * pressure;
            //trims the number
            DecimalFormat inHg = new DecimalFormat("##.#");
            terminal.write("Pressure: " + inHg.format(inHgPressure) + " inHg");

            //sets position of pressure in terminal
            terminal.setTextColor(AnsiTerminal.Color.MAGENTA);

            terminal.moveTo(10, xPosition);
            terminal.write("Humidity: " + humid + " %");

            //sunrise time
            //Returns Epoch time.
            terminal.setTextColor(AnsiTerminal.Color.YELLOW, false);
            terminal.moveTo(12, xPosition);
            //converts epoch time to standard readable time.
            String sunRise = new java.text.SimpleDateFormat("HH:mm:ss")
                    .format(new java.util.Date(sunrsTime * 1000));
            terminal.write("Sunrise time: " + sunRise + " AM");


            // Write sunset time in dark yellow.
            String sunsetTime = DateTime.formatTime(sunset, true);
            terminal.setTextColor(AnsiTerminal.Color.YELLOW, false);
            terminal.moveTo(13, xPosition);
            terminal.write("Sunset time: " + sunsetTime + " PM");


            //Daylights
            terminal.setTextColor(AnsiTerminal.Color.RED);
            terminal.moveTo(14, xPosition);
            boolean isDST = DST.isDST(cal);
            if(isDST)
            {
                terminal.write("Daylights Saving: yes");
            }
            else
            {
                terminal.write("Daylights Saving: No");
            }


            // 3. Show whether this is a national holiday, and if so, which.
            HashMap<Calendar, String> holidays = Holidays.getHolidays("National holiday");
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.moveTo(15, xPosition);

            if(holidays.get(date) == null)
            {
                terminal.write("National Holiday:  None");
            }
            else
            {
                terminal.write("National Holiday:  " + holidays.get(date));
            }


            //Display current weather pic.
            int sec = cal.get(Calendar.SECOND);
            WeatherDisplay.weather(10, 1, jsonweatherobj,terminal,sec);


            // display wind information
            displayWind(numCols - 25,1,jsonweatherobj, terminal, sec);

            // display calendar
            CalendarPrinter.printMonthCalendar(numCols - 35, numRows - 25, cal, terminal);

            // graphical representation of day/night
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (daytime(cal, sunrise1, sunset)) {
                terminal.setTextColor(AnsiTerminal.Color.YELLOW);
            } else {
                terminal.setTextColor(AnsiTerminal.Color.CYAN);
            }

            // draws digital clock display
            terminal.moveTo(numRows - 12, 0);
            terminal.write(buildClock(cal));
            terminal.moveTo(numRows - 1, 57);
            terminal.write(amPm(cal));


            // Pause for one second, and do it again.
            DateTime.pause(1.0);
        }
    }

    public static String buildClock(Calendar cal) {

        String[] digits = {DigitalClock.zero, DigitalClock.uno, DigitalClock.two, DigitalClock.three, DigitalClock.four, DigitalClock.five, DigitalClock.six, DigitalClock.seven, DigitalClock.eight, DigitalClock.nine};
        String clock;

        int hour = cal.get(cal.HOUR);
        int minute = cal.get(cal.MINUTE);

        if (hour == 0) {
            clock = concatenate(digits[1], digits[2], DigitalClock.colon, digits[minute/10], digits[minute%10]);
        } else if (hour == 11) {
            clock = concatenate(digits[1], digits[1], DigitalClock.colon, digits[minute/10], digits[minute%10]);
        } else if (hour == 10) {
            clock = concatenate(digits[1], digits[0], DigitalClock.colon, digits[minute / 10], digits[minute % 10]);
        } else {
            clock = concatenate(DigitalClock.spacer, digits[hour], DigitalClock.colon, digits[minute/10], digits[minute%10]);
        }

        return clock;
    }

    /*
    * Scans each ascii digit into individual lines which are spaced out and concatenated together
    * into a single block
    */
    public static String concatenate(String digit1, String digit2, String colon, String digit3, String digit4)
    {
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

        while(scanner1.hasNext())
        {
            String nextLine = scanner1.next() + "   " + scanner2.next() + "   " + scanner3
                    .next() + "   " + scanner4.next() + "   " + scanner5.next();
            clockDisplay += nextLine + "\n";
        }

        return clockDisplay;
    }

    /*
    * Uses sunrise and sunset times to determine whether it's daytime
    */
    public static boolean daytime(Calendar cal, Calendar sunrise, Calendar sunset) {

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

    public static String amPm(Calendar cal)
    {
        String amPm = "PM";
        if(cal.get(Calendar.AM_PM) == 0)
        {
            amPm = "AM";
        }
        return amPm;


    }

    //Written by John with a lot of help from Ramona.
    public static void alarm(AnsiTerminal terminal, int frames) throws IOException, InterruptedException
    {

        terminal.clear();

        while(frames > 0)
        {

            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(15, 0);
            terminal.write(eye.eyeClosed);
            DateTime.pause(0.3);
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(15, 0);
            terminal.write(eye.eyeSemi);
            DateTime.pause(0.3);
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(15, 0);
            terminal.write(eye.eyeOpen);
            DateTime.pause(0.3);

            frames--;

            //calls upon the terminal to read a string once the alarm goes off.
            //Written by Ramona. Random function by John.
            terminal.setTextColor(AnsiTerminal.Color.GREEN, false);
            terminal.moveTo(1, 1);

            int jokeLength = alarmPhrases.badJokes.length;
            int voiceLength = alarmPhrases.voice.length;

            int randBadJoke = (int) (Math.random() * jokeLength);
            int randVoice = (int) (Math.random() * voiceLength);

            String command = "say --voice=" +
                    alarmPhrases.voice[randVoice] + " " +
                    alarmPhrases.badJokes[randBadJoke];


            Process proc = Runtime.getRuntime().exec(command);

            // Read the output

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));

            String line = "";
            while((line = reader.readLine()) != null)
            {
                System.out.print(line + "\n");
            }

            proc.waitFor();

            terminal.clear();

        }


    }
}
