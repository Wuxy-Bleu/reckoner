package demo.usul;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActionService {

    public void submitTask(Object updateAccountVo) {
      // 1. produce update msg to rabbitmq (async save msg)
      // 2. consume msg update postgresql, produce reply msg
        // 3. consume msg update mongo, produce reply msg
        // 4. consume twp reply msg,
    }
}
