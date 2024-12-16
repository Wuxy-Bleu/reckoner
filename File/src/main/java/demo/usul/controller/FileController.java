package demo.usul.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/upload")
public class FileController {

    @PostMapping
    public void upload(@RequestParam MultipartFile[] file) {
        log.info(Arrays.stream(file).map(MultipartFile::getName).collect(Collectors.joining(", ")));
    }
}
