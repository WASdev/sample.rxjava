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
package net.wasdev.rxjava;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * WebSocket Endpoint implementation class RxJavaSocket
 */
@ServerEndpoint("/")
public class RxJavaSocket {

	@Resource
	private ManagedScheduledExecutorService subscriberExecutor;

	@Resource
	private ManagedScheduledExecutorService observerExecutor;

	private Observable<DestinationInfo> mainObservable;

	private Session session;

	public RxJavaSocket() {
	}

	public class DestinationInfo {
		private String destination;
		private int averageTemp;
		private String last5Reviews;
		private int ranking;

		public DestinationInfo(String destination) {
			this.destination = destination;
			this.averageTemp = getAverageTemp(destination);
			this.last5Reviews = getLast5Reviews(destination);
			this.ranking = getRanking(destination);
		}

		private int getRanking(String dest) {
			switch (dest) {
			case "Rio de Janeiro":
				return 1;
			case "Cancun":
				return 2;
			case "Punta Cana":
				return 3;
			case "Varadero":
				return 4;
			case "Hawaii":
				return 5;
			default:
				return 0;
			}
		}

		private String getLast5Reviews(String dest) {
			switch (dest) {
			case "Rio de Janeiro":
				return "\n- Great trip and food. \n" +
						"- Very hot weather, drink lots of water. \n" +
						"- I recommend staying close to the beach. \n" +
						"- The hotel ABC could use an upgrade. \n" +
						"- Boat trip was great.";

			case "Cancun":
				return "\n- Food was outstanding! \n" +
						"- I recommend hotel ABC for a short stay. \n" +
						"- Beach had a lot of seaweeds. \n" +
						"- Great family trip. \n" +
						"- Rained a few days, but activities were good.";

			case "Punta Cana":
				return "\n- Pool activities were ok. \n" +
						"- Long ride from airport to the resort. \n" +
						"- Nice clean beach. \n" +
						"- Great couples trip. \n" +
						"- Snorkeling was fun. ";

			case "Varadero":
				return "\n- Food was ok. \n" +
						"- Blue ocean and white sand! \n" +
						"- Lost my wallet in the trip! \n" +
						"- I recommend the local market. \n" +
						"- Bus ride to airport was fun.";

			case "Hawaii":
				return "\n- Food was very diversed! \n" +
						"- I recommend hotel XYZ for families. \n" +
						"- Airport is close to all resorts. \n" +
						"- Great family trip. \n" +
						"- Weather was very hot.";

			default:
				return "";
			}
		}

		private int getAverageTemp(String dest) {
			switch (dest) {
			case "Rio de Janeiro":
				return 35;
			case "Cancun":
				return 30;
			case "Punta Cana":
				return 28;
			case "Varadero":
				return 29;
			case "Hawaii":
				return 27;
			default:
				return 0;
			}
		}

		public String getDestination() {
			return destination;
		}

		public int getAverageTemp() {
			return averageTemp;
		}

		public String getLast5Reviews() {
			return last5Reviews;
		}

		public int getRanking() {
			return ranking;
		}
	}

	@OnMessage
	public void processDestinations(String message, Session session) {
		this.session = session;
		String[] destinations = message.split(",");

		Scheduler observeOnScheduler = Schedulers.from(observerExecutor);
		Scheduler subscribeOnScheduler = Schedulers.from(subscriberExecutor);

		// Map incoming destination strings into Destination objects, which are observed on the specific scheduler
		Observable<DestinationInfo> newDestinations = Observable.from(destinations)
				.map(destination -> new DestinationInfo(destination)).observeOn(observeOnScheduler);

		// If we already have some destinations from before, concatenate the two observables
		if (mainObservable == null) {
			mainObservable = newDestinations;
		} else {
			mainObservable = mainObservable.concatWith(newDestinations);
		}

		System.out.println("First:" + mainObservable);

		// Sort the list by alphabetical order and display back the results
		mainObservable = mainObservable
				.toSortedList((destination1, destination2) -> destination1.getDestination().compareToIgnoreCase(destination2.getDestination()))
				.flatMap(myDestinations -> Observable.from(myDestinations)).subscribeOn(subscribeOnScheduler);

		mainObservable.subscribe(destinationInfo -> sendText("Destination: " + destinationInfo.getDestination() + " | Ranking: " + destinationInfo.getRanking()));
		mainObservable.subscribe(destinationInfo -> sendText("Destination: " + destinationInfo.getDestination() + " | Average Temp: " + destinationInfo.getAverageTemp()));
		mainObservable.subscribe(destinationInfo -> sendText("Destination: " + destinationInfo.getDestination() + " | Last 5 Reviews: " + destinationInfo.getLast5Reviews()));

		System.out.println("Second:" + mainObservable);
		System.out.println("Message \"" + message + "\" received from:" + session.getId());
	}

	private synchronized void sendText(String string) {
		System.out.println("Sending: " + string + " | Timestamp: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		try {
			session.getBasicRemote().sendText(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
