package demo.usul.service;

import demo.usul.feign.LabFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final LabFeign labFeign;

    public String xxx(){
        return labFeign.greeting();
    }

}
