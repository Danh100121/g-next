package com.demo.resource;

import com.demo.dto.request.UserCriteria;
import com.demo.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelResource {
    private final ExcelService fileService;

    @PostMapping("/download")
    public ResponseEntity<Resource> getFile(@RequestBody(required = false) UserCriteria userCriteria) {
        String filename = "Users.xlsx";
        InputStreamResource file = new InputStreamResource(fileService.load(userCriteria));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }
}
