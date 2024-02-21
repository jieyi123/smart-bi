package com.pjieyi.smartbi.utils;

import com.alibaba.excel.EasyExcel;
import com.pjieyi.smartbi.common.ErrorCode;
import com.pjieyi.smartbi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pjieyi
 * @desc excel处理
 */
@Slf4j
public class ExcelUtils {

    public static String excelToCsv(MultipartFile multipartFile) {
        List<Map<Integer, String>> mapList=null;
        try {
            //File file= ResourceUtils.getFile("classpath:test_excel.xlsx");
            mapList = EasyExcel.read(multipartFile.getInputStream()).sheet().headRowNumber(0).doReadSync();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"表格处理错误");
        }
        //转为csv
        //StringBuilder线程不安全 快
        StringBuilder stringBuilder = new StringBuilder();
        //第一行为表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap) mapList.get(0);
        //筛选不为空的数据
        List<String> headerList = headerMap.values().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerList, ",")).append("\n");
        //源数据
        for (int i = 1; i < mapList.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap) mapList.get(i);
            List<String> dataList = dataMap.values().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList, ",")).append("\n");
        }
        return stringBuilder.toString();


    }

}
