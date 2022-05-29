package com.xjh.library.common.utils;

import com.xjh.library.common.exception.MyException;
import org.springframework.web.multipart.MultipartFile;

// 有关文件的插件
public class FileUtil {
    // 获取文件后缀名 例如:1.png 返回.png
    public static String getFileSuffix(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename == null) throw new MyException("文件异常");
        return filename.replaceAll("^.*(\\..*)$","$1").toLowerCase();
    }
}
