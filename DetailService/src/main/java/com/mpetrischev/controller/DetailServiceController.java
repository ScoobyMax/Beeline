package com.mpetrischev.controller;

import com.mpetrischev.dto.ResponseAbonents;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/data")
public interface DetailServiceController {
    @GetMapping(value = {"/{cellid}"})
    ResponseAbonents getAbonents(@PathVariable("cellid") String cellid);
}
