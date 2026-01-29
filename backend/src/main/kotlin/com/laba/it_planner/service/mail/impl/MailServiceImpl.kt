package com.laba.it_planner.service.mail.impl

import com.laba.it_planner.exception.DataException
import com.laba.it_planner.service.mail.MailService
import com.laba.it_planner.utils.feature.FeatureToggleService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class MailServiceImpl(
    @Value("\${spring.mail.properties.thread_count}")
    private val COUNT_OF_THREADS_FOR_MAIL_SERVICE: Int,

    @Value("\${spring.mail.email.from}")
    private val EMAIL_FROM: String,

    @Value("\${spring.mail.path.verification_form}")
    private val PATH_TO_VERIFICATION_FORM_TEMPLATE: String,

    @Value("\${spring.mail.path.information_form}")
    private val PATH_TO_INFORMATION_FORM_TEMPLATE: String,

    val mailSender: JavaMailSender,
    val featureToggleService: FeatureToggleService
) : MailService {

    private lateinit var emailExecutor: ExecutorService

    @PostConstruct
    fun init() {
        emailExecutor = Executors.newFixedThreadPool(COUNT_OF_THREADS_FOR_MAIL_SERVICE);
    }

    private fun fillCodeInTemplate(template: String, code: String): String {
        var result = template
        val codeArray = code.toCharArray()

        for (i in codeArray.indices) {
            result = result.replace("\${code[$i]}", codeArray[i].toString())
        }
        return result
    }

    private fun loadTemplate(relativePath: String): String {
        val resource = ClassPathResource(relativePath)

        return try {
            resource.inputStream.use { inputStream ->
                StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)
            }
        } catch (e: IOException) {
            println("Failed to load template from path: $relativePath \n\n$e")
            throw IllegalArgumentException("Template not found or cannot be read: $relativePath", e)
        }
    }

    override fun sendActivationCodeForm(to: String, code: String) {
        if (featureToggleService.isEnabled("email.sending")) {
            emailExecutor.submit {
                try {
                    val template = loadTemplate(PATH_TO_VERIFICATION_FORM_TEMPLATE)
                    val filledTemplate = fillCodeInTemplate(template, code)

                    val message = mailSender.createMimeMessage()
                    MimeMessageHelper(message, true, "UTF-8").apply {
                        setTo(to)
                        setSubject("Activation code")
                        setFrom(EMAIL_FROM)
                        setText(filledTemplate, true)
                    }

                    mailSender.send(message)
                    println("Activation code email sent to: $to")
                } catch (e: Exception) {
                    println("Failed to send activation code email to: $to \n\n $e")
                    throw DataException("error.mail.failed_to_send", to)
                }
            }
        }
    }

    override fun sendInformationForm(to: String, text: String) {
        if (featureToggleService.isEnabled("email.sending")) {
            emailExecutor.submit {
                try {
                    val template = loadTemplate(PATH_TO_INFORMATION_FORM_TEMPLATE)
                        .replace("\${text}", text)

                    val message = mailSender.createMimeMessage()
                    MimeMessageHelper(message, true, "UTF-8").apply {
                        setTo(to)
                        setSubject("Information")
                        setFrom(EMAIL_FROM)
                        setText(template, true)
                    }

                    mailSender.send(message)
                    println("Information email sent to: $to")
                } catch (e: Exception) {
                    println("Failed to send information email to: $to\n\n$e")
                    throw DataException("errors.mail.failed_to_send", to)
                }
            }
        }
    }


    override fun sendSimpleMail(to: String, subject: String, text: String) {
        if (featureToggleService.isEnabled("email.sending")) {
            emailExecutor.submit {
                try {
                    val message = SimpleMailMessage().apply {
                        setTo(to)
                        this.subject = subject
                        this.text = text
                        from = EMAIL_FROM
                    }

                    mailSender.send(message)
                    println("Email sent successfully to: $to")
                } catch (e: Exception) {
                    println("Failed to send email to: $to \n\n" + e.message)
                }
            }
        }
    }

    @PreDestroy
    private fun shutdownExecutor() {
        emailExecutor.shutdown();
    }
}