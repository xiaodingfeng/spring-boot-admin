package com.ciicgat.circlefkbff.feign;

import com.ciicgat.circlefkbff.common.sdk.lang.contants.HeaderConstants;
import com.ciicgat.circlefkbff.common.sdk.lang.contants.VersionConstants;
import com.ciicgat.circlefkbff.common.sdk.lang.convert.ApiResponse;
import com.ciicgat.circlefkbff.common.sdk.lang.exception.BusinessFeignException;
import com.ciicgat.circlefkbff.common.sdk.lang.url.UrlCoder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class StashDecoder implements Decoder{
    private static final Logger LOGGER = LoggerFactory.getLogger(StashDecoder.class);


    private final ObjectMapper mapper;

    StashDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public Object decode(final Response response, final Type type) throws IOException {
        if (response.status() == 404) return Util.emptyValueOf(type);
        if (response.body() == null) return null;
        Reader reader = response.body().asReader();
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader, 1);
        }
        try {
            // Read the first byte to see if we have any data
            reader.mark(1);
            if (reader.read() == -1) {
                return null; // Eagerly returning null avoids "No content to map due to end-of-input"
            }
            reader.reset();

            return decodeFromResponse(response, type, reader);
        } catch (RuntimeJsonMappingException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause(); //NOSONAR
            }
            throw e;
        }
    }


    private Object decodeFromResponse(Response response, Type type, Reader reader) throws IOException {
        final JavaType javaType = mapper.constructType(type);

        String version = getHeader(response.headers(), HeaderConstants.API_VERSION_HEADER);
        if (javaType.getRawClass() == String.class && VersionConstants.V2.equals(version)) {
            return Util.toString(reader);
        }

        final JsonNode rootNode = mapper.readTree(reader);
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass == ApiResponse.class) {
            return toObject(rootNode, javaType);
        }

        if (rawClass == JsonNode.class) {
            return rootNode;
        }

        // 不同版本对象体解析
        if (VersionConstants.V2.equals(version)) {
            String errorCode = getHeader(response.headers(), HeaderConstants.ERROR_CODE_HEADER);
            if (!StringUtils.hasText(errorCode)) {
                throw new RuntimeException("SYS_EX uses wrong feign protocol");
            }

            int code = Integer.parseInt(errorCode);
            String msg = UrlCoder.decode(getHeader(response.headers(), HeaderConstants.ERROR_MSG_HEADER));

            if (code != 0) {
                LOGGER.warn("result is not ok ,code {} msg {}", code, msg);
                throw new BusinessFeignException(response.status(), code, msg);
            }
        } else {
            // 兼容历史逻辑
            JsonNode codeNode = rootNode.get("code");
            if (codeNode != null) {
                int code = codeNode.asInt();
                if (code != 0) {
                    String msg = rootNode.get("msg").asText();
                    LOGGER.warn("result is not ok ,code {} msg {}", code, msg);
                    throw new BusinessFeignException(response.status(), code, msg);
                }
                JsonNode data = rootNode.get("data");
                return toObject(data, javaType);
            }
        }

        return toObject(rootNode, javaType);
    }

    private Object toObject(JsonNode jsonNode, JavaType javaType) throws IOException {
        if (jsonNode == null) {
            return null;
        }
        JsonParser jsonParser = mapper.treeAsTokens(jsonNode);
        return mapper.readValue(jsonParser, javaType);
    }

    private String getHeader(Map<String, Collection<String>> headers, String key) {
        Collection<String> values = headers.get(key);
        if (values != null && !values.isEmpty()) {
            return values.iterator().next();
        }
        return null;
    }
}
