# Logisim 5.0.0 Lab additions

This fork keeps the upstream Logisim-evolution 5.0.0 simulator and adds the same classroom-focused
features to the Windows and macOS builds.

The application is displayed simply as **Logisim**, without “Evolution” in its name. The upstream
project and its developers remain credited in the documentation and About dialog.

## Added in `5.0.0-lab1`

- **Project → Check Circuit** (`Проект → Проверить схему`).
- A bottom **Problems** (`Проблемы`) panel.
- Detection of incompatible bit widths, shown as errors.
- Detection of unconnected component inputs, shown as warnings.
- Detection of dangling wire ends, shown as warnings.
- Double-click navigation from a problem to its location on the circuit canvas.
- English and Russian interface text for the new feature.

The checker is non-destructive: it reads the current circuit and never changes components, wires,
or saved project files. Some component inputs are optional by design, so an unconnected input is a
warning rather than an error.

---

# Что добавлено в Logisim 5.0.0 Lab

Эта ветка сохраняет симулятор Logisim-evolution 5.0.0 и добавляет одинаковые учебные функции в
сборки для Windows и macOS.

В интерфейсе приложение называется просто **Logisim**, без слова «Evolution». При этом ссылка на
исходный проект и авторство разработчиков сохранены в документации и окне «О программе».

## Добавлено в `5.0.0-lab1`

- Пункт **«Проект → Проверить схему»**.
- Нижняя панель **«Проблемы»**.
- Поиск несовместимой разрядности — отмечается как ошибка.
- Поиск неподключённых входов компонентов — отмечается как предупреждение.
- Поиск висящих концов проводов — отмечается как предупреждение.
- Переход к проблемному месту схемы двойным щелчком.
- Английский и русский текст нового интерфейса.

Проверка безопасна для проекта: она только читает текущую схему и не изменяет компоненты, провода
или сохранённый файл. Некоторые входы могут быть необязательными, поэтому свободный вход считается
предупреждением, а не ошибкой.
