package com.example.springboot.controllers;

import com.example.springboot.repository.ImageDAO;
import com.example.springboot.services.FormClassifierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Form classifier", description = "Api Endpoints for Form Classifier")
public class FormClassifierController {
    private final FormClassifierService formClassifierService;

    
    
    @Autowired
    public FormClassifierController(FormClassifierService formClassifierService) {
        this.formClassifierService = formClassifierService;
    }

    @ApiOperation(value = "Classifies forms and generates report, download link")
    @RequestMapping(value = "/formClassification", method = RequestMethod.POST)
    public HashMap<String, Object> processInputFile(@RequestParam("file") MultipartFile file,
                                                    @RequestParam(name= "classifyBy", defaultValue="type" ) String classifyBy,
                                                    @RequestParam(name= "imageId") int imageId
    ) {
        return formClassifierService.processInputFile(file, classifyBy, imageId);
    }

    @ApiOperation(value = "To extract keyword(s) from sample file to train the algorithm")
    @RequestMapping(value = "/keywordGenerator", method = RequestMethod.POST)
    public List<String> processSampleFile(@RequestParam("file") MultipartFile file,
                                          @RequestParam("category") String type,
                                          @RequestParam(name = "bias", required = false) String bias) {
        return formClassifierService.processSampleFile(file, type, bias);
    }

    @ApiOperation(value = "View keyword(s) for all file categories")
    @RequestMapping(value = "/returnKeywords", method = RequestMethod.GET)
    public Map<String,String> getKeywords() {
        return formClassifierService.getKeywords();
    }

    @ApiOperation(value = "View keywords(s) generated for the given file category")
    @RequestMapping(value = "/returnKeywords/{category}", method = RequestMethod.GET)
    public String[] getKeywords(@PathVariable String category) {
        return formClassifierService.getKeywords(category);
    }

    @ApiOperation(value = "Get processed text from input file")
    @RequestMapping(value = "/processedTextFromFile", method = RequestMethod.POST)
    public ResponseEntity getProcessedTextFromFile(@RequestParam("file") MultipartFile file) {
        String processedText = formClassifierService.getProcessedTextFromFile(file);
        return new ResponseEntity(processedText, HttpStatus.CREATED);
    }
}