package com.casic.patrol.permission;

public abstract interface UrlSourceFetcher
{
  public abstract UserObj getSource(String username, String password);
}