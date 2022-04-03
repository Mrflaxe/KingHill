package ru.starmc.kinghill.formatter;

public class TimeFormatter {

    public String format(int timeInSeconds, String pattern) {
        int seconds = timeInSeconds / 60;
        int minutes = timeInSeconds / 60;
        int hours = timeInSeconds / 3600;
        int days = hours / 24;

        if(pattern.contains("mm")) {
            seconds = timeInSeconds % 60;
        }
        
        if(pattern.contains("hh")) {
           minutes = timeInSeconds % 3600;
        }
        
        if(pattern.contains("dd")) {
            hours = hours % 24;
        }
        
        
        String result = pattern.replace("ss", "" + seconds);
        result = result.replace("mm", "" + minutes);
        result = result.replace("hh", "" + hours);
        result = result.replace("dd", "" + days);
        
        return result;
    }
}
