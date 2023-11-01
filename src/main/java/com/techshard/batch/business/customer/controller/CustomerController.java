package com.techshard.batch.business.customer.controller;

import com.techshard.batch.configuration.CustomerExportService;
import com.techshard.batch.configuration.CustomerImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JobLauncher jobLauncher;

    private final CustomerImportService importService;
    private final CustomerExportService exportService;

    @PostMapping("/sync-import")
    private void importBulkData(){
        JobParameters jobParameter = new JobParametersBuilder().addLong("executedAt", System.currentTimeMillis()).toJobParameters();

        try {
            jobLauncher.run(importService.importCustomerJob(), jobParameter);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/sync-export")
    private void exportBulkData(){
        JobParameters jobParameter = new JobParametersBuilder().addLong("executedAt", System.currentTimeMillis()).toJobParameters();
        String fileName = "customer" + System.currentTimeMillis();
        try {
            jobLauncher.run(exportService.importCustomerJob(fileName), jobParameter);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
