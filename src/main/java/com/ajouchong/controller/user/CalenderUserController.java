package com.ajouchong.controller.user;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.EventRequestDto;
import com.ajouchong.entity.Event;
import com.ajouchong.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/notice/calender")
public class CalenderUserController {
    private final EventService eventService;

    public CalenderUserController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ApiResponse<List<EventRequestDto>> getEvents() {
        List<Event> events = eventService.getEvents();
        List<EventRequestDto> eventDto = events.stream()
                .map(this::convertToDto)
                .toList();
        return new ApiResponse<>(1, "일정 목록을 불러왔습니다.", eventDto);
    }

    private EventRequestDto convertToDto(Event event) {
        EventRequestDto eventRequestDto = new EventRequestDto();

        eventRequestDto.setEventId(event.getId());
        eventRequestDto.setTitle(event.getTitle());
        eventRequestDto.setDate(event.getDate().toString());

        return eventRequestDto;
    }
}
