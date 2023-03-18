package com.franc.mapper;

import com.franc.vo.MbspVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MbspMapper {

    MbspVO findById(@Param("mbspId")String mbspId) throws Exception;


}
