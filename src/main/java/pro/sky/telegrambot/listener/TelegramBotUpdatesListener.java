package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.BotState;
import pro.sky.telegrambot.model.ProductEntity;
import pro.sky.telegrambot.model.UserEntity;
import pro.sky.telegrambot.repository.BotStateRepository;
import jakarta.annotation.PostConstruct;
import pro.sky.telegrambot.repository.UserRepository;

import java.util.List;
@Service
public class TelegramBotUpdatesListener <Users> implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final String RECOMMEND_COMMAND = "/recommend ";

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BotStateRepository botStateRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            try {
                if (update.message() == null || update.message().text() == null) continue;
                Long chatId = update.message().chat().id();
                String text = update.message().text();
                if (isFirstTime(chatId)) {
                    sendHelp(chatId);
                    continue;
                }
                if (text.startsWith(RECOMMEND_COMMAND)) {
                    String query = text.substring(RECOMMEND_COMMAND.length()).trim();
                    handleRecommend(chatId, query);
                }
            } catch (Exception e) {
                logger.error("Ошибка обработки: ", e);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private boolean isFirstTime(Long chatId) {
        BotState state = botStateRepository.findById(chatId).orElse(new BotState(chatId));
        if (state.isFirstTime()) {
            state.setFirstTime(false);
            botStateRepository.save(state);
            return true;
        }
        return false;
    }
    private void handleRecommend(Long chatId, String query) {
        List<UserEntity> foundUsers = userRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCase(query, query);


        if (foundUsers.size() == 1) {
            UserEntity user = foundUsers.get(0);
            sendMessage(chatId, formatResponse(user));
        } else {
            sendMessage(chatId, "Пользователь не найден");
        }
    }

    private String formatResponse(UserEntity user) {
        StringBuilder sb = new StringBuilder();
        sb.append("Здравствуйте ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("nn");
        sb.append("Новые продукты для вас:n");
        if (user.getRecommendations() != null) {
            for (ProductEntity product : user.getRecommendations()) {
                sb.append("• ").append(product.getName()).append("n");
            }
        }
        return sb.toString();
    }
    private void sendHelp(Long chatId) {
        String help = "👋 Здравствуйте! Я бот-рекомендатель.nn" +
                "Чтобы получить список предложений, используйте команду:n" +
                "/recommend <имя_или_фамилия>";
        sendMessage(chatId, help);
    }
    private void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
