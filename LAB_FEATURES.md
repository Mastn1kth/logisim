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

## Packaging fixes in `5.0.0-lab2`

- Both Intel and Apple Silicon macOS applications receive an ad-hoc signature.
- Release automation verifies the macOS signature and bundled runtime before publishing.
- Windows installer and portable archive names consistently include the Lab version.

The checker is non-destructive: it reads the current circuit and never changes components, wires,
or saved project files. Some component inputs are optional by design, so an unconnected input is a
warning rather than an error.

## Installation notes

- Every Windows and macOS package is self-contained and includes its own Java runtime. Installing
  Java separately is not required.
- Choose `aarch64` for an Apple Silicon Mac (M1/M2/M3/M4 and newer), or `x86_64` for an Intel Mac.
- The macOS applications have an ad-hoc signature, but are not Apple-notarized. On first launch,
  macOS may therefore require **Control-click → Open → Open**. Official notarization requires an
  Apple Developer account and certificate, which this community build does not have.

---

## Что добавлено в Logisim 5.0.0 Lab

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

## Исправления упаковки в `5.0.0-lab2`

- Приложения для Intel и Apple Silicon получают ad-hoc подпись.
- Перед публикацией автоматика проверяет подпись macOS и наличие встроенной Java.
- В названиях установщика и портативного архива Windows теперь всегда указана Lab-версия.

Проверка безопасна для проекта: она только читает текущую схему и не изменяет компоненты, провода
или сохранённый файл. Некоторые входы могут быть необязательными, поэтому свободный вход считается
предупреждением, а не ошибкой.

## Установка

- Все пакеты для Windows и macOS автономны и уже содержат Java. Устанавливать Java отдельно не
  требуется.
- Для Mac с Apple Silicon (M1/M2/M3/M4 и новее) выбирайте `aarch64`, для Intel Mac — `x86_64`.
- Приложения для macOS имеют локальную ad-hoc подпись, но не нотариализованы Apple. Поэтому при
  первом запуске может понадобиться **Control-click → Open → Open** («Открыть»). Для официальной
  нотариализации нужны платная учётная запись и сертификат Apple Developer, которых у этой
  общественной сборки нет.
