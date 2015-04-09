package nyc.c4q.ac21.weatherclock;

import nyc.c4q.ac21.weatherclock.AnsiTerminal;
import nyc.c4q.ac21.weatherclock.CloudASCII;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by c4q-nali on 4/8/15.
 */
public class WeatherDisplay {

    public static void weather(int x, int y, JSONObject weatherData, AnsiTerminal terminal, int sec) {


        JSONArray weather = (JSONArray) weatherData.get("weather");
        JSONObject weatherinfo = (JSONObject) weather.get(0);
        Long weatherID = (Long) weatherinfo.get("id");

        for (int i = 0; i < CloudASCII.clouds1.length; i++) {
            terminal.moveTo(y + 1 + i, x);
            terminal.write(CloudASCII.space);
        }

        if (weatherID >= 500 && weatherID <= 531) {
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
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
        } else if (weatherID >= 600 && weatherID <= 622) {
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.snow1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.snow1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.snow2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.snow2[i]);

                }
            }

        } else if (weatherID >= 200 && weatherID <= 232) {
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.strom1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.strom1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.strom2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.strom2[i]);

                }
            }

        } else if (weatherID >= 800 && weatherID <= 804) {
            terminal.setTextColor(AnsiTerminal.Color.YELLOW);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.clouds1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.clouds1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.clouds2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.clouds2[i]);

                }
            }
        }
        else if (weatherID >= 701 && weatherID <= 781){
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.atmosphere1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.atmosphere1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.atmosphere2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.atmosphere2[i]);

                }
            }
        }
        else if (weatherID >= 300 && weatherID <= 321){
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.drizzle1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.drizzle1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.drizzle2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.drizzle2[i]);

                }
            }
        }
        else if (weatherID >= 900 && weatherID <= 906){
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.extreme1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.extreme1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.extreme2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.extreme2[i]);

                }
            }
        }
        else if (weatherID >= 951 && weatherID <= 962){
            terminal.setTextColor(AnsiTerminal.Color.WHITE);
            terminal.setBackgroundColor(AnsiTerminal.Color.BLACK);
            if (sec % 2 == 0) {

                for (int i = 0; i < CloudASCII.additional1.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.additional1[i]);
                }

            } else {
                for (int i = 0; i < CloudASCII.additional2.length; i++) {
                    terminal.moveTo(y + 1 + i, x);
                    terminal.write(CloudASCII.additional2[i]);

                }
            }
        }


    }
}