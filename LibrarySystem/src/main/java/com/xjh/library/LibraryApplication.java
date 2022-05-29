package com.xjh.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.xjh.library"})
public class LibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class,args);
    }
}
