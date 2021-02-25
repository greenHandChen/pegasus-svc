package com.pegasus.file.service;

import com.pegasus.common.service.ICommonService;
import com.pegasus.file.domain.FileChunk;
import com.pegasus.file.domain.PeFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by enHui.Chen on 2021/2/19.
 */
public interface IFileService<T> extends ICommonService<T> {
    /**
     * @Author: enHui.Chen
     * @Description: 文件分片上传
     * @Data 2021/2/19
     */
    void uploadFileByChunk(FileChunk fileChunk, MultipartFile multipartFile) throws Exception;

    /**
     * @Author: enHui.Chen
     * @Description: 大文件准备开始上传
     * @Data 2021/2/19
     */
    void bigFilePrepare(PeFile file);

    /**
     * @Author: enHui.Chen
     * @Description: 文件下载
     * @Data 2021/2/20
     */
    void download();
}
