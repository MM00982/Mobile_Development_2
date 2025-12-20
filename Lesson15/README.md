# Практическая работа №15

## Глава 1. Принципы навигации (Проект NavigationApp)

В рамках данной работы было разработано приложение NavigationApp, демонстрирующее различные подходы к организации навигации в Android.

<img width="2560" height="1534" alt="image_2025-12-20_03-42-21" src="https://github.com/user-attachments/assets/667c74ac-f940-46ba-8f13-153ab6d026ec" />

### 1.1. Fragment Manager (Ручное управление)

Реализован классический подход с использованием FragmentManager и транзакций (beginTransaction, replace, commit). 
В Activity размещены кнопки, по нажатию на которые происходит подмена фрагмента в контейнере FrameLayout. 
Это базовый способ, требующий ручного управления стеком и состоянием.

<img width="2560" height="1532" alt="image_2025-12-20_03-42-31" src="https://github.com/user-attachments/assets/ca0cf7f1-c903-4416-b2f1-5f28cdd92ad7" />
<img width="2560" height="1539" alt="image_2025-12-20_03-42-38" src="https://github.com/user-attachments/assets/fec08dbb-ec95-45f5-af4c-6755c58854a4" />

### 1.2. Bottom Navigation

Реализована навигация с нижней панелью. Использован компонент BottomNavigationView в связке с 
Android Navigation Component. Создан граф навигации (nav_graph.xml), где определены фрагменты 
(Home, Info, Profile). Контроллер навигации (NavController) автоматически обрабатывает переходы при 
нажатии на иконки внизу экрана.

<img width="2560" height="1534" alt="image_2025-12-20_03-42-49" src="https://github.com/user-attachments/assets/2894d600-c4b6-4c1e-9a78-2a7960783694" />
<img width="2560" height="1540" alt="image_2025-12-20_03-42-55" src="https://github.com/user-attachments/assets/f0c0599d-d649-4ec5-b918-87e36723250c" />

### 1.3. Navigation Drawer (Боковая шторка)

Реализовано боковое меню ("гамбургер"). Использован DrawerLayout и NavigationView. Меню связано с тем 
же графом навигации, что позволяет переиспользовать фрагменты. Настроена синхронизация состояния шторки 
и кнопки в Toolbar с помощью AppBarConfiguration.

<img width="2560" height="1535" alt="image_2025-12-20_03-43-08" src="https://github.com/user-attachments/assets/398f63cb-3448-4738-9219-3ded11406948" />
<img width="2560" height="1542" alt="image_2025-12-20_03-43-15" src="https://github.com/user-attachments/assets/b9b1082a-9b69-41c5-8302-824d6c6525fd" />

---

## Глава 2. Контрольное задание (Travel Weather)

В основном приложении «Travel Weather» была выполнена полная миграция навигации на Jetpack Navigation Component.

Реализация:

1. Single Activity: Приложение переведено на архитектуру с одной Activity (MainActivity), которая содержит NavHostFragment и BottomNavigationView.
2. NavGraph: Создан навигационный граф, описывающий все экраны (Login, Home, Weather, Profile) и связи между ними (Actions).
3. Bottom Navigation: Внедрена настоящая нижняя навигационная панель (вместо имитации LinearLayout). Реализована логика скрытия панели на экранах входа и детального прогноза.
4. Безопасная навигация: Все переходы (например, от списка городов к прогнозу) осуществляются через NavController с передачей аргументов (Bundle) безопасным способом. Настроена работа кнопки "Назад" и очистка стека при выходе из профиля.

<img width="2560" height="1537" alt="image_2025-12-20_03-50-58" src="https://github.com/user-attachments/assets/c6b85b00-eb30-417e-b998-f8d9a695e33f" />
<img width="2560" height="1540" alt="image_2025-12-20_03-51-05" src="https://github.com/user-attachments/assets/4dbba45a-0d20-410d-b880-96a5ee5e67b0" />
<img width="2560" height="1537" alt="image_2025-12-20_03-51-15" src="https://github.com/user-attachments/assets/d4291576-4e02-434b-b084-eafcb00b3944" />

---

## Вывод

В ходе работы изучены современные подходы к навигации. Использование Navigation Component позволило 
значительно упростить код (избавившись от ручных транзакций фрагментов), визуализировать связи между 
экранами в редакторе графов и стандартизировать анимации переходов. Проект Travel Weather теперь обладает 
профессиональной навигационной структурой, соответствующей гайдлайнам Material Design.

---

**Выполнил**: Мусин М.Р.  
**Группа**: БСБО-09-22
