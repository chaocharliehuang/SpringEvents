package com.chaocharliehuang.events.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.chaocharliehuang.events.models.Event;

@Component
public class EventValidator implements Validator {
	
	@Override
    public boolean supports(Class<?> clazz) {
        return Event.class.equals(clazz);
    }
	
	@Override
    public void validate(Object object, Errors errors) {
		Date today = new Date();
        Event event = (Event) object;
        
        if (event.getDate() == null || event.getDate().before(today)) {
            errors.rejectValue("date", "FutureDate");
        }         
    }

}
