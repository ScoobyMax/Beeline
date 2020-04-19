<!--
![Java CI with Maven](https://github.com/ScoobyMax/BeeLine/workflows/Java%20CI%20with%20Maven/badge.svg)
[![Build Status](https://travis-ci.com/scoobymax/beeline.svg)](https://travis-ci.com/ScoobyMax/beeline)
-->
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ScoobyMax_Beeline&metric=alert_status)](https://sonarcloud.io/dashboard?id=ScoobyMax_Beeline)

# ProfileService
[![docker build](https://img.shields.io/docker/cloud/build/scoobymax/profileservice)](https://cloud.docker.com/u/ScoobyMax/repository/docker/scoobymax/profileservice)
[![codecov](https://codecov.io/gh/ScoobyMax/profileservice/branch/master/graph/badge.svg)](https://codecov.io/gh/ScoobyMax/profileservice)


# DetailService
[![docker build](https://img.shields.io/docker/cloud/build/scoobymax/detailservice)](https://cloud.docker.com/u/ScoobyMax/repository/docker/scoobymax/detailservice)
[![codecov](https://codecov.io/gh/ScoobyMax/detailservice/branch/master/graph/badge.svg)](https://codecov.io/gh/ScoobyMax/detailservice)



# License
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


# BeeLine
Инструкция по сборке и запуску:

для сборки образа(нужен установленный docker и docker-compose) 
для сборки проекта достаточно выполнить 
- mvn clean install

далее для запуска
- docker-compose up

Сервис поиска абонетов по базовой станции будет доступен по адресу 
- http://localhost:80/data/{cell_id}


Задание

-	Необходимо разработать web-сервис, который по номеру базовой станции будет отдавать список абонентов, которые в данный момент находятся на ней. Помимо номера телефона в ответе должен присутствовать профиль абонента. 

Компоненты
1.	База данных (DB). В базе данных хранится связка номера базовой станции(Cell ID) с номером абонента (CTN). Связь один ко многим.  
И таблица abonentid Содержащая информацию об ID абонента (связка ctn и uuid). Связь один к одному.  
2.	Web-сервис (ProfileService) который по номеру абонента отдает его профиль 
Для этого необходимо использовать сервис https://randomuser.me , который генерирует фейковые профили пользователей. 
Пример запроса: 
   curl "https://randomuser.me/api/?phone=1111111111&inc=name,email", где “name,email” список необходимых полей, а 1111111111 номер абонента
3.	Web-сервис (DetailService) который по номеру базовой станции отдает список абонентов с их профилями

Что требуется от кандидата:
-	Развернуть  БД с таблицей sessions  и таблицей abonentid (mysql или postgresql) 
-	Создать таблицу sessions и загрузить в нее файл sessions.csv
-	Создать таблицу abonentid и загрузить в нее тестовую информацию (ctn ->abonent_id) для каждого ctn (данные сгенерировать самостоятельно).
-	Создать web-сервис DetailService, который будет:
-	Принимать GET запрос с номером Cell ID 
-	Сервис идет в БД и получает по переданному Cell ID список CNT-ов
-	С полученным списком CTN-ов необходимо сходить во внешний сервис ProfileService (в лице https://randomuser.me) и получить по каждому номеру профиль
-	По полученному списку CTN-ов необходимо выполнить запросы к базе  к табличке abonentid для получения uuid по ctn.
-	Результат необходимо собрать в массив и вернуть в формате 

```{
	total: 10,
	results: [
		{
			ctn: 1234567890,
			abonentId: d79fccde-4cb3-11ea-b77f-2e728ce88125
			name: “Ivanov Ivan”,
			email: “i.ivanov@mail.com”
		}, {
			ctn: 1234567891,
			abonentId:f9ce619e-4cb3-11ea-b77f-2e728ce88125 
			name: “Petrov Ivan”,
			email: “i.petrov@mail.com”
		}, 
		…
	]
}
```
Требования к реализации 
-	Язык программирования Java или Scala
-	Наличие HTTP API, состоящего из одного метода
-	Сервис DetailService должен уметь обрабатывать несколько запросов одновременно
-	Запросы к ProfileService (в лице https://randomuser.me) должны осуществляться параллельно
-	Запросы к ABONENTID должны выполняться параллельно
-	Учесть что ответ от  ProfileService и  ABONENTID могут приходить с задержкой.
-	Необходимо приложить скрипты по созданию схемы в БД
-	Сервис DetailService  всегда должен сформировать результат ответа в установленный лимит (по умолчанию 2 сек), в случае если сервис не успевает получить данны о профиле какого -то абонента или об abonentId, то необходимо в ответе для данного абонента оставить неполученные поля пустыми.
-	“упаковать” данный сервис в docker-compose 

Результат выполнения задания
- необходимо предоставить исходные коды реализующие логику проекта
- sql скрипты
- скрипты для сборки проекта
- конфигурации docker-compose
- инструкцию по сборке проекта и запуску

