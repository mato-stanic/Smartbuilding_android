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
public class ApartmentLayout {

    private ApartmentLayoutEdit apartmentLayoutEdit = null;
    public static void changeRoomState(Switch sw, Context context, Long apartmentId, String roomToChange, boolean state){
        new ApartmentLayout().startAsyncTask(sw, context, apartmentId, roomToChange, state);
    }

    public void startAsyncTask(Switch sw, Context context, Long apartmentId, String roomToChange, boolean state){
        apartmentLayoutEdit = new ApartmentLayoutEdit(sw, context, apartmentId, roomToChange, state);
        apartmentLayoutEdit.execute((Void) null);
    }

    public class ApartmentLayoutEdit extends AsyncTask<Void, Void, Boolean> {

        private final Long apartmentId;
        private final String roomToChange;
        private final Boolean state;
        private final Context context;
        private final Switch sw;
        private String apartmentLayout;
        private Integer statusCode;

        ApartmentLayoutEdit(Switch sw, Context context, Long apartmentId, String roomToChange, Boolean state) {
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
            HttpPost httppost = new HttpPost("http://192.168.178.33:8080/smartbuilding/android/admin/apartmentLayout/edit");
//            HttpPost httppost = new HttpPost("http://89.107.57.144:8080/smartbuilding/android/admin/apartmentLayout/edit");
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
}
