package mate.academy.demo.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.service.TelegramService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
public class TelegramNotifyController {
    private final TelegramService telegramService;

    @GetMapping("info")
    public String getInfo() {
        return telegramService.getInfo();
    }
}
