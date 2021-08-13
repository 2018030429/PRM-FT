package com.upsin.agenda_web_service.Objects;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProcessPHP implements Response.Listener<JSONObject>, Response.ErrorListener {
  private RequestQueue request;
  private JsonObjectRequest jsonObjectRequest;
  private ArrayList<Contact> contacts;
  private final String serverip = "https://2018030429.000webhostapp.com/WebService/";

  public void setContext(Context context) {
    this.request = Volley.newRequestQueue(context);
  }

  public void insertContactWS(Contact c) {
    String url = this.serverip + "wsRegistro.php?nombre=" + c.get_name() +
      "&telefono1=" + c.get_main_phone() + "&telefono2="  + c.get_secondary_phone() +
      "&direccion=" + c.get_address()    + "&notas="      + c.get_notes() +
      "&favorite="  + c.get_isFavorite() + "&idMovil="    + c.get_idMobil();
    url = url.replace(" ", "%20");
    this.jsonObjectRequest = new JsonObjectRequest(
      Request.Method.GET, url, null, this, this
    );
    this.request.add(this.jsonObjectRequest);
  }

  public void updateContactWS(Contact c, int id) {
    String url = this.serverip + "wsActualizar.php?_ID="  + id + "&nombre=" + c.get_name() +
      "&telefono1=" + c.get_main_phone() + "&telefono2="  + c.get_secondary_phone() +
      "&direccion=" + c.get_address()    + "&notas="      + c.get_notes() +
      "&favorite="  + c.get_isFavorite() + "&idMovil="    + c.get_idMobil();
    url = url.replace(" ", "%20");
    this.jsonObjectRequest = new JsonObjectRequest(
      Request.Method.GET, url, null, this, this
    );
    this.request.add(this.jsonObjectRequest);
  }

  public void deleteContactWS(int id) {
    String url = this.serverip + "wsEliminar.php?_ID=" + id;
    this.jsonObjectRequest = new JsonObjectRequest(
      Request.Method.GET, url, null, this, this
    );
    this.request.add(this.jsonObjectRequest);
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    Log.i("ERROR: ", error.toString());
  }

  @Override
  public void onResponse(JSONObject response) {

  }
}
