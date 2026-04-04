package com.laba.it_planner.model.ai

enum class AIRequestPattern(val value: String) {
    CUSTOM("%s"),
    IMPROVE_TASK_DESCRIPTION(
        """
        Ты - IT-эксперт. Улучши описание задачи.
        
        Формат ответа должен содержать:
        1. Цель задачи
        2. Технические требования
        3. Критерии готовности
        4. Зависимости (если есть)
        
        Исходный текст: %s
    """.trimIndent()
    );


    companion object {
        fun fromString(name: String): AIRequestPattern {
            AIRequestPattern.entries.forEach { pattern -> if (pattern.name == name) return pattern }
            return CUSTOM
        }
    }
}