package com.cdcas.controller;

import com.cdcas.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${jier.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws Exception {


//        file是一个临时文件,需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

//        原始文件名

        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//      使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        }

        String fileName = UUID.randomUUID() + suffix;

//        创建一个目录对象
        File dir=new File(basePath);
//        判断当前目录是否存在
        if (!dir.exists()){
            dir.mkdir();
        }
//        将临时文件存起来
        file.transferTo(new File(basePath + fileName));
        return R.success(fileName);
    }
}
