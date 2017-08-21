package com.chaocharliehuang.events.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chaocharliehuang.events.models.Comment;
import com.chaocharliehuang.events.models.Event;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
	List<Comment> findByEventOrderByCreatedAtAsc(Event event);
}
