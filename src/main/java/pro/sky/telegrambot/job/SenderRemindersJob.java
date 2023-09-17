package pro.sky.telegrambot.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramBotSender;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SenderRemindersJob {

    private final Logger logger = LoggerFactory.getLogger(SenderRemindersJob.class);

    private final NotificationTaskRepository notificationTaskRepository;

    private final TelegramBotSender telegramBotSender;

    public SenderRemindersJob(NotificationTaskRepository notificationTaskRepository, TelegramBotSender telegramBotSender) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBotSender = telegramBotSender;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void sendReminders() {

        LocalDateTime currentLocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        logger.info("SenderRemindersJob started for dateTime = {}", currentLocalDateTime);

        List<NotificationTask> notifications =
                notificationTaskRepository.findAllByNotificationDateTime(currentLocalDateTime);

        notifications.forEach(notificationTask -> {
            logger.info("Processing notification task = {}", notificationTask);

            telegramBotSender.send(
                    notificationTask.getChatId(),
                    "Reminder! " + notificationTask.getMassage()
            );
        });

        logger.info("SenderRemindersJob finished, {} notifications have been processed", notifications.size());

    }
}
