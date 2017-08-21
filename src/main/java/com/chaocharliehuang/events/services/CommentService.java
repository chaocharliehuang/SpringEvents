package com.chaocharliehuang.events.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chaocharliehuang.events.models.Comment;
import com.chaocharliehuang.events.models.Event;
import com.chaocharliehuang.events.repositories.CommentRepository;

@Service
public class CommentService {
	
	private CommentRepository commentRepository;
	
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	public void createComment(Comment comment) {
		commentRepository.save(comment);
	}
	
	public List<Comment> findCommentsOfEvent(Event event) {
		return commentRepository.findByEventOrderByCreatedAtAsc(event);
	}

}
