package com.example.project.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "file-processing-service",
        url = "${app.validateUrl}"
)
public interface FileProcessingClient {

    @PostMapping(
            value = "/api/process-csv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    ResponseEntity<Resource> processCsvFile(@RequestPart("file") MultipartFile file);
}
