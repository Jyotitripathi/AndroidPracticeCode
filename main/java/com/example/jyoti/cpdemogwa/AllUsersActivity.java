package com.example.jyoti.cpdemogwa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AllUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    @InjectView(R.id.listView)
    ListView listView;

    StringRequest request;
    RequestQueue requestQueue;

    String RETRIEVE_URL = "https://androidprojrct.000webhostapp.com/retrieve.php";

    ProgressDialog dialog;

    ArrayList<User> userList;
    UserAdapter userAdapter;

    ArrayList<String> userNameList;
    ArrayAdapter<String> adapter;

    User user;
    int position;

    //RecyclerView and RecyclerView.Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        ButterKnife.inject(this);

        requestQueue = Volley.newRequestQueue(this);
        userList = new ArrayList<>();
       // userNameList = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait..");

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        retrieveAllUsers();

    }

    void retrieveAllUsers() {

        request = new StringRequest(
                Request.Method.GET, RETRIEVE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    JSONArray jsonArray = jsonObject.getJSONArray("USERS");



                    int id = 0;
                    String n ="",e ="",p ="",g ="",c ="";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);



                        id =jObj.getInt("ID");
                        n = jObj.getString("NAME");
                        e = jObj.getString("EMAIL");
                        p = jObj.getString("PASSWORD");
                        g = jObj.getString("GENDER");
                        c = jObj.getString("CITY");

                        User user = new User(id,n,e,p,g,c);
                        userList.add(user);

                        //userNameList.add(n);
                    }

                    //adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,userNameList);
                    //listView.setAdapter(adapter);

                    userAdapter = new UserAdapter(getApplicationContext(), R.layout.listitem, userList);
                    listView.setAdapter(userAdapter);
                    listView.setOnItemClickListener(AllUsersActivity.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Some Exception: " + e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                }
                dialog.dismiss();

            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Some Volley Error: " + error, Toast.LENGTH_LONG).show();

                    }
                }
        );
        dialog.show();
        requestQueue.add(request); //process the request
    }


        void showUser(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Details of " + user.getName());
            builder.setMessage(user.toString());
            builder.setPositiveButton("Done", null);
            builder.create().show();
        }

        void showOptions() {
            final AlertDialog.Builder builder = new  AlertDialog.Builder(this);
            String[] items = {
                    "View User","Update User"
            };
            builder.setItems(items,new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface,int position) {

                    switch (position){
                        case 0:
                            showUser();
                            break;
                        case 1:
                            Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                            intent.putExtra("UpdateData", user);
                            startActivityForResult(intent, 101);

                    }
                }
                    }
            );
            builder.create().show();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101 && resultCode==201){
            User u1=(User) data.getSerializableExtra("UpdatedUser");
            userList.set(position,u1);
            userAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               user = userList.get(position);
                showOptions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
