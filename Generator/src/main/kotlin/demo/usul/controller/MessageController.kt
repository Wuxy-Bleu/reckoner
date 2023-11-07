package demo.usul.controller

import demo.usul.repository.ColumnRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(val repository: ColumnRepository) {


    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

    @GetMapping("/xxxx")
    fun findAll() = repository.findFromView()
}