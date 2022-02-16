# FileSorter
Java 16

Система сборки Gradle 7.1

Прграмма сортировки слиянием нескольких отсортированных файлов в один.

Параметры программы задаются при запуске через аргументы командной строки, по порядку:
1. режим сортировки (-a или -d), необязательный, по умолчанию сортируем по возрастанию;
2. тип данных (-s или -i), обязательный;
3. имя выходного файла, обязательное;
4. остальные параметры – имена входных файлов, не менее одного.

Примеры запуска из командной строки для Windows:
sort-it.exe -i -a out.txt in.txt (для целых чисел по возрастанию)
sort-it.exe -s out.txt in1.txt in2.txt in3.txt (для строк по возрастанию)
sort-it.exe -d -s out.txt in1.txt in2.txt (для строк по убыванию)

Особенность реализациии:

1)В случае ошибки открытия/чтения входных файлов из списка,
программа продолжает работать с другими файлами, удаляя ненужный файл.
 
2)Если файл содержит некорректные значения, программа выводит сообщение о содержании некорректных данных и продолжает работу с файлом.
 
3)Ошибки начальной сортировки во входном файле, программа ищет первый элемент в поврежденном файле,
который восстанавливает порядок и продолжает работать с найденным значением
