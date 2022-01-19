package com.app.common.utils;

import static android.content.Context.ACTIVITY_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.app.common.R;
import com.app.common.constant.AppCommonConstants;
import com.app.common.interfaces.DatePickerDialogListener;
import com.app.common.interfaces.GPSUtilsGetGPSStatus;
import com.app.common.interfaces.TimePickerDialogListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;

/**
 * This class is used to define the common functions to be used in the application
 *
 * @author Ritesh Pandhurkar
 * @author Shivam Jamaiwar
 * @version 1.7
 */
public class Utility {

    /**
     * SDF to generate a unique name for our compress file.
     */
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final String TAG = Utility.class.getSimpleName();
    Context context;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TimePickerDialogListener mTimePickerDialogListerner;
    private DatePickerDialogListener mDatePickerDialogListener;

    public Utility(Context context) {
        this.context = context;
    }

    /**
     * print any data to log
     *
     * @param key  key for identification
     * @param data data to show in logcat
     */
    public static void printLogs(String key, String data) {
        Log.e(key, data);
    }

    /**
     * Show toaster.
     *
     * @param context Current activity context.
     * @param message Message to be show on toaster.
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show snackbar.
     *
     * @param view    Current view.
     * @param message Message to be show on snackbar.
     * @param length  Showing length.
     * @return Snackbar instance.
     */
    public static Snackbar showErrorSnackBar(View view, String message, int length) {
        Snackbar s = Snackbar
                .make(view, message, length);
        View mview = s.getView();
        mview.setBackgroundColor(Color.RED);
        return s;
    }

