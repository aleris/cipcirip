package ro.cipcirip.model

import androidx.room.TypeConverter

enum class Language {
    Rom,
    Eng,
    Lat;

    companion object {
        val Default = Rom
    }
}

class LanguageConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromLanguage(value: Language) = value.ordinal

        @TypeConverter
        @JvmStatic
        fun toLanguage(value: Int) = enumValues<Language>()[value]
    }
}
