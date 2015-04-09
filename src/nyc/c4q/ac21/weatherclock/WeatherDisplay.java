package nyc.c4q.ac21.weatherclock;

import nyc.c4q.ac21.weatherclock.AnsiTerminal;
import nyc.c4q.ac21.weatherclock.CloudASCII;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by c4q-nali on 4/8/15.
 */
public class WeatherDisplay {

    public static void weather (int x, int y, JSONObject weatherData, AnsiTerminal terminal, int sec){
        terminal.setTextColor(AnsiTerminal.Color.WHITE);
        terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);

        JSONArray weather = (JSONArray) weatherData.get("weather");
        JSONObject weatherinfo = (JSONObject) weather.get(0);
        Long weatherID = (Long) weatherinfo.get("id");

        for (int i=0; i<CloudASCII.rain1.length;i++){
            terminal.moveTo(y+1+i,x);
            terminal.write(CloudASCII.space);
        }

        if (weatherID >= 500 && weatherID <= 531) {
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.rain1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.rain1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.rain2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.rain2[i]);

                }
            }
        }

    }
}
