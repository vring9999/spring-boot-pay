package com.instead.pay.percentage.mapper;

import com.instead.pay.percentage.model.Percentage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PercentageMapper {

    void insert(Percentage percentage);

    List<Percentage> selectAll(Map<String,Object> params);
}
