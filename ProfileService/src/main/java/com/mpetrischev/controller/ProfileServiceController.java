package com.mpetrischev.controller;

import com.mpetrischev.dto.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/profile")
public interface ProfileServiceController {
    @GetMapping(value = {"/{ctn}"})
    Profile getProfile(@PathVariable("ctn") String ctn);
}
