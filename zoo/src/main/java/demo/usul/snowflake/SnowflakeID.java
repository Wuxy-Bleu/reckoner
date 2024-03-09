package demo.usul.snowflake;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class SnowflakeID {

    public static final String SEQUENTIAL_PREFIX = "snowflake";
    public static final String SEPARATOR = "/";

    public static final long DEFAULT_EPOCH =
            ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
    public static final int DEFAULT_TIMESTAMP_BIT = 41;
    public static final int DEFAULT_MACHINE_BIT = 10;
    public static final int DEFAULT_SEQUENCE_BIT = 12;

    private long epoch = DEFAULT_EPOCH;
    private int timestampBit = DEFAULT_TIMESTAMP_BIT;
    private int machineBit = DEFAULT_MACHINE_BIT;
    private int sequenceBit = DEFAULT_SEQUENCE_BIT;
    private int timestampLeft = machineBit + sequenceBit;
    private int machineLeft = sequenceBit;

    private long maxTimeStamp;
    private long maxMachine;
    private long maxSequence;

    public SnowflakeID() {
        setMaxBit();
    }

    public SnowflakeID(long epoch, int timestampBit, int machineBit, int sequenceBit) {
        this.epoch = epoch;
        checkArgument(
                timestampBit > 0 && timestampBit < 64
                        && machineBit > 0 && machineBit < 64
                        && sequenceBit > 0 && sequenceBit < 64
                        && timestampBit + machineBit + sequenceBit <= 64);
        this.timestampBit = timestampBit;
        this.machineBit = machineBit;
        this.sequenceBit = sequenceBit;
        setMaxBit();
    }

    public static Long generateDefault(long machineId, int sequenceId) {
        SnowflakeID snowflakeID = new SnowflakeID();
        return snowflakeID.generate(machineId, sequenceId);
    }

    public static String generateFriendlyDefault(long machineId, int sequenceId) {
        SnowflakeID snowflakeID = new SnowflakeID();
        return snowflakeID.generateUserFriendly(machineId, sequenceId);
    }

    private void setMaxBit() {
        maxTimeStamp = ~(-1L << timestampBit);
        maxMachine = ~(-1L << machineBit);
        maxSequence = ~(-1L << sequenceBit);
    }

    public Long generate(long machineId, int sequenceId) {
        long timestamp = ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
        checkArgument(timestamp < maxTimeStamp, "timestamp over limit");
        checkArgument(machineId < maxMachine, "too many machines");
        checkArgument(sequenceId < maxSequence, "invalid sequence id");
        log.info("----->> {} {} {}", timestamp, machineId, sequenceId);
        return (timestamp << timestampLeft)
                | (machineId << machineLeft)
                | sequenceId;
    }

    public String generateUserFriendly(long machineId, int sequenceId) {
        long snowflakeId = generate(machineId, sequenceId);
        long timestamp = snowflakeId >> timestampLeft;
        String ts = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"))
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return ts + "-" +
                machineId + "-" +
                sequenceId + "/" +
                snowflakeId + "/" +
                timestamp + "+" + machineId + "+" + sequenceId;
    }
}
