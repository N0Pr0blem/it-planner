package com.laba.it_planner.service.mail

interface MailService {
    fun sendSimpleMail(to: String, subject: String, text: String)

    fun sendActivationCodeForm(to: String, code: String)

    fun sendInformationForm(to: String, text: String)
}