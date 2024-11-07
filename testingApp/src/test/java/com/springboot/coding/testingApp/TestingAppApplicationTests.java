package com.springboot.coding.testingApp;

import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
// import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
//@SpringBootTest
class TestingAppApplicationTests {

    @BeforeEach
    void setUp() {
        log.info("Starting the method, setting up config");
    }

    @AfterEach
    void tearDown() {
        log.info("Tearing down the method");
    }

    @BeforeAll
    static void setUpOnce() {
        log.info("Setup Once...");
    }

    @AfterAll
    static void tearDownOnce() {
        log.info("Tearing down all...");
    }


    // don't write two test case in one method --> Follow the single responsibility principle --> Solid Principle
    @Test
//	@Disabled
    void testNumberOne() {
        log.info("Test Number One");
        int a = 5;
        int b = 3;
        int result = addTwoNumbers(a, b);
/*
        JUnit Assertions
        Assertions.assertEquals(9, result);
        Assertions.assertEquals(8, result);
*/
/*      assertj --> Library
        Assertions
                .assertThat(result).isEqualTo(8)
                .isCloseTo(9, Offset.offset(1));
 */
        // Use the static method
        assertThat(result).isEqualTo(8)
                .isCloseTo(9, Offset.offset(1));

    }

    @Test
//	@DisplayName("displayTestNameTwo")
    void tesNumberTwo() {
        log.info("Test Number Two");
        assertThat("Apple")
                .isEqualTo("Apple")
                .startsWith("App")
                .endsWith("le")
                .hasSize(5);
    }

    @Test
    void testDivideTwoNumbers_whenDenominatorIsZero_ThenArithmeticException() {
        log.info("Test Divide Two Numbers");
        int a = 5;
        int b = 0;

        assertThatThrownBy(() -> divideTwoNumbers(a, b))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Attempted to divide by zero");
    }

    int addTwoNumbers(int a, int b) {
        return a + b;
    }

    void divideTwoNumbers(int a, int b) {


        try {
//            return (double) a / b; // infinity is b = 0
            int result = (a / b);
            log.info("Result: {}", result);
        } catch (ArithmeticException e) {
            log.error("Arithmetic exception occurred: {}", e.getLocalizedMessage());
            throw new ArithmeticException("Attempted to divide by zero");
        }
    }
}
