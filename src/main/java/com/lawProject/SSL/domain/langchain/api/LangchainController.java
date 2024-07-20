//package com.lawProject.SSL.domain.langchain.api;
//
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/chat")
//public class LangchainController {
//
//    private final ChatLanguageModel chatLanguageModel;
//
//    @GetMapping("/generate")
//    public String model(@RequestParam(value = "message", defaultValue = "Hello") String message) {
//        return chatLanguageModel.generate(message);
//    }
//}