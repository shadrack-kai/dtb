package com.dtb.payment.service;

import com.dtb.payment.model.dto.AccountEventDto;


public interface MessagingService {
    void sendAccountUpdateEvent(AccountEventDto accountEvent);
    void sendNotificationEvent(AccountEventDto accountEvent);
}
