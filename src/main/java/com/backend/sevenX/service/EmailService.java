package com.backend.sevenX.service;

import com.backend.sevenX.utills.Mail;

public interface EmailService {

    void sendForgotPasswordMail(Mail mail);
}
