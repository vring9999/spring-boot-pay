package com.instead.pay.superpay.service;

import java.io.IOException;

public abstract interface AsyncCallBack
{
  public abstract void onResponse(String paramString)
    throws IOException;

  public abstract void onFailure(String paramString);
}
