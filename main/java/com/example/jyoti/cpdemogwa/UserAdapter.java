package com.example.jyoti.cpdemogwa;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;

/**
 * Created by jyoti on 8/1/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {

    Context context;
    int resource;
    ArrayList<User> userList;

    public UserAdapter(Context context, int resource, ArrayList<User> userList) {
        super(context, resource, userList);

        this.context = context;
        this.resource = resource;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= null;

        view = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView txtName = (TextView)view.findViewById(R.id.textViewName);
        TextView txtEmail = (TextView)view.findViewById(R.id.textViewEmail);

        User user= userList.get(position);

        txtName.setText(user.getId()+" - "+user.getName());
        txtEmail.setText(user.getEmail());

        return view;
    }

}
