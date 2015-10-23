# NonDecimal-Fractions-Period-Java
Thins program finds the length of period of non-decimal fraction in Java.
Intellij IDEA Community Edition 14.1.5 project

Задача

    Дробь
    Даны два числа: a и b (вводятся в десятичной системе счисления).
    Найдите значение числа a/b, записанного в k-ичной системе счисления.
    Если a/b — периодическая дробь, то период следует заключить в скобки.

    Пример входных данных:
    1 2 8
    1 12 10

    Пример выходных данных:
    0.4
    0.08(3)
    
Как пользоваться программой

Доступно 2 способа ввода данных:

    1. В качестве аргументов командной строки.
        а. java com.none.Main fractions.txt
            где fractions.txt - имя файла с исходными данными для расчётов
            в src/main/resources. В этом случае используется установленная
            по умолчанию точность расчётов (50 знаков после точки).
            
            Пример файла fractions.txt:
            1 2 8
            1 12 10
            16.77 -13.2 20
            
            где первое число - числитель, второе знаменатель, третье - основание
            целевой системы счисления (целое число от 2 до 36).
        
       б. java com.none.Main 50 fractions.txt
               где  50 - точность расчётов (знаков после точки);
                    fractions.txt - имя файла с исходными данными для расчётов.
                        (аналогично варианту 1.а.)
                    
       в. java com.none.Main 50 -3 15.6 20
               где  50 - точность расчётов (знаков после точки);
                    -3 - числитель дроби;
                    15.6 - знаменатель дроби;
                    20 - основание целевой системы счисления (целое число от 2 до 36).
                    
    2. В меню консоли программы. Здесь доступны следующие опции:
        а. Ввод дробей в консоль. Подразумевает следующие шаги:
            - Определение точность расчётов;
            - Определение количества дробей, подлежащих обработке в настоящей сессии;
            - Ввод числителя, знаменателя и основания целевой системы счисления
                                                            для каждой из дробей.
        
        б. Чтение дробей из файла fractions.txt (точность расчётов вводится в консоль).

            
      
Комментарий к условиям

    Условиями задачи не определен важный для решения вопрос:

    Каков диапозон систем счисления, в которые предполагается переводить числа?

    Я выбирал между тремя диапозонам:
    
    1. 2 - 36 (Стандарнтый диапозон систем счисления Java, он же - наиболее широко применяемый диапозон).
    2. 2 - Integer.MAX_VALUE
    3. Integer.MIN_VALUE - Integer.MAX_VALUE

    Расчёты во втором и третьем диапазонах можно было организовать, используя
    списки вместо привычных при конвертации String. Однако, ввиду узости
    применения второго диапазона и отсутствия видимой практической пользы у
    третьего, а так же ввиду удобства первого, было принято решение остановиться
    на первом диапазоне.


Обоснование применяемых методов

    Факторизация

    А. Факторизация базы системы счисления

    Так как максимальная база системы счисления в данной программе - 36, наиболее
    быстрым способом ее факторизации будет использование таблицы простых множителей.

    Число: праймы

    2: 2
    3: 3
    4: 2
    5: 5
    6: 2 3
    7: 7
    8: 2
    9: 3
    10: 2 5
    11: 11
    12: 2 3
    13: 13
    14: 2 7
    15: 3 5
    16: 2
    17: 17
    18: 2 3
    19: 19
    20: 2 5
    21: 3 7
    22: 2 11
    23: 23
    24: 2 3
    25: 5
    26: 2 13
    27: 3
    28: 2 7
    29: 29
    30: 2 3 5
    31: 31
    32: 2
    33: 3 11
    34: 2 17
    35: 5 7
    36: 2 3

    Иначе:

    Прайм: числа

    2: 2 4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36
    3: 3 9 12 15 18 21 24 27 29 31 34
    5: 5 10 15 20 25 30 35
    7: 7 14 21 28 35
    11: 11 22 33
    13: 13 26
    17: 17 34
    19: 19
    23: 23
    29: 29
    31: 31

    Таким образом, нам просто нужно проверить делимость без остатка входных
    чисел на 11 простых множителей, при этом 11 итераций будет максимальной
    длиной лупа. Кроме того, так как 30 является единственном числом в списке,
    имеющим 3 простых множителя, мы можем прерывать луп каждый раз, как список
    множителей будет насчитывать 2 значения в случаях, когда входное число не
    равно 30.

    Б. Факторизация знаменателя дроби

    Для факторизации чисел больших, чем 36, используется Р-алгоритм Полларда,
    сложность которого оценивается как О(N^1/4).

    Источник:
    https://ru.wikipedia.org/wiki/P-алгоритм_Полларда



Решение

    Дано:
    N - числитель.
    D - знаменатель.
    B - база целевой системы счисления.


    Общий алгоритм решения:

    Известно:

    Дробь N/D в системе счисления с основанием B является терминальной, если
    все множители D являются простыми множителями B.

    1. Определим, является ли дробь N/D терминальной.
    Для этого:

        A. Используем проверку (N mod D == 0). Если выражение истинно,
        возвращаем ответ.

        Б. Так как алгоритмы определения длины периода, которые мы будем
        использовать, требуют, чтобы числитель дроби не имел общих множителей 
        со знаменателем (иначе дробь может быть сокращена, и знаменатель изменится),
        выделим из дроби целую часть, а остаток сократим.

        В. Разложим знаменатель и основание системы счисления на простые множители.
        Если среди простых множителей знаменателя нет таких, какие не являлись бы
        простыми множителями базы, дробь терминальная.

        Г. Рассчитываем и возвращаем ответ.

    2. Определим, является ли сокращенный остаток дроби N/D периодической
    дробью чистого типа (т.е. дробью, в которой период начинается сразу после
    точки) или смешанного типа (т.е. дробью, в которой периодической части
    предшествует непериодическая последовательность цифр).

    Известно:

    Дробь n/d, где n и d не имеют общих множителей, является периодической дробью
    чистого типа тогда и только тогда, когда ни один из простых множителей базы B
    не является простым множителем d.

        А. Если дробь является периодической дробью чистого типа, используем метод
        определения длины периода для периодических дробей чистого типа:

        Пусть L - наименьшая степень, при возведении в которую B^L mod d = 1.
        Тогда длина периода дроби n/d будет равна L.

        Вычисляем L. Рассчитываем и возвращаем ответ.

        Б. Если дробь является периодической дробью смешанного типа, используем
        метод определения длины периода для периодических дробей смешанного типа:

        Представим d как Д*Е, где все простые множители Д соответствуют
        простым множителям B.

        Тогда:
        1) Длина непериодической части дроби n/d будет равна количеству
        знаков после запятой в терминальной дроби 1/Д.
        2) Длина периода дроби n/d будет равна длине периода 1/E, рассчитанной как
           L - наименьшая степень, при возведении в которую B^L mod E = 1.



       Источники:
       http://www.maths.surrey.ac.uk/hosted-sites/R.Knott/Fractions/fractions.html на 18:43 13.10.15.
       https://en.wikipedia.org/wiki/Repeating_decimal на 18:43 13.10.15.