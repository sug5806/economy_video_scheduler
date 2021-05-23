package com.example.economy_scheduler.service;

import com.example.economy_scheduler.entity.EconomyVideo;
import com.example.economy_scheduler.entity.ScheduleChannel;
import com.example.economy_scheduler.repository.EconomyVideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private static WebDriver webDriver;
    private static JavascriptExecutor js;
    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static String WEB_DRIVER_PATH = "/home/sug5806/chromedriver";
    private final EconomyVideoRepository economyVideoRepository;

    @PostConstruct
    private void initasdf() {
        setUp();
    }

    private void setUp() {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--no-sandbox");

        webDriver = new ChromeDriver(chromeOptions);
        webDriver.manage().window().maximize();

        js = (JavascriptExecutor) webDriver;

    }

    private void setDown() {
        webDriver.quit();
    }

    public void economyVideoScheduling(ScheduleChannel scheduleChannel) {
        calculate(scheduleChannel);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void calculate(ScheduleChannel scheduleChannel) {
        webDriver.get(scheduleChannel.getChannelUrl());

        try {
            long lastHeight = (long) js.executeScript(getScrollHeight());

            while (true) {

                js.executeScript(moveScroll());
                Thread.sleep(2000);

                long newHeight = (long) js.executeScript(getScrollHeight());

                if (newHeight == lastHeight) {
                    break;
                }

                lastHeight = newHeight;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> elements = webDriver.findElements(By.cssSelector("#contents #items .ytd-grid-renderer #dismissible"));

        log.info(String.format("%s 채널 업데이트 중 ===============================", scheduleChannel.getChannelName()));
        if (scheduleChannel.getEconomyVideoList().size() < 1) {
            log.warn("데이터가 비어있어서 전부 추가");
            saveAllVideo(elements, scheduleChannel);
        } else {
            if (!getTitleElement(elements.get(0)).getText().equals(scheduleChannel.getEconomyVideoList().get(0).getTitle())) {
                int count = elements.size() - scheduleChannel.getEconomyVideoList().size();
                log.error("새로운 데이터가 있어 그 데이터만 추가");
                for (int index = 0; index < count; index++) {
                    saveVideo(elements.get(index), scheduleChannel);
                }
            }

            log.info("업데이트 할 데이터 없음");
        }
        log.info(String.format("%s 채널 업데이트 끝 ===============================", scheduleChannel.getChannelName()));
    }

    private void saveVideo(WebElement element, ScheduleChannel scheduleChannel) {
        WebElement titleElement = getTitleElement(element);
        String url = titleElement.getAttribute("href");
        String title = titleElement.getText();

        String thumbnailUrl = getImgThumbnailUrl(url);

        System.out.println(title + "   " + url + "  " + thumbnailUrl);

        EconomyVideo video = EconomyVideo
                .builder()
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .url(url)
                .createdAt(LocalDateTime.now())
                .build();

        video.mappingChannel(scheduleChannel);

        economyVideoRepository.save(video);
    }

    private void saveAllVideo(List<WebElement> elements, ScheduleChannel scheduleChannel) {
        Collections.reverse(elements);
        for (WebElement element : elements) {
            saveVideo(element, scheduleChannel);
        }
    }

    private WebElement getTitleElement(WebElement element) {
        return element.findElement(By.cssSelector("#dismissible #meta h3 #video-title"));
    }

    private String getImgThumbnailUrl(String href) {
        String[] split = href.split("=");

        return String.format("https://img.youtube.com/vi/%s/0.jpg", split[1]);
    }

    private String moveScroll() {
        return "window.scrollTo(0, document.documentElement.scrollHeight);";
    }

    private String getScrollHeight() {
        return "return document.documentElement.scrollHeight";
    }
}
