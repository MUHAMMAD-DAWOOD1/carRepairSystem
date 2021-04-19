package ca.mcgill.ecse321.repairsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUp extends AppCompatActivity {
    private String error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signUp(findViewById(R.id.button_signUp));
    }

    private void refreshErrorMessage() {
        TextView tvError = findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }
    /*
        create a customer's profile
     */
    public void signUp(View v) {
        Button toSignUp = (Button) findViewById(R.id.button_signUp);

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error = "";
                final EditText email = (EditText) findViewById(R.id.new_email);
                final EditText password = (EditText) findViewById(R.id.new_password);
                final EditText name = (EditText) findViewById(R.id.new_name);
                final EditText phone = (EditText) findViewById(R.id.new_phone);
                final EditText address = (EditText) findViewById(R.id.new_address);
                final EditText credit = (EditText) findViewById(R.id.credit);
                final EditText debit = (EditText) findViewById(R.id.debit);

                String request = "";
                request = request.concat(name.getText().toString().substring(0,name.getText().toString().indexOf(" ")));
                request = request.concat(name.getText().toString().substring(name.getText().toString().indexOf(" ")+1,(name.getText().toString().length() )));
                request =  request.concat("?password="+password.getText().toString());
                request =  request.concat("&phone="+phone.getText().toString());
                request =  request.concat("&email="+email.getText().toString());
                request =  request.concat("&credit="+credit.getText().toString());
                request = request.concat("&debit="+debit.getText().toString());


                request = request.concat("&address="+address.getText().toString().substring(0,address.getText().toString().indexOf(" ")));
                request = request.concat(address.getText().toString().substring(address.getText().toString().indexOf(" "),address.getText().toString().indexOf(" ", 1)));

                HttpUtils.post("customer/" + request, new RequestParams(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Intent intent = new Intent(SignUp.this, homePage.class);
                        try {
                            intent.putExtra("customerId", response.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {
                            error += errorResponse.get("").toString();
                        } catch (JSONException e) {
                            error += e.getMessage();
                        }
                        refreshErrorMessage();
                    }
                });
            }
        });
    }
}