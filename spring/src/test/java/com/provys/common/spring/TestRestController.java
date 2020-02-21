package com.provys.common.spring;

import com.provys.common.exception.RegularException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/exception", produces = {MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE})
class TestRestController {

  @GetMapping("/throw/{nameNm:[a-zA-Z][a-zA-Z_0-9]*}")
  public String throwException(@PathVariable("nameNm") String nameNm) {
    throw new RegularException(nameNm, "Exception thrown.");
  }
}
