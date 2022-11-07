package me.jhwang;

import org.apache.kafka.common.security.auth.AuthenticateCallbackHandler;
import org.apache.kafka.common.security.plain.PlainAuthenticateCallback;
import org.slf4j.*;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MyCustomCallbackHandler implements AuthenticateCallbackHandler {
  private final Logger log = LoggerFactory.getLogger(MyCustomCallbackHandler.class);
  private List<AppConfigurationEntry> jaasConfigEntries;
  private Map<String, String> moduleOptions = null;
  private boolean configured;

  @Override
  public void configure(Map<String, ?> configs, String saslMechanism, List<AppConfigurationEntry> jaasConfigEntries) {
    log.info("\t\t--------------- Starting MyCustomCallbackHandler ----------------");
    this.configured = true;
  }

  @Override
  public void close() { }

  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    log.info("\t\t------------------------ Handle username + password");

    if(!this.configured)
      throw new IllegalStateException("Callback handler not configured");

    String username = null, password = null;

    for (Callback callback : callbacks) {
      if (callback instanceof NameCallback) {
        NameCallback plainCallback = (NameCallback) callback;
        username = plainCallback.getDefaultName();
      } else if (callback instanceof PlainAuthenticateCallback)  {
        try {
          PlainAuthenticateCallback plainCallback = (PlainAuthenticateCallback) callback;
          password = String.valueOf(plainCallback.password());
          log.info("\t\t---------------"+ password);

          if("client".equals(username))
            plainCallback.authenticated(true);
          else
            plainCallback.authenticated(false);

        } catch (Exception e) {
          throw new IOException (e.getMessage(), e);
        }
      }
    }
  }

}