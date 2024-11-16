package com.ajouchong.controller.admin;

import com.ajouchong.common.ApiResponse;
import com.ajouchong.dto.request.EventRequestDto;
import com.ajouchong.entity.Event;
import com.ajouchong.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/admin/notice/calender")
public class CalenderAdminController {
    private final EventService eventService;

    public CalenderAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/add")
    public ApiResponse<EventRequestDto> addEvent(@RequestParam(required = false) Long id, @RequestBody EventRequestDto eventRequestDto) {
            Event event = convertToEntity(eventRequestDto);
            Event savedEvent = eventService.addEvent(id, event);
            EventRequestDto savedEventRequestDto = convertToDto(savedEvent);

            String message = (id == null) ? "학사일정을 추가했습니다." : "학사일정을 수정했습니다.";
            return new ApiResponse<>(1, message, savedEventRequestDto);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ApiResponse<>(1, "일정을 삭제했습니다.", null);
    }

    private Event convertToEntity(EventRequestDto eventRequestDto) {
        Event event = new Event();

        eventRequestDto.setEventId(event.getId());
        event.setTitle(eventRequestDto.getTitle());
        event.setDate(LocalDate.parse(eventRequestDto.getDate()));

        return event;
    }

    private EventRequestDto convertToDto(Event event) {
        EventRequestDto eventRequestDto = new EventRequestDto();

        eventRequestDto.setEventId(event.getId());
        eventRequestDto.setTitle(event.getTitle());
        eventRequestDto.setDate(event.getDate().toString());

        return eventRequestDto;
    }
}
