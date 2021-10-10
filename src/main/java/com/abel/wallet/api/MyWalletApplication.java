package com.abel.wallet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()//exclude = HibernateJpaAutoConfiguration.class
@ComponentScan(basePackages="com.abel.wallet.api")
public class MyWalletApplication  {

	public static void main(String[] args) {
		SpringApplication.run(MyWalletApplication.class, args);
	}


//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
//		return builder.sources(MyWalletApplication.class);
//	}

}
