package com.service_desk.services;

import com.service_desk.entity.TicketEntity;
import com.service_desk.model.TicketResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketMapper {

    public List<TicketResponse> map(List<TicketEntity> entities){
        return entities.stream().map(this::map).toList();
    }
    public TicketResponse map(TicketEntity entity){
        return TicketResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .email(entity.getEmail())
                .problem(entity.getProblem())
                .priority(entity.getPriority().toString())
                .closed(entity.getClosed())
                .createdDate(entity.getCreatedDate())
                .closedDate(entity.getClosedDate())
                .build();
    }
}
