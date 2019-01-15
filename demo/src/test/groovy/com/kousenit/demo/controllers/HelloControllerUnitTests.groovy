package com.kousenit.demo.controllers

import org.junit.Test
import org.springframework.ui.Model
import org.springframework.validation.support.BindingAwareModelMap

class HelloControllerUnitTests {
    @Test
    void testSayHello() throws Exception {
        HelloController controller = new HelloController()
        Model model = new BindingAwareModelMap()
        String result = controller.sayHello('World', model)
        assert 'World' == model.asMap().get('user')
        assert 'hello' == result
    }
}
