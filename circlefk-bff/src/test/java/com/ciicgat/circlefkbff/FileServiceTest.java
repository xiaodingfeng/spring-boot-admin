package com.ciicgat.circlefkbff;

import com.ciicgat.circlefkbff.service.file.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {

    @Resource
    private FileService fileService;

    @Test
    public void upload() {
        String strUrl = "D:\\workpace\\biz\\合同审批表单.md";
        File file = new File(strUrl);
        System.out.println(fileService.upload(file));
    }
}