    /**
     * Convert date from one date format to another date format.
     *
     * @param date           the date we want to change
     * @param currentFormat  current date format
     * @param requiredFormat date format which you want to convert
     * @return convertable date
     */
    public static String convertDate(String date, String currentFormat, String requiredFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(currentFormat);
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat(requiredFormat);
            return serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * convert string date to Date object
     *
     * @param dateString date in the form of string
     * @param dateFormat format of date which you want to convert
     * @return converted date object
     */
    public static Date convertStringToDate(String dateString, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateInString = dateString;
        Date date = null;
        try {
            date = formatter.parse(dateInString);
            System.out.println(date);
            System.out.println(formatter.format(date));
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert date object to string in given date format.
     *
     * @param date       Object of Date, Which we have to convert.
     * @param dateFormat Date format.
     * @return String date in given date format.
     */
    public static String convertDateToString(Date date, String dateFormat) {
        DateFormat dateFormatPattern = new SimpleDateFormat(dateFormat);
        String strDate = dateFormatPattern.format(date);
        return strDate;
    }

    /**
     * Calculate days between two dates.
     *
     * @param d1 1st date
     * @param d2 2nd date
     * @return no. of days between given dates.
     */
    public static long getDifferenceBetweenTwoDate(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static long getDifferenceBetweenTwoDateInMinutes(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * Calculate age from given date of birth.
     *
     * @param year  Year of given date.
     * @param month Month of given date.
     * @param day   Day of given date.
     * @return
     */
    public static int getAge(int year, int month, int day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, noofyears;

        y = cal.get(Calendar.YEAR);// current year ,
        m = cal.get(Calendar.MONTH);// current month
        d = cal.get(Calendar.DAY_OF_MONTH);//current day
        cal.set(year, month, day);// here ur date
        noofyears = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --noofyears;
        }
        if (noofyears < 0)
            throw new IllegalArgumentException("age < 0");
        System.out.println(noofyears);
        return noofyears;
    }

    /**
     * check is com.prasko.vendor.network connectivity is available or not.
     *
     * @return boolean value "true" if com.prasko.vendor.network available.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean value = activeNetworkInfo != null && activeNetworkInfo.isConnected() || mWifi != null && mWifi.isConnected();

        if (!value) {
            Utility.showToast(context, "Network not available !!");
        }
        return value;
    }

    /**
     * make spannable from item value and return spannable text
     *
     * @param filter    text to be filter
     * @param itemValue item which you want to filter
     * @return spannable text
     */
    public static Spannable highlightSearchText(String filter, String itemValue) {

        if (!filter.equals("")) {
            // find all occurrences forward
            Spannable spannable = new SpannableString(itemValue);
            int n = itemValue.indexOf(filter, -1 + 1);
            for (int i = 0; (i = itemValue.toLowerCase().indexOf(filter.toLowerCase(), i)) != -1; i++) {
                int startPos = i;
                int endPos = startPos + filter.length();

                if (startPos != -1) // This should always be true, just a sanity check
                {
                    ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.rgb(33, 150, 243)});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);

                    spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
            return spannable;
        }
        return null;
    }

    /**
     * get metioned json file data in single string variable and return that string
     *
     * @param fileName json file name
     * @return json file data into single variable
     */
    public static String loadJSONFromAsset(Context context, String fileName) {
        String jsonString = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonString;
    }

    /**
     * check entered mail id is correct or not
     *
     * @param email entered mail id
     * @return boolean value, if entered mail id is correct return true otherwise return false
     */
    public static boolean checkEmail(String email) {
        if (email == null) {
            return false;
        }
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * check entered contact no is correct or not
     *
     * @param contact entered contact no.
     * @return
     */
    public static boolean checkContactNo(String contact) {
        if (contact == null) {
            return false;
        }
        Pattern pattern;
        Matcher matcher;
        final String PHONE_PATTERN = "^[0-9]{10}$";
        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(contact);
        return matcher.matches();
    }

    public static boolean checkValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Minutes convert into days, hours and minutes in this format (x days x hrs x min).
     *
     * @param timeInMinutes time in minutes.
     * @return Converted string.
     */
    public static String timeConvert(long timeInMinutes) {

        long days = timeInMinutes / 24 / 60;
        long hours = timeInMinutes / 60 % 24;
        long minutes = timeInMinutes % 60;

        if (hours == 0) {
            return minutes + " min";
        } else if (days == 0) {
            return hours + " hrs " + minutes + " min";
        } else {
            if (days > 1) {
                return days + " days " + hours + " hrs " + minutes + " min";
            } else {
                return days + " day " + hours + " hrs " + minutes + " min";
            }
        }
    }

    //-------------------------------------------------------------------------------

    public static String convertMinutesToDaysAndHours(long timeInMinutes) {
        long days = timeInMinutes / 24 / 60;
        long hours = timeInMinutes / 60 % 24;

        if (days == 0) {
            return hours + " hrs";
        } else {
            if (days > 1) {
                return days + " days " + hours + " hrs";
            } else {
                return days + " day " + hours + " hrs";
            }
        }
    }

    /**
     * convert given seconds into hours, minutes and second in this format (hh:mm:ss)
     *
     * @param durationInSecond time in seconds.
     * @return time string in this format  (hh:mm:ss).
     */
    public static String convertSecondToTime(long durationInSecond) {
        long day = durationInSecond / (24 * 3600);

        durationInSecond = durationInSecond % (24 * 3600);
        long hour = durationInSecond / 3600;
        String hours = hour > 9 ? hour + "" : "0" + hour;

        durationInSecond %= 3600;
        long minute = durationInSecond / 60;
        String minutes = minute > 9 ? minute + "" : "0" + minute;

        durationInSecond %= 60;
        long second = durationInSecond;
        String seconds = second > 9 ? second + "" : "0" + second;

        if (hour > 0) {
            return hours + ":" + minutes + ":" + seconds;
        } else {
            return minutes + ":" + seconds;
        }
    }

    // 1 minute = 60 seconds
    // 1 hour = 60 x 60 = 3600
    // 1 day = 3600 x 24 = 86400
    public static String getsplitTime(long time) {
        String result = null;

        //milliseconds
        long different = time;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays > 0) {
            result = elapsedDays + " day " + elapsedHours + "hour " + elapsedMinutes + " min";
        } else if (elapsedHours > 0) {
            result = elapsedHours + "h " + elapsedMinutes + "m";
        } else if (elapsedMinutes > 0) {
            result = elapsedMinutes + "m";
        }

        return result;
    }

    public static String getTimeAgo(long timeStamp) {
         Date  date = new Date(timeStamp);

        long time = date.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = getCurrentTime();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a min ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " min ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {

            String requiredDateFormat = "Yesterday"
                    + " at "
                    + convertDate(calendar.getTime().toString(), AppCommonConstants.TIMESTAMP_DATE_FORMAT, AppCommonConstants.TIME_FORMAT);
            return requiredDateFormat;
        } else {
            if (diff / DAY_MILLIS <= 3) {

                String requiredDateFormat = diff / DAY_MILLIS + " days ago"
                        + " at "
                        + convertDate(calendar.getTime().toString(), AppCommonConstants.TIMESTAMP_DATE_FORMAT, AppCommonConstants.TIME_FORMAT);
                return requiredDateFormat;
            } else {
                String requiredDateFormat = convertDate(calendar.getTime().toString(), AppCommonConstants.TIMESTAMP_DATE_FORMAT, "dd MMM")
                        + " at "
                        + convertDate(calendar.getTime().toString(), AppCommonConstants.TIMESTAMP_DATE_FORMAT, AppCommonConstants.TIME_FORMAT);
                return requiredDateFormat;
            }
        }
    }

    private static long getCurrentTime() {
        Date currentDate = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);

        return cal1.getTime().getTime();
    }

    private static long changeTimezoneFromUTCtoIST(Date serverTimeStamp) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(serverTimeStamp);
        calendar.add(Calendar.HOUR_OF_DAY, 5);
        calendar.add(Calendar.MINUTE, 30);
        return calendar.getTime().getTime();
    }

