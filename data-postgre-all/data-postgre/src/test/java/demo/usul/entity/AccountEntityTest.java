package demo.usul.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static demo.usul.Const.SHANG_HAI_OFFSET;
import static org.assertj.core.api.Assertions.assertThat;

class AccountEntityTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    // 单测的方法中用了now
//    @Test
    void return_dueDateThisMonth_when_dayOfMonth_of_dueDate_largeThanOrEquals_now() {
        AccountEntity sut = new AccountEntity();
        sut.setDueDate("30");
        AccountEntity sut2 = new AccountEntity();
        sut2.setDueDate("28");

        LocalDate nearestDeadline = sut.getNearestDeadline();
        LocalDate nearestDeadline2 = sut2.getNearestDeadline();

        assertThat(nearestDeadline).hasDayOfMonth(30).hasMonthValue(12).hasYear(2024);
        assertThat(nearestDeadline2).hasDayOfMonth(28).hasMonthValue(12).hasYear(2024);
    }

//    @Test
    void return_dueDateNextMonth_when_dayOfMonth_of_dueDate_largeThan_now() {
        AccountEntity sut = new AccountEntity();
        sut.setDueDate("12");

        LocalDate nearestDeadline = sut.getNearestDeadline();

        assertThat(nearestDeadline).hasMonthValue(1).hasDayOfMonth(12).hasYear(2025);
    }

    static Stream<OffsetDateTime> provideDates() {
        return Stream.of(
//                OffsetDateTime.now(), // 2024 12 29
                OffsetDateTime.of(LocalDate.of(2024, 12, 14), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 15), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 31), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2025, 1, 12), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2025, 1, 13), LocalTime.now(), SHANG_HAI_OFFSET)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDates")
    void feb7th2025_as_repaymentDate_of(OffsetDateTime transactionDate) {
        AccountEntity sut = new AccountEntity();
        sut.setBillingCycle("14~+13");
        sut.setDueDate("7");

        LocalDate localDate = sut.calculateDueDateOfTransDate(transactionDate);

        assertThat(localDate).isEqualTo(LocalDate.of(2025, 2, 7));
    }

    static Stream<OffsetDateTime> provideDates2() {
        return Stream.of(
                OffsetDateTime.of(LocalDate.of(2024, 12, 13), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 12), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 1), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 14), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 15), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 30), LocalTime.now(), SHANG_HAI_OFFSET)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDates2")
    void jan7th2025_as_repaymentDate_of(OffsetDateTime transactionDate) {
        AccountEntity sut = new AccountEntity();
        sut.setBillingCycle("14~+13");
        sut.setDueDate("7");

        LocalDate localDate = sut.calculateDueDateOfTransDate(transactionDate);

        assertThat(localDate).isEqualTo(LocalDate.of(2025, 1, 7));
    }

    static Stream<OffsetDateTime> provideDates3() {
        return Stream.of(
                OffsetDateTime.of(LocalDate.of(2024, 12, 7), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 8), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 31), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2025, 1, 5), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2025, 1, 6), LocalTime.now(), SHANG_HAI_OFFSET)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDates3")
    void Jan24th2025_as_repaymentDate_of(OffsetDateTime transactionDate) {
        AccountEntity sut = new AccountEntity();
        sut.setBillingCycle("7~+6");
        sut.setDueDate("24");

        LocalDate localDate = sut.calculateDueDateOfTransDate(transactionDate);

        assertThat(localDate).isEqualTo(LocalDate.of(2025, 1, 24));
    }

    static Stream<OffsetDateTime> provideDates4() {
        return Stream.of(
                OffsetDateTime.of(LocalDate.of(2024, 12, 6), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 5), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 1), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 7), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 8), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 30), LocalTime.now(), SHANG_HAI_OFFSET)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDates4")
    void dec24th2024_as_repaymentDate_of(OffsetDateTime transactionDate) {
        AccountEntity sut = new AccountEntity();
        sut.setBillingCycle("7~+6");
        sut.setDueDate("24");

        LocalDate localDate = sut.calculateDueDateOfTransDate(transactionDate);

        assertThat(localDate).isEqualTo(LocalDate.of(2024, 12, 24));
    }

    static Stream<OffsetDateTime> provideDates5() {
        return Stream.of(
                OffsetDateTime.of(LocalDate.of(2024, 12, 1), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 2), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 30), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 12, 31), LocalTime.now(), SHANG_HAI_OFFSET)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDates5")
    void jan8th2025_as_repaymentDate_of(OffsetDateTime transactionDate) {
        AccountEntity sut = new AccountEntity();
        sut.setBillingCycle("1~+0");
        sut.setDueDate("8");

        LocalDate localDate = sut.calculateDueDateOfTransDate(transactionDate);

        assertThat(localDate).isEqualTo(LocalDate.of(2025, 1, 8));
    }
    static Stream<OffsetDateTime> provideDates6() {
        return Stream.of(
                OffsetDateTime.of(LocalDate.of(2024, 11, 1), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 2), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 30), LocalTime.now(), SHANG_HAI_OFFSET),
                OffsetDateTime.of(LocalDate.of(2024, 11, 29), LocalTime.now(), SHANG_HAI_OFFSET)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDates6")
    void dec8th2024_as_repaymentDate_of(OffsetDateTime transactionDate) {
        AccountEntity sut = new AccountEntity();
        sut.setBillingCycle("1~+0");
        sut.setDueDate("8");

        LocalDate localDate = sut.calculateDueDateOfTransDate(transactionDate);

        assertThat(localDate).isEqualTo(LocalDate.of(2024, 12, 8));
    }
}