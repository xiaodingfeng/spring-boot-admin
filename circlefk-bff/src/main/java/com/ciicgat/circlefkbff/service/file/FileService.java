package com.ciicgat.circlefkbff.service.file;

import com.ciicgat.circlefkbff.feign.api.FeignFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * 文件上传类
 */
@Service
public class FileService {

    @Resource
    private FeignFileService feignFileService;

    public String upload(File file)  {
        return feignFileService.upload(file);
    }
}
