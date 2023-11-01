package com.techshard.batch.configuration;

import com.techshard.batch.business.customer.entity.Customer;
import com.techshard.batch.business.customer.repository.CustomerRepository;
import com.techshard.batch.business.customer.service.CustomerProcessor;
import com.techshard.batch.business.customer.service.CustomerProcessorExport;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@EnableBatchProcessing
@AllArgsConstructor
@Service
public class CustomerExportService {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerRepository customerRepository;

    private RepositoryItemReader<Customer> reader(){
        RepositoryItemReader<Customer> itemReader = new RepositoryItemReader<>();
        itemReader.setRepository(customerRepository);
        itemReader.setMethodName("findAll");
        Map<String, Sort.Direction> sort = new HashMap<String, Sort.Direction>();
        itemReader.setSort(sort);
        return itemReader;
    }

    private CustomerProcessorExport processor(){
        return new CustomerProcessorExport();
    }

    private FlatFileItemWriter<Customer> writer(String fileName){
        File file = new File("src/main/resources/", fileName+".txt");
        BeanWrapperFieldExtractor<String> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"name","age", "gender"});

        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        writer.setName("customerItemWriter");
        writer.setLineSeparator("\n");
        writer.setResource(new FileSystemResource(file));

        DelimitedLineAggregator lineAggregator = new DelimitedLineAggregator();
        lineAggregator.setDelimiter("||");
        lineAggregator.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(lineAggregator);

        return writer;
    }

//    @Bean
    public Step stepExport(String fileName){
        return stepBuilderFactory.get("exportCustomerStep")
                .<Customer,Customer>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer(fileName))
//                .taskExecutor(taskExecutor())
                .build();
    }

    public Job importCustomerJob(String fileName){
        return jobBuilderFactory.get("customerExportJob").flow(stepExport(fileName)).end().build();
    }
}
