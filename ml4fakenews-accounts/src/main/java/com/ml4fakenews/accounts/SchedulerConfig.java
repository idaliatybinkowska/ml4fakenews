package com.ml4fakenews.accounts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Configuration
@EnableScheduling
public class SchedulerConfig {


    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${backup.databaseName}")
    private String databaseName;
    @Value("${backup.dirPath}")
    private String dirPath;

    //0 0 12 */7 * ? --> co tydzien w niedziele o polnocy
    //0 * * ? * * --> co minute
    @Scheduled(cron = "0 0 12 */7 * ?")
    public void doDatabaseBackup() throws IOException {
        LocalDate backupDate = LocalDate.now();
        String cmd = "mysqldump -u" + username + " -p" + password + " " + databaseName + " -r " + dirPath + File.separator + "backup" + backupDate.toString() + ".sql";
        Runtime.getRuntime().exec(cmd);
    }
}