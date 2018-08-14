package pl.wturnieju.helloworld;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {
    private HelloWorldRepository helloWorldRepository;

    public HelloWorldController(HelloWorldRepository helloWorldRepository) {
        this.helloWorldRepository = helloWorldRepository;
    }

    @PostMapping(path = "message")
    public HelloWorldMessage addMessage(@RequestBody Map<String, String> payload) {
        if (payload.containsKey("message")) {
            return helloWorldRepository.insert(HelloWorldMessage.builder().message(payload.get("message")).build());
        }
        return null;
    }

    @GetMapping(path = "message/{id}")
    public HelloWorldMessage getMessage(@PathVariable("id") String id) {
        return helloWorldRepository.findById(id).orElse(null);
    }

    @GetMapping(path = "/message")
    public List<HelloWorldMessage> getMessages() {
        return helloWorldRepository.findAll();
    }

    @DeleteMapping(path = "message/{id}")
    public HelloWorldMessage deleteMessage(@PathVariable("id") String id) {
        HelloWorldMessage helloWorldMessage = helloWorldRepository.findById(id).orElse(null);
        if (helloWorldMessage != null) {
            helloWorldRepository.deleteById(id);
            return helloWorldMessage;
        }
        return null;
    }

    @DeleteMapping(path = "message")
    public List<HelloWorldMessage> deleteMessages(@RequestHeader(value = "data") String data) {
        List<String> ids = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ids = Stream.of(mapper.readValue(data, String[].class)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<HelloWorldMessage> messages = new ArrayList<>();
        ids.forEach(id -> helloWorldRepository.findById(id).ifPresent(helloWorldMessage -> {
            messages.add(helloWorldMessage);
            helloWorldRepository.delete(helloWorldMessage);
        }));
        return messages;
    }
}
