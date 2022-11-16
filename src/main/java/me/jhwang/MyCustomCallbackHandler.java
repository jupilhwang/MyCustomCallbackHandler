package me.jhwang;

import org.apache.kafka.common.security.auth.AuthenticateCallbackHandler;
import org.apache.kafka.common.security.plain.PlainAuthenticateCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class MyCustomCallbackHandler implements AuthenticateCallbackHandler {
  private final Logger log = LoggerFactory.getLogger(MyCustomCallbackHandler.class);
  private List<AppConfigurationEntry> jaasConfigEntries;
  private Map<String, String> moduleOptions = null;
  private boolean configured;

  private final HttpClient httpClient = HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .followRedirects(HttpClient.Redirect.ALWAYS)
          .build();

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


          // TODO: validate the username and password (ex. JDBC call or RESTFul API call)
//          try (CloseableHttpClient httpClient = HttpClients.createDefault())  {
//            HttpPost httpPost = new HttpPost("http://www.google.com");
//            try ( CloseableHttpResponse response = httpClient.execute(httpPost) ) {
//              var responseEntity = response.getEntity();
//
//              EntityUtils.consume(responseEntity);
//            }
//          }

//            var responseContent = Request.post("http://www.google.com")
//                .bodyForm(
//                    Form.form().add("username", "abc").add("password", "password").build()
//                ).execute().returnContent();

//           HttpRequest httpRequest = HttpRequest.newBuilder()
//               .uri(URI.create("http://www.google.com"))
//               .GET()
//               .build();

//           HttpResponse<?> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//           log.info((String) httpResponse.body());

          // TOOD

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
