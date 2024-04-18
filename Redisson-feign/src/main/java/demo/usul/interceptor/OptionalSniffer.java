package demo.usul.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OptionalSniffer implements RequestInterceptor {

    // 遇到feign还是别用optional 太麻烦了
    @Override
    public void apply(RequestTemplate template) {
      log.info("xxxxx");
    }
}