    /**
     * Get real path from given Uri.
     *
     * @param context    Current activity content.
     * @param contentUri Uri of file.
     * @return Path in string.
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    //-------------------------------------------------------------------------------

    /**
     * compress the file/photo from @param <b>path</b> to a private location on the current device and return the compressed file.
     *
     * @param context Current android Context
     * @param path    The original image path
     * @return
     * @throws IOException
     */
    public static File getCompressed(Context context, String path) throws IOException {

        if (context == null)
            throw new NullPointerException("Context must not be null.");
        //getting device external cache directory, might not be available on some devices,
        // so our code fall back to internal storage cache directory, which is always available but in smaller quantity
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            //fall back
            cacheDir = context.getCacheDir();

        String rootDir = cacheDir.getAbsolutePath() + "/ImageCompressor";
        File root = new File(rootDir);

        //Create ImageCompressor folder if it doesnt already exists.
        if (!root.exists())
            root.mkdirs();

        //decode and resize the original bitmap from @param path.
        Bitmap bitmap = decodeImageFromFiles(path, /* your desired width*/300, /*your desired height*/ 300);

        //create placeholder for the compressed image file
        File compressed = new File(root, SDF.format(new Date()) + ".jpg" /*Your desired format*/);

        //convert the decoded bitmap to stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        /*compress bitmap into byteArrayOutputStream
            Bitmap.compress(Format, Quality, OutputStream)
            Where Quality ranges from 1 - 100.
         */
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        /*
        Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
        java.io.FileOutputStream can help us do just That!
         */
        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();

        fileOutputStream.close();

        //File written, return to the caller. Done!
        return compressed;
    }

    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        // decode with the sample size
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }

    /**
     * Check given service name running or not.
     *
     * @param serviceName Service name.
     * @param context     Current android Context
     * @return boolean value "true" if given service is running in foreground.
     */
    public static boolean isServiceRunning(String serviceName, Context context) {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(Integer.MAX_VALUE);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if (runningServiceInfo.service.getClassName().equalsIgnoreCase(serviceName)) {

                if (runningServiceInfo.foreground) {
                    //service run in foreground
                    serviceRunning = true;
                    break;
                }
            }
        }
        return serviceRunning;
    }

    /**
     * check phone state permission.
     *
     * @param activity
     * @return
     */
    public static boolean checkPhoneStatePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, AppCommonConstants.RUNTIME_PERMISSION_REQUEST_CODE.PHONE_STATE_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, AppCommonConstants.RUNTIME_PERMISSION_REQUEST_CODE.PHONE_STATE_PERMISSION_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check location permission.
     *
     * @param activity
     * @return
     */
    public static boolean checkLocationPermission(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, AppCommonConstants.RUNTIME_PERMISSION_REQUEST_CODE.REQUEST_LOCATION_CODE);
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, AppCommonConstants.RUNTIME_PERMISSION_REQUEST_CODE.REQUEST_LOCATION_CODE);
                }
                return false;
            } else {
                if (activity.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return false;
                else
                    return true;
            }
        } else {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppCommonConstants.RUNTIME_PERMISSION_REQUEST_CODE.REQUEST_LOCATION_CODE);
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppCommonConstants.RUNTIME_PERMISSION_REQUEST_CODE.REQUEST_LOCATION_CODE);
                }
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * Calculate time and return wish message respective current time.
     *
     * @return wishing message.
     */
    public static String getWishMsg() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String wishMsg = null;

        if (timeOfDay >= 0 && timeOfDay < 12) {
            wishMsg = "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 18) {
            wishMsg = "Good Afternoon";
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            wishMsg = "Good Evening";
        }
        return wishMsg;
    }

    /**
     * show date picker dialog
     *
     * @param context                  Current activity context
     * @param datePickerDialogListener Object of DatePickerDialogListener interface.
     * @param dateToSet                By default date.
     */
    public void datePickerDialog(Context context, DatePickerDialogListener datePickerDialogListener, Date dateToSet) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        if (dateToSet != null) {
            c.setTime(dateToSet);
        }
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mDatePickerDialogListener = datePickerDialogListener;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mDatePickerDialogListener.getSelectedDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 410240376000L);
        datePickerDialog.show();
    }

    /**
     * Show time picker dialog.
     *
     * @param context                   Current activity context
     * @param timePickerDialogListerner Object of TimePickerDialogListerner interface.
     * @param dateToSet                 By default date.
     */
    public void timePickerDialog(Context context, TimePickerDialogListener timePickerDialogListerner, Date dateToSet, final int RequestCode) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        if (dateToSet != null) {
            c.setTime(dateToSet);
        }
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        mTimePickerDialogListerner = timePickerDialogListerner;

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        String am_pm = "";

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);

                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";

                        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
                        //----
                        String minutes = "" + minute;
                        if (minute < 10) {
                            minutes = "0" + minute;
                        }
                        //----
                        mTimePickerDialogListerner.getSelectedTime(strHrsToShow + ":" + minutes + " " + am_pm, RequestCode);
                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }

    public static String getErrorType(int code) {
        if (code >= 400 && code < 500) {
            if (code == 400) {
                return AppCommonConstants.ERROR_TYPE.BAD_REQUEST_ERROR;
            } else {
                return AppCommonConstants.ERROR_TYPE.OTHER_CLIENT_ERROR;
            }
        } else {
            return AppCommonConstants.ERROR_TYPE.SERVER_ERROR;
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
                Toast.makeText(pContext, hashKey + "", Toast.LENGTH_SHORT).show();
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    /**
     * @return the last know best location
     */
    public static Location getLastLocation(Context context, LocationManager locationManager) {
        Location location = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }
            long NetLocationTime = 0;
            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }
            if (0 < GPSLocationTime - NetLocationTime) {
                location = locationGPS;
            } else {
                location = locationNet;
            }
        }
        return location;
    }

    public static void doAskToTurnGPS(Activity context, GPSUtilsGetGPSStatus gpsUtilsGetGPSStatus) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new GpsUtils(context).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                    gpsUtilsGetGPSStatus.receivedGPSStatus(isGPSEnable);
                }
            });
        } else {
            new GpsUtils(context).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                    gpsUtilsGetGPSStatus.receivedGPSStatus(isGPSEnable);
                }
            });
        }
    }

    public static String readJsonFromAssets(Context context, String filePath) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(filePath);
            int size = inputStream.available();
            byte[] byteArray = new byte[size];
            inputStream.read(byteArray);
            inputStream.close();
            json = new String(byteArray, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * @param responseBody this method is used for parse the error message from error body in Retrofit call
     **/
    public static String getApiFailureErrorMsg(ResponseBody responseBody) {
        JSONObject jsonObject = null;
        String errorMsg = null;
        try {
            jsonObject = new JSONObject(new String(responseBody.bytes()));
            errorMsg = jsonObject.get("Message").toString();
        } catch (JSONException | IOException e) {
            errorMsg = e.getMessage();
        }
        return errorMsg;
    }
}
