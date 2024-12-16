package demo.usul;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class Const {

    public static final ZoneId SHANG_HAI = ZoneId.of("Asia/Shanghai");

    public static final OffsetDateTime SHANG_HAI_NOW = OffsetDateTime.now(SHANG_HAI);
}
