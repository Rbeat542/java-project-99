### Hexlet tests and linter status:
[![Actions Status](https://github.com/Rbeat542/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/Rbeat542/java-project-99/actions)
[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=Rbeat542_java-project-99)](https://sonarcloud.io/summary/new_code?id=Rbeat542_java-project-99)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Rbeat542_java-project-99&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Rbeat542_java-project-99)
Deploy: 

## Проект. Анализатор сайтов.

Task Manager – система управления задачами, подобная http://www.redmine.org/. Она позволяет ставить задачи, назначать исполнителей и менять их статусы. Для работы с системой требуется регистрация и аутентификация

В проекте основное внимание уделяется проектированию моделей через ORM, включая связи между сущностями (один-ко-многим, многие-ко-многим), что упрощает работу с данными. Для автоматизации CRUD-операций применяется ресурсный роутинг, стандартизирующий URL-структуру. Реализуется авторизация для контроля доступа пользователей к ресурсам. Также отрабатывается правильный подход к фильтрации данных в API, избегая сложного кода. Тестирование и мониторинг ошибок в продакшене (например, через Rollbar) обеспечивают стабильность работы. Проект позволяет закрепить эти ключевые аспекты веб-разработки.

## Работа с программой

### Требования

### Запуск
```
$ git clone git@github.com:Rbeat542/java-project-99.git  
$ cd java-projet-99  
$ make run  
http://localhost:8080  
```

