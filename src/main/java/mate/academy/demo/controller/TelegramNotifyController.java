package mate.academy.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.demo.service.TelegramService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/telegram")
@Tag(name = "Telegram", description = "Endpoint for Telegram integration and notifications")
public class TelegramNotifyController {
    private final TelegramService telegramService;

    @Operation(summary = "Get Telegram info",
            description = "Returns information related"
                    + " to the Telegram bot integration")
    @GetMapping("info")
    public String getInfo() {
        return telegramService.getInfo();
    }
}
