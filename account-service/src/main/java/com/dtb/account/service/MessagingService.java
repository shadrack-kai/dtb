package com.dtb.account.service;


import com.dtb.account.model.dto.AccountEventDto;

public interface MessagingService {
    void sendAccountUpdateEvent(AccountEventDto accountEvent);
}
