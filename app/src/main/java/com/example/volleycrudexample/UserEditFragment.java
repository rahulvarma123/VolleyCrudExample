package com.example.volleycrudexample;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserEditFragment extends DialogFragment {

    EditText etName, etEmail, etPassword, etPhoneNum;
    Button btnRegister;
    EditResponseListener listener;

    UserModel userModel;

    private String putUrl = "https://jaladhi-server.herokuapp.com/updateUser";

    MySingleTon singleTon;

    public UserEditFragment() {
        // Required empty public constructor
    }

    public static UserEditFragment getInstance(UserModel userModel) {
        UserEditFragment userEditFragment = new UserEditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", userModel);
        userEditFragment.setArguments(bundle);
        return userEditFragment;
    }

    public void setResponseListener(EditResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppCompat_Dialog_Alert);

        Bundle bundle = getArguments();
        userModel = (UserModel) bundle.getSerializable("user");
        singleTon = MySingleTon.getInstance(getContext());
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

        etName.setText(userModel.getUserName());
        etEmail.setText(userModel.getEmailId());
        etPassword.setText(userModel.getPassword());
        etPhoneNum.setText(userModel.getPhoneNum());
        btnRegister.setText("Update User");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putUserData();
            }
        });
        return view;
    }

    private void putUserData() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", userModel.getUserId());
            jsonObject.put("userName", etName.getText().toString());
            jsonObject.put("emailId", etEmail.getText().toString());
            jsonObject.put("password", etPassword.getText().toString());
            jsonObject.put("phNumber", etPhoneNum.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, putUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Edit Success", Toast.LENGTH_LONG).show();
                        dismiss();
                        listener.updateUserList();
                    }
                },
                    new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Edit Failure", Toast.LENGTH_LONG).show();
                    }
                });
        /*RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjectRequest);*/
        singleTon.addRequestToQueue(jsonObjectRequest);
    }

    interface EditResponseListener {
        public void updateUserList();
    }

}
