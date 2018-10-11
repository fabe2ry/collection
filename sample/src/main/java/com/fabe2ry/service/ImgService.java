package com.fabe2ry.service;

import com.fabe2ry.model.util.ResultVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xiaoxq on 2018/10/11.
 */
@Service
public interface ImgService {

    ResultVo imgUpload(MultipartFile multipartFile);

    void imgUpload(MultipartFile[] multipartFiles);

    ResultVo imgDownload();

    ResultVo imgShow();
}
