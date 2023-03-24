package com.franc.mapper;

import com.franc.vo.MyMbspVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MyMbspMapper {

    void save(MyMbspVO vo) throws Exception;

    /**
     * @param map {acntId, mbspId}
     * @return
     * @throws Exception
     */
    MyMbspVO findById(Map<String, Object> map) throws Exception;

    void modify(MyMbspVO vo) throws Exception;

    Integer getBarCdSeq() throws Exception;

    MyMbspVO findByBarCd(@Param("barCd") String barCd) throws Exception;

}
