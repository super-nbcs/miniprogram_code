package com.zfw.scheduling;

import com.zfw.core.sys.entity.User;
import com.zfw.core.sys.service.IUserService;
import com.zfw.utils.FileStore.FileStoreUtils;
import com.zfw.utils.FileStore.YamlConfigurerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author:zfw
 * @Date:2020-10-12
 * @Content: 关于备份的定时任务
 */
@Component
@Configurable
@EnableScheduling
public class BackScheduling {

    @Value("${open.scheduling}")
    private boolean isOpen;


    @Autowired
    private IUserService iUserService;
    //shell脚本备份 在resources/shell/photo_bak下
    //每天早晨三点,备份一次人员底库照片，结合shell脚本+crontab进行备份 /home/v1/projects/shell/photo_bak.sh
    @Scheduled(cron = "0 0 3 * * ?")
    public void handlerUserPhotos() throws IOException {
        if (!isOpen){
            return;
        }
    }

}
