package com.email.amw.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;


// this will help us make an api call
@RequiredArgsConstructor //this was needed for injecting value into webClient
@Service
public class emailGeneratorService {


    private final WebClient webClient;


    @Value("${api.url}")
    private String apiUrl;
    @Value("${api.key}")
    private String apiKey;

     public String generateEmailReply(EmailRequest emailRequest){


         //Build the prompt
         // Craft a request
         // Send it to gemini API and get response
         // return response

         String prompt = buildPrompt(emailRequest);
         // Request has to be a key-value pair so we are using Map here.
         // The structure used below is to copy the structure of original requestBody which is to be sent.
         // Can refer to API body from Postman
         Map<String, Object> requestBody = Map.of(
                 "contents", List.of(
                         Map.of("parts", List.of(
                                 Map.of("text", prompt)
                         ))
                 )
         );



         String fullUrl = UriComponentsBuilder
                 .fromHttpUrl(apiUrl)
                 .queryParam("key", apiKey)
                 .toUriString();


         String response = webClient.post().
              uri(fullUrl)
                 .header("Content-Type", "application/json")
                 .bodyValue(requestBody)
              .retrieve()
              .bodyToMono(String.class)
              .block();
     return extractResponseContent(response);  // since the response which we are receiving is not directly
                                               // the needed answer. You can look into Postman to know what i mean
     }

    private String extractResponseContent(String response) {
         try {
            ObjectMapper mapper = new ObjectMapper();
             JsonNode rootNode = mapper.readTree(response);
             return rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
         } catch (Exception e) {
             return "Error processing request:"+e.getMessage();
         }
    }

    private String buildPrompt(EmailRequest emailRequest){


         StringBuilder prompt = new StringBuilder();
         prompt.append("Generate a email reply for the following email content & considering the context of the mail and Please don't generate a subject line ");
         if(emailRequest.getTone() != null &&  !emailRequest.getTone().isEmpty()){
             prompt.append("  use a"+emailRequest.getTone()+"tone ");
         }
         prompt.append("\nOriginal Email Content:  ").append(emailRequest.getEmailContent());

        return prompt.toString();
     }
}
