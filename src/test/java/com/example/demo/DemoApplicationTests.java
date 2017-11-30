package com.example.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

/*
	@Test
	public void contextLoads() {
	}
*/

	/*public static void main(String[] args) {
		String []datas = new String[] {"peng","zhao","li"};
		Arrays.sort(datas);
		Stream.of(datas).forEach(param ->     System.out.println(param));
	}*/



		public static void main(String[] args) {
			BigDecimal decimal = new BigDecimal("");
			System.out.println(decimal);
			BigDecimal setScale = decimal.setScale(4,BigDecimal.ROUND_HALF_DOWN);
			System.out.println(setScale);

			BigDecimal setScale1 = decimal.setScale(4,BigDecimal.ROUND_HALF_UP);
			System.out.println(setScale1);
		}


}
