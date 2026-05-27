package com.lefesafety.liveoff.domain.morse

class MorseCodec {

    private val charToMorse = mapOf(
        'А' to ".-",     'Б' to "-...",   'В' to ".--",    'Г' to "--.",
        'Д' to "-..",    'Е' to ".",      'Ж' to "...-",   'З' to "--..",
        'И' to "..",     'Й' to ".---",   'К' to "-.-",    'Л' to ".-..",
        'М' to "--",     'Н' to "-.",     'О' to "---",    'П' to ".--.",
        'Р' to ".-.",    'С' to "...",    'Т' to "-",      'У' to "..-",
        'Ф' to "..-.",   'Х' to "....",   'Ц' to "-.-.",   'Ч' to "---.",
        'Ш' to "----",   'Щ' to "--.-",   'Ъ' to "--.--",  'Ы' to "-.--",
        'Ь' to "-..-",   'Э' to "..-..",  'Ю' to "..--",   'Я' to ".-.-",
        'Ё' to ".",
        '0' to "-----",  '1' to ".----",  '2' to "..---",  '3' to "...--",
        '4' to "....-",  '5' to ".....",  '6' to "-....",  '7' to "--...",
        '8' to "---..",  '9' to "----.",
        'S' to "...",    'O' to "---"
    )

    private val morseToChar = charToMorse.entries
        .groupBy({ it.value }, { it.key })
        .mapValues { (_, chars) -> chars.first() }

    fun encode(text: String): String =
        text.uppercase()
            .split(" ")
            .joinToString(" / ") { word ->
                word.mapNotNull { char -> charToMorse[char] }
                    .joinToString(" ")
            }

    fun decode(morse: String): String =
        morse.split(" / ").joinToString(" ") { word ->
            word.split(" ").mapNotNull { code -> morseToChar[code] }.joinToString("")
        }

    fun getAlphabet(): List<Pair<Char, String>> =
        charToMorse.entries
            .filter { it.key in 'А'..'Я' || it.key == 'Ё' || it.key in '0'..'9' }
            .map { it.key to it.value }
            .sortedBy { (char, _) ->
                when {
                    char in 'А'..'Я' -> char.code
                    char == 'Ё' -> 'Е'.code + 1
                    else -> char.code + 10000
                }
            }
}
