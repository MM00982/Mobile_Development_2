# Практическая работа №9

## Глава 1. Диаграмма вариантов использования (Use Case)

На рисунке показаны акторы Гость и Авторизованный пользователь, границы системы, внешние сервисы (OpenWeather API, локальная БД, TFLite-модель) и связи include/extend между сценариями.

Ключевые сценарии:

* Просмотр списка городов и страницы города (подробный прогноз).
* Поиск города; голосовой запрос extends «Поиск».
* Синхронизация погоды из API (include в страницу города).
* Авторизация, управление «Избранным» (include сохранение в БД).
* Внешние системы: OpenWeatherMap, локальная БД (Room), TFLite-модель.

![TravelWeather_1](https://github.com/user-attachments/assets/d06e56d3-2dea-441e-80c8-d4b53e210613)

---

## Глава 2. Разделение на слои (Clean Architecture)

Диаграмма иллюстрирует связи Presentation → Domain → Data, интерфейсы репозиториев в домене и их реализации в data-слое, а также источники данных (Remote/Local/ML).

Состав слоёв:
* Presentation: экраны Auth/CityList/CityDetails/Favorites/Settings и их ViewModel’и. Обращаются только к use case.
* Domain: сущности (User, City, WeatherSummary/Forecast), use case’ы (LoginUser, GetCityList, GetForecastByCity, SearchCity, Add/Remove/GetFavorites, RecognizeVoiceToCity, SyncWeatherFromApi), интерфейсы репозиториев (Auth/Weather/Favorites/VoiceRepository).
* Data: реализации репозиториев, источники данных: OpenWeather API (remote), Room (local), TFLite (ML), EncryptedPrefs (secure storage).

![Слои_1](https://github.com/user-attachments/assets/7c0e91a6-4c34-4841-8744-1c2a411cd1ec)

---

## Глава 3. Приложение про фильм (MovieProject)

В этой главе собран учебный мини-проект «MovieProject», демонстрирующий базовый каркас Clean Architecture на простом кейсе «любимый фильм». 
В доменном слое определена сущность Movie и два use-case’а: SaveMovieToFavoriteUseCase и GetFavoriteFilmUseCase. 
Контракт репозитория объявлен в domain.repository.MovieRepository, его реализация находится в data.repository.MovieRepositoryImpl. 
Для хранения данных подключён SharedPreferences (передача Context только в data-слой, домен от Android не зависит). 
Экран содержит поле ввода и две кнопки — сохранить и показать, что позволяет проверить путь данных: Presentation → UseCase → Repository → (SharedPreferences) и обратно.

<img width="2560" height="1539" alt="image_2025-10-04_01-15-30" src="https://github.com/user-attachments/assets/8d05ded7-1f15-402f-acf2-d9d7314300a8" />
<img width="2560" height="1537" alt="image_2025-10-04_01-15-41" src="https://github.com/user-attachments/assets/f4e47c20-4c0c-4533-976d-492c96e27da8" />
<img width="2560" height="1533" alt="image_2025-10-04_01-16-17" src="https://github.com/user-attachments/assets/0ba62665-91d8-4921-a760-7bcf096d4b91" />
<img width="2560" height="1538" alt="image_2025-10-04_01-16-25" src="https://github.com/user-attachments/assets/4c6b6ec0-e209-4006-b46d-eea19c983b35" />

---

## Глава 4. Каркас приложения Travel Weather

Travel Weather — мобильное приложение-ассистент для путешественника. Пользователь быстро находит города, смотрит текущую погоду со значками-иконками 
и открывает подробный прогноз по выбранному городу. Поддерживаются два режима доступа: гость (просмотр погоды) и авторизованный пользователь (все 
возможности гостя + сохранение городов в «Избранное»). Данные о погоде поступают из внешнего сервиса, список избранных 
хранится локально в базе. Для удобства поиска предусмотрен голосовой ввод: встроенная TFLite-модель распознаёт название города (например, 
«Москва») и сразу открывает экран прогноза для него.

Здесь показан «скелет» основного приложения Travel Weather, спроектированного ранее по Use Case и разложенного на три слоя.
Domain содержит сущности (User, City, WeatherNow/Forecast) и use-case’ы: LoginUser, GetCityList, SearchCity, GetForecastByCity, Add/Remove/GetFavorites, GetFavorites, RecognizeVoiceToCity, SyncWeatherFromApi, а также интерфейсы Auth/Weather/Favorites/VoiceRepository.
Data реализует эти контракты и подключает источники: удалённый API (OpenWeatherMap), локальную БД (Room), безопасное хранилище (EncryptedPrefs) и ML-модель (TFLite).
Presentation (экраны Auth, CityList, CityDetails, Favorites, Settings) взаимодействует только с use-case’ами. Авторизованный пользователь наследует все возможности гостя.

<img width="2560" height="1532" alt="image_2025-10-04_01-48-22" src="https://github.com/user-attachments/assets/d9213238-f407-49c4-9245-608e12f575b5" />
<img width="2560" height="1535" alt="image_2025-10-04_01-48-28" src="https://github.com/user-attachments/assets/17ab908d-4792-4d65-b253-b0a97c853976" />
<img width="2560" height="1535" alt="image_2025-10-04_01-47-51" src="https://github.com/user-attachments/assets/0279b9aa-0438-40a8-906b-2a87b98d6ca7" />
<img width="2560" height="1532" alt="image_2025-10-04_01-48-57" src="https://github.com/user-attachments/assets/170a32dc-0aca-4b37-9863-6a99ed499b0d" />
<img width="2560" height="1537" alt="image_2025-10-04_01-49-07" src="https://github.com/user-attachments/assets/126b308f-de7a-47e8-9699-adf059a7ca74" />
<img width="2560" height="1539" alt="image_2025-10-04_01-49-16" src="https://github.com/user-attachments/assets/c209d69c-4a01-48fc-bed7-2e158ad32207" />
<img width="2560" height="1530" alt="image_2025-10-04_01-50-02" src="https://github.com/user-attachments/assets/5e918488-849b-45fe-a89a-807509188814" />
<img width="2560" height="1538" alt="image_2025-10-04_01-50-09" src="https://github.com/user-attachments/assets/6e847fc8-e855-4088-bf7d-ba1b6a8385dd" />
<img width="2560" height="1540" alt="image_2025-10-04_01-50-17" src="https://github.com/user-attachments/assets/6a381b2f-a1d7-4bbc-ad64-3858bcd43dd7" />
<img width="2560" height="1532" alt="image_2025-10-04_01-50-24" src="https://github.com/user-attachments/assets/3b90ece2-e08a-4521-b16b-63386221b541" />

---

## Вывод

В рамках практики №9 спроектирован и собран каркас двух частей: учебного MovieProject и основного приложения Travel Weather. 
Для Travel Weather выполнены: диаграмма вариантов использования, разложение на слои Presentation–Domain–Data, определены сущности и контракты репозиториев в 
домене, реализованы мок-репозитории в data, а также подготовлена dev-панель, покрывающая все ключевые use case’ы (логин, список/поиск городов, прогноз, избранное, 
синхронизация, голосовой ввод). Доменный слой изолирован от Android-зависимостей; взаимодействие с данными идёт через интерфейсы репозиториев, что делает систему 
расширяемой и тестируемой. Для учебного MovieProject показана практическая связка domain-data на примере сохранения/чтения «любимого фильма» через SharedPreferences.

---

**Выполнил**: Мусин М.Р.  
**Группа**: БСБО-09-22
