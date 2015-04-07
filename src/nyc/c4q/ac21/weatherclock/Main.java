package nyc.c4q.ac21.weatherclock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;

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
    public static void main(String[] args) throws IOException, ParseException
    {
        //Url to get the weather api. as it stands it throws an exception error because it's receiving
        //a string.
//        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=New%20York,NY");
//        String weatherInfo = HTTP.get(url);


        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=New%20York,NY");

        String info = HTTP.get(url);

        JSONObject jsonweatherobj = (JSONObject) JSONValue.parse(info);

        //city
        String cityName = (String)jsonweatherobj.get("name");

        JSONObject tempJSONObj = (JSONObject)jsonweatherobj.get("main");
        Double temp = (Double)tempJSONObj.get("temp");
        Double pressure = (Double)tempJSONObj.get("pressure");
        Long humid = (Long)tempJSONObj.get("humidity");

        JSONObject sunrise = (JSONObject)jsonweatherobj.get("sys");
        Long sunrsTime = (Long)sunrise.get("sunrise");

        JSONArray weathr = (JSONArray)jsonweatherobj.get("weather");
        JSONObject weatherJsonObj =(JSONObject)weathr.get(0);
        String currWeather = (String)weatherJsonObj.get("description");








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












            // Pause for one second, and do it again.
            DateTime.pause(1.0);
        }
    }
}
