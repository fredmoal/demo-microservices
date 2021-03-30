package fr.univ.orleans.innov.gateway;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DemoLiveClient {
    public static void main(String[] args) {
        String consulHost = "localhost";
        String consulPort = "8500";
        String publicKey = "AAABBABA746464";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://"+consulHost+":"+consulPort+"/v1/kv/nouvelleCle"))
                .header("Content-Type","text-plain")
                .PUT(HttpRequest.BodyPublishers.ofString(publicKey))
                .build();
        try {
            HttpResponse<String> reponse = HttpClient
                    .newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("status :" +reponse.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
