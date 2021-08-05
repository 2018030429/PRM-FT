package com.upsin.agenda_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upsin.agenda_firebase.objects.Contacts;
import com.upsin.agenda_firebase.objects.FirebaseReferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private Button _btnSave;
  private Button _btnList;
  private Button _btnClear;
  private TextView _txtName;
  private TextView _txtAddress;
  private TextView _txtMainPhone;
  private TextView _txtSecondaryPhone;
  private TextView _txtNotes;
  private CheckBox _cbxFavorite;
  private FirebaseDatabase _database;
  private DatabaseReference _reference;
  private Contacts _savedContacts;
  private String _id;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      this.initializeComponents();
      this.setEvents();
  }

  private void initializeComponents() {
    this._database = FirebaseDatabase.getInstance();
    this._reference = this._database.getReferenceFromUrl(
      FirebaseReferences.URL_DATABASE + FirebaseReferences.DATABASE_NAME + "/" +
      FirebaseReferences.TABLE_NAME
    );
    this._txtName = findViewById(R.id.txtName);
    this._txtMainPhone = findViewById(R.id.txtMainPhone);
    this._txtSecondaryPhone = findViewById(R.id.txtSecondaryPhone);
    this._txtAddress = findViewById(R.id.txtAddress);
    this._txtNotes = findViewById(R.id.txtNotes);
    this._cbxFavorite = findViewById(R.id.cbxFavorite);
    this._btnSave = findViewById(R.id.btnSave);
    this._btnList = findViewById(R.id.btnList);
    this._btnClear = findViewById(R.id.btnClean);
    this._savedContacts = null;
  }

  public void setEvents() {
    this._btnSave.setOnClickListener(this);
    this._btnList.setOnClickListener(this);
    this._btnClear.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (this.isNetworkAvailable()) {
      switch (view.getId()) {
        case R.id.btnSave:
          boolean complete = true;
          if (this.isInputEmpty(this._txtName)) {
            this._txtName.setError("Fill the name");
            complete = false;
          }
          if (this.isInputEmpty(this._txtMainPhone)) {
            this._txtMainPhone.setError("Fill the main phone");
            complete = false;
          }
          if (this.isInputEmpty(this._txtAddress)) {
            this._txtAddress.setError("Fill the address");
            complete = false;
          }
          if (complete) {
            Contacts contact = new Contacts();
            contact.set_name(this._txtName.getText().toString());
            contact.set_main_phone(this._txtMainPhone.getText().toString());
            contact.set_secondary_phone(this._txtSecondaryPhone.getText().toString());
            contact.set_address(this._txtAddress.getText().toString());
            contact.set_notes(this._txtNotes.getText().toString());
            contact.set_isFavorite(this._cbxFavorite.isChecked()? 1 : 0);

            if (this._savedContacts == null) {
              this.createContact(contact);
              this.createToast("Contact saved successfully");
              this.clear();
            } else {
              this.updateContact(this._id, contact);
              this.createToast("Contact updated successfully");
              this.clear();
            }
          }
          break;
        case R.id.btnClean:
          this.clear();
          break;
        case R.id.btnList:
          Intent intent = new Intent(MainActivity.this, ListActivity.class);
          this.clear();
          startActivityForResult(intent, 0);
          break;
      }
    } else {
      this.createToast("There are no internet connection");
    }
  }

  private void createContact(Contacts contact) {
    DatabaseReference contactReference = this._reference.push();
    String id = contactReference.getKey();
    contact.set_ID(id);
    contactReference.setValue(contact);
  }

  private void updateContact(String id, Contacts contact) {
    contact.set_ID(id);
    this._reference.child(String.valueOf(id)).setValue(contact);
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    return ni != null && ni.isConnected();
  }

  private void createToast(String message) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }

  private boolean isInputEmpty(TextView textView) {
    return textView.getText().toString().isEmpty();
  }

  private void clear() {
    this._savedContacts = null;
    this._txtName.setText("");
    this._txtMainPhone.setText("");
    this._txtSecondaryPhone.setText("");
    this._txtNotes.setText("");
    this._txtAddress.setText("");
    this._cbxFavorite.setChecked(false);
    this._id = "";
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (intent != null) {
      Bundle bundle = intent.getExtras();
      if (Activity.RESULT_OK == resultCode) {
        Contacts contact = (Contacts) bundle.getSerializable("contact");
        this._savedContacts = contact;
        this._id = contact.get_ID();
        this._txtName.setText(contact.get_name());
        this._txtMainPhone.setText(contact.get_main_phone());
        this._txtSecondaryPhone.setText(contact.get_secondary_phone());
        this._txtAddress.setText(contact.get_address());
        this._txtNotes.setText(contact.get_notes());
        if (contact.get_isFavorite() > 0) this._cbxFavorite.setChecked(true);
      } else {
        this.clear();
      }
    }
  }
}