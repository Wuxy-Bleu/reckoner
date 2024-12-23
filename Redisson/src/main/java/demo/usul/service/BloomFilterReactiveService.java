package demo.usul.service;

import demo.usul.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BloomFilterReactiveService {

    private final RedissonReactiveClient redissonReactiveClient;

    @Autowired
    public BloomFilterReactiveService(RedissonReactiveClient redissonReactiveClient) {
        this.redissonReactiveClient = redissonReactiveClient;
    }

    public void bfAddIds(List<AccountDto> accountDtos) {
    }


}
