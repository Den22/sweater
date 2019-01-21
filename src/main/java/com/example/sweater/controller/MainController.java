package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = filter != null && !filter.isEmpty() ?
                messageRepo.findByTag(filter) :
                messageRepo.findAll();

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("add")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {

        Message message = new Message(text, tag, user);

        if (file != null) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = format("%s.%s", uuidFile, file.getOriginalFilename());

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }

        messageRepo.save(message);

        return main(null, model);
    }

    @GetMapping("message")
    public String delete(@RequestParam Message message) {

        messageRepo.delete(message);

        return "redirect:/main";
    }
}
