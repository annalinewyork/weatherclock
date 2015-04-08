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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    /**
     * SAMPLE CODE: Returns sunset time for the current day.
     */
    public static Calendar getSunset() {
        URL url = HTTP.stringToURL("http://api.openweathermap.org/data/2.5/weather?q=New%20York,NY");
        String doc = HTTP.get(url);
        JSONObject obj = (JSONObject) JSONValue.parse(doc);

        JSONObject sys = (JSONObject) obj.get("sys");
        if (sys == null)
            return null;
        Long sunsetTimestamp = (Long) sys.get("sunset");
        if (sunsetTimestamp == null)
            return null;
        return DateTime.fromTimestamp(sunsetTimestamp);
    }

    /**
     * SAMPLE CODE: Displays a very primitive clock.
     */
    public static void main(String[] args) throws IOException, ParseException, InterruptedException
    {


        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=New%20York,NY");

        String info = HTTP.get(url);

        JSONObject jsonweatherobj = (JSONObject) JSONValue.parse(info);

        //Gets the city name
        String cityName = (String)jsonweatherobj.get("name");

        //calls the map main in the API to get values within it.
        JSONObject tempJSONObj = (JSONObject)jsonweatherobj.get("main");
        //calls temp,pressure, and humidity from "main".
        Double temp = (Double)tempJSONObj.get("temp");
        Double pressure = (Double)tempJSONObj.get("pressure");
        Long humid = (Long)tempJSONObj.get("humidity");
        //calls the map sys in the API to get values within it.
        JSONObject sunrise = (JSONObject)jsonweatherobj.get("sys");
        //calls sunrise time from "sys".
        Long sunrsTime = (Long)sunrise.get("sunrise");
        //calls the array "weather, to gain values within it.
        JSONArray weathr = (JSONArray)jsonweatherobj.get("weather");
        //calls object at index 0.
        JSONObject weatherJsonObj =(JSONObject)weathr.get(0);
        //calls "description" within that object, which the api tells us is the
        //current weather.
        String currWeather = (String)weatherJsonObj.get("description");

        //sets up alarm.
        System.out.println("Set your alarm: HH:MM");
        Scanner scanner = new Scanner(System.in);

        String alarm = scanner.next();

        int alarmHour = Integer.valueOf(alarm.substring(0,2));
        int alarmMinute = Integer.valueOf(alarm.substring(3,5));




        // Find out the size of the terminal currently.
        final int numCols = TerminalSize.getNumColumns();
        final int numRows = TerminalSize.getNumLines();

        // Create the terminal.
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

        // Get sunset time for the current day.
        Calendar sunset = getSunset();

        int xPosition = 1 + numCols / 2 - 5;
        while (true) {



            //Set the City name.
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(19,xPosition-50);
            terminal.write("City: "+ cityName);


            // Get the current date and time.
            Calendar cal = Calendar.getInstance();

            //Sets off alarm if user input equal cal output.
            if(cal.get(Calendar.HOUR)==alarmHour&& cal.get(Calendar.MINUTE) == alarmMinute){
                eyeAnimation(terminal,5);
            }

            // Write the day of the week in green on a blue background.
            String dayOfWeek = DateTime.getDayOfWeekNames().get(cal.get(Calendar.DAY_OF_WEEK));
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            terminal.moveTo(21, xPosition-50);
            terminal.write("Day : " + dayOfWeek + "  ");


            // Write the time, including seconds, in white.
            String time = DateTime.formatTime(cal, true);
            if (cal.get(Calendar.HOUR_OF_DAY) >= 12)
                time += " PM";
            else time += " AM";
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            terminal.moveTo(20, xPosition-50);
            terminal.write("Time: "+ time);

            // Write the date in gray.
            String date = DateTime.formatDate(cal);
            terminal.setTextColor(AnsiTerminal.Color.WHITE, false);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            terminal.moveTo(4, xPosition);
            terminal.write("Date: "+date);



            // Set the background color back to black.
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);


            //sets the position of the current weather
            terminal.setTextColor(AnsiTerminal.Color.GREEN);
            terminal.moveTo(6, xPosition);
            terminal.write("Weather: "+currWeather);


            //sets position of temperature in terminal
            terminal.setTextColor(AnsiTerminal.Color.MAGENTA);
            terminal.moveTo(8,xPosition);
            //temp returns kelvin, convert to farenheit using F = 1.8 x (K - 273) + 32.
            double F = 1.8*(temp-273) +32;
            // Following code trims the double.
            DecimalFormat df = new DecimalFormat("#.##");
            terminal.write("Temp: "+ df.format(F)+"F");

            //sets position of pressure in terminal
            terminal.setTextColor(AnsiTerminal.Color.MAGENTA);
            terminal.moveTo(9,xPosition);
            //Pressure returns hectopascal(hPa), convert to inch of mercury (inHg) using,
            //0.02952998751*hPa = inHg
            Double inHgPressure = 0.02952998751*pressure;
            //trims the number
            DecimalFormat inHg = new DecimalFormat("##.#");
            terminal.write("Pressure: "+inHg.format(inHgPressure)+" inHg");

            //sets position of pressure in terminal
            terminal.setTextColor(AnsiTerminal.Color.MAGENTA);
            terminal.moveTo(10,xPosition);
            terminal.write("Humidity: "+humid+" %");




            //sunrise time
            //Returns Epoch time.
            terminal.setTextColor(AnsiTerminal.Color.YELLOW, false);
            terminal.moveTo(12, xPosition);
            //converts epoch time to standard readable time.
            String sunRise = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date (sunrsTime*1000));
            terminal.write("Sunrise time: " + sunRise +" AM");


            // Write sunset time in dark yellow.
            String sunsetTime = DateTime.formatTime(sunset, true);
            terminal.setTextColor(AnsiTerminal.Color.YELLOW, false);
            terminal.moveTo(13, xPosition);
            terminal.write("Sunset time: " + sunsetTime +" PM");


            //Daylights
            terminal.setTextColor(AnsiTerminal.Color.BLUE);
            terminal.moveTo(14, xPosition);
            boolean isDST = DST.isDST(cal);
            if(isDST){
                terminal.write("Daylights Saving: yes");
            }else{
                terminal.write("Daylights Saving: No");
            }


            // 3. Show whether this is a national holiday, and if so, which.
            HashMap<Calendar, String> holidays = Holidays.getHolidays("National holiday");
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.moveTo(15,xPosition);

            if (holidays.get(date)==null){
                terminal.write("National Holiday:  None");
            }else {
                terminal.write("National Holiday:  " + holidays.get(date));
            }




            // Pause for one second, and do it again.
            DateTime.pause(1.0);
        }
    }
    //Written by John with a lot of help from Ramona.
    public static void eyeAnimation(AnsiTerminal terminal,int frames) throws IOException, InterruptedException
    {

        terminal.clear();

        while(frames > 0){

            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(15,0);
            terminal.write(eye.eyeClosed);
            DateTime.pause(0.3);
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(15,0);
            terminal.write(eye.eyeSemi);
            DateTime.pause(0.3);
            terminal.setTextColor(AnsiTerminal.Color.CYAN);
            terminal.moveTo(15,0);
            terminal.write(eye.eyeOpen);
            DateTime.pause(0.3);

            frames--;

            //calls upon the terminal to read a string once the alarm goes off.
            //Written by Ramona. Random function by John.
            terminal.setTextColor(AnsiTerminal.Color.GREEN, false);
            terminal.moveTo(1, 1);

            int jokeLength = alarmPhrases.badJokes.length;
            int voiceLength = alarmPhrases.voice.length;

            int randBadJoke = (int)(Math.random()*jokeLength);
            int randVoice = (int)(Math.random()*voiceLength);

            String command = "say --voice="+
                    alarmPhrases.voice[randVoice]+" "+
                    alarmPhrases.badJokes[randBadJoke];


            Process proc = Runtime.getRuntime().exec(command);

            // Read the output

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while((line = reader.readLine()) != null) {
                System.out.print(line + "\n");
            }

            proc.waitFor();

            terminal.clear();


        }


    }
}
