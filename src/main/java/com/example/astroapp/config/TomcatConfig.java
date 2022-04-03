package com.example.astroapp.config;

import org.apache.coyote.ajp.AjpNioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Optional AJP connector configuration for the embedded Tomcat.
 * Activate with:
 * <pre>
 *     java -Dserver.port=8009 -Dserver.tomcat.ajp.enabled=true -Dserver.tomcat.ajp.secret=5ecr3t -jar ...
 * </pre>
 */
@Configuration
public class TomcatConfig {

    private static final Logger log = LoggerFactory.getLogger(TomcatConfig.class);

    @Value("${server.tomcat.ajp.enabled:false}")
    private boolean enableAjp;

    @Value("${server.tomcat.ajp.secretRequired:false}")
    private boolean secretRequired;

    @Value("${server.tomcat.ajp.secret:}")
    private String secret;

    @Value("${server.tomcat.ajp.tomcatAuthentication:false}")
    private boolean tomcatAuthentication;

    @Value("${server.tomcat.ajp.address:127.0.0.1}")
    private String address;

    @Value("${server.tomcat.ajp.packetSize:8192}")
    private int packetSize;

    @Value("${server.tomcat.ajp.allowedRequestAttributesPattern:}")
    private String allowedRequestAttributesPattern;

    @Bean
    public TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        if (enableAjp) {
            tomcat.setProtocol("AJP/1.3");
            tomcat.getTomcatProtocolHandlerCustomizers().add(protocolHandler -> {
                log.info("TomcatProtocolHandlerCustomizer protocolHandler={}", protocolHandler);
                if (protocolHandler instanceof AjpNioProtocol) {
                    AjpNioProtocol ajp = (AjpNioProtocol) protocolHandler;
                    ajp.setSecretRequired(secretRequired);
                    ajp.setSecret(secret);
                    ajp.setTomcatAuthentication(tomcatAuthentication);
                    try {
                        ajp.setAddress(InetAddress.getByName(address));
                    } catch (UnknownHostException e) {
                        log.error("Cannot resolve IP address " + address, e);
                    }
                    ajp.setPacketSize(packetSize);
                    if(allowedRequestAttributesPattern!=null && !allowedRequestAttributesPattern.isEmpty()) {
                        log.info("setting allowedRequestAttributesPattern={}",allowedRequestAttributesPattern);
                        ajp.setAllowedRequestAttributesPattern(allowedRequestAttributesPattern);
                    }
                }
            });
        }
        return tomcat;
    }
}