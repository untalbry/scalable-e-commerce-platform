package com.binarybrains.userservice.utils.error;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Getter
public class ErrorGlobalMapper {
    private ErrorInfoData errorInfoData;
    private ErrorInfo rn000;
    private ErrorInfo rn001;
    private ErrorInfo rn002;
    private ErrorInfo rn003;
    private ErrorInfo rn004;
    private ErrorInfo rn005;
    private ErrorInfo rn006;
    private ErrorInfo rn007;


    @PostConstruct
    public void init(){
        Optional<ErrorInfoData> loaded = load("error-info.json");
        this.errorInfoData = loaded
                .orElseThrow(() -> new IllegalStateException("No se pudo cargar correctamente error-info.json"));

        if (errorInfoData.getErrorsInfo().isEmpty()) {
            throw new IllegalStateException("El archivo error-info.json no contiene errores definidos.");
        }
        this.rn000 = errorInfoData.getErrorsInfo().get(0);
        this.rn001 = errorInfoData.getErrorsInfo().get(1);
        this.rn002 = errorInfoData.getErrorsInfo().get(2);
        this.rn003 = errorInfoData.getErrorsInfo().get(3);
        this.rn004 = errorInfoData.getErrorsInfo().get(4);
        this.rn005 = errorInfoData.getErrorsInfo().get(5);
        this.rn006 = errorInfoData.getErrorsInfo().get(6);
        this.rn007 = errorInfoData.getErrorsInfo().get(7);

    }

    public static Optional<ErrorInfoData> load(String file){
        ObjectMapper mapper = new ObjectMapper();
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        if (input == null) {
            log.error("Resource '{}' not found on classpath", file);
            return Optional.empty();
        }
        try{
            log.info("Loading {}", file);
            return Optional.of(mapper.readValue(input, ErrorInfoData.class));
        }catch (Exception e){
            log.error("No se pudo leer el archivo '{}': {}", file, e.getMessage(), e);
            return Optional.empty();
        }
    }
}
