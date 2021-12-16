package com.ciicgat.circlefk.web.controller;

import com.ciicgat.circlefk.common.sdk.lang.convert.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file")
public class FileController {

    @RequestMapping(name = "上传文件", value = "/upload", method = RequestMethod.POST)
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) {
        System.out.println("==========");
        System.out.println(file.getOriginalFilename());
//        throw new BusinessRuntimeException(ErrorCode.REQUEST_BLOCK);
        return ApiResponse.success("文件路径1111");
    }
}
