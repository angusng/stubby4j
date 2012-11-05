/*
HTTP stub server written in Java with embedded Jetty

Copyright (C) 2012 Alexander Zagniotov, Isa Goksu and Eric Mrak

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package by.stub.client.http;

import by.stub.cli.CommandLineIntepreter;
import by.stub.server.JettyFactory;
import by.stub.server.JettyManager;
import by.stub.server.JettyManagerFactory;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.http.HttpSchemes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public final class StubbyClient {

   private JettyManager jettyManager;
   private String yamlConfigurationFilename;

   private StubbyClient() {

   }

   public StubbyClient(final String newYamlConfigurationFilename) {
      this.yamlConfigurationFilename = newYamlConfigurationFilename;
   }

   public void startJetty() throws Exception {
      startJetty(JettyFactory.DEFAULT_STUBS_PORT, JettyFactory.DEFAULT_ADMIN_PORT);
   }

   public void startJetty(final int clientPort, final int adminPort) throws Exception {

      final Map<String, String> params = new HashMap<String, String>();
      params.put(CommandLineIntepreter.OPTION_CLIENTPORT, String.format("%s", clientPort));
      params.put(CommandLineIntepreter.OPTION_ADMINPORT, String.format("%s", adminPort));

      jettyManager = new JettyManagerFactory().construct(yamlConfigurationFilename, params);
      jettyManager.startJetty();
   }

   public void stopJetty() throws Exception {
      if (jettyManager != null) {
         jettyManager.stopJetty();
      }
   }

   /**
    * Makes GET HTTP request to stubby
    *
    * @param host      host that stubby4j is running on
    * @param uri       URI for the HTTP request
    * @param stubsPort port that stubby4j Stubs is running on
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doGet(final String host, final String uri, final int stubsPort) throws IOException {
      return doGet(host, uri, stubsPort, null);
   }

   /**
    * Makes GET HTTP request to stubby over SSL on stubby4j default SSL port: 7443
    *
    * @param host host that stubby4j is running on
    * @param uri  URI for the HTTP request
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doGetOverSsl(final String host, final String uri) throws IOException {
      return doGetOverSsl(host, uri, null);
   }

   /**
    * Makes GET HTTP request to stubby over SSL on stubby4j default SSL port: 7443
    * Also sets basic authorisation HTTP header using provided encoded credentials.
    * The credentials should be base-64 encoded using the following format - username:password
    *
    * @param host               host that stubby4j is running on
    * @param uri                URI for the HTTP request
    * @param encodedCredentials Base 64 encoded username and password for the basic authorisation HTTP header
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doGetOverSsl(final String host, final String uri, final String encodedCredentials) throws IOException {
      final ClientHttpRequest clientHttpRequest = new ClientHttpRequest(HttpSchemes.HTTPS, HttpMethods.GET, uri, host, JettyFactory.DEFAULT_SSL_PORT, encodedCredentials);

      return makeRequest(clientHttpRequest);
   }

   /**
    * Makes GET HTTP request to stubby
    * Also sets basic authorisation HTTP header using provided encoded credentials.
    * The credentials should be base-64 encoded using the following format - username:password
    *
    * @param host               host that stubby4j is running on
    * @param uri                URI for the HTTP request
    * @param stubsPort          port that stubby4j Stubs is running on
    * @param encodedCredentials Base 64 encoded username and password for the basic authorisation HTTP header
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doGet(final String host, final String uri, final int stubsPort, final String encodedCredentials) throws IOException {
      final ClientHttpRequest clientHttpRequest = new ClientHttpRequest(HttpSchemes.HTTP, HttpMethods.GET, uri, host, stubsPort, encodedCredentials);

      return makeRequest(clientHttpRequest);
   }


   /**
    * Makes GET HTTP request to stubby running on default host and port - localhost:8882
    *
    * @param uri URI for the HTTP request
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doGetUsingDefaults(final String uri) throws IOException {
      return doGetUsingDefaults(uri, null);
   }

   /**
    * Makes GET HTTP request to stubby running on default host and port - localhost:8882.
    * Also sets basic authorisation HTTP header using provided encoded credentials.
    * The credentials should be base-64 encoded using the following format - username:password
    *
    * @param uri                URI for the HTTP request
    * @param encodedCredentials Base 64 encoded username and password for the basic authorisation HTTP header
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doGetUsingDefaults(final String uri, final String encodedCredentials) throws IOException {
      return doGet(JettyFactory.DEFAULT_HOST, uri, JettyFactory.DEFAULT_STUBS_PORT, encodedCredentials);
   }

   /**
    * Makes POST HTTP request to stubby
    *
    * @param host      host that stubby4j is running on
    * @param uri       URI for the HTTP request
    * @param stubsPort port that stubby4j Stubs is running on
    * @param post      data to POST to the server
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doPost(final String host, final String uri, final int stubsPort, final String post) throws IOException {
      return doPost(host, uri, stubsPort, null, post);
   }

   /**
    * Makes POST HTTP request to stubby
    * Also sets basic authorisation HTTP header using provided encoded credentials.
    * The credentials should be base-64 encoded using the following format - username:password
    *
    * @param host               host that stubby4j is running on
    * @param uri                URI for the HTTP request
    * @param stubsPort          port that stubby4j Stubs is running on
    * @param encodedCredentials Base 64 encoded username and password for the basic authorisation HTTP header
    * @param post               data to POST to the server
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doPost(final String host, final String uri, final int stubsPort, final String encodedCredentials, final String post) throws IOException {
      final ClientHttpRequest clientHttpRequest = new ClientHttpRequest(HttpSchemes.HTTP, HttpMethods.POST, uri, host, stubsPort, encodedCredentials, post);

      return makeRequest(clientHttpRequest);
   }

   /**
    * Makes POST HTTP request to stubby running on default host and port - localhost:8882
    *
    * @param uri  URI for the HTTP request
    * @param post data to POST to the server
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doPostUsingDefaults(final String uri, final String post) throws IOException {
      return doPostUsingDefaults(uri, post, null);
   }

   /**
    * Makes POST HTTP request to stubby running on default host and port - localhost:8882.
    * Also sets basic authorisation HTTP header using provided encoded credentials.
    * The credentials should be base-64 encoded using the following format - username:password
    *
    * @param uri                URI for the HTTP request
    * @param post               data to POST to the server
    * @param encodedCredentials Base 64 encoded username and password for the basic authorisation HTTP header
    * @return ClientHttpResponse with HTTP status code and message from the server
    * @throws IOException
    */
   public ClientHttpResponse doPostUsingDefaults(final String uri, final String post, final String encodedCredentials) throws IOException {
      return doPost(JettyFactory.DEFAULT_HOST, uri, JettyFactory.DEFAULT_STUBS_PORT, encodedCredentials, post);
   }

   private ClientHttpResponse makeRequest(final ClientHttpRequest clientHttpRequest) throws IOException {
      final ClientHttpTransport clientHttpTransport = new ClientHttpTransport(clientHttpRequest);
      final HttpURLConnection connection = clientHttpTransport.constructHttpConnection();

      try {
         connection.connect();
         final ClientHttpResponseFactory responseFactory = new ClientHttpResponseFactory(connection);

         return responseFactory.construct();
      } finally {
         connection.disconnect();
      }
   }
}