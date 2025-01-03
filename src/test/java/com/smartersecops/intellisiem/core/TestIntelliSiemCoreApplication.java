package com.smartersecops.intellisiem.core;

import org.springframework.boot.SpringApplication;

public class TestIntelliSiemCoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(IntelliSiemCoreApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
