package com.pegasus.file.service.impl;

import com.pegasus.common.service.impl.CommonServiceImpl;
import com.pegasus.file.domain.FileChunk;
import com.pegasus.file.domain.PeFile;
import com.pegasus.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by enHui.Chen on 2021/2/19.
 */
@Slf4j
@Service
public class FileServiceImpl extends CommonServiceImpl<PeFile> implements IFileService<PeFile> {
    private static final String ROOT_PATH = "C:\\Users\\Administrator\\Desktop\\测试\\";
    private static final String SUFFIX_TYPE = ".tmp";

    @Override
    public void uploadFileByChunk(FileChunk fileChunk, MultipartFile multipartFile) throws Exception {
        log.info(multipartFile.getOriginalFilename());
        log.info(multipartFile.getContentType());

        // 指定tmp文件名称
        File tmpDir = new File(ROOT_PATH);
        File tmpFile = new File(ROOT_PATH + multipartFile.getOriginalFilename() + "_" + fileChunk.getFileChunkMd5() + SUFFIX_TYPE);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        RandomAccessFile outRaf = new RandomAccessFile(tmpFile, "rw");
        // 分块写入
        outRaf.seek(fileChunk.getChunkIndex() * fileChunk.getChunkLength());
        outRaf.write(multipartFile.getBytes());

        outRaf.close();

        // 校验是否已全部写入

        // 若已全部写入则重命名文件

    }

    @Override
    public void bigFilePrepare(PeFile file) {

    }

    @Override
    public void download() {

    }

    private boolean checkIsWriteFinish() {
        return false;
    }
}
