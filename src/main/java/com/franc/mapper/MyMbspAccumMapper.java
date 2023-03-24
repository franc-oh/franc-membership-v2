package com.franc.mapper;

import com.franc.vo.MyMbspAccumVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyMbspAccumMapper {

    void save(MyMbspAccumVO vo) throws Exception;
    void modify(MyMbspAccumVO vo) throws Exception;

    MyMbspAccumVO findById(@Param("cancelBarCd") String cancelBarCd) throws Exception;

}
