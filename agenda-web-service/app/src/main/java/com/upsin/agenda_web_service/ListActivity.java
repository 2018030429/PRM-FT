package com.upsin.agenda_web_service;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.upsin.agenda_web_service.Objects.Contact;
import com.upsin.agenda_web_service.Objects.Device;
import com.upsin.agenda_web_service.Objects.ProcessPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ListActivity extends android.app.ListActivity implements Response.Listener<JSONObject>,
  Response.ErrorListener {
  private Button _btnNew;
  private final Context context = this;
  private ProcessPHP php = new ProcessPHP();
  private RequestQueue request;
  private JsonObjectRequest jsonObjectRequest;
  private ArrayList<Contact> listContact;
  private String serverip = "https://2018030429.000webhostapp.com/WebService/";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    this.initComponents();

    this._btnNew.setOnClickListener(v -> {
      setResult(Activity.RESULT_CANCELED);
      finish();
    });
  }

  private void initComponents() {
    this.listContact = new ArrayList<>();
    this.request = Volley.newRequestQueue(this.context);
    this.consultAllWebServices();
    this._btnNew = findViewById(R.id.btnNew);
  }

  private void consultAllWebServices() {
    String url = this.serverip + "wsConsultarTodos.php?idMovil=" + Device.getSecureId(this);
    this.jsonObjectRequest = new JsonObjectRequest(
      Request.Method.GET, url, null, this, this
    );
    this.request.add(this.jsonObjectRequest);
  }

  @Override
  public void onErrorResponse(VolleyError error) {

  }

  @Override
  public void onResponse(JSONObject response) {
    Contact contact = null;
    JSONArray json  = response.optJSONArray("contactos");
    try {
      for (int i=0; i < json.length(); i++) {
        contact = new Contact();
        JSONObject jsonObject = null;
        jsonObject = json.getJSONObject(i);
        contact.set_ID(jsonObject.optInt("_ID"));
        contact.set_name(jsonObject.optString("nombre"));
        contact.set_main_phone(jsonObject.optString("telefono1"));
        contact.set_secondary_phone(jsonObject.optString("telefono2"));
        contact.set_address(jsonObject.optString("direccion"));
        contact.set_notes(jsonObject.optString("notas"));
        contact.set_isFavorite(jsonObject.optInt("favorite"));
        contact.set_idMobil(jsonObject.optString("idMovil"));
        listContact.add(contact);
      }
      MyArrayAdapter adapter = new MyArrayAdapter(context, R.layout.layout_contact, listContact);
      setListAdapter(adapter);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  class MyArrayAdapter extends ArrayAdapter<Contact> {
    Context context;
    int textViewResourceId;
    ArrayList<Contact> objects;

    public MyArrayAdapter(Context context, int textViewResourceId, ArrayList<Contact> objects) {
      super(context, textViewResourceId, objects);
      this.context = context;
      this.textViewResourceId = textViewResourceId;
      this.objects = objects;
    }

    public View getView(final int position, View convertView, ViewGroup viewGroup) {
      LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View view = layoutInflater.inflate(this.textViewResourceId, null);
      TextView lblName = view.findViewById(R.id.lblContactName);
      TextView lblPhone = view.findViewById(R.id.lblMainPhone);
      Button btnUpdate = view.findViewById(R.id.btnUpdate);
      Button btnDelete = view.findViewById(R.id.btnDelete);

      if (objects.get(position).get_isFavorite() > 0) {
        lblName.setTextColor(Color.BLUE);
        lblPhone.setTextColor(Color.BLUE);
      } else {
        lblName.setTextColor(Color.BLACK);
        lblPhone.setTextColor(Color.BLACK);
      }

      lblName.setText(objects.get(position).get_name());
      lblPhone.setText(objects.get(position).get_main_phone());

      btnDelete.setOnClickListener(v -> {
        php.setContext(context);
        php.deleteContactWS(objects.get(position).get_ID());
        objects.remove(position);
        notifyDataSetChanged();
        Toast.makeText(
          getApplicationContext(),
          "Concact deleted successfully!!",
          Toast.LENGTH_SHORT
        ).show();
      });

      btnUpdate.setOnClickListener(v -> {
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact", objects.get(position));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
      });

      return view;
    }
  }

}