package com.kousenit.demo.controllers

import com.kousenit.demo.entities.Greeting
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloRestController {

    @GetMapping("/rest")
    Greeting greet(@RequestParam(required = false,
            defaultValue = "World") String name) {
        new Greeting(message: "Hello, $name!")
    }
}
