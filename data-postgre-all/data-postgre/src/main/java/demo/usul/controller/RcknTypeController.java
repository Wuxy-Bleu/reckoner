package demo.usul.controller;

import demo.usul.dto.ReckonerTypeDto;
import demo.usul.service.RcknTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rckn-type/v1")
public class RcknTypeController {

    private final RcknTypeService service;

    public RcknTypeController(RcknTypeService service) {this.service = service;}

    @PostMapping
    public ReckonerTypeDto createOne(@RequestBody ReckonerTypeDto dto) {
        return service.createOne(dto);
    }

    @GetMapping
    public List<ReckonerTypeDto> retrieveAll() {
        return service.retrieveAll();
    }

    @DeleteMapping
    public List<ReckonerTypeDto> deleteByNames(@RequestBody List<String> names) {
        return service.deleteByName(names);
    }
}
