package server.api.kiwes.domain.translate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.api.kiwes.domain.translate.dto.TranslateRequsetDto;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateException;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TranslateService {
    public String translate(TranslateRequsetDto requset) {
        Region region = Region.AP_NORTHEAST_2;
        TranslateClient translateClient = TranslateClient.builder()
                .region(region)
                .build();

        String result = translateText(translateClient, requset.getSource(), requset.getTarget(), requset.getText());
        translateClient.close();
        return result;
    }

    public static String translateText(TranslateClient translateClient, String sourceLang, String targetLang, String text) {
        try {
            TranslateTextRequest textRequest = TranslateTextRequest.builder()
                    .sourceLanguageCode(sourceLang)
                    .targetLanguageCode(targetLang)
                    .text(text)
                    .build();

            TranslateTextResponse textResponse = translateClient.translateText(textRequest);
            return textResponse.translatedText();

        } catch (TranslateException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
}
