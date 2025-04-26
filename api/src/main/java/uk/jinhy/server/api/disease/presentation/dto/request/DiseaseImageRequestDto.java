package uk.jinhy.server.api.disease.presentation.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DiseaseImageRequestDto {
    private MultipartFile image;
}
