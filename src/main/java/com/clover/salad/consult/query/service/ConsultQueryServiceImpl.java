package com.clover.salad.consult.query.service;

import com.clover.salad.consult.query.dto.ConsultQueryDTO;
import com.clover.salad.consult.query.mapper.ConsultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultQueryServiceImpl implements ConsultQueryService {

    private final ConsultMapper consultMapper;

    @Override
    public ConsultQueryDTO findById(int id) {
        return consultMapper.findById(id);
    }

    @Override
    public List<ConsultQueryDTO> findAll(int page, int size) {
        int offset = (page - 1) * size;
        return consultMapper.findAll(size, offset);
    }
}
