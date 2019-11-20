package com.lixin.dto.es;

import lombok.Data;
import java.util.Map;

/**
 * @description: where查询dto对象
 * @author: zhenglubo
 * @create: 2019-10-23 14:59
 **/

@Data
public class BoolQueryBuilderDto {

    private Map<String,Object> mustMap;
    private Map<String,Object> mustNotMap;
    private Map<String,Object> shouldMap;
}
