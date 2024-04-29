package demo.usul.controller;

import demo.usul.dto.ReckonerDto;
import demo.usul.service.ReckonerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/rckn/v2")
public class RcknControllerV2 {

    private final ReckonerService reckonerService;
    private final ReckonerController reckonerController;

    @Autowired
    public RcknControllerV2(ReckonerService reckonerService, ReckonerController reckonerController) {
        this.reckonerService = reckonerService;
        this.reckonerController = reckonerController;
    }

    @PostMapping("/{trigger}")
    public ReckonerDto createV2(@RequestBody ReckonerDto dto, @PathVariable(required = false) Optional<Boolean> trigger, @RequestParam(required = false) List<String> tags) {
        dto.setTags("[ " + String.join(", ", tags) + " ]");
        return reckonerController.createOne(dto, trigger);
    }
}
