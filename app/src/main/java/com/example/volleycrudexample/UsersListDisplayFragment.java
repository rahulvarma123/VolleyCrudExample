package com.example.volleycrudexample;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersListDisplayFragment extends Fragment implements UserInputFragment.RegistrationResponseListener, UserAdapter.OnRowButtonClickListener, UserEditFragment.EditResponseListener {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<UserModel> userList = new ArrayList<>();

    MySingleTon singleTon;

    private String url = "https://jaladhi-server.herokuapp.com/getAllUsers";

    public UsersListDisplayFragment() {
        // Required empty public constructor
    }

    public static UsersListDisplayFragment getInstance() {
        UsersListDisplayFragment usersListDisplayFragment = new UsersListDisplayFragment();
        return usersListDisplayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        singleTon = MySingleTon.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_list_display, container, false);
        loadAllUserData();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        userAdapter = new UserAdapter(getActivity(), userList, this);
        recyclerView.setAdapter(userAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.items_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemAdd) {
            loadInputDialogFragment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadInputDialogFragment() {
        UserInputFragment userInputFragment = new UserInputFragment();
        userInputFragment.setResponseListener(this);
        userInputFragment.show(getFragmentManager(), "UserInput");
    }

    private void loadAllUserData() {
        userList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonResponseObject) {
                        try {
                            JSONArray jsonArray = jsonResponseObject.getJSONArray("Users");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //parsing logic
                                int id = jsonObject.getInt("userId");
                                String email = jsonObject.getString("emailId");
                                String password = jsonObject.getString("password");
                                String phoneNumber = jsonObject.getString("phNumber");
                                String userName = jsonObject.getString("userName");

                                // storing data in model object
                                UserModel userModel = new UserModel();
                                userModel.setUserId(id);
                                userModel.setEmailId(email);
                                userModel.setPassword(password);
                                userModel.setPhoneNum(phoneNumber);
                                userModel.setUserName(userName);

                                // Add the model object to Array List
                                userList.add(userModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Collections.sort(userList, new UserIdComparator());
                        userAdapter.setUserList(userList);
                        Log.v("User List", jsonResponseObject.toString());

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Error " + error);
                Toast.makeText(getActivity(), "Volley Loading Error", Toast.LENGTH_LONG).show();
            }
        });



        /*RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);*/
        singleTon.addRequestToQueue(jsonObjectRequest);
    }

    @Override
    public void showAllUsers() {
        loadAllUserData();
    }

    @Override
    public void onEditClicked(int position) {
        UserModel userModel = userList.get(position);
        UserEditFragment userEditFragment = UserEditFragment.getInstance(userModel);
        userEditFragment.setResponseListener(this);
        userEditFragment.show(getFragmentManager(), "EditFragment");
    }

    @Override
    public void updateUserList() {
        loadAllUserData();
        // userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteClicked(int position) { // position = 1

        final UserModel userModel = userList.get(position);
        int userId = userModel.getUserId();

        String deleteUrl = "https://jaladhi-server.herokuapp.com/deleteUser/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Toast.makeText(getActivity(), "Delete Success", Toast.LENGTH_LONG).show();
                            userList.remove(userModel);
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Delete Failure", Toast.LENGTH_LONG).show();
                    }
                });
       /* RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjectRequest);*/
        singleTon.addRequestToQueue(jsonObjectRequest);
    }


}
