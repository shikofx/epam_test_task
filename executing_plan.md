# test_cases
Выполняемые тесты:
##Класс SearchAccomodationTests 
Класс с набором тестов для тестирования главной страницы поиска https://www.booking.com/

###Файл данных search-positive.data
Содержит данные в фомате CSV (данные разделены запятыми)
Ограничения исходных данных:
- город (ввод в поле) - валидный город или направление
- дата заезда - не меньше чем текущая дата
- дата отъезда - не меньше чем дата заезда 
- количество номеров (до 10) 
- количество взрослых (до 30)
- количество детей(до 10)
- валюта (из списка валют)
- количество звезд (0, 1, 2, 3, 4, 5)

Пример: Минск,20/12/2019,30/01/2020,5,2,3,US,5  
город = Минск
дата заезда = 20/12/2019
дата отъезда = 30/01/2020 
количество номеров = 5
количество взрослых = 2
количество детей = 3
валюта = US
количество звезд = 5

###1. Тест testSortByPrice(String currency)
####Summary:
Найти 5 мест проживания по минимальной стоимости выполнив сортировку по цене
####Preconditions:
Файл данных search-positive.data
Стартовая страница для теста https://www.booking.com/
####Expected results:
Результаты поиска отображаются в порядке возрастания цены
Подсвечен пункт выбора сортировки по цене

###2. Тест testSortByDistance(String currency)
####Summary:
Найти 3 места проживания с минимильным расстоянием до центра города

####Preconditions:
Файл данных search-positive.data
Стартовая страница для теста https://www.booking.com/
####Expected results:
Результаты поиска отображаются в порядке возрастания расстояния
Отображается и подсвечивается пункт выбора сортировки по расстоянию до центра города

###3. Тест testFilterByStars(String city, String stars)
####Summary:
Найти все места проживания с заданным количеством звезд "Количество звезд"
####Preconditions:
Файл данных search-positive.data
Стартовая страница для теста https://www.booking.com/
####Expected results:
Чек-бокс необходимого элемента выбран
Отображаются только элементы с указанным количеством звезд
Количество найденных записей указанное напротив элементов списка совпадает с расчетным
Количество отображаемых записей совпадает с расчетным

###4. Тест testFilterByAvailability(String city, String crooms, String cadults, String cchildren)
####Summary:
Фильтр "Отображать только доступные"
####Preconditions:
Файл данных search-positive.data
Стартовая страница для теста https://www.booking.com/
####Expected results:

Budget

