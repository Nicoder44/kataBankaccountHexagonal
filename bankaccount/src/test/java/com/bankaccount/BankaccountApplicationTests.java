package com.bankaccount;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BankaccountApplicationTests {

	@Test
	void mainTest() {
		assertDoesNotThrow(() -> BankaccountApplication.main(new String[] {}));
	}

}
