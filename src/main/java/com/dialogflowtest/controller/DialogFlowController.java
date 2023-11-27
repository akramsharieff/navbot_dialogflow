package com.dialogflowtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dialogflowtest.service.DialogFlowService;
import com.google.cloud.dialogflow.v2.QueryResult;

@RestController
@RequestMapping("/api/dialogflow")
public class DialogFlowController {
	private final DialogFlowService dialogflowService;

    @Autowired
    public DialogFlowController(DialogFlowService dialogflowService) {
        this.dialogflowService = dialogflowService;
    }
	    

    @PostMapping("/detectIntent")
    public String detectIntent(@RequestParam String sessionId, @RequestBody String text) {
        return dialogflowService.detectIntent(sessionId, text);
    }
}
