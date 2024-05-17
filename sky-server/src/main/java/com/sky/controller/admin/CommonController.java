package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * common interface
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "common interface")
@Slf4j
public class CommonController {

    @Value("${cbs.imagesPath}")
    private String imagesPath;

    @PostMapping("/upload")
    @ApiOperation(value = "upload file")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传: {}", file);

        // generate the uuid
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + extension;

        // save the image to the local machine
        File dest = new File(imagesPath + objectName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败: {}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

        // return the url
        return Result.success("http://localhost:8081/file/image/" + objectName);
    }
}
