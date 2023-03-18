package com.franc.mapper;

import com.franc.vo.AcntVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AcntMapper {

    AcntVO findById(@Param("acntId") Long acntId) throws Exception;

}
