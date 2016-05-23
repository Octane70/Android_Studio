package com.octane70.rob.app_gardenpi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    EditText editText;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.Name);
        editText2 = (EditText) findViewById(R.id.part_nr);
    }
    class Create_Part extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Sending part to the database..."); //Set the message for the loading window
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show(); //Place the loading message on the screen
        }

        @Override
        protected String doInBackground(String... args) {
            String String_name = editText.getText().toString(); //Get the inserted text from the editText files
            String Int_Part = editText2.getText().toString();

            List params = new ArrayList<>();
            params.add(new BasicNameValuePair("Name", String_name)); //Add the parameters to an array
            params.add(new BasicNameValuePair("part_nr", Int_Part));

            JSONObject json = jsonParser.makeHttpRequest("24.70.110.31/db_create.php", "POST", params);

            try {
                int success = json.getInt("success");

                if(success == 1){
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url){
            pDialog.dismiss();
        }
    }
}


