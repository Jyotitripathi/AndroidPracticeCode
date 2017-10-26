package com.example.jyoti.cpdemogwa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    @InjectView(R.id.editTextName)
    EditText eTxtName;

    @InjectView(R.id.editTextEmail)
    EditText eTxtEmail;

    @InjectView(R.id.editTextPassword)
    EditText eTxtPassword;

    @InjectView(R.id.radioButtonMale)
    RadioButton rbMale;

    @InjectView(R.id.radioButtonFemale)
    RadioButton rbFemale;

    @InjectView(R.id.spinnerCity)
    public Spinner spCity;

    @InjectView(R.id.buttonSignUp)
    Button btnSignUp;

//    Button btnSign =(Button) findViewById(R.id.buttonSignUp);

    ArrayAdapter<String> adapter;
    User user, rcvUser;
    StringRequest request;
    RequestQueue requestQueue;

    String SignUp_URL = "https://androidprojrct.000webhostapp.com/insert.php";
    String UPDATE_URL = "https://androidprojrct.000webhostapp.com/update.php";

    ProgressDialog dialog;
    boolean updateMode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);

        ButterKnife.inject(this);

        // initialise volley's request queue
        requestQueue = Volley.newRequestQueue(this);

        user = new User();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.add("--Select City"); //0
        adapter.add("Ludhiana");
        adapter.add("chandigarh");
        adapter.add("Delhi");
        adapter.add("Banglore");
        adapter.add("Pune"); // n-1

        spCity.setAdapter(adapter);
        spCity.setOnItemSelectedListener(this);
        btnSignUp.setOnClickListener(this);
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("UpdateData");

        if (updateMode) {
            rcvUser = (User) rcv.getSerializableExtra("UpdateData");
            eTxtName.setText(rcvUser.getName());
            eTxtEmail.setText(rcvUser.getEmail());
            eTxtPassword.setText(rcvUser.getPassword());

            if (rcvUser.getGender().equals("Male")) {
                rbMale.setChecked(true);
                rbFemale.setChecked(false);

            } else {
                rbFemale.setChecked(true);
                rbMale.setChecked(false);
            }
            for (int i = 0; i < adapter.getCount(); i--) {
                if (adapter.getItem(i).equals(rcvUser.getCity())) {
                    spCity.setSelection(i);
                    break;
                }
            }
        }
        btnSignUp.setText("UPDATE");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonSignUp:
                user.setName(eTxtName.getText().toString().trim());
                user.setEmail(eTxtEmail.getText().toString().trim());
                user.setPassword(eTxtPassword.getText().toString().trim());

                SignUpUser();

                break;
            case R.id.radioButtonMale:
                user.setGender("Male");
                break;
            case R.id.radioButtonFemale:
                user.setGender("Female");
                break;
        }
    }

    void SignUpUser() {
        String url = "";
        if (updateMode)
            url = UPDATE_URL;
        else
            url = SignUp_URL;
        dialog.show();

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), message + " - " + success, Toast.LENGTH_LONG).show();

                    if (updateMode){
                        Intent intent = new Intent(getApplicationContext(),AllUsersActivity.class);
                        intent.putExtra("UpdatedData",user);
                        setResult(201,intent);
                        finish();

                    }
                    else

                        clearFields();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Some Exception: " + e, Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                //  Log.i("test",user.toString()); for testing purpose

                if (updateMode) {
                    map.put("id", String.valueOf(rcvUser.getId()));
                }
                map.put("name", user.getName());
                map.put("email", user.getEmail());
                map.put("password", user.getPassword());
                map.put("gender", user.getGender());
                map.put("city", user.getCity());
                // Log.i("test",user.toString());
                return map;
            }
        }
        ;

        requestQueue.add(request);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String City = adapter.getItem(position);
        //uesr.city=city;
        user.setCity(adapter.getItem(position));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void clearFields() {
        eTxtName.setText("");
        eTxtEmail.setText("");
        eTxtPassword.setText("");

        spCity.setSelection(0);

        rbMale.setChecked(false);
        rbFemale.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_ShowAllUsers){
            Intent intent = new Intent(getApplicationContext(), AllUsersActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}