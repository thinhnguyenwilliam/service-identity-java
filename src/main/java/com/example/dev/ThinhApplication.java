package com.example.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ThinhApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThinhApplication.class, args);
  }

  ///  secret key jwt token
  //	package solution;
  //	/*
  //	 * 2024 ^_^
  //	 *ThinhNguyen97
  //	 *
  //	 */
  // import java.security.SecureRandom;
  // import java.util.*;
  //
  //	public class Solution
  //	{
  //		static String generateSecretKey()
  //		{
  //			SecureRandom random = new SecureRandom();
  //			byte[] keyBytes = new byte[32]; // 256-bit key
  //			random.nextBytes(keyBytes);
  //			String secretKey = Base64.getEncoder().encodeToString(keyBytes);
  //			return secretKey;
  //		}
  //
  //		public static void main(String[] args)
  //		{
  //			Scanner sc = new Scanner(System.in);
  //			System.out.println("Generated Secret Key: " + generateSecretKey());
  //
  //			sc.close();
  //		}
  //	}

  ///

}
