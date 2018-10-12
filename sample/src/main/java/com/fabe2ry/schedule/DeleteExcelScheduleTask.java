package com.fabe2ry.schedule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/10/12.
 */
@Component
public class DeleteExcelScheduleTask {

    Logger logger = Logger.getLogger(DeleteExcelScheduleTask.class.getName());

    @Value("${excel.local.path}")
    String excelLocalPath;

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void deleteExcel(){
        logger.info("start delete excel file");
        File directory = new File(excelLocalPath);
        if(directory.exists()){
            for(File file : directory.listFiles()){
                if(! file.isDirectory()){
                    long dis = (System.currentTimeMillis() - file.lastModified())/(1000 * 60);
                    if(dis > 10){
                        file.delete();
                    }
                }
            }
        }
    }


}
