Библиотека postman
https://api.getpostman.com/collections/49197979-dc04cdcc-bc90-4ede-bdad-5df3f295eadc

from pathlib import Path

readme_content = """# 📚 Library Management System

Проект представляет собой REST API для управления библиотекой.  
Реализованы CRUD-операции для авторов, книг, читателей и выдач книг, а также дополнительные бизнес-операции.



##  Основные функции

- 📖 Управление **книгами**
- ✍️ Управление **авторами**
- 👥 Управление **читателями**
- 🔄 Управление **выдачами книг**
- ⚙️ Бизнес-операции:
    - Выдача книги читателю
    - Возврат книги
    - Просмотр просроченных выдач
    - Получение активных выдач
    - Поиск книг по автору или названию



##  Тема проекта

**Система управления библиотекой**

Позволяет библиотекарю:
- Добавлять и редактировать книги и авторов
- Регистрировать читателей
- Оформлять выдачу и возврат книг
- Контролировать сроки возврата



##  Используемые технологии

- **Java 17**
- **Spring Boot 3**
- **Spring Web / Data JPA**
- **Hibernate**
- **PostgreSQL** (основная база данных)
- **H2** (in-memory БД для тестов)
- **Swagger / OpenAPI** для документации
- **Lombok** для упрощения кода
- **Docker Compose** для контейнеризации базы данных

---

##  Основные сущности

| Сущность | Описание |
|-----------|-----------|
| **Author** | Автор книги (имя, биография) |
| **Book** | Книга (название, год издания, наличие, ссылка на автора) |
| **Reader** | Читатель (имя, email, телефон) |
| **Loan** | Выдача книги (книга, читатель, даты выдачи и возврата) |

###  Связи между таблицами
- `Book` → `Author` : Many-to-One
- `Loan` → `Book` : Many-to-One
- `Loan` → `Reader` : Many-to-One

---

##  Настройки базы данных

Файлы конфигурации:
- `application-h2.properties` — для локального тестирования
- `application-postgres.properties` — для работы с PostgreSQL
- `application-supabase.properties` — для облачной БД (Supabase)

Активный профиль задаётся в `application.properties`:
```properties
spring.profiles.active=postgres

###

## СБОРКА И ЗАПУСК ПРОЕКТА

# Клонируем проект
git clone https://github.com/akula5561/laba2-rbpo.git
cd laba2-rbpo

# Запуск PostgreSQL через Docker
docker-compose up -d

# Сборка проекта
./mvnw clean package

# Запуск Spring Boot приложения
./mvnw spring-boot:run

После запуска API доступно по адресу:
 http://localhost:8080/api


Примеры запросов (Postman)
 Автор
Создать автора
Всегда показывать подробности
POST /api/authors
{
  "name": "J.K. Rowling",
  "bio": "British writer"
}
Получить всех авторов
Всегда показывать подробности
GET /api/authors
Изменить автора
Всегда показывать подробности
PUT /api/authors/1
{
  "name": "Joanne Rowling",
  "bio": "Updated bio"
}

 Книга
Создать книгу
Всегда показывать подробности
POST /api/books
{
  "title": "Harry Potter and the Philosopher's Stone",
  "publishedYear": 1997,
  "available": true,
  "authorId": 1
}
Получить все книги
Всегда показывать подробности
GET /api/books
Найти книги по автору
Всегда показывать подробности
GET /api/books/query/author/1

 Читатель
Создать читателя
Всегда показывать подробности
POST /api/readers
{
  "name": "Ivan Ivanov",
  "email": "ivan@mail.ru",
  "phone": "+79999999999"
}
Получить всех читателей
Всегда показывать подробности
GET /api/readers

 Выдачи (Loan)
Выдать книгу
Всегда показывать подробности
POST /api/loans
{
  "bookId": 1,
  "readerId": 1,
  "loanDate": "2025-11-07",
  "dueDate": "2025-11-14"
}
Вернуть книгу
Всегда показывать подробности
PATCH /api/loans/1/return
Проверить просрочки
Всегда показывать подробности
GET /api/loans/overdue

 Бизнес-операции (5 примеров)
1. Оформление выдачи книги (POST /api/loans)
2. Возврат книги (PATCH /api/loans/{id}/return)
3. Проверка просроченных выдач (GET /api/loans/overdue)
4. Поиск всех книг определённого автора (GET /api/books/query/author/{id})
5. Список всех активных выдач (GET /api/loans/active)

Коллекция Postman
Коллекция содержит:
* CRUD-запросы по всем сущностям
* 5 бизнес-операций
* Переменные окружения (host, port, basePath=/api)

 Проверка сценария
 1. Создать автора  2. Добавить книгу этому автору  3. Создать читателя  4. Выдать книгу  5. Проверить выдачу и вернуть книгу  6. Убедиться, что книга снова доступна  7. Проверить просрочки
 
 

 Работа с базой данных PostgreSQL
 Подключение к PostgreSQL

psql -U postgres
Если ты используешь Postgres.app (на macOS), просто открой её и введи:

psql -p5432 -U danilparfenov

 Создание базы данных и пользователя

-- Создание базы данных
CREATE DATABASE library;

-- Создание пользователя (если его ещё нет)
CREATE ROLE library WITH LOGIN PASSWORD 'library';

-- Назначение прав пользователю
GRANT ALL PRIVILEGES ON DATABASE library TO library;

 Подключение к нужной БД

\c library

 Настройка прав и схемы

-- Разрешаем пользователю доступ к схеме public
GRANT ALL ON SCHEMA public TO library;

-- Даём права на создание таблиц
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO library;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON SEQUENCES TO library;

 Проверка существующих таблиц

\dt
Вывод покажет, например:

 public | authors | table | library
 public | books   | table | library
 public | loans   | table | library
 public | readers | table | library

 Просмотр данных в таблице

SELECT * FROM authors;
SELECT * FROM books;
SELECT * FROM readers;
SELECT * FROM loans;

 Примеры SQL-запросов

-- Добавить автора
INSERT INTO authors (name, bio)
VALUES ('J.K. Rowling', 'British writer');

-- Добавить книгу
INSERT INTO books (title, published_year, available, author_id)
VALUES ('Harry Potter and the Chamber of Secrets', 1998, true, 1);

-- Добавить читателя
INSERT INTO readers (name, email, phone)
VALUES ('Ivan Ivanov', 'ivan@mail.ru', '+79999999999');

-- Зарегистрировать выдачу книги
INSERT INTO loans (book_id, reader_id, loan_date, due_date)
VALUES (1, 1, CURRENT_DATE, CURRENT_DATE + INTERVAL '7 day');

-- Проверить просроченные выдачи
SELECT * FROM loans WHERE due_date < CURRENT_DATE AND return_date IS NULL;

 Полезные команды psql
Команда	Назначение
\l	 Показать все базы данных
\c db_name	Подключиться к БД
\dt	 Показать таблицы
\d table_name	 Структура таблицы
\du	 Список пользователей
\q	 Выйти из psql

 Удаление БД и пользователя (если нужно)

DROP DATABASE library;
DROP ROLE library;

Автор работы;
Парфенов Данил
Группа; БАС2301
 
