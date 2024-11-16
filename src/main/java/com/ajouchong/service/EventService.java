package com.ajouchong.service;

import com.ajouchong.entity.Event;
import com.ajouchong.exception.ResourceNotFoundException;
import com.ajouchong.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event addEvent(Long id, Event event) {
        if (id == null) {
            // ID가 없는 경우 새로운 이벤트를 추가
            return eventRepository.save(event);
        } else {
            // ID가 있는 경우 기존 이벤트 수정
            Event existingEvent = eventRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("일정이 존재하지 않습니다."));
            existingEvent.setTitle(event.getTitle());
            existingEvent.setDate(event.getDate());
            return eventRepository.save(existingEvent);
        }
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
