package com.instead.pay.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/upload")
@Slf4j
public class UploadController {

//    @Autowired
//    private ConstantQiniu constantQiniu;

    /**
     * 上传文件到七牛云存储
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/qrUpload")
    @ResponseBody
    public JSONObject uploadImgQiniu(@RequestParam("file") MultipartFile file,int imgType) throws Exception {
        Assert.notNull(file, "上传文件不能为空");
        JSONObject json = new JSONObject();
        boolean flag = file.isEmpty();
        if (!flag) {
            String name=System.currentTimeMillis()+file.getOriginalFilename();
            FileInputStream inputStream = (FileInputStream) file.getInputStream();
            ConstantQiniu constantQiniu=new ConstantQiniu();
            if(imgType == 1){//1普通图片
                json = constantQiniu.uploadQNImg(file);
            }else if(imgType == 2){//2二维码
                flag = constantQiniu.testQr(inputStream);
                if(flag){
                    json = constantQiniu.uploadQNImg(file);
                }else{
                    json.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                    json.put(ResultKey.KEY_MSG, "解析失败，请检查上传文件是否为二维码");
                }
            }else{
                json.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                json.put(ResultKey.KEY_MSG, "请设置图片类型");
            }
        } else {
            json.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            json.put(ResultKey.KEY_MSG, "文件为空");
        }
        return json;
    }


}