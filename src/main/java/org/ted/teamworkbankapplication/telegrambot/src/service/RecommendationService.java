package org.ted.teamworkbankapplication.telegrambot.src.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class RecommendationService {

    private final List<String> recommendations = List.of(
            "«Только для своих ❤ Лучшие предложения в нашем телеграм-канале...»",
            "Не пропустите нашу новую коллекцию!",
            "Закрытый доступ: особые условия для вас",
            "Свежие тренды, которые точно подойдут для любого повода!!!",
            "Персональная подборка лучших товаров!"
    );

    public String getPersonalizedRecommendation(String username) {
        String baseMessage = String.format("Привет, %s! ", username);
        String randomRecommendation = recommendations.get(
                new Random().nextInt(recommendations.size())
        );
        return baseMessage + randomRecommendation;
    }
}