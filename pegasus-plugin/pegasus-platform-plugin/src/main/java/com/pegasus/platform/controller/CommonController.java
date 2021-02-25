package com.pegasus.platform.controller;

import com.pegasus.platform.service.ILovService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by enHui.Chen on 2019/11/11.
 */
@RestController
@RequestMapping("/v1/common")
public class CommonController {
    @Autowired
    private ILovService lovService;

    @GetMapping("/lov/query/{lovCode}")
    public ResponseEntity<Map> lovQuery(Pageable pageable,
                                        @PathVariable("lovCode") String lovCode,
                                        @RequestParam(name = "init", required = false, defaultValue = "true") Boolean init) {
        return ResponseEntity.ok(lovService.lovQuery(pageable, lovCode, init));
    }
}
