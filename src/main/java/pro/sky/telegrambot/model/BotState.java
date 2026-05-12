
package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bot_users_state")
public class BotState {
    @Id
    private Long chatId;
    private boolean firstTime = true;

    public BotState() {}
    public BotState(Long chatId) { this.chatId = chatId; }

    public boolean isFirstTime() { return firstTime; }
    public void setFirstTime(boolean firstTime) { this.firstTime = firstTime; }
}