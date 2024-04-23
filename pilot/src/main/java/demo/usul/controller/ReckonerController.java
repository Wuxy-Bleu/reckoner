package demo.usul.controller;

import demo.usul.dto.ReckonerDto;
import demo.usul.service.ReckonerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reckoner")
public class ReckonerController {

    private final ReckonerService reckonerService;

    @GetMapping("/count/{fromAcct}")
    public ResponseEntity<Long> countByFromAcct(@PathVariable @NotBlank UUID fromAcct) {
        return ResponseEntity.ok(reckonerService.countDistinctByFromAcctAllIgnoreCase(fromAcct));
    }

    @GetMapping("/{fromAcctName}")
    public ResponseEntity<List<ReckonerDto>> retrieveByFromAcctName(@PathVariable @NotBlank String fromAcctName) {
        return ResponseEntity.ok(reckonerService.retrieveByFromAcctName(fromAcctName));
    }

    @GetMapping("/to/{name}")
    public ResponseEntity<List<ReckonerDto>> retrieveByToAcctName(@PathVariable @NotBlank String name) {
        return ResponseEntity.ok(reckonerService.retrieveByToAcctName(name));
    }

    @GetMapping("")
    public ResponseEntity<List<ReckonerDto>> retrieveAll() {
        return ResponseEntity.ok(reckonerService.retrieveAll());
    }

    @PostMapping("")
    public ReckonerDto createOne(@RequestBody @Valid ReckonerDto reckoner) {
        return reckonerService.createOne(reckoner);
    }

    @PostMapping("/no-trigger")
    public ReckonerDto createOneNoTrigger(@RequestBody ReckonerDto reckoner) {
        return reckonerService.createOneNoTrigger(reckoner);
    }
}
