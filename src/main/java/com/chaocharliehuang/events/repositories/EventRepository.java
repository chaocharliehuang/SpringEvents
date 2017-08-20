package com.chaocharliehuang.events.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chaocharliehuang.events.models.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
	List<Event> findByState(String state);
	
	@Query("SELECT e FROM Event e WHERE e.state != ?1")
	List<Event> findEventsNotInState(String state);
}
