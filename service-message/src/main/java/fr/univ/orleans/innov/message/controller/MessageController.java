package fr.univ.orleans.innov.message.controller;

import fr.univ.orleans.innov.message.modele.Message;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
public class MessageController {
    private static List<Message> messages = new ArrayList<>();

    // STREAM de notifications
    private ReplayProcessor<Message> notifications = ReplayProcessor.create(0, false);;

    @GetMapping(value = "/messages/subscribe", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Message> notification() {
        return Flux.from(notifications);
    }

    @PostMapping(value = "/messages")
    public ResponseEntity<Message> create(Principal principal, @RequestBody Message messageBody, UriComponentsBuilder base) {
        int id = messages.size();
        messageBody.setId((long)id);
        messageBody.setUtilisateur(principal.getName());
        messages.add(messageBody);
        URI location = base.path("/api/messages/{id}").buildAndExpand(id).toUri();
        // notification d'un nouveau message dans le Stream
        notifications.onNext(messageBody);

        return ResponseEntity.created(location).body(messageBody);
    }

    @GetMapping(value = "/messages/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable int id) {
        if (!messages.contains(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messages.get(id));
    }

}
