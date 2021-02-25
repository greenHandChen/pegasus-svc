package com.pegasus.file.controller;

import com.pegasus.file.domain.FileChunk;
import com.pegasus.file.domain.PeFile;
import com.pegasus.file.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by enHui.Chen on 2021/2/19.
 */
@RestController
@RequestMapping("/v1/file")
public class FileController {
    @Autowired
    private IFileService iFileService;

    /**
     * @Author: enHui.Chen
     * @Description: 大文件准备开始上传
     * @Data 2021/2/19
     */
    @PostMapping("/big-file-prepare")
    public ResponseEntity<Void> bigFilePrepare(@RequestBody PeFile file) {
        iFileService.bigFilePrepare(file);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 文件分片上传
     * @Data 2021/2/19
     */
    @PostMapping("/upload-file-chunk")
    public ResponseEntity<Void> uploadFileByChunk(FileChunk fileChunk, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        iFileService.uploadFileByChunk(fileChunk, multipartFile);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
