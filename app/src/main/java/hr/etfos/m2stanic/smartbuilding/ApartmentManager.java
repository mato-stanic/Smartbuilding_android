package hr.etfos.m2stanic.smartbuilding;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mato on 19.04.16..
 */
public class ApartmentManager {

    private ApartmentLayoutEditSimple apartmentLayoutEditSimple = null;
    private ApartmentLayoutEditAdvanced apartmentLayoutEditAdvanced = null;
    private ApartmentDeleteCron apartmentDeleteCron = null;

    public static void changeRoomState(Switch sw, Context context, Long apartmentId, String roomToChange, boolean state){
        new ApartmentManager().startSimpleAsyncTask(sw, context, apartmentId, roomToChange, state);
    }

    public void startSimpleAsyncTask(Switch sw, Context context, Long apartmentId, String roomToChange, boolean state){
        apartmentLayoutEditSimple = new ApartmentLayoutEditSimple(sw, context, apartmentId, roomToChange, state);
        apartmentLayoutEditSimple.execute((Void) null);
    }

    public static void advancedLayout(Context context, List<String> days, Long apartmentId, String roomToChange, String action, String time){
        new ApartmentManager().startAdvancedAsyncTask(context, days, apartmentId, roomToChange, action, time);
    }

    public void startAdvancedAsyncTask(Context context, List<String> days, Long apartmentId, String roomToChange, String action, String time){
        apartmentLayoutEditAdvanced = new ApartmentLayoutEditAdvanced(context, days, apartmentId, roomToChange, action, time);
        apartmentLayoutEditAdvanced.execute((Void) null);
    }

    public static void deleteCron(Context context, Long cronJobId){
        new ApartmentManager().startDeleteCronAsyncTask(context, cronJobId);
    }

    public void startDeleteCronAsyncTask(Context context, Long cronJobId){
        apartmentDeleteCron = new ApartmentDeleteCron(context, cronJobId);
        apartmentDeleteCron.execute((Void) null);
    }

    public class ApartmentLayoutEditSimple extends AsyncTask<Void, Void, Boolean> {

        private final Long apartmentId;
        private final String roomToChange;
        private final Boolean state;
        private final Context context;
        private final Switch sw;
        private String apartmentLayout;
        private Integer statusCode;

        ApartmentLayoutEditSimple(Switch sw, Context context, Long apartmentId, String roomToChange, Boolean state) {
            this.sw = sw;
            this.context = context;
            this.apartmentId = apartmentId;
            this.roomToChange = roomToChange;
            this.state = state;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/editSimple");
//            HttpPost httppost = new HttpPost("http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/editSimple");
            try {
                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("apartmentId", String.valueOf(apartmentId)));
                postParameters.add(new BasicNameValuePair("roomToChange", roomToChange));
                postParameters.add(new BasicNameValuePair("state", String.valueOf(state)));


                httppost.setEntity(new UrlEncodedFormEntity(postParameters));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK)
                {
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    apartmentLayout = ParseResponse.iStream_to_String(is);

                }
                else{
                    return false;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                System.out.println("apartment layout: " + apartmentLayout);
                Toast.makeText(context, "Uspješno promijenjena vrijednost", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, "Nažalost dogodila se greška", Toast.LENGTH_LONG).show();
                sw.toggle();
//                Intent intent = new Intent(context, SimpleLayoutActivity.class);
//                context.startActivity(intent);

            }
        }

    }


    public class ApartmentLayoutEditAdvanced extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private final List<String> days;
        private final Long apartmentId;
        private final String roomToChange;
        private final String action;
        private final String time;
        private String apartmentCronJob;
        private Integer statusCode;

        ApartmentLayoutEditAdvanced(Context context, List<String> days, Long apartmentId, String roomToChange, String action, String time) {
            this.context = context;
            this.days = days;
            this.apartmentId = apartmentId;
            this.roomToChange = roomToChange;
            this.action = action;
            this.time = time;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/editAdvanced");
//            HttpPost httppost = new HttpPost("http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/editAdvanced");
            try {
                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("apartmentId", String.valueOf(apartmentId)));
                postParameters.add(new BasicNameValuePair("roomToChange", roomToChange));
                postParameters.add(new BasicNameValuePair("action", action));
                for (String day : days) {
                    postParameters.add(new BasicNameValuePair("days", day));
                }
                postParameters.add(new BasicNameValuePair("time", time));

                httppost.setEntity(new UrlEncodedFormEntity(postParameters));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK)
                {
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    apartmentCronJob = ParseResponse.iStream_to_String(is);

                }
                else{
                    return false;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                System.out.println("cron job: " + apartmentCronJob);
                Toast.makeText(context, "Uspješno dodan automatski zadatak", Toast.LENGTH_LONG).show();
            }
            else {
                if(statusCode == null)
                    Toast.makeText(context, "Nažalost dogodila se greška", Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(context, "Automatski zadatak već postoji", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    public class ApartmentDeleteCron extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
        private final Long cronId;
        private Integer statusCode;

        ApartmentDeleteCron(Context context, Long cronId) {
            this.context = context;
            this.cronId = cronId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/deleteCron");
//            HttpPost httppost = new HttpPost("http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/deleteCron");
            try {
                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("cronJobId", String.valueOf(cronId)));
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK)
                {
                    return true;

                }
                else{
                    return false;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
//            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Toast.makeText(context, "Uspješno obrisan automatski zadatak", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, CronListActivity.class);
                context.startActivity(intent);
            }
            else {
                if(statusCode == null)
                    Toast.makeText(context, "Nažalost dogodila se greška", Toast.LENGTH_LONG).show();
                else{
                    Toast.makeText(context, "Automatski zadatak već postoji", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
