package com.lawProject.SSL.domain.lawsuit.api;

import com.lawProject.SSL.domain.lawsuit.repository.LawSuitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LawSuitController {
    private final LawSuitRepository lawSuitRepository;
}
