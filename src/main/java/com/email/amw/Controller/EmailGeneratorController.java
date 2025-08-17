package com.email.amw.Controller;

import com.email.amw.Model.EmailRequest;
import com.email.amw.Model.emailGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/email")
@CrossOrigin(origins = "*") //enables to accept request from all origins
public class EmailGeneratorController {


    private final emailGeneratorService service;
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){
        String response = service.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }

}
