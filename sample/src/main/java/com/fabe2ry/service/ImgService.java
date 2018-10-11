package com.fabe2ry.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xiaoxq on 2018/10/11.
 */
@Service
public interface ImgService {

    void imgUpload(MultipartFile multipartFile);

    void imgUpload(MultipartFile[] multipartFiles);

    void imgDownload();
}
