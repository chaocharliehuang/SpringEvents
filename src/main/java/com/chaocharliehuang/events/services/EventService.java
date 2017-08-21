package com.chaocharliehuang.events.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chaocharliehuang.events.models.Event;
import com.chaocharliehuang.events.repositories.EventRepository;

@Service
public class EventService {
	
	private EventRepository eventRepository;
	
	public EventService(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	
	public void createEvent(Event event) {
		eventRepository.save(event);
	}
	
	public List<Event> findEventsByState(String state) {
		return eventRepository.findByState(state);
	}
	
	public List<Event> findEventsNotInState(String state) {
		return eventRepository.findEventsNotInState(state);
	}
	
	public Event findEventById(Long id) {
		return eventRepository.findOne(id);
	}
	
	public void deleteEvent(Long id) {
		eventRepository.delete(id);
	}

}
