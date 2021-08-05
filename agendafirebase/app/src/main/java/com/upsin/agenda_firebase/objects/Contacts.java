package com.upsin.agenda_firebase.objects;

import java.io.Serializable;

public class Contacts implements Serializable {
  private String _ID;
  private String _name;
  private String _main_phone;
  private String _secondary_phone;
  private String _address;
  private String _notes;
  private int _isFavorite;

  public Contacts () { }

  public Contacts (
    String name,
    String main_phone,
    String secondary_phone,
    String address,
    String notes,
    int isFavorite
  ) {
    this._name = name;
    this._address = address;
    this._main_phone = main_phone;
    this._secondary_phone = secondary_phone;
    this._notes = notes;
    this._isFavorite = isFavorite;
  }

  public String get_ID() {
    return _ID;
  }

  public void set_ID(String _ID) {
    this._ID = _ID;
  }

  public String get_name() {
    return _name;
  }

  public void set_name(String _name) {
    this._name = _name;
  }

  public String get_main_phone() {
    return _main_phone;
  }

  public void set_main_phone(String _main_phone) {
    this._main_phone = _main_phone;
  }

  public String get_secondary_phone() {
    return _secondary_phone;
  }

  public void set_secondary_phone(String _secondary_phone) {
    this._secondary_phone = _secondary_phone;
  }

  public String get_address() {
    return _address;
  }

  public void set_address(String _address) {
    this._address = _address;
  }

  public String get_notes() {
    return _notes;
  }

  public void set_notes(String _notes) {
    this._notes = _notes;
  }

  public int get_isFavorite() {
    return _isFavorite;
  }

  public void set_isFavorite(int _isFavorite) {
    this._isFavorite = _isFavorite;
  }
}
