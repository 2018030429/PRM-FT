package com.upsin.agenda_web_service.Objects;

import android.content.Context;
import android.provider.Settings.Secure;

public class Device {
  public static final String getSecureId(Context context) {
    String id = Secure.getString(
      context.getContentResolver(),
      Secure.ANDROID_ID
    );
    return id;
  };
}
