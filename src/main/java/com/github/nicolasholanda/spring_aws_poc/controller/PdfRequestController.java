package com.github.nicolasholanda.spring_aws_poc.controller;

import com.github.nicolasholanda.spring_aws_poc.model.PdfRequest;
import com.github.nicolasholanda.spring_aws_poc.service.PdfRequestService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdf")
public class PdfRequestController {
    @Autowired
    private PdfRequestService pdfRequestService;

    @PostMapping()
    public ResponseEntity<String> generatePdf(@RequestBody PdfRequest request) {
        pdfRequestService.sendPdfRequest(request.getText(), request.getEmail());
        return ResponseEntity.accepted().body("Your request is being processed. You will be notified through e-mail.");
    }
}

