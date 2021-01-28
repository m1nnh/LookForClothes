package com.sangmin.LookForClothes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

//import android.support.v7.app.AppCompatActivity;


public class DBsearch extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            // 2초뒤에 다음화면(MainActivity)으로 넘어가기 Handler 사용
            Intent intent2 = getIntent();
            String name = intent2.getStringExtra("DATA");
            Intent intent3 = getIntent();
            String size = intent3.getStringExtra("SIZE");
            Intent intent4 = getIntent();
            String color = intent4.getStringExtra("COLOR");
            Intent intent = new Intent(getApplicationContext(), GoogleMaps.class);
            intent.putExtra("DATA2", name);
            intent.putExtra("SIZE2", size);
            intent.putExtra("COLOR2", color);
            startActivity(intent); // 다음화면으로 넘어가기
            overridePendingTransition(0,0);
            finish(); // Activity 화면 제거
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    // 다시 화면에 들어왔을 때 예약 걸어주기
        handler.postDelayed(r, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    // 화면을 벗어나면, handler 에 예약해놓은 작업을 취소하자
        handler.removeCallbacks(r); // 예약 취소
    }


    private static String TAG = "lookforclothes";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_COLOR = "color";
    private static final String TAG_SIZE ="size";
    private static final String TAG_STORE ="store";
    private static final String TAG_DETAILS ="details";
    private static final String TAG_LATITUDE ="latitude";
    private static final String TAG_LONGTITUDE ="longtitude";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;


    public static double arr[][] = new double[7][2];
    public static String[] loc = new String[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbresult);


        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        //mArrayList.clear();

        GetData task = new GetData();
        task.execute(TextCameraActivity.input_product_id,TextCameraActivity.input_size,TextCameraActivity.input_color);


        mArrayList = new ArrayList<>();


    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(DBsearch.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String searchKeyword3 = params[2];

            String link = "http://245a6a29.ngrok.io/sss.php?id="
                    +searchKeyword1+"&size="
                    +searchKeyword2+"&color="
                    +searchKeyword3;


            try {
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }



    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String color = item.getString(TAG_COLOR);
                String size = item.getString(TAG_SIZE);
                String store = item.getString(TAG_STORE);
                String details = item.getString(TAG_DETAILS);
                loc[i] = details;
                String latitude = item.getString(TAG_LATITUDE);
                arr[i][0] = Double.parseDouble(latitude);
                String longtitude = item.getString(TAG_LONGTITUDE);
                arr[i][1] = Double.parseDouble(longtitude);




                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_COLOR, color);
                hashMap.put(TAG_SIZE, size);
                hashMap.put(TAG_STORE, store);
                hashMap.put(TAG_LATITUDE, latitude);
                hashMap.put(TAG_LONGTITUDE, longtitude);


                mArrayList.add(hashMap);
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}