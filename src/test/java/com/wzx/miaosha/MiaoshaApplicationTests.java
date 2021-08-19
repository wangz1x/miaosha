package com.wzx.miaosha;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class MiaoshaApplicationTests {

	@Test
	void contextLoads() {
		String s = "a|b|c|||";
		System.out.println(Arrays.toString(s.split("\\|", -1)));
	}

}
