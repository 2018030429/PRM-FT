package com.upsin.agenda_web_service;

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

import com.upsin.agenda_web_service.Objects.Contact;
import com.upsin.agenda_web_service.Objects.Device;
import com.upsin.agenda_web_service.Objects.ProcessPHP;

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
  private Contact savedContact;
  private ProcessPHP php;
  private int id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.initComponents();
    this.setEvents();
  }

  private void initComponents() {
    this.php = new ProcessPHP();
    php.setContext(this);
    this._txtName = findViewById(R.id.edtName);
    this._txtMainPhone = findViewById(R.id.edtMainPhone);
    this._txtSecondaryPhone = findViewById(R.id.edtSecondaryPhone);
    this._txtAddress = findViewById(R.id.edtAddress);
    this._txtNotes = findViewById(R.id.edtNotes);
    this._cbxFavorite = findViewById(R.id.cbxFavorite);
    this._btnSave = findViewById(R.id.btnSave);
    this._btnClear = findViewById(R.id.btnClear);
    this._btnList = findViewById(R.id.btnList);
    this.savedContact = null;
  }

  private void setEvents() {
    this._btnSave.setOnClickListener(this);
    this._btnClear.setOnClickListener(this);
    this._btnList.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (this.isNetworkAvailable()) {
      switch (view.getId()) {
        case R.id.btnSave:
          boolean isCompleted = true;
          if (this._txtName.getText().toString().equals("")) {
            this._txtName.setError("Fill the name");
          }
          if (this._txtMainPhone.getText().toString().equals("")) {
            this._txtMainPhone.setError("Fill the main phone");
          }
          if (this._txtAddress.getText().toString().equals("")) {
            this._txtAddress.setError("Fill the address");
          }
          if (isCompleted) {
            Contact contact = new Contact();
            contact.set_name(this._txtName.getText().toString());
            contact.set_main_phone(this._txtMainPhone.getText().toString());
            contact.set_secondary_phone(this._txtSecondaryPhone.getText().toString());
            contact.set_address(this._txtAddress.getText().toString());
            contact.set_notes(this._txtNotes.getText().toString());
            contact.set_isFavorite(this._cbxFavorite.isChecked()? 1 : 0);
            contact.set_idMobil(Device.getSecureId(this));

            if (this.savedContact == null) {
              this.php.insertContactWS(contact);
              this.createToast("Contact saved successfully!!");
            } else {
              this.php.updateContactWS(contact, this.id);
              this.createToast("Contact updated successfully!!");
            }
            this.clear();
          }
          break;
        case R.id.btnClear:
          this.clear();
          break;
        case R.id.btnList:
          Intent intent = new Intent(MainActivity.this, ListActivity.class);
          this.clear();
          startActivityForResult(intent, 0);
          break;
      }
    } else {
      this.createToast("Your are offline!!");
    }
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo ni = cm.getActiveNetworkInfo();
    return ni != null && ni.isConnected();
  }

  private void createToast(String message) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

  private void clear() {
    this.savedContact = null;
    this._txtName.setText("");
    this._txtMainPhone.setText("");
    this._txtSecondaryPhone.setText("");
    this._txtNotes.setText("");
    this._txtAddress.setText("");
    this._cbxFavorite.setChecked(false);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (intent != null) {
      Bundle bundle = intent.getExtras();
      if (Activity.RESULT_OK == resultCode) {
        Contact contact = (Contact) bundle.getSerializable("contact");
        this.savedContact = contact;
        this.id = contact.get_ID();
        this._txtName.setText(contact.get_name());
        this._txtMainPhone.setText(contact.get_main_phone());
        this._txtSecondaryPhone.setText(contact.get_secondary_phone());
        this._txtAddress.setText(contact.get_address());
        this._txtNotes.setText(contact.get_notes());

        if (contact.get_isFavorite() > 0) {
          this._cbxFavorite.setChecked(true);
        }
      } else {
        this.clear();
      }
    }
  }
}