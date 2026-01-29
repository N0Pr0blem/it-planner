package com.laba.it_planner.utils.files

import java.util.Locale

enum class MimeType(
    val extensions: Set<String>,
    val mime: String
) {
    // Документы
    DOC(setOf("doc"), "application/msword"),
    DOCX(setOf("docx"), "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLS(setOf("xls"), "application/vnd.ms-excel"),
    XLSX(setOf("xlsx"), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PPT(setOf("ppt"), "application/vnd.ms-powerpoint"),
    PPTX(setOf("pptx"), "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    ODT(setOf("odt"), "application/vnd.oasis.opendocument.text"),

    // Изображения
    JPEG(setOf("jpg", "jpeg", "jpe", "jfif"), "image/jpeg"),
    PNG(setOf("png"), "image/png"),
    GIF(setOf("gif"), "image/gif"),
    BMP(setOf("bmp", "dib"), "image/bmp"),
    WEBP(setOf("webp"), "image/webp"),
    SVG(setOf("svg"), "image/svg+xml"),
    ICO(setOf("ico"), "image/x-icon"),
    TIFF(setOf("tiff", "tif"), "image/tiff"),
    HEIC(setOf("heic", "heif"), "image/heic"),
    AVIF(setOf("avif"), "image/avif"),

    // PDF
    PDF(setOf("pdf"), "application/pdf"),

    // Текстовые
    TXT(setOf("txt", "text", "log", "ini", "cfg", "conf"), "text/plain"),
    MARKDOWN(setOf("md", "markdown", "mdown"), "text/markdown"),
    CSV(setOf("csv"), "text/csv"),
    TSV(setOf("tsv"), "text/tab-separated-values"),
    JSON(setOf("json"), "application/json"),
    XML(setOf("xml"), "application/xml"),
    YAML(setOf("yml", "yaml"), "application/x-yaml"),
    HTML(setOf("html", "htm"), "text/html"),
    CSS(setOf("css"), "text/css"),

    // Код
    JAVASCRIPT(setOf("js", "mjs", "cjs"), "application/javascript"),
    TYPESCRIPT(setOf("ts"), "application/typescript"),
    JAVA(setOf("java"), "text/x-java-source"),
    KOTLIN(setOf("kt", "kts"), "text/x-kotlin"),
    PYTHON(setOf("py", "pyc", "pyo"), "text/x-python"),
    C(setOf("c", "h"), "text/x-c"),
    CPP(setOf("cpp", "cc", "cxx", "hpp", "hh", "hxx"), "text/x-c++"),
    PHP(setOf("php", "php3", "php4", "php5", "php7", "phps"), "application/x-php"),
    RUBY(setOf("rb"), "application/x-ruby"),
    GO(setOf("go"), "text/x-go"),
    RUST(setOf("rs"), "text/x-rust"),
    SQL(setOf("sql"), "application/sql"),

    // Архивы
    ZIP(setOf("zip"), "application/zip"),
    RAR(setOf("rar"), "application/x-rar-compressed"),
    TAR(setOf("tar"), "application/x-tar"),
    GZ(setOf("gz", "gzip"), "application/gzip"),
    SEVEN_Z(setOf("7z"), "application/x-7z-compressed"),

    // Аудио
    MP3(setOf("mp3"), "audio/mpeg"),
    WAV(setOf("wav"), "audio/wav"),
    OGG(setOf("ogg", "oga"), "audio/ogg"),
    FLAC(setOf("flac"), "audio/flac"),
    AAC(setOf("aac"), "audio/aac"),

    // Видео
    MP4(setOf("mp4"), "video/mp4"),
    AVI(setOf("avi"), "video/x-msvideo"),
    MKV(setOf("mkv"), "video/x-matroska"),
    MOV(setOf("mov"), "video/quicktime"),
    WEBM(setOf("webm"), "video/webm"),

    // Прочие
    UNKNOWN(setOf(), "application/octet-stream");


    companion object{
        fun getByExtension(name: String): MimeType {
            for (type in MimeType.entries) {
                if (type.extensions.contains(name.lowercase(Locale.getDefault()))) {
                    return type
                }
            }
            return UNKNOWN
        }

        fun getByFileName(fileName: String): MimeType {
            val extension = fileName.substringAfterLast('.').lowercase()
            return getByExtension(extension)
        }
    }

}