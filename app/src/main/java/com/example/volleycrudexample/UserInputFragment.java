package com.example.volleycrudexample;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInputFragment extends DialogFragment {

    EditText etName, etEmail, etPassword, etPhoneNum;
    Button btnRegister;
    RegistrationResponseListener listener;
    MySingleTon singleTon;

    private String url = "https://jaladhi-server.herokuapp.com/signup";

    public UserInputFragment() {
        // Required empty public constructor
    }

    public void setResponseListener(RegistrationResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        singleTon = MySingleTon.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_input, container, false);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etPhoneNum = view.findViewById(R.id.etPhoneNum);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewUserData();
            }
        });
        return view;
    }

    private void postNewUserData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", etName.getText().toString());
            jsonObject.put("emailId", etEmail.getText().toString());
            jsonObject.put("password", etPassword.getText().toString());
            jsonObject.put("phNumber", etPhoneNum.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Registration Success", Toast.LENGTH_LONG).show();
                        dismiss();
                        listener.showAllUsers();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Registration Failure", Toast.LENGTH_LONG).show();
                    }
                });
       /* RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjectRequest);*/
        singleTon.addRequestToQueue(jsonObjectRequest);

    }

    interface RegistrationResponseListener {
        public void showAllUsers();
    }

}
