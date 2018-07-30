package com.example.intern.sessionsapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    //Format of date in json file
    public static String JSONformat = "yyyy-MM-dd'T'HH:mm:ssXXX";

    /* Method that returns the date String formatted as given by dateFormatString
    * @param dateString i.e the date to be formatted
    * @param dateFormatString i.e the format of the date
    * */
    public static String getDateString(String dateString, String dateFormatString){


        Date date = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat(JSONformat);
            date = format.parse(dateString);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        String res = dateFormat.format(date);


        return res;




    }






}
