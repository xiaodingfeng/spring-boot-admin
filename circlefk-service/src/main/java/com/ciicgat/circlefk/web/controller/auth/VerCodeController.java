package com.ciicgat.circlefk.web.controller.auth;

import com.ciicgat.circlefk.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefk.common.sdk.lang.convert.ErrorCode;
import com.ciicgat.circlefk.common.sdk.lang.exception.BusinessRuntimeException;
import com.wf.captcha.SpecCaptcha;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/vercode")
public class VerCodeController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final static String VER_CODE_ID = "VER_CODE_ID_";

    @PostMapping(value = "createCaptcha")
    public ApiResponse<String> createCaptcha(@RequestParam("sessionId") String sessionId, @RequestParam("length") Integer length) {

        System.out.println(sessionId);
        Long count = stringRedisTemplate.opsForValue().increment(VER_CODE_ID + sessionId.concat("_limit_count"), 1);
        if (count == null) {
            throw new BusinessRuntimeException(ErrorCode.REQUEST_BLOCK);
        }
        //刚创建
        if (count == 1) {
            //设置1分钟过期
            stringRedisTemplate.expire(VER_CODE_ID + sessionId.concat("_limit_count"), 1, TimeUnit.MINUTES);
        }
        if (count > 30) {
            throw new BusinessRuntimeException(-1, "超出访问次数限制");
        }

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, length);
        // 设置字体
        specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字2、纯字母3、字母数字1混合
        specCaptcha.setCharType(3);
        //将验证码以<key,value>形式缓存到redis
        stringRedisTemplate.opsForValue().set(VER_CODE_ID + sessionId, specCaptcha.text().toLowerCase(), 3*60, TimeUnit.SECONDS);
        return ApiResponse.success(specCaptcha.toBase64());
    }

    @PostMapping(value = "getVerCode")
    public ApiResponse<String> getCaptcha(@RequestParam("sessionId") String sessionId) {
        return ApiResponse.success(stringRedisTemplate.opsForValue().get(VER_CODE_ID + sessionId));
    }

    @PostMapping(value = "validateToken")
    public ApiResponse<Boolean> validateToken(@RequestParam("token") String token) {
        return ApiResponse.success(stringRedisTemplate.opsForValue().get(token) != null);
    }

    @PostMapping(value = "addToken")
    public ApiResponse<Boolean> addToken(@RequestParam("token") String token, @RequestParam("s") Long s) {
        if (stringRedisTemplate.opsForValue().get(token) != null) {
            stringRedisTemplate.delete(token);
        }
        stringRedisTemplate.opsForValue().set(token,"", s, TimeUnit.SECONDS);
        return ApiResponse.success(true);
    }

}
