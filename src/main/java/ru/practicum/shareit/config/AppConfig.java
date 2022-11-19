package ru.practicum.shareit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@Slf4j
public class AppConfig {

    protected static String INSTANCE_ID = null;

    static {
        try {
            INSTANCE_ID = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Unable to get host name", e);
        }
    }
}
