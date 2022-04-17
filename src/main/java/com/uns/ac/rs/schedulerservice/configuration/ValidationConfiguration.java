package com.uns.ac.rs.schedulerservice.configuration;

import com.uns.ac.rs.schedulerservice.service.impl.validators.Validator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@AllArgsConstructor
public class ValidationConfiguration {

    private final List<Validator> validators;

    @PostConstruct
    private void sortValidators(){

        //Setting up validators chain - Validators
        validators.sort(AnnotationAwareOrderComparator.INSTANCE);
        int validatorsSize = validators.size();

        for (int i = 0; i < validatorsSize; i++) {
            if (i == validatorsSize-1) validators.get(i).setNext(null);
            else validators.get(i).setNext(validators.get(i+1));
        }

    }
}
