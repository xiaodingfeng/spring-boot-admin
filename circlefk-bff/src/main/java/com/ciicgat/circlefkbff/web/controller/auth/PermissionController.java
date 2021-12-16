package com.ciicgat.circlefkbff.web.controller.auth;

import com.ciicgat.circlefkbff.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefkbff.common.sdk.lang.utils.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 权限
 */
@RestController
public class PermissionController {

    @Value("classpath:static/permission.json")
    private Resource permissionResource;

    @GetMapping(value = "permission")
    public ApiResponse<JsonNode> permission() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(permissionResource.getInputStream(), StandardCharsets.UTF_8))){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception ignored) {}
        return ApiResponse.success(JSON.parse(content.toString()));
    }
}
