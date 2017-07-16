package com.casic.patrol.core.mail;

import java.net.UnknownHostException;

public interface HostGenerator {
    String generateLocalAddress() throws UnknownHostException;
}
