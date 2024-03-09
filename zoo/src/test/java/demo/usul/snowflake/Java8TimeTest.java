package demo.usul.snowflake;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Slf4j
class Java8TimeTest {

    @Test
    void isInstantOf0BeUnixTime0() {
        // Unix time January 1, 1970
        Instant unixOrigin = Instant.ofEpochMilli(0);
        LocalDateTime epoch0 = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.ofHours(0));
        LocalDateTime the1970 = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
        ZonedDateTime shangHai1970 = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Shanghai"));

        Duration span1 = Duration.between(unixOrigin, epoch0.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        Duration span2 = Duration.between(unixOrigin, the1970.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        Duration span3 = Duration.between(unixOrigin, shangHai1970.toInstant());
        Assertions.assertThat(span1).isEqualTo(Duration.ofHours(-8L)).isEqualTo(span2).isEqualTo(span3);
    }
}
