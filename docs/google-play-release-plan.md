# LiveOFF — План подготовки к публикации в Google Play

## Статус

| # | Шаг | Кто делает | Статус |
|---|------|-----------|--------|
| 1 | Включить R8 минификацию и написать ProGuard-правила | Claude | done |
| 2 | Добавить signing config в build.gradle.kts | Claude | done |
| 3 | Создать keystore.properties (шаблон) | Claude | done |
| 4 | Создать Privacy Policy | Claude | done |
| 5 | Создать файл keystore (.jks) | Пользователь | todo |
| 6 | Заполнить keystore.properties реальными данными | Пользователь | todo |
| 7 | Создать кастомную иконку приложения | Пользователь | todo |
| 8 | Собрать release AAB | Пользователь | todo |
| 9 | Зарегистрировать Google Play Developer аккаунт | Пользователь | todo |
| 10 | Подготовить материалы для Store Listing | Пользователь | todo |
| 11 | Загрузить AAB и заполнить консоль | Пользователь | todo |

---

## Шаг 1: R8 минификация и ProGuard-правила (Claude)

- Включить `isMinifyEnabled = true` и `isShrinkResources = true` в release-сборке
- Написать ProGuard-правила для Room, Hilt, Kotlinx Serialization, Compose

---

## Шаг 2: Signing config (Claude)

- Добавить `signingConfigs` блок в `app/build.gradle.kts`
- Конфигурация читает данные из `keystore.properties` (файл НЕ коммитится в git)

---

## Шаг 3: keystore.properties шаблон (Claude)

- Создать файл-пример `keystore.properties.example` с плейсхолдерами
- Добавить `keystore.properties` в `.gitignore`

---

## Шаг 4: Privacy Policy (Claude)

- Создать шаблон политики конфиденциальности (markdown)
- Приложение использует: камеру, вибрацию, SMS, звонки
- Данные хранятся только локально на устройстве

---

## Шаг 5: Создать keystore (Пользователь)

Выполнить в терминале:

```bash
keytool -genkey -v -keystore liveoff-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias liveoff
```

Запомните пароль! Потеря keystore = невозможность обновлять приложение.

**Рекомендация:** Сохраните keystore и пароли в надежном месте (НЕ в git-репозитории).

---

## Шаг 6: Заполнить keystore.properties (Пользователь)

Скопировать `keystore.properties.example` -> `keystore.properties` и заполнить:

```properties
storeFile=../liveoff-release.jks
storePassword=ваш_пароль
keyAlias=liveoff
keyPassword=ваш_пароль
```

---

## Шаг 7: Кастомная иконка (Пользователь)

Текущая иконка — стандартный Android-робот. Для Google Play нужна уникальная иконка.

Варианты:
- **Android Studio** → File → New → Image Asset → выбрать изображение
- **Figma / Canva** — нарисовать иконку 512x512 px, затем через Image Asset сгенерировать все размеры
- Adaptive icon: foreground (логотип) + background (цвет/градиент)

Требования Google Play:
- 512x512 px PNG для Store Listing
- Формат adaptive icon (foreground + background) для устройства

---

## Шаг 8: Собрать Release AAB (Пользователь)

В Android Studio:
1. Build → Generate Signed Bundle / APK
2. Выбрать Android App Bundle
3. Указать keystore из шага 5
4. Выбрать release build variant
5. Файл появится в `app/build/outputs/bundle/release/`

Или из командной строки:

```bash
./gradlew bundleRelease
```

---

## Шаг 9: Google Play Developer аккаунт (Пользователь)

1. Перейти на https://play.google.com/console
2. Зарегистрироваться (единовременная плата $25)
3. Заполнить профиль разработчика
4. Подтвердить личность (может занять несколько дней)

---

## Шаг 10: Материалы для Store Listing (Пользователь)

### Обязательные:
- **Название:** LiveOFF (до 30 символов)
- **Краткое описание:** до 80 символов
- **Полное описание:** до 4000 символов
- **Иконка:** 512x512 px PNG
- **Скриншоты:** минимум 2 для телефона (16:9 или 9:16, мин. 320px, макс. 3840px)
- **Feature Graphic:** 1024x500 px (для баннера в магазине)
- **Категория:** Tools или Education
- **Контактный email**

### Рекомендуемое описание (черновик):

**Краткое:** Офлайн-справочник по выживанию с SOS и азбукой Морзе

**Полное:**
```
LiveOFF — карманный справочник по выживанию, который работает без интернета.

Возможности:
- Карточки с инструкциями по выживанию в разных ситуациях
- Чек-листы для подготовки к походам и чрезвычайным ситуациям
- SOS-функция: экстренный звонок и SMS с одной кнопки
- Азбука Морзе: алфавит, тренажер и передатчик (фонарик/звук/вибрация)
- Поиск по всему содержимому
- Избранное и заметки
- Полностью офлайн — все данные на устройстве

Идеально для туристов, путешественников и любителей активного отдыха.
```

### Privacy Policy URL
Разместите файл `PRIVACY_POLICY.md` на GitHub Pages, в Google Docs или на своем сайте. Google Play требует публичную ссылку.

---

## Шаг 11: Загрузка в Google Play Console (Пользователь)

1. Создать приложение в Console
2. Заполнить Store Listing (название, описание, скриншоты, иконка)
3. Заполнить Content Rating (анкета о содержимом — ответ на все вопросы "нет")
4. Настроить Pricing & Distribution (бесплатное, все страны)
5. Указать Privacy Policy URL
6. Загрузить AAB в Production или Internal Testing track
7. Отправить на Review

**Совет:** Сначала загрузите в Internal Testing — проверьте на реальном устройстве через Play Store, затем продвиньте в Production.

---

## Чеклист перед отправкой на ревью

- [ ] Приложение не крашится на основных сценариях
- [ ] Все разрешения обоснованы и запрашиваются в runtime
- [ ] Privacy Policy опубликована и URL указан в консоли
- [ ] Иконка уникальная (не стандартный Android-робот)
- [ ] Скриншоты актуальные и качественные
- [ ] Описание не содержит запрещенных слов/обещаний
- [ ] AAB подписан release-ключом
- [ ] minSdk и targetSdk соответствуют требованиям Google Play (targetSdk >= 34)
- [ ] Content Rating заполнен
