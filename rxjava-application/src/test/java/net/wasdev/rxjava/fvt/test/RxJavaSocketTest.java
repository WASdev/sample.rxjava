package net.wasdev.rxjava.fvt.test;

/**
* (C) Copyright IBM Corporation 2016.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.junit.Before;
import org.junit.Test;

@ClientEndpoint
public class RxJavaSocketTest {
	private Session session;
	private static String response = null;
	private static String error = null;

	// We need a no-arg constructor because Jetty attempts to create an instance
	// of this class using reflection.
	public RxJavaSocketTest() {
	}

	@Before
	public void cleanTestVars() {
		// Reset the static variables we use to test that the responses are correct.
		RxJavaSocketTest.response = null;
		RxJavaSocketTest.error = null;
	}

	@Test
	public void testWebSocket() throws Exception {
		testWebsocket("Hawaii", "Destination: Hawaii");
	}

	/**
	 * The main test method. This accepts the websocket endpoint to connect to,
	 * and sends a request through to the server. It then waits for a response
	 * and check that it is the correct response.
	 */
	public void testWebsocket(String requestMessage, String expectedResponse) throws Exception {
		try {
			// Create a WebSocket client, and build up the websocket URL to use.
			WebSocketContainer c = ContainerProvider.getWebSocketContainer();
			String port = System.getProperty("liberty.test.port");
			String websocketURL = "ws://localhost:" + port + "/rxjava-application/";
			URI uriServerEP = URI.create(websocketURL);

			System.out.println("Connecting to: " + websocketURL);

			// Connect to the websocket
			session = c.connectToServer(RxJavaSocketTest.class, uriServerEP);

			// Send the text string to the server
			session.getBasicRemote().sendText(requestMessage);

			// Now wait for up to 5 secs to get the response back.
			int count = 0;
			while (RxJavaSocketTest.response == null && count < 10) {
				count++;
				Thread.sleep(500);
			}

			System.out.println(RxJavaSocketTest.response);

			// Run the Test asserts to ensure we have received the correct string, and that we haven't hit any errors.
			assertTrue("Message sent from the server doesn't match expected String",
					RxJavaSocketTest.response != null && RxJavaSocketTest.response.contains(expectedResponse));
			assertNull("There was an unexpected error during test: " + RxJavaSocketTest.error, RxJavaSocketTest.error);

		} finally {
			// Finally if we have created a session, close it off.
			if (session != null) {
				try {
					session.close();
				} catch (Exception ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	/**
	 * This method is triggered when the server sends a message across the
	 * websocket. We store this away in a static var so that we can check that
	 * the response was as expected.
	 * 
	 * @param message - A String returned from the server.
	 * @param session - The session that we are using for the connection.
	 * @throws IOException
	 */
	@OnMessage
	public void receiveMessage(String message, Session session) throws IOException {
		RxJavaSocketTest.response = message;
	}

	/**
	 * This method is triggered when the server sends an error across the
	 * websocket. We store this away in a static var so that we can check
	 * whether it was expected or not.
	 * 
	 * @param t - A Throwable object
	 */
	@OnError
	public void onError(Throwable t) {
		RxJavaSocketTest.error = t.getMessage();
	}
}
