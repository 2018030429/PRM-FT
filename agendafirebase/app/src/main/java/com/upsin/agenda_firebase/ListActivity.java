package com.upsin.agenda_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upsin.agenda_firebase.objects.Contacts;
import com.upsin.agenda_firebase.objects.FirebaseReferences;

import java.util.ArrayList;

public class ListActivity extends android.app.ListActivity {

  private FirebaseDatabase _database;
  private DatabaseReference _reference;
  private Button _btnNew;
  final Context context = this;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    this.initializeComponents();

    this._btnNew.setOnClickListener(v -> {
      setResult(Activity.RESULT_CANCELED);
      finish();
    });
  }

  private void initializeComponents() {
    this._database = FirebaseDatabase.getInstance();
    this._reference = this._database.getReferenceFromUrl(
      FirebaseReferences.URL_DATABASE + FirebaseReferences.DATABASE_NAME + "/" +
      FirebaseReferences.TABLE_NAME
    );
    this._btnNew = findViewById(R.id.btnNew);
    this.getContacts();
  }

  public void getContacts() {
    final ArrayList<Contacts> contactList = new ArrayList<>();
    ChildEventListener listener = new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Contacts contact = snapshot.getValue(Contacts.class);
        contactList.add(contact);
        final MyArrayAdapter adapter = new MyArrayAdapter(
          context, R.layout.layout_contact, contactList
        );
        setListAdapter(adapter);
      }

      @Override
      public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

      @Override
      public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

      @Override
      public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

      @Override
      public void onCancelled(@NonNull DatabaseError error) {}
    };
    this._reference.addChildEventListener(listener);
  }

  class MyArrayAdapter extends ArrayAdapter<Contacts> {
    Context context;
    int textViewResourceId;
    ArrayList<Contacts> objects;

    public MyArrayAdapter(Context context, int textViewResourceId, ArrayList<Contacts> objects) {
      super(context, textViewResourceId, objects);
      this.context = context;
      this.textViewResourceId = textViewResourceId;
      this.objects = objects;
    }

    public View getView(final int position, View conView, ViewGroup viewGroup) {
      LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View view = layoutInflater.inflate(this.textViewResourceId, null);
      TextView lblName = view.findViewById(R.id.lblNameContact);
      TextView lblPhone = view.findViewById(R.id.lblPhoneContact);
      Button btnUpdate = view.findViewById(R.id.btnUpdate);
      Button btnDelete = view.findViewById(R.id.btnDelete);

      if (this.objects.get(position).get_isFavorite() > 0) {
        lblName.setTextColor(Color.BLUE);
        lblPhone.setTextColor(Color.BLUE);
      } else {
        lblName.setTextColor(Color.BLACK);
        lblPhone.setTextColor(Color.BLACK);
      }

      lblName.setText(objects.get(position).get_name());
      lblPhone.setText(objects.get(position).get_main_phone());

      btnDelete.setOnClickListener(v -> {
        deleteContact(objects.get(position).get_ID());
        objects.remove(position);
        notifyDataSetChanged();
        Toast.makeText(
          getApplicationContext(),
          "Contact deleted successfully",
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

  public void deleteContact(String childIndex) {
    this._reference.child(String.valueOf(childIndex)).removeValue();
  }

}